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
