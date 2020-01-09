/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Creates client connection to an Apache Solr instance.
 */
public class SolrConnection extends TenantEngineLifecycleComponent {

    /** Solr configuration */
    private SolrConfiguration solrConfiguration;

    /** Solr client instance */
    private SolrClient solrClient;

    public SolrConnection(SolrConfiguration solrConfiguration) {
	super(LifecycleComponentType.Other);
	this.solrConfiguration = solrConfiguration;
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
	getLogger().info("Solr initializing with URL: " + getSolrConfiguration().getSolrServerUrl());
	setSolrClient(new HttpSolrClient.Builder(getSolrConfiguration().getSolrServerUrl()).build());
    }

    public SolrConfiguration getSolrConfiguration() {
	return solrConfiguration;
    }

    public void setSolrConfiguration(SolrConfiguration solrConfiguration) {
	this.solrConfiguration = solrConfiguration;
    }

    public SolrClient getSolrClient() {
	return solrClient;
    }

    public void setSolrClient(SolrClient solrClient) {
	this.solrClient = solrClient;
    }
}