/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.communication.mqtt;

/**
 * Fields that specify MQTT connection parameters.
 */
public interface IMqttConnectionFields {

    /** Default protocol for broker connection */
    public static final String DEFAULT_PROTOCOL = "tcp";

    /** Default hostname for broker connection */
    public static final String DEFAULT_HOSTNAME = "mqtt";

    /** Default port for broker connection */
    public static final int DEFAULT_PORT = 1883;

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
