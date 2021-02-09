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
package com.sitewhere.communication.mqtt;

import java.util.concurrent.TimeUnit;

import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Extends {@link TenantEngineLifecycleComponent} with base functionality for
 * connecting to MQTT.
 */
public class MqttLifecycleComponent extends TenantEngineLifecycleComponent {

    /** Default connection timeout in seconds */
    public static final long DEFAULT_CONNECT_TIMEOUT_SECS = 5;

    /** Configuration parameters */
    private IMqttConfiguration configuration;

    /** MQTT client */
    private MQTT mqtt;

    public MqttLifecycleComponent(LifecycleComponentType type, IMqttConfiguration configuration) {
	super(type);
	this.configuration = configuration;
    }

    /**
     * Gets information about the broker.
     * 
     * @return
     * @throws SiteWhereException
     */
    public String getBrokerInfo() throws SiteWhereException {
	return mqtt.getHost().toString();
    }

    /**
     * Get a {@link FutureConnection} to the MQTT broker.
     * 
     * @return
     * @throws SiteWhereException
     */
    public FutureConnection getConnection() throws SiteWhereException {
	FutureConnection connection = mqtt.futureConnection();
	try {
	    Future<Void> future = connection.connect();
	    future.await(DEFAULT_CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS);
	    return connection;
	} catch (Exception e) {
	    throw new SiteWhereException("Unable to connect to MQTT broker.", e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#start(com.sitewhere.spi
     * .server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void start(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	this.mqtt = MqttConfigurer.configure(configuration);
    }

    protected IMqttConfiguration getConfiguration() {
	return configuration;
    }
}