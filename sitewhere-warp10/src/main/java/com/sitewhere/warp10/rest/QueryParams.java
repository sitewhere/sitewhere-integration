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
package com.sitewhere.warp10.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class QueryParams {

    private final String SELECTOR = "selector";
    private final String START_DATE = "start";
    private final String STOP_DATE = "stop";
    private final String NOW = "now";
    private final String TIMESTAMP = "timespan";
    private final String INITIAL_DATE = "2000-01-01T00:00:00.000-00:00";
    private final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sssssssss'Z'";

    private Map<String, String> parameters = new HashMap<String, String>();

    private String className = "~.*";

    private Date startDate;

    private Date endDate;

    public static QueryParams builder() {
        QueryParams queryParams = new QueryParams();
        return queryParams;
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        query.append(getDateFilter());
        query.append("&").append(SELECTOR).append("=");
        query.append(className).append(getLabelParameters()).append("&format=json&showattr=true&dedup=true");
        return query.toString();
    }

    private String getLabelParameters() {
        return "{" + parameters.entrySet().stream().map(entry -> entry.getKey() + '~' + entry.getValue()).collect(Collectors.joining(",")) + "}";
    }

    private String getDateFilter() {
        StringBuilder filter = new StringBuilder();
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date now = new Date();

        if (startDate !=null && endDate != null) {
            filter.append(START_DATE).append("=").append(df.format(startDate)).append("&").append(STOP_DATE).append("=").append(df.format(endDate));
        } else if(startDate !=null && endDate == null) {
            filter.append(START_DATE).append("=").append(df.format(startDate)).append("&").append(STOP_DATE).append("=").append(df.format(now));
        } else if(startDate ==null && endDate != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT);
            DateTime initial = new DateTime(INITIAL_DATE);
            filter.append(START_DATE).append("=").append(fmt.print(initial)).append("&").append(STOP_DATE).append("=").append(df.format(endDate));
        } else {
            filter.append(NOW).append("=now").append("&").append(TIMESTAMP).append("=-10");
        }

        return filter.toString();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
