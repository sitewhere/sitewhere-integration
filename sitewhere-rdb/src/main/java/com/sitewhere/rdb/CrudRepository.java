/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import java.util.Optional;

public interface CrudRepository<E, K> {

    /**
     * Find an entity by id.
     * 
     * @param key
     * @return
     */
    Optional<E> findById(K key);

    /**
     * Persist entity in repository.
     * 
     * @param source
     * @return
     */
    E save(E source);

    /**
     * Delete an entity by id.
     * 
     * @param key
     */
    void deleteById(K key);
}
