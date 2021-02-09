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
package com.sitewhere.warp10;

import com.fasterxml.jackson.databind.JsonNode;
import com.sitewhere.microservice.configuration.json.JsonConfiguration;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ITenantEngineLifecycleComponent;

/**
 * Manages properties used to configure a Warp 10 connection.
 */
public class Warp10Configuration extends JsonConfiguration {

    /** Default hostname */
    private static final String DEFAULT_HOSTNAME = "http://sitewhere-warp10.sitewhere-system:8080/api/v0";

    /** Default port */
    private static final int DEFAULT_PORT = 8080;

    /** Default application */
    private static final String DEFAULT_APPLICATION = "sitewhere";

    /** Default token secret */
    private static final String DEFAULT_TOKEN_SECRET = "sitewhere";

    /** Hostname */
    private String hostname;

    /** Port */
    private int port;

    /** Application */
    private String application;

    /** Token secret */
    private String tokenSecret;

    public Warp10Configuration(ITenantEngineLifecycleComponent component) {
	super(component);
    }

    public Warp10Configuration loadFrom(JsonNode configuration) throws SiteWhereException {
	Warp10Configuration warp10 = new Warp10Configuration(getComponent());
	this.hostname = configurableString("hostname", configuration, DEFAULT_HOSTNAME);
	this.port = configurableInt("port", configuration, DEFAULT_PORT);
	this.application = configurableString("application", configuration, DEFAULT_APPLICATION);
	this.tokenSecret = configurableString("tokenSecret", configuration, DEFAULT_TOKEN_SECRET);
	return warp10;
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

    public String getApplication() {
	return application;
    }

    public void setApplication(String application) {
	this.application = application;
    }

    public String getTokenSecret() {
	return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
	this.tokenSecret = tokenSecret;
    }
}
