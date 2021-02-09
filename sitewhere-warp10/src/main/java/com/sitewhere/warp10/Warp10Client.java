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

import java.util.List;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.warp10.rest.GTSInput;
import com.sitewhere.warp10.rest.GTSOutput;
import com.sitewhere.warp10.rest.QueryParams;
import com.sitewhere.warp10.rest.Warp10RestClient;

/**
 * Client used for connecting to and interacting with an Warp 10 server.
 */
public class Warp10Client extends TenantEngineLifecycleComponent {

    /** Warp10 configuration parameters */
    private Warp10Configuration configuration;

    private Warp10RestClient warp10RestClient;

    public Warp10Client(Warp10Configuration configuration) {
	this.configuration = configuration;
    }

    /*
     * @see com.sitewhere.microservice.lifecycle.LifecycleComponent#initialize(com.
     * sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void initialize(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	super.initialize(monitor);
	String baseUrl = String.format("http://%s:%d/api/v0", getConfiguration().getHostname(), 8080);
	this.warp10RestClient = Warp10RestClient.newBuilder()
		.withConnectionTo(baseUrl, getConfiguration().getTokenSecret(), getConfiguration().getApplication())
		.build();
    }

    public int insertGTS(GTSInput gtsInput) throws SiteWhereException {
	return warp10RestClient.ingress(gtsInput);
    }

    public List<GTSOutput> findGTS(QueryParams queryParams) throws SiteWhereException {
	return warp10RestClient.fetch(queryParams);
    }

    public Warp10Configuration getConfiguration() {
	return configuration;
    }
}
