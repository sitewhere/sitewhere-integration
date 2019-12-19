/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.IDiscoverableTenantLifecycleComponent;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Client used to connect to relational databases.
 */
public class RdbClient extends TenantEngineLifecycleComponent implements IDiscoverableTenantLifecycleComponent {

    /** Relational database configuration */
    @SuppressWarnings("unused")
    private RDBConfiguration configuration;

    /**
     *
     * @param configuration
     */
    public RdbClient(RDBConfiguration configuration) {
	super(LifecycleComponentType.DataStore);
	this.configuration = configuration;
    }

    @Override
    public boolean isRequired() {
	return true;
    }

    @Override
    public void start(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// String tenantId = this.getTenantEngine().getTenant().getId().toString();
	// Add a new datasource.
	// MultiTenantContext.setTenantId(tenantId);
    }
}
