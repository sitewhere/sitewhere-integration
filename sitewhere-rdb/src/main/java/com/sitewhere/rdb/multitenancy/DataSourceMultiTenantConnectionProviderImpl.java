/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

/**
 * Handles accessing data source for multiple tenants.
 */
public class DataSourceMultiTenantConnectionProviderImpl
	extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    /** Serial version UID */
    private static final long serialVersionUID = 3384522199689242438L;

    /** RDB data sources */
    private Map<String, DataSource> rdbDataSources;

    /*
     * @see org.hibernate.engine.jdbc.connections.spi.
     * AbstractDataSourceBasedMultiTenantConnectionProviderImpl#selectAnyDataSource(
     * )
     */
    @Override
    protected DataSource selectAnyDataSource() {
	DataSource selected = getRdbDataSources().get(MultitenantContext.getTenantId());
	return selected;
    }

    /*
     * @see org.hibernate.engine.jdbc.connections.spi.
     * AbstractDataSourceBasedMultiTenantConnectionProviderImpl#selectDataSource(
     * java.lang.String)
     */
    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
	DataSource dataSource = getRdbDataSources().get(tenantIdentifier);
	return dataSource;
    }

    /*
     * @see org.hibernate.engine.jdbc.connections.spi.
     * AbstractDataSourceBasedMultiTenantConnectionProviderImpl#getConnection(java.
     * lang.String)
     */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
	Connection connection = super.getConnection(tenantIdentifier);
	connection.setSchema(tenantIdentifier);
	return connection;
    }

    protected Map<String, DataSource> getRdbDataSources() {
	return rdbDataSources;
    }
}