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
