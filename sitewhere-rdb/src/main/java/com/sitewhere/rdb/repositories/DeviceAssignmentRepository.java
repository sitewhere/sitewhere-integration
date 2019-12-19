/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.repositories;

import java.util.Optional;
import java.util.UUID;

import com.sitewhere.rdb.CrudRepository;
import com.sitewhere.rdb.JpaSpecificationExecutor;
import com.sitewhere.rdb.entities.DeviceAssignment;

public interface DeviceAssignmentRepository
	extends CrudRepository<DeviceAssignment, UUID>, JpaSpecificationExecutor<DeviceAssignment> {

    Optional<DeviceAssignment> findByToken(String token);
}
