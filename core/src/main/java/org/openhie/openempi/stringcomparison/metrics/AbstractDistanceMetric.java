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
package org.openhie.openempi.stringcomparison.metrics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDistanceMetric implements DistanceMetric
{
	protected final Log log = LogFactory.getLog(getClass());
	
	private String name;
	private Map<String,Object> parameters;
	private boolean missingValuesMatch = false;
	
	public AbstractDistanceMetric() {
		parameters = new HashMap<String,Object>();
	}
	
	public AbstractDistanceMetric(Map<String,Object> parameters) {
		this.parameters = parameters;
	}
	
	public AbstractDistanceMetric(String name) {
		this.name = name;
		parameters = new HashMap<String,Object>();
	}
	
	public AbstractDistanceMetric(String name, Map<String,Object> parameters) {
		this.name = name;
		this.parameters = parameters;
	}
	
	public String upperCase(String value) {
		if (value == null) {
			return null;
		}
		return value.toUpperCase();
	}
	
	public boolean missingValues(Object value1, Object value2) {
		if (value1 == null || value2 == null || value1.toString().length() == 0 || value2.toString().length() == 0) {
			return true;
		}
		return false;
	}
	
	
	public double handleMissingValues(Object value1, Object value2) {
		double distance = 0;
		if (value1 == null && value2 != null || value1 != null && value2 == null) {
			;
		} else if ((value1 == null && value2 == null) || (value1.toString().length() == 0 && value2.toString().length() == 0)) {
			if (isMissingValuesMatch()) {
				distance = 1.0;
			}
		}
		log.trace("Computed the distance between :" + value1 + ": and :" + value2 + ": to be " + distance);
		return distance;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParameter(String key, Object value) {
		this.parameters.put(key, value);
	}

	public Object getParameter(String key) {
		return this.parameters.get(key);
	}

	public Map<String,Object> getParameters() {
		return this.parameters;
	}
	
	public void setParameters(Map<String,String> parameters) {
		for (String parameterName : parameters.keySet()) {
			setParameter(parameterName, parameters.get(parameterName));
		}
	}
	
	public void setMissingValuesMatch(boolean missingValuesMatch) {
		this.missingValuesMatch = missingValuesMatch;
	}
	
	public boolean isMissingValuesMatch() {
		return missingValuesMatch;
	}
	
	public String toString() {
		return name;
	}
}
