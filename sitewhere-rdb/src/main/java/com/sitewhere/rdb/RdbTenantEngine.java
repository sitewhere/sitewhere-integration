/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import com.sitewhere.datastore.DatastoreDefinition;
import com.sitewhere.microservice.lifecycle.CompositeLifecycleStep;
import com.sitewhere.microservice.multitenant.MicroserviceTenantEngine;
import com.sitewhere.rdb.providers.RdbProviderHandler;
import com.sitewhere.rdb.spi.IRdbEntityManagerProvider;
import com.sitewhere.rdb.spi.IRdbTenantEngine;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ICompositeLifecycleStep;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.multitenant.ITenantEngineConfiguration;

import io.sitewhere.k8s.crd.tenant.engine.SiteWhereTenantEngine;

/**
 * Base class for tenant engines which persist data in a relational database.
 * 
 * @param <T>
 */
public abstract class RdbTenantEngine<T extends ITenantEngineConfiguration> extends MicroserviceTenantEngine<T>
	implements IRdbTenantEngine<T> {

    /** RDB entity manager provider */
    private IRdbEntityManagerProvider rdbEntityManagerProvider;

    public RdbTenantEngine(SiteWhereTenantEngine tenantEngineResource) {
	super(tenantEngineResource);
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine#
     * tenantInitialize(com.sitewhere.spi.microservice.lifecycle.
     * ILifecycleProgressMonitor)
     */
    @Override
    public void tenantInitialize(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	this.rdbEntityManagerProvider = new RdbEntityManagerProvider(getProviderInformation(), getPersistenceOptions(),
		getEntityClasses());

	// Create step that will initialize components.
	ICompositeLifecycleStep init = new CompositeLifecycleStep("Initialize RDB " + getComponentName());

	// Initialize RDB entity management.
	init.addInitializeStep(this, getRdbEntityManagerProvider(), true);

	// Execute initialization steps.
	init.execute(monitor);
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine#
     * tenantStart(com.sitewhere.spi.microservice.lifecycle.
     * ILifecycleProgressMonitor)
     */
    @Override
    public void tenantStart(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// Create step that will start components.
	ICompositeLifecycleStep start = new CompositeLifecycleStep("Start RDB " + getComponentName());

	// Start RDB entity management.
	start.addStartStep(this, getRdbEntityManagerProvider(), true);

	// Execute startup steps.
	start.execute(monitor);
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMicroserviceTenantEngine#
     * tenantStop(com.sitewhere.spi.microservice.lifecycle.
     * ILifecycleProgressMonitor)
     */
    @Override
    public void tenantStop(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// Create step that will stop components.
	ICompositeLifecycleStep stop = new CompositeLifecycleStep("Stop RDB " + getComponentName());

	// Stop RDB entity management.
	stop.addStopStep(this, getRdbEntityManagerProvider());

	// Execute shutdown steps.
	stop.execute(monitor);
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbTenantEngine#getProviderInformation()
     */
    @Override
    public RdbProviderInformation<?> getProviderInformation() throws SiteWhereException {
	DatastoreDefinition datastore = getDatastoreDefinition();
	return RdbProviderHandler.getProviderInformation(datastore);
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbTenantEngine#getRdbEntityManagerProvider()
     */
    @Override
    public IRdbEntityManagerProvider getRdbEntityManagerProvider() {
	return rdbEntityManagerProvider;
    }

    /*
     * @see com.sitewhere.rdb.spi.IRdbTenantEngine#getPersistenceOptions()
     */
    @Override
    public RdbPersistenceOptions getPersistenceOptions() {
	return new RdbPersistenceOptions();
    }
}
