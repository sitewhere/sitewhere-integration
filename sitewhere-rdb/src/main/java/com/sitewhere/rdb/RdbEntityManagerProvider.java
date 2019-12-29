/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.evanlennick.retry4j.CallExecutorBuilder;
import com.evanlennick.retry4j.Status;
import com.evanlennick.retry4j.config.RetryConfig;
import com.evanlennick.retry4j.config.RetryConfigBuilder;
import com.evanlennick.retry4j.listener.RetryListener;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.rdb.spi.IRdbEntityManagerProvider;
import com.sitewhere.rdb.spi.IRdbQueryProvider;
import com.sitewhere.rest.model.search.SearchResults;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;
import com.sitewhere.spi.search.ISearchCriteria;
import com.sitewhere.spi.search.ISearchResults;

/**
 * Component which bootstraps a JPA entity manager for use in tenant engines
 * which require access to the relational schema.
 */
public class RdbEntityManagerProvider extends TenantEngineLifecycleComponent implements IRdbEntityManagerProvider {

    /** JPA entity manager factory */
    private EntityManagerFactory entityManagerFactory;

    /** Entity manager */
    private EntityManager entityManager;

    /** Managed entity classes */
    private Class<?>[] entityClasses;

    /** Data source */
    private ComboPooledDataSource dataSource;

    /** Provider */
    private RdbProviderInformation<?> provider;

    /** Allows blocking until database is available */
    private CountDownLatch databaseAvailable = new CountDownLatch(1);

    /** Thread for database connection waiter */
    private Executor executor = Executors.newSingleThreadExecutor();

    public RdbEntityManagerProvider(RdbProviderInformation<?> provider, Class<?>[] entityClasses) {
	super(LifecycleComponentType.DataStore);
	this.provider = provider;
	this.entityClasses = entityClasses;
    }

    /*
     * @see
     * com.sitewhere.microservice.lifecycle.LifecycleComponent#start(com.sitewhere.
     * spi.microservice.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void start(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// Prevent hanging references.
	shutDownIfAlreadyCreated();

	// Attempt to create database if it doesn't already exist.
	executor.execute(new DatabaseConnectionWaiter());
	try {
	    getDatabaseAvailable().await();
	} catch (InterruptedException e) {
	    getLogger().info("Interrupted while waiting for database connection.", e);
	    return;
	}

	// Create datasource.
	String schema = FlywayConfig.getSchemaName(getMicroservice().getIdentifier());
	this.dataSource = new ComboPooledDataSource();

	// Set datasource connectivity.
	getDataSource().setJdbcUrl(getProvider().buildJdbcUrl(getDatabaseName()));
	getDataSource().setUser(getProvider().getConnectionInfo().getUsername());
	getDataSource().setPassword(getProvider().getConnectionInfo().getPassword());

	// Execute Flyway migration.
	FlywayConfig.migrateTenantData(getDataSource(), getMicroservice().getIdentifier());

	// Create a new factory and entity manager.
	List<Class<?>> entityClasses = Arrays.asList(getEntityClasses());
	this.entityManagerFactory = RdbEntityManagerFactoryBuilder.buildFrom(provider, entityClasses, getDataSource(),
		schema);
	this.entityManager = getEntityManagerFactory().createEntityManager();
    }

    protected String getDatabaseName() {
	String tenantToken = getTenantEngine().getTenantResource().getMetadata().getName();
	return String.format(getProvider().getDatabaseNameTemplate(), tenantToken);
    }

    /**
     * Waits for Syncope connection to become available.
     */
    protected class DatabaseConnectionWaiter implements Runnable {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() {
	    getLogger().info("Attempting to connect to database ...");
	    Callable<Boolean> connectCheck = () -> {
		Connection connection = DriverManager.getConnection(getProvider().buildRootJdbcUrl(),
			getProvider().getConnectionInfo().getUsername(),
			getProvider().getConnectionInfo().getPassword());
		String dbname = getDatabaseName();
		getProvider().getCreateDatabaseProvider().createDatabase(connection, dbname);
		connection.close();
		getDatabaseAvailable().countDown();
		return true;
	    };
	    RetryConfig config = new RetryConfigBuilder().retryOnAnyException().retryIndefinitely()
		    .withDelayBetweenTries(Duration.ofSeconds(5)).withFixedBackoff().build();
	    RetryListener listener = new RetryListener<Boolean>() {

		@Override
		public void onEvent(Status<Boolean> status) {
		    getLogger().info(String.format(
			    "Unable to connect to database attempt %d [%s] (total wait so far %dms). Retrying after fallback...",
			    status.getTotalTries(), status.getLastExceptionThatCausedRetry().getMessage(),
			    status.getTotalElapsedDuration().toMillis()));
		}
	    };
	    new CallExecutorBuilder().config(config).afterFailedTryListener(listener).build().execute(connectCheck);
	}
    }

    /*
     * @see
     * com.sitewhere.microservice.lifecycle.LifecycleComponent#stop(com.sitewhere.
     * spi.microservice.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void stop(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	shutDownIfAlreadyCreated();
    }

    /**
     * Shut down entity manager and factory if already created.
     */
    protected void shutDownIfAlreadyCreated() {
	if (getEntityManager() != null) {
	    getEntityManager().close();
	}
	if (getEntityManagerFactory() != null) {
	    getEntityManagerFactory().close();
	}
	if (getDataSource() != null) {
	    getDataSource().close();
	}
    }

