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
