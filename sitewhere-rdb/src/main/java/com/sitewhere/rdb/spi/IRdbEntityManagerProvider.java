/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.spi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;

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
}
