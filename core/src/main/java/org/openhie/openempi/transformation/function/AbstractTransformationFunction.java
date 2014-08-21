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
package org.openhie.openempi.transformation.function;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTransformationFunction implements TransformationFunction
{
	protected final Log log = LogFactory.getLog(getClass());

	private Map<String, String> configuration;

	private String name;
	
	public AbstractTransformationFunction() {
		configuration = new java.util.HashMap<String,String>();
	}
	
	public AbstractTransformationFunction(String name) {
		this.name = name;
	}
	
	public void init(Map<String, String> configParameters) {
		configuration = configParameters;
	}

	public abstract Object transform(Object field);

	protected String getParameter(String parameterName) {
		return getConfiguration().get(parameterName);
	}
	
	protected String getParameter(String parameterName, String defaultValue) {
		String parameter = getConfiguration().get(parameterName);
		if (parameter == null) {
			return defaultValue;
		}
		return parameter;
	}
	
	protected Integer getParameterAsInteger(String parameterName) {
		String valueStr = getConfiguration().get(parameterName);
		if (valueStr == null) {
			return null;
		}
		try {
			Integer value = Integer.parseInt(valueStr);
			return value;
		} catch (NumberFormatException e) {
			log.warn("Parameter " + parameterName + " has an invalid integer value of " + valueStr);
			return null;
		}
	}

	public void setParameter(String parameterName, String parameterValue) {
		configuration.put(parameterName, parameterValue);
	}
	
	public String[] getParameterNames() {
		return new String[]{};
	}
	
	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
