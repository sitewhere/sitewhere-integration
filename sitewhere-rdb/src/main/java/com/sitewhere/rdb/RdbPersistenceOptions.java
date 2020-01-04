/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

/**
 * Configurable options which control relational database persistence.
 */
public class RdbPersistenceOptions {

    /** RBMTODDL_Auto setting */
    private String hbmToDdlAuto = "validate";

    /** Display SQL in log */
    private boolean showSql = true;

    /** Format SQL in log */
    private boolean formatSql = false;

    public String getHbmToDdlAuto() {
	return hbmToDdlAuto;
    }

    public void setHbmToDdlAuto(String hbmToDdlAuto) {
	this.hbmToDdlAuto = hbmToDdlAuto;
    }

    public boolean isShowSql() {
	return showSql;
    }

    public void setShowSql(boolean showSql) {
	this.showSql = showSql;
    }

    public boolean isFormatSql() {
	return formatSql;
    }

    public void setFormatSql(boolean formatSql) {
	this.formatSql = formatSql;
    }
}
