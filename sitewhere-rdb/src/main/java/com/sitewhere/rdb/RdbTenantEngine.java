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
package com.sitewhere.rdb;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import com.sitewhere.microservice.configuration.model.instance.persistence.RdbConfiguration;
import com.sitewhere.microservice.datastore.DatastoreDefinition;
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

    /** Application context */
    private ApplicationContext applicationContext;

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
	String[] classNames = Arrays.asList(getEntityClasses()).stream().map(entityClass -> entityClass.getName())
		.collect(Collectors.toList()).toArray(new String[0]);
	getLogger().info(String.format("Entity manager will manage the following JPA entities:\n%s",
		String.join(", ", classNames)));
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

	// Handle global reference.
	if (datastore.getReference() != null) {
	    RdbConfiguration rdb = getMicroservice().getInstanceConfiguration().getPersistenceConfigurations()
		    .getRdbConfigurations().get(datastore.getReference());
	    if (rdb == null) {
		throw new SiteWhereException(
			String.format("Tenant engine references missing global datastore configuration '%s'.",
				datastore.getReference()));
	    }
	    DatastoreDefinition global = new DatastoreDefinition();
	    global.setType(rdb.getType());
	    global.setConfiguration(rdb.getConfiguration());

	    getLogger().info(
		    String.format("Using datastore definition defined globally as '%s'.", datastore.getReference()));
	    return RdbProviderHandler.getProviderInformation(this, global);
	}

	// Handle local definition.
	else {
	    getLogger().info("Using datastore definition defined locally.");
	    return RdbProviderHandler.getProviderInformation(this, datastore);
	}
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

    protected ApplicationContext getApplicationContext() {
	return applicationContext;
    }
}