    /*
     * @see
     * com.sitewhere.rdb.spi.IRdbEntityManagerProvider#persist(java.lang.Object)
     */
    @Override
    public <T> void persist(T created) throws SiteWhereException {
	EntityTransaction tx = getEntityManager().getTransaction();
	try {
	    tx.begin();
	    getEntityManager().persist(created);
	    tx.commit();
	} catch (Exception e) {
	    tx.rollback();
	    throw new SiteWhereException("Entity persist failed.", e);
	}
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbEntityManagerProvider#merge(java.lang.Object)
     */
    @Override
    public <T> T merge(T updated) throws SiteWhereException {
	EntityTransaction tx = getEntityManager().getTransaction();
	try {
	    tx.begin();
	    T result = getEntityManager().merge(updated);
	    tx.commit();
	    return result;
	} catch (Exception e) {
	    tx.rollback();
	    throw new SiteWhereException("Entity persist failed.", e);
	}
    }

    /*
     * @see
     * com.sitewhere.rdb.spi.IRdbEntityManagerProvider#findById(java.lang.Object,
     * java.lang.Class)
     */
    @Override
    public <T> T findById(Object key, Class<T> clazz) throws SiteWhereException {
	return getEntityManager().find(clazz, key);
    }

    /*
     * @see
     * com.sitewhere.rdb.spi.IRdbEntityManagerProvider#findOne(javax.persistence.
     * Query, java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findOne(Query query, Class<T> clazz) throws SiteWhereException {
	try {
	    return (T) query.getSingleResult();
	} catch (NoResultException e) {
	    return null;
	} catch (NonUniqueResultException e) {
	    throw new SiteWhereException("Expected on result but found multiple.", e);
	} catch (Throwable e) {
	    throw new SiteWhereException("Unhandled exception retrieving single element.", e);
	}
    }

    /*
     * @see
     * com.sitewhere.rdb.spi.IRdbEntityManagerProvider#findMany(javax.persistence.
     * Query, java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findMany(Query query, Class<T> clazz) throws SiteWhereException {
	try {
	    return (List<T>) query.getResultList();
	} catch (Throwable e) {
	    throw new SiteWhereException("Unhandled exception retrieving list.", e);
	}
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbEntityManagerProvider#remove(java.lang.Object)
     */
    @Override
    public <T> T remove(Object key, Class<T> clazz) throws SiteWhereException {
	EntityTransaction tx = getEntityManager().getTransaction();
	try {
	    tx.begin();
	    T existing = getEntityManager().find(clazz, key);
	    if (existing != null) {
		getEntityManager().remove(existing);
	    }
	    tx.commit();
	    return existing;
	} catch (Exception e) {
	    tx.rollback();
	    throw new SiteWhereException("Entity remove failed.", e);
	}
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbEntityManagerProvider#findWithCriteria(com.
     * sitewhere.spi.search.ISearchCriteria,
     * com.sitewhere.rdb.spi.IRdbQueryProvider, java.lang.Class)
     */
    @Override
    public <T> ISearchResults<T> findWithCriteria(ISearchCriteria criteria, IRdbQueryProvider<T> queryProvider,
	    Class<T> clazz) throws SiteWhereException {
	CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

	// Build query template.
	CriteriaQuery<T> query = cb.createQuery(clazz);
	Root<T> from = query.from(clazz);
	query = query.select(from);

	List<Predicate> predicatesList = new ArrayList<>();
	queryProvider.addPredicates(cb, predicatesList, from);
	Predicate[] predicates = predicatesList.toArray(new Predicate[0]);
	if (predicates.length > 0) {
	    query = query.where(predicates);
	}

	// Sort.
	query = queryProvider.addSort(cb, from, query);

	// Build query.
	TypedQuery<T> tq = getEntityManager().createQuery(query);

	// Handle paging.
	if (criteria.getPageSize() > 0) {
	    int start = criteria.getPageNumber() * criteria.getPageSize() - 1;
	    if (start < 0) {
		start = 0;
	    }
	    tq.setFirstResult(start);
	    tq.setMaxResults(criteria.getPageSize());
	}

	List<T> results = tq.getResultList();

	CriteriaQuery<Long> counter = cb.createQuery(Long.class);
	counter = counter.select(cb.count(counter.from(clazz)));
	if (predicates.length > 0) {
	    counter = counter.where(predicates);
	}

	TypedQuery<Long> counterQuery = getEntityManager().createQuery(counter);
	Long count = counterQuery.getSingleResult();
	return new SearchResults<T>(results, count);
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbEntityManagerProvider#query(java.lang.String)
     */
    @Override
    public Query query(String name) throws SiteWhereException {
	return getEntityManager().createNamedQuery(name);
    }

    /*
     * @see
     * com.sitewhere.rdb.spi.IRdbEntityManagerProvider#getEntityManagerFactory()
     */
    @Override
    public EntityManagerFactory getEntityManagerFactory() {
	return entityManagerFactory;
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbEntityManagerProvider#getEntityManager()
     */
    @Override
    public EntityManager getEntityManager() {
	return entityManager;
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbEntityManagerProvider#getEntityClasses()
     */
    @Override
    public Class<?>[] getEntityClasses() {
	return entityClasses;
    }

    protected CountDownLatch getDatabaseAvailable() {
	return databaseAvailable;
    }

    protected RdbProviderInformation<?> getProvider() {
	return provider;
    }

    protected ComboPooledDataSource getDataSource() {
	return dataSource;
    }
}
