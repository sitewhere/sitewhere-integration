/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.spi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;
import com.sitewhere.spi.search.ISearchCriteria;
import com.sitewhere.spi.search.ISearchResults;

/**
 * Component which bootstraps a JPA entity manager for use in tenant engines
 * which require access to the relational schema.
 */
public interface IRdbEntityManagerProvider extends ITenantEngineLifecycleComponent {

    /**
     * JPA entity manager factory.
     * 
     * @return
     */
    EntityManagerFactory getEntityManagerFactory();

    /**
     * JPA entity manager.
     * 
     * @return
     */
    EntityManager getEntityManager();

    /**
     * Get classes managed as entities.
     * 
     * @return
     */
    Class<?>[] getEntityClasses();

    /**
     * Persist an entity via the {@link EntityManager}.
     * 
     * @param <T>
     * @param created
     * @throws SiteWhereException
     */
    <T> void persist(T created) throws SiteWhereException;

    /**
     * Merge updates into the database.
     * 
     * @param <T>
     * @param updated
     * @return
     * @throws SiteWhereException
     */
    <T> T merge(T updated) throws SiteWhereException;

    /**
     * Find an entity by unique id.
     * 
     * @param <T>
     * @param key
     * @param clazz
     * @return
     * @throws SiteWhereException
     */
    <T> T findById(Object key, Class<T> clazz) throws SiteWhereException;

    /**
     * Query for a single result.
     * 
     * @param <T>
     * @param query
     * @param clazz
     * @return
     * @throws SiteWhereException
     */
    <T> T findOne(Query query, Class<T> clazz) throws SiteWhereException;

    /**
     * Query for multiple results.
     * 
     * @param <T>
     * @param query
     * @param clazz
     * @return
     * @throws SiteWhereException
     */
    <T> List<T> findMany(Query query, Class<T> clazz) throws SiteWhereException;

    /**
     * Remove an entity via the {@link EntityManager}.
     * 
     * @param <T>
     * @param key
     * @param clazz
     * @return
     * @throws SiteWhereException
     */
    <T> T remove(Object key, Class<T> clazz) throws SiteWhereException;

    /**
     * Find objects which match the given criteria.
     * 
     * @param <T>
     * @param criteria
     * @param queryProvider
     * @param clazz
     * @return
     * @throws SiteWhereException
     */
    <T> ISearchResults<T> findWithCriteria(ISearchCriteria criteria, IRdbQueryProvider<T> queryProvider, Class<T> clazz)
	    throws SiteWhereException;

    /**
     * Accesses a named query registered with the {@link EntityManager}.
     * 
     * @param name
     * @return
     * @throws SiteWhereException
     */
    Query query(String name) throws SiteWhereException;
}
