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
