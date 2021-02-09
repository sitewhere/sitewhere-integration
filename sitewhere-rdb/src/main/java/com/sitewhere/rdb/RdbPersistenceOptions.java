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
