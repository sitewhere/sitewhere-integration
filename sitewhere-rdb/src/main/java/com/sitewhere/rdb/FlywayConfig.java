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

public class FlywayConfig {

    public Boolean tenantsFlyway(String tenantName, DataSource dataSource) {
	Flyway flyway = Flyway.configure().locations("db/migrations/tenants").dataSource(dataSource).schemas(tenantName)
		.load();
	int migrate = flyway.migrate();
	return (migrate > 0);
    }
}
