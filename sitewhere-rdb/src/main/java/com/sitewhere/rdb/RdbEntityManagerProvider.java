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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.evanlennick.retry4j.CallExecutorBuilder;
import com.evanlennick.retry4j.Status;
import com.evanlennick.retry4j.config.RetryConfig;
import com.evanlennick.retry4j.config.RetryConfigBuilder;
import com.evanlennick.retry4j.listener.RetryListener;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.rdb.spi.IRdbEntityManagerProvider;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

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
    private RdbProviderInformation provider;;

    /** Allows blocking until database is available */
    private CountDownLatch databaseAvailable = new CountDownLatch(1);

    /** Thread for database connection waiter */
    private Executor executor = Executors.newSingleThreadExecutor();

    public RdbEntityManagerProvider(RdbProviderInformation provider, Class<?>[] entityClasses) {
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
	String dbname = getDatabaseName();
	String schema = FlywayConfig.getSchemaName(getMicroservice().getIdentifier());
	this.dataSource = new ComboPooledDataSource();

	// Set datasource connectivity.
	getDataSource().setJdbcUrl(String.format("jdbc:postgresql://sitewhere-postgresql/%s", dbname));
	getDataSource().setUser("syncope");
	getDataSource().setPassword("syncope");

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
		Connection connection = DriverManager.getConnection("jdbc:postgresql://sitewhere-postgresql/?",
			"syncope", "syncope");
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

    protected RdbProviderInformation getProvider() {
	return provider;
    }

    protected ComboPooledDataSource getDataSource() {
	return dataSource;
    }
}
