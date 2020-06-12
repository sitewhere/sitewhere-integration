/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.providers;

import com.sitewhere.microservice.datastore.DatastoreDefinition;
import com.sitewhere.rdb.RdbProviderInformation;
import com.sitewhere.rdb.providers.postgresql.Postgres95Provider;
import com.sitewhere.rdb.providers.postgresql.PostgresConnectionInfo;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;

/**
 * Utility class for building RDB providers based on types specified in generic
 * configuration.
 */
public class RdbProviderHandler {

    public static RdbProviderInformation<?> getProviderInformation(ITenantEngineLifecycleComponent component,
	    DatastoreDefinition datastore) throws SiteWhereException {
	switch (datastore.getType()) {
	case "postgres95": {
	    PostgresConnectionInfo connInfo = new PostgresConnectionInfo(component);
	    connInfo.loadFrom(datastore.getConfiguration());
	    return new Postgres95Provider(connInfo);
	}
	default: {
	    throw new SiteWhereException(String.format("Unknown RDB provider type '%s'.", datastore.getType()));
	}
	}
    }
}
