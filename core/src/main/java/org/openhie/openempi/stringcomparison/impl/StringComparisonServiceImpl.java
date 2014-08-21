/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.stringcomparison.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ValidationException;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.openhie.openempi.stringcomparison.DistanceMetricType;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.stringcomparison.metrics.DistanceMetric;

public class StringComparisonServiceImpl extends BaseServiceImpl implements StringComparisonService
{
	private HashMap<String,DistanceMetric> distanceMetricTypeMap;
	private final static String DEFAULT_DATE_FORMAT = "yyyy.MM.dd";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	private Boolean missingValuesMatch = Boolean.FALSE;

	public double score(String metricType, Object value1, Object value2) {
		DistanceMetric distanceMetric = getDistanceMetric(metricType);
		return distanceMetric.score(value1, value2);
	}
	
	public double score(String metricType, Map<String,String> parameters, Object value1, Object value2) {
		DistanceMetric distanceMetric = getDistanceMetric(metricType);
		distanceMetric.setParameters(parameters);
		distanceMetric.setMissingValuesMatch(getMissingValuesMatch());
		return distanceMetric.score(convertToString(value1), convertToString(value2));
	}

	// Factory method pattern
	private DistanceMetric getDistanceMetric(String metricTypeName) {
		DistanceMetric distanceMetric = distanceMetricTypeMap.get(metricTypeName);
		if (distanceMetric == null) {
			log.error("Unknown distance metric requested: " + metricTypeName);
			throw new ValidationException("Unknown distance metric requested for string comparision: " + distanceMetric);
		}
		return distanceMetric;
	}
	
	public DistanceMetricType getDistanceMetricType(String name) {
		DistanceMetric metric = distanceMetricTypeMap.get(name);
		if (metric == null) {
			return null;
		}
		return new DistanceMetricType(name, metric);
	}

	public DistanceMetricType[] getDistanceMetricTypes() {
		DistanceMetricType[] list = new DistanceMetricType[distanceMetricTypeMap.keySet().size()];
		int index=0;
		for (String key : distanceMetricTypeMap.keySet()) {
			list[index++] = new DistanceMetricType(key, distanceMetricTypeMap.get(key));
		}
		return list;
	}
	
	public List<String> getComparisonFunctionNames()
	{
		List<String> comparisonFunctionNames = new ArrayList<String>();
		for (String key : distanceMetricTypeMap.keySet()) {
			comparisonFunctionNames.add(key);
		}
		return comparisonFunctionNames;
	}

	private Object convertToString(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return value;
		}
		if (value instanceof java.util.Date) {
			java.util.Date dateValue = (java.util.Date) value;
			return dateFormatter.format(dateValue);
		}
		return value.toString();
	}

	public HashMap<String, DistanceMetric> getDistanceMetricTypeMap() {
		return distanceMetricTypeMap;
	}

	public void setDistanceMetricTypeMap(HashMap<String, DistanceMetric> distanceMetricTypeMap) {
		this.distanceMetricTypeMap = distanceMetricTypeMap;
	}

	public Boolean getMissingValuesMatch() {
		return missingValuesMatch;
	}

	public void setMissingValuesMatch(Boolean missingValuesMatch) {
		this.missingValuesMatch = missingValuesMatch;
	}
}
