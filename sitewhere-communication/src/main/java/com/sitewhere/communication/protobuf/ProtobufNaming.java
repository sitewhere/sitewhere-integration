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
package com.sitewhere.communication.protobuf;

import com.sitewhere.spi.device.IDeviceType;
import com.sitewhere.spi.device.command.IDeviceCommand;

/**
 * Common methods for naming scheme used in internal Google Protocol Buffer
 * representation.
 * 
 * @author Derek
 */
public class ProtobufNaming {

    /** Name of enum for message types */
    public static final String COMMAND_TYPES_ENUM = "Command";

    /** Name of command field in header */
    public static final String HEADER_COMMAND_FIELD_NAME = "command";

    /** Name of originator field in header */
    public static final String HEADER_ORIGINATOR_FIELD_NAME = "originator";

    /** Name of nested path field in header */
    public static final String HEADER_NESTED_PATH_FIELD_NAME = "nestedPath";

    /** Name of nested type field in header */
    public static final String HEADER_NESTED_TYPE_FIELD_NAME = "nestedType";

    /** Name associated with uuid message */
    public static final String UUID_MSG_NAME = "Uuid";

    /** Name associated with header message */
    public static final String HEADER_MSG_NAME = "_Header";

    /**
     * Get the device type identifier.
     * 
     * @param deviceType
     * @return
     */
    protected static String getDeviceTypeIdentifier(IDeviceType deviceType) {
	return "Spec_" + deviceType.getToken();
    }

    /**
     * Get the command enum entry name.
     * 
     * @param command
     * @return
     */
    protected static String getCommandEnumName(IDeviceCommand command) {
	return command.getName().toUpperCase();
    }
}