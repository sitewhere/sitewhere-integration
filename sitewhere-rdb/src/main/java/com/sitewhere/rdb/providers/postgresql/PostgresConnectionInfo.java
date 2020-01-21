/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.providers.postgresql;

import com.fasterxml.jackson.databind.JsonNode;
import com.sitewhere.microservice.configuration.json.JsonConfiguration;
import com.sitewhere.rdb.spi.IConnectionInformation;
import com.sitewhere.spi.SiteWhereException;

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

    /** Database hostname */
    private String hostname;

    /** Database port */
    private int port;

    /** Database username */
    private String username;

    /** Database password */
    private String password;

    public PostgresConnectionInfo loadFrom(JsonNode configuration) throws SiteWhereException {
	PostgresConnectionInfo conn = new PostgresConnectionInfo();
	this.hostname = configurableString("hostname", configuration, DEFAULT_HOSTNAME);
	this.port = configurableInt("port", configuration, DEFAULT_PORT);
	this.username = configurableString("username", configuration, DEFAULT_USERNAME);
	this.password = configurableString("password", configuration, DEFAULT_PASSWORD);
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
}
