/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.spi;

/**
 * Provides information used to connect to a database.
 */
public interface IConnectionInformation {

    /**
     * Get database username.
     * 
     * @return
     */
    String getUsername();

    /**
     * Get database password.
     * 
     * @return
     */
    String getPassword();
}
