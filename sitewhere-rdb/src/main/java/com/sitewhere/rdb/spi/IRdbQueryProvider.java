/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.spi;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.sitewhere.spi.SiteWhereException;

/**
 * Provides details required to execute a criteria query.
 */
public interface IRdbQueryProvider<T> {

    /**
     * Add predicates used for criteria.
     * 
     * @param cb
     * @param predicates
     * @param root
     * @throws SiteWhereException
     */
    void addPredicates(CriteriaBuilder cb, List<Predicate> predicates, Root<T> root) throws SiteWhereException;

    /**
     * Add sort criteria.
     * 
     * @param cb
     * @param root
     * @param query
     */
    CriteriaQuery<T> addSort(CriteriaBuilder cb, Root<T> root, CriteriaQuery<T> query);
}
