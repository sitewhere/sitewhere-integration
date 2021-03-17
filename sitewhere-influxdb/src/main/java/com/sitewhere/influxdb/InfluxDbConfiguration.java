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
package com.sitewhere.influxdb;

import com.fasterxml.jackson.databind.JsonNode;
import com.sitewhere.microservice.configuration.json.JsonConfiguration;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;

/**
 * Manages properties used to configure an InfluxDB connection.
 */
public class InfluxDbConfiguration extends JsonConfiguration {

    /** Default hostname */
    private static final String DEFAULT_HOSTNAME = "sitewhere-infrastructure-influxdb.sitewhere-system";

    /** Default port */
    private static final int DEFAULT_PORT = 8086;

    /** Default username */
    private static final String DEFAULT_USERNAME = "root";

    /** Default password */
    private static final String DEFAULT_PASSWORD = "root";

    /** Default database */
    private static final String DEFAULT_DATABASE = "tenant_${tenant.id}";

    /** Default retention */
    private static final String DEFAULT_RETENTION = "autogen";

    /** InfluxDB hostname */
    private String hostname = DEFAULT_HOSTNAME;

    /** InfluxDB port */
    private int port = DEFAULT_PORT;

    /** Username */
    private String username = DEFAULT_USERNAME;

    /** Password */
    private String password = DEFAULT_PASSWORD;

    /** Database name */
    private String database = DEFAULT_DATABASE;

    /** Retention policy */
    private String retention = DEFAULT_RETENTION;

    /** Indicates if batch delivery is enabled */
    private boolean enableBatch = true;

    /** Max records in batch */
    private int batchChunkSize = 2000;

    /** Max time to wait for sending batch */
    private int batchIntervalMs = 100;

    /** Log level */
    private String logLevel;

    public InfluxDbConfiguration(ITenantEngineLifecycleComponent component) {
	super(component);
    }

    public InfluxDbConfiguration loadFrom(JsonNode configuration) throws SiteWhereException {
	InfluxDbConfiguration influx = new InfluxDbConfiguration(getComponent());
	this.hostname = configurableString("hostname", configuration, DEFAULT_HOSTNAME);
	this.port = configurableInt("port", configuration, DEFAULT_PORT);
	this.username = configurableString("username", configuration, DEFAULT_USERNAME);
	this.password = configurableString("password", configuration, DEFAULT_PASSWORD);
	this.database = configurableString("databaseName", configuration, DEFAULT_DATABASE);
	this.retention = configurableString("retention", configuration, DEFAULT_RETENTION);
	return influx;
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

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getDatabase() {
	return database;
    }

    public void setDatabase(String database) {
	this.database = database;
    }

    public String getRetention() {
	return retention;
    }

    public void setRetention(String retention) {
	this.retention = retention;
    }

    public boolean isEnableBatch() {
	return enableBatch;
    }

    public void setEnableBatch(boolean enableBatch) {
	this.enableBatch = enableBatch;
    }

    public int getBatchChunkSize() {
	return batchChunkSize;
    }

    public void setBatchChunkSize(int batchChunkSize) {
	this.batchChunkSize = batchChunkSize;
    }

    public int getBatchIntervalMs() {
	return batchIntervalMs;
    }

    public void setBatchIntervalMs(int batchIntervalMs) {
	this.batchIntervalMs = batchIntervalMs;
    }

    public String getLogLevel() {
	return logLevel;
    }

    public void setLogLevel(String logLevel) {
	this.logLevel = logLevel;
    }
}
