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
package com.sitewhere.rdb.providers.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sitewhere.rdb.RdbProviderInformation;
import com.sitewhere.rdb.spi.IDatabaseCreationProvider;
import com.sitewhere.spi.SiteWhereException;

/**
 * Provider compatible with PostgreSQL 9.5.
 */
public class Postgres95Provider extends RdbProviderInformation<PostgresConnectionInfo> {

    /** Static logger instance */
    private static Logger LOGGER = LoggerFactory.getLogger(Postgres95Provider.class);

    public Postgres95Provider(PostgresConnectionInfo connectionInfo) {
	super(connectionInfo);

	setName("postgres");
	setDescription("PostgreSQL");
	setDialect(PostgreSQL95Dialect.class.getName());
	setCreateDatabaseProvider(new IDatabaseCreationProvider() {

	    /*
	     * @see com.sitewhere.rdb.spi.IDatabaseCreationProvider#createDatabase(java.sql.
	     * Connection, java.lang.String)
	     */
	    @Override
	    public void createDatabase(Connection connection, String dbname) throws SiteWhereException {
		try {
		    boolean found = false;
		    PreparedStatement ps = connection
			    .prepareStatement("SELECT datname FROM pg_database WHERE datistemplate = false;");
		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
			String dbmatch = rs.getString(1);
			if (dbmatch.equals(dbname)) {
			    LOGGER.info(String.format("Found existing database '%s'", dbname));
			    found = true;
			}
		    }
		    rs.close();
		    ps.close();

		    if (!found) {
			LOGGER.info(String.format("Creating database '%s'", dbname));
			Statement statement = connection.createStatement();
			statement.executeUpdate(String.format("CREATE DATABASE %s", dbname));
			statement.close();
		    }
		} catch (Throwable t) {
		    throw new SiteWhereException("Unable to create database.", t);
		}
	    }
	});
	setDatabaseNameTemplate("tenant_%s");
	setFlywayMigrationPath("db/postgres/migrations/tenant");
    }

    /*
     * @see com.sitewhere.rdb.RdbProviderInformation#buildRootJdbcUrl()
     */
    @Override
    public String buildRootJdbcUrl() {
	return String.format("jdbc:postgresql://%s:%d/?", getConnectionInfo().getHostname(),
		getConnectionInfo().getPort());
    }

    /*
     * @see com.sitewhere.rdb.RdbProviderInformation#buildJdbcUrl(java.lang.String)
     */
    @Override
    public String buildJdbcUrl(String dbname) {
	return String.format("jdbc:postgresql://%s:%d/%s", getConnectionInfo().getHostname(),
		getConnectionInfo().getPort(), dbname);
    }
}
