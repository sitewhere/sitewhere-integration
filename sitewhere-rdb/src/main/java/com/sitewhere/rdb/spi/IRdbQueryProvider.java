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
