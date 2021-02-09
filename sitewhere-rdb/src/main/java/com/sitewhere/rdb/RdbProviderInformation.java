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
