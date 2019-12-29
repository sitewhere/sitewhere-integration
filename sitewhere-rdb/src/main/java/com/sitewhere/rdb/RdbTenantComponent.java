/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.rdb.spi.IRdbTenantComponent;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Base class for components in a tenant engine that rely on RDB connectivity.
 */
public abstract class RdbTenantComponent extends TenantEngineLifecycleComponent implements IRdbTenantComponent {

    public RdbTenantComponent() {
	super(LifecycleComponentType.DataStore);
    }
}
