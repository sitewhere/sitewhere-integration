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