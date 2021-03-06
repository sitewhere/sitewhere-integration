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

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;

/**
 * Client used for connecting to and interacting with an InfluxDB server.
 */
public class InfluxDbClient extends TenantEngineLifecycleComponent {

    /** InfluxDB configuration parameters */
    private InfluxDbConfiguration configuration;

    /** InfluxDB handle */
    private InfluxDB influx;

    public InfluxDbClient(InfluxDbConfiguration configuration) {
	this.configuration = configuration;
    }

    /*
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#initialize(com.sitewhere.
     * spi.server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void initialize(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	super.initialize(monitor);

	String connectionUrl = "http://" + getConfiguration().getHostname() + ":" + getConfiguration().getPort();
	this.influx = InfluxDBFactory.connect(connectionUrl, getConfiguration().getUsername(),
		getConfiguration().getPassword());
	getLogger().info(String.format("Connecting to InfluxDB using URL '%s' and database '%s'.", connectionUrl,
		getConfiguration().getDatabase()));
	QueryResult result = getInflux()
		.query(new Query(String.format("CREATE DATABASE \"%s\";", getConfiguration().getDatabase()), ""));
	String error = result.getError();
	if (error != null) {
	    throw new SiteWhereException(String.format("Unable to execute InfluxDB database create query: %s", error));
	} else {
	    getLogger().info("Connected successfully.");
	}
	if (getConfiguration().isEnableBatch()) {
	    getLogger().info(String.format("InfluxDB batch mode enabled with interval set to %s ms.",
		    String.valueOf(getConfiguration().getBatchIntervalMs())));
	    getInflux().enableBatch(getConfiguration().getBatchChunkSize(), getConfiguration().getBatchIntervalMs(),
		    TimeUnit.MILLISECONDS);
	}
	LogLevel level = convertLogLevel(getConfiguration().getLogLevel());
	getInflux().setLogLevel(level);
	getLogger().info(String.format("InfluxDB log level set to %s", level.name()));
    }

    /**
     * Convert log level setting to expected enum value.
     * 
     * @param level
     * @return
     */
    protected LogLevel convertLogLevel(String level) {
	if ((level == null) || (level.equalsIgnoreCase("none"))) {
	    return LogLevel.NONE;
	} else if (level.equalsIgnoreCase("basic")) {
	    return LogLevel.BASIC;
	} else if (level.equalsIgnoreCase("headers")) {
	    return LogLevel.HEADERS;
	} else if (level.equalsIgnoreCase("full")) {
	    return LogLevel.FULL;
	}
	return LogLevel.NONE;
    }

    public InfluxDbConfiguration getConfiguration() {
	return configuration;
    }

    public void setConfiguration(InfluxDbConfiguration configuration) {
	this.configuration = configuration;
    }

    public InfluxDB getInflux() {
	return influx;
    }

    public void setInflux(InfluxDB influx) {
	this.influx = influx;
    }
}