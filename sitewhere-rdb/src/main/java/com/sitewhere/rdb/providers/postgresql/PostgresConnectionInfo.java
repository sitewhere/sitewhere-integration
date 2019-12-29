/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.providers.postgresql;

import com.sitewhere.rdb.spi.IConnectionInformation;

/**
 * Information used to connect to a PostgreSQL database.
 */
public class PostgresConnectionInfo implements IConnectionInformation {

    /** Database hostname */
    private String hostname;

    /** Database port */
    private int port;

    /** Database username */
    private String username;

    /** Database password */
    private String password;

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
