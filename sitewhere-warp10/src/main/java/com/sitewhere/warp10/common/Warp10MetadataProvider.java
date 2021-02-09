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
package com.sitewhere.warp10.common;

import com.sitewhere.spi.common.IMetadataProvider;
import com.sitewhere.warp10.rest.GTSInput;
import com.sitewhere.warp10.rest.GTSOutput;

/**
 * Used to load or save device entity metdata to Warp 10.
 */
public class Warp10MetadataProvider {

    /**
     * Property for entity metadata
     */
    public static final String PROP_METADATA = "meta_";

    /**
     * Store data into a {@link GTSInput} using default property name.
     *
     * @param source
     * @param target
     */
    public static void toGTS(IMetadataProvider source, GTSInput target) {
	Warp10MetadataProvider.toGTS(PROP_METADATA, source, target);
    }

    /**
     * Store data into a {@link GTSInput}.
     *
     * @param propertyName
     * @param source
     * @param target
     */
    public static void toGTS(String propertyName, IMetadataProvider source, GTSInput target) {
	for (String key : source.getMetadata().keySet()) {
	    target.getAttributes().put(propertyName + key, source.getMetadata().get(key));
	}
    }

    /**
     * Load data from a DBObject using default property name.
     *
     * @param source
     * @param target
     */
    public static void fromGTS(GTSOutput source, IMetadataProvider target) {
	Warp10MetadataProvider.fromGTS(PROP_METADATA, source, target);
    }

    /**
     * Load data from a DBObject.
     *
     * @param propertyName
     * @param source
     * @param target
     */
    public static void fromGTS(String propertyName, GTSOutput source, IMetadataProvider target) {
	if (source.getAttributes() != null && source.getAttributes().size() > 0) {
	    for (String key : source.getAttributes().keySet()) {
		if (key.contains(PROP_METADATA)) {
		    target.getMetadata().put(key, source.getAttributes().get(key));
		}
	    }
	}
    }
}