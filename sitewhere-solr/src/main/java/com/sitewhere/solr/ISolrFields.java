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

/**
 * Contains constants for Solr field names.
 */
public interface ISolrFields {

    /** Unique device event identifier */
    public static final String EVENT_ID = "eventId";

    /** Event type indicator */
    public static final String EVENT_TYPE = "eventType";

    /** Unique device identifier */
    public static final String DEVICE_ID = "deviceId";

    /** Unique device assignment identifier */
    public static final String ASSIGNMENT_ID = "assignmentId";

    /** Unique asset identifier */
    public static final String ASSET_ID = "assetId";

    /** Unique area identifier */
    public static final String AREA_ID = "areaId";

    /** Event date */
    public static final String EVENT_DATE = "eventDate";

    /** Event received date */
    public static final String RECEIVED_DATE = "receivedDate";

    /** Measurement name */
    public static final String MX_NAME = "mxName";

    /** Measurement value */
    public static final String MX_VALUE = "mxValue";

    /** Location */
    public static final String LOCATION = "location";

    /** Elevation */
    public static final String ELEVATION = "elevation";

    /** Alert type */
    public static final String ALERT_TYPE = "alertType";

    /** Alert message */
    public static final String ALERT_MESSAGE = "alertMessage";

    /** Alert level */
    public static final String ALERT_LEVEL = "alertLevel";

    /** Alert source */
    public static final String ALERT_SOURCE = "alertSource";

    /** Prefix for metadata fields */
    public static final String META_PREFIX = "meta.";
}