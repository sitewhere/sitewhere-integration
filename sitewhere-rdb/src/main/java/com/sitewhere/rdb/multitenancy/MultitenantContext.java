/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.multitenancy;

/**
 * {@link ThreadLocal} context used to store which tenant is accessing the
 * database.
 */
public class MultitenantContext {

    /** Thread local storage for tenant token */
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    /**
     * Set thread context for tenant.
     * 
     * @param tenantId
     */
    public static void setTenantId(String tenantId) {
	CONTEXT.set(tenantId);
    }

    /**
     * Get tenant from thread context.
     * 
     * @return
     */
    public static String getTenantId() {
	return CONTEXT.get();
    }

    /**
     * Clear context for thread.
     */
    public static void clear() {
	CONTEXT.remove();
    }
}