/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import com.sitewhere.rdb.spi.IConnectionInformation;
import com.sitewhere.rdb.spi.IDatabaseCreationProvider;

/**
 * Holds information particular to a RDB persistence provider.
 */
public abstract class RdbProviderInformation<T extends IConnectionInformation> {

    /** Provider name */
    private String name;

    /** Provider description */
    private String description;

    /** Hibernate dialect */
    private String dialect;

    /** Template for database name */
    private String databaseNameTemplate;

    /** Template for command to create database */
    private IDatabaseCreationProvider createDatabaseProvider;

    /** Classpath location of Flyway migration */
    private String flywayMigrationPath;

    /** Connection information */
    private T connectionInfo;

    public RdbProviderInformation(T connectionInfo) {
	this.connectionInfo = connectionInfo;
    }

    /**
     * Build JDBC URL for connecting as DB creation user.
     * 
     * @return
     */
    public abstract String buildRootJdbcUrl();

    /**
     * Implemented in subclasses to build the JDBC URL.
     * 
     * @param dbname
     * @return
     */
    public abstract String buildJdbcUrl(String dbname);

    public T getConnectionInfo() {
	return connectionInfo;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDialect() {
	return dialect;
    }

    public void setDialect(String dialect) {
	this.dialect = dialect;
    }

    public String getDatabaseNameTemplate() {
	return databaseNameTemplate;
    }

    public void setDatabaseNameTemplate(String databaseNameTemplate) {
	this.databaseNameTemplate = databaseNameTemplate;
    }

    public IDatabaseCreationProvider getCreateDatabaseProvider() {
	return createDatabaseProvider;
    }

    public void setCreateDatabaseProvider(IDatabaseCreationProvider createDatabaseProvider) {
	this.createDatabaseProvider = createDatabaseProvider;
    }

    public String getFlywayMigrationPath() {
	return flywayMigrationPath;
    }

    public void setFlywayMigrationPath(String flywayMigrationPath) {
	this.flywayMigrationPath = flywayMigrationPath;
    }
}
