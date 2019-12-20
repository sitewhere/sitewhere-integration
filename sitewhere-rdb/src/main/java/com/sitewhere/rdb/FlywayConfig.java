/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
