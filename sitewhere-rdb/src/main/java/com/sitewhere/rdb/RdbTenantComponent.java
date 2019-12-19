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
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Base class for components in a tenant engine that rely on RDB connectivity.
 */
public abstract class RdbTenantComponent<T extends RdbClient> extends TenantEngineLifecycleComponent {

    public RdbTenantComponent() {
    }

    public RdbTenantComponent(LifecycleComponentType type) {
	super(type);
    }

    /**
     * Get the RDB client used to access the database.
     *
     * @return
     * @throws SiteWhereException
     */
    public abstract T getRDBClient() throws SiteWhereException;

    /*
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#provision(com.sitewhere.spi
     * .server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void provision(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// String tenantId = this.getTenantEngine().getTenant().getId().toString();
	// Map<String, DataSource> rdbDataSources = (Map<String, DataSource>)
	// ApplicationContextUtils
	// .getBean("rdbDataSources");
	// FlywayConfig flywayConfig = new FlywayConfig();
	// flywayConfig.tenantsFlyway(tenantId, rdbDataSources.get(tenantId));
    }
}
