/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.communication.mqtt;

/**
 * Fields that specify MQTT security parameters.
 */
public interface IMqttSecurityFields {

    /**
     * Get trust store path.
     * 
     * @return
     */
    String getTrustStorePath();

    /**
     * Get trust store password.
     * 
     * @return
     */
    String getTrustStorePassword();

    /**
     * Get key store path.
     * 
     * @return
     */
    String getKeyStorePath();

    /**
     * Get key store password.
     * 
     * @return
     */
    String getKeyStorePassword();

    /**
     * Get auth username.
     * 
     * @return
     */
    String getUsername();

    /**
     * Get auth password.
     * 
     * @return
     */
    String getPassword();
}
