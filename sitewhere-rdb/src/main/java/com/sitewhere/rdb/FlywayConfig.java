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

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import com.sitewhere.spi.microservice.IFunctionIdentifier;

public class FlywayConfig {

    /**
     * Convert a function identifier into a schema name.
     * 
     * @param function
     * @return
     */
    public static String getSchemaName(IFunctionIdentifier function) {
	return function.getPath().replaceAll("[-]", "");
    }

    /**
     * Migrate tenant data if needed.
     * 
     * @param dataSource
     * @param function
     * @return
     */
    public static boolean migrateTenantData(DataSource dataSource, IFunctionIdentifier function) {
	String area = getSchemaName(function);
	Flyway flyway = Flyway.configure().locations(String.format("db/migrations/tenants/%s", area))
		.dataSource(dataSource).schemas(area).load();
	int migrate = flyway.migrate();
	return (migrate > 0);
    }
}
