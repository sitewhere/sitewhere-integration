/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.spi;

import java.sql.Connection;

import com.sitewhere.spi.SiteWhereException;

/**
 * Indicates whether a database exists.
 */
public interface IDatabaseCreationProvider {

    /**
     * Creates a new database if one does not already exist.
     * 
     * @param connection
     * @param dbname
     * @throws SiteWhereException
     */
    void createDatabase(Connection connection, String dbname) throws SiteWhereException;
}
