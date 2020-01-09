/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.warp10;

import java.util.List;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.microservice.lifecycle.parameters.StringComponentParameter;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleComponentParameter;
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

    /** Hostname parameter */
    private ILifecycleComponentParameter<String> baseUrl;

    /** Port parameter */
    private ILifecycleComponentParameter<String> tokenSecret;

    private Warp10RestClient warp10RestClient;

    public Warp10Client(Warp10Configuration configuration) {
	this.configuration = configuration;
    }

    /*
     * @see
     * com.sitewhere.microservice.lifecycle.LifecycleComponent#initializeParameters(
     * )
     */
    @Override
    public void initializeParameters() throws SiteWhereException {
	// Add base URL.
	this.baseUrl = StringComponentParameter.newBuilder(this, "Base URL").value(getConfiguration().getBaseUrl())
		.makeRequired().build();
	getParameters().add(baseUrl);

	// Add token secret.
	this.tokenSecret = StringComponentParameter.newBuilder(this, "Token secret")
		.value(getConfiguration().getTokenSecret()).makeRequired().build();
	getParameters().add(tokenSecret);
    }

    /*
     * @see com.sitewhere.microservice.lifecycle.LifecycleComponent#initialize(com.
     * sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void initialize(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	super.initialize(monitor);
	this.warp10RestClient = Warp10RestClient.newBuilder().withConnectionTo(getConfiguration().getBaseUrl(),
		getConfiguration().getTokenSecret(), getTenantEngine().getName()).build();
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
