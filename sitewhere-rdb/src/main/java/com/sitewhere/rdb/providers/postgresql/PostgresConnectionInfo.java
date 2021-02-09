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

import com.fasterxml.jackson.databind.JsonNode;
import com.sitewhere.microservice.configuration.json.JsonConfiguration;
import com.sitewhere.rdb.spi.IConnectionInformation;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;

/**
 * Information used to connect to a PostgreSQL database.
 */
public class PostgresConnectionInfo extends JsonConfiguration implements IConnectionInformation {

    /** Default PostgreSQL server hostname */
    private static final String DEFAULT_HOSTNAME = "postgres";

    /** Default PostgreSQL server port */
    private static final int DEFAULT_PORT = 5432;

    /** Default PostgreSQL server username */
    private static final String DEFAULT_USERNAME = "postgres";

    /** Default PostgreSQL server password */
    private static final String DEFAULT_PASSWORD = "";

    /** Default max connections */
    private static final int DEFAULT_MAX_CONNECTIONS = 5;

    /** Database hostname */
    private String hostname;

    /** Database port */
    private int port;

    /** Database username */
    private String username;

    /** Database password */
    private String password;

    /** Max number of database connections */
    private int maxConnections;

    public PostgresConnectionInfo(ITenantEngineLifecycleComponent component) {
	super(component);
    }

    public PostgresConnectionInfo loadFrom(JsonNode configuration) throws SiteWhereException {
	PostgresConnectionInfo conn = new PostgresConnectionInfo(getComponent());
	this.hostname = configurableString("hostname", configuration, DEFAULT_HOSTNAME);
	this.port = configurableInt("port", configuration, DEFAULT_PORT);
	this.username = configurableString("username", configuration, DEFAULT_USERNAME);
	this.password = configurableString("password", configuration, DEFAULT_PASSWORD);
	this.maxConnections = configurableInt("maxConnections", configuration, DEFAULT_MAX_CONNECTIONS);
	return conn;
    }

    public String getHostname() {
	return hostname;
    }

    public void setHostname(String hostname) {
	this.hostname = hostname;
    }

    public int getPort() {
	return port;
    }

    public void setPort(int port) {
	this.port = port;
    }

    /*
     * @see com.sitewhere.rdb.spi.IConnectionInformation#getUsername()
     */
    @Override
    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    /*
     * @see com.sitewhere.rdb.spi.IConnectionInformation#getPassword()
     */
    @Override
    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    /*
     * @see com.sitewhere.rdb.spi.IConnectionInformation#getMaxConnections()
     */
    @Override
    public int getMaxConnections() {
	return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
	this.maxConnections = maxConnections;
    }
}
