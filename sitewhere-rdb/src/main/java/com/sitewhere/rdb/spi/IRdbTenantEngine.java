/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.spi;

import javax.persistence.EntityManager;

import com.sitewhere.microservice.datastore.DatastoreDefinition;
import com.sitewhere.rdb.RdbPersistenceOptions;
import com.sitewhere.rdb.RdbProviderInformation;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine;
import com.sitewhere.spi.microservice.multitenant.ITenantEngineConfiguration;

/**
 * Tenant engine which includes relational database support.
 * 
 * @param <T>
 */
public interface IRdbTenantEngine<T extends ITenantEngineConfiguration> extends IMicroserviceTenantEngine<T> {

    /**
     * Get datastore definition from tenant engine configuration.
     * 
     * @return
     */
    DatastoreDefinition getDatastoreDefinition();

    /**
     * Get connection provider information.
     * 
     * @return
     * @throws SiteWhereException
     */
    RdbProviderInformation<?> getProviderInformation() throws SiteWhereException;

    /**
     * Get options related to relational persistence.
     * 
     * @return
     */
    RdbPersistenceOptions getPersistenceOptions();

    /**
     * Get classes managed by the {@link EntityManager}.
     * 
     * @return
     */
    Class<?>[] getEntityClasses();

    /**
     * Get RDB entity manager provider.
     * 
     * @return
     */
    IRdbEntityManagerProvider getRdbEntityManagerProvider();
}
