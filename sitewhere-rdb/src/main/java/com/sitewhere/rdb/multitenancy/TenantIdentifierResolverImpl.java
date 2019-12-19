/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * Resolves current tenant id based on {@link ThreadLocal} setting.
 */
public class TenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    /*
     * @see org.hibernate.context.spi.CurrentTenantIdentifierResolver#
     * resolveCurrentTenantIdentifier()
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
	return MultitenantContext.getTenantId();
    }

    /*
     * @see org.hibernate.context.spi.CurrentTenantIdentifierResolver#
     * validateExistingCurrentSessions()
     */
    @Override
    public boolean validateExistingCurrentSessions() {
	return true;
    }
}