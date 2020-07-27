/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb.spi;

import com.sitewhere.spi.SiteWhereException;

public interface ITransactionCallback<T> {

    /**
     * Execute code in the context of a transaction.
     * 
     * @return
     * @throws SiteWhereException
     */
    T process() throws SiteWhereException;
}
