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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

public class GTSOutput {

    private String className;
    private Map<String, String> labels;
    private Map<String, String> attributes;
    private String id;
    private List<DataPoint> points;

    public static List<GTSOutput> fromOutputFormat(String output) {

	if (output == null || output.equals(""))
	    return new ArrayList<>();

	final String regex = "\\{(\"c\"):(?<c>.*?),(\"l\"):(?<l>\\{.*?\\}),(\"a\"):(?<a>\\{.*?\\}),((\"i\"):(?<i>\".*?\"),)?(\"la\"):(?<la>.*?),(\"v\"):(?<v>\\[\\[.*?]])\\}";

	@SuppressWarnings("unused")
	Gson gson = new Gson();

	Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(output);

	List<GTSOutput> outputs = new ArrayList<>();

	while (matcher.find()) {
	    GTSOutput gts = new GTSOutput();

	    gts.className = stripExtraQuotes(matcher.group("c"));
	    gts.labels = populateMap(matcher.group("l"));
	    gts.attributes = populateMap(matcher.group("a"));
	    gts.id = stripExtraQuotes(matcher.group("i"));
	    gts.points = DataPoint.extractDataPoint(matcher.group("v"));

	    outputs.add(gts);
	}

	return outputs;
    }

    private static String stripExtraQuotes(String string) {
	if (string != null) {
	    return string.replaceAll("\"", "");
	}
	return "";
    }

    private static Map<String, String> populateMap(String source) {
	source = source.replace("{", "").replace("}", "");

	String[] groups = source.split(",");

	if (!source.equals("") && groups.length > 0) {
	    return Stream.of(groups).collect(Collectors.toMap(GTSOutput::extractMapKey, GTSOutput::extractMapValue));
	}
	return new HashMap<>();
    }

    private static String extractMapValue(String item) {
	return stripExtraQuotes(item.split(":")[1]);
    }

    private static String extractMapKey(String item) {
	return stripExtraQuotes(item.split(":")[0]);
    }

    public String getClassName() {
	return className;
    }

    public Map<String, String> getLabels() {
	return labels;
    }

    public Map<String, String> getAttributes() {
	return attributes;
    }

    public String getId() {
	return id;
    }

    public List<DataPoint> getPoints() {
	return points;
    }
}
