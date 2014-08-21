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
package org.openempi.webapp.client.model;

import java.io.Serializable;
import java.util.Map;

public class CustomFieldWeb extends BaseFieldWeb implements Serializable
{
	public static final String SOURCE_FIELD_NAME = "sourceFieldName";
	public static final String SOURCE_FIELD_NAME_DESCRIPTION = "sourceFieldNameDescription";
	public static final String TRANSFORMATION_FUNCTION_NAME = "transformationFunctionName";
	public static final String TRANSFORMATION_FUNCTION_NAME_DESCRIPTION = "transformationFunctionNameDescription";	
	

	public CustomFieldWeb() {
	}

	public java.lang.String getSourceFieldName() {
		return get(SOURCE_FIELD_NAME);
	}

	public void setSourceFieldName(java.lang.String sourceFieldName) {
		set(SOURCE_FIELD_NAME, sourceFieldName);
	}

	public java.lang.String getSourceFieldNameDescription() {
		return get(SOURCE_FIELD_NAME_DESCRIPTION);
	}

	public void setSourceFieldNameDescription(java.lang.String sourceFieldNameDescription) {
		set(SOURCE_FIELD_NAME_DESCRIPTION, sourceFieldNameDescription);
	}
	
	public java.lang.String getTransformationFunctionName() {
		return get(TRANSFORMATION_FUNCTION_NAME);
	}

	public void setTransformationFunctionName(java.lang.String transformationFunctionName) {
		set(TRANSFORMATION_FUNCTION_NAME, transformationFunctionName);
	}
	
	public java.lang.String getTransformationFunctionNameDescription() {
		return get(TRANSFORMATION_FUNCTION_NAME_DESCRIPTION);
	}

	public void setTransformationFunctionNameDescription(java.lang.String transformationFunctionNameDescription) {
		set(TRANSFORMATION_FUNCTION_NAME_DESCRIPTION, transformationFunctionNameDescription);
	}
	
	public Map<String, String> getConfigurationParameters() {
		return get("configurationParameters");
	}

	public void setConfigurationParameters(Map<String, String> configurationParameters) {
		set("configurationParameters", configurationParameters);
	}	
}
