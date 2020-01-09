/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.warp10;

/**
 * Common configuration settings for an Warp 10 client.
 */
public class Warp10Configuration {

    /** Default token.secret */
    private static final String DEFAULT_TOKEN_SECRET = "sitewhere";

    /** Base URL */
    private String baseUrl;

    /** Token secret */
    private String tokenSecret = DEFAULT_TOKEN_SECRET;

    /** Application */
    private String application;

    public String getBaseUrl() {
	return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
	this.baseUrl = baseUrl;
    }

    public String getTokenSecret() {
	return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
	this.tokenSecret = tokenSecret;
    }

    public String getApplication() {
	return application;
    }

    public void setApplication(String application) {
	this.application = application;
    }
}
