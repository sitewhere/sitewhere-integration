/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.communication.mqtt;

/**
 * Common configuration for components which have MQTT connectivity.
 */
public interface IMqttConfiguration {

    /**
     * Get connection protocol for broker.
     * 
     * @return
     */
    String getProtocol();

    /**
     * Get hostname for broker.
     * 
     * @return
     */
    String getHostname();

    /**
     * Get port for broker.
     * 
     * @return
     */
    int getPort();

    /**
     * Get MQTT topic.
     * 
     * @return
     */
    String getTopic();

    /**
     * Get number of threads used for processing.
     * 
     * @return
     */
    int getNumThreads();

    /**
     * Get quality of service setting.F
     * 
     * @return
     */
    String getQos();

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

    /**
     * Get client id.
     * 
     * @return
     */
    String getClientId();

    /**
     * Flag for whether to use clean session.
     * 
     * @return
     */
    boolean isCleanSession();
}
