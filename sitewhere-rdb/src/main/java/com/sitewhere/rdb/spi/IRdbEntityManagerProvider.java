/**
 * Copyright © 2014-2021 The SiteWhere Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
     * Execute code in the context of a transaction.
     * 
     * @param <T>
     * @param callback
     * @return
     * @throws SiteWhereException
     */
    <T> T runInTransaction(ITransactionCallback<T> callback) throws SiteWhereException;

    /**
     * Persist an entity via the {@link EntityManager}.
     * 
     * @param <T>
     * @param created
     * @throws SiteWhereException
     */
    <T> T persist(T created) throws SiteWhereException;

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
