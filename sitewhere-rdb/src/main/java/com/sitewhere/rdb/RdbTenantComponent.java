/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.rdb.spi.IRdbClient;
import com.sitewhere.rdb.spi.IRdbEntityManagerProvider;
import com.sitewhere.rdb.spi.IRdbTenantComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Base class for components in a tenant engine that rely on RDB connectivity.
 */
public abstract class RdbTenantComponent<T extends IRdbClient> extends TenantEngineLifecycleComponent
	implements IRdbTenantComponent<T> {

    /** RDB client implementation */
    private T client;

    public RdbTenantComponent() {
	super(LifecycleComponentType.DataStore);
    }

    /*
     * @see com.sitewhere.microservice.lifecycle.LifecycleComponent#initialize(com.
     * sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void initialize(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	this.client = createRdbClient(getEntityManagerProvider());
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbTenantComponent#getClient()
     */
    @Override
    public T getClient() {
	return client;
    }

    /**
     * Create an RDB client for accessing the database.
     * 
     * @param provider
     * @return
     * @throws SiteWhereException
     */
    public abstract T createRdbClient(IRdbEntityManagerProvider provider) throws SiteWhereException;
}
