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
package org.openhie.openempi.configuration;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CustomField encapsulates a custom field that contains generated information from another field
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
public class CustomField extends BaseField
{
	private static final long serialVersionUID = 1224297603229522913L;

	private String sourceFieldName;
	private String transformationFunctionName;
	private Map<String, String> configurationParameters;

	public CustomField() {
		super();
		configurationParameters = new java.util.HashMap<String,String>();
	}

	public String getSourceFieldName() {
		return sourceFieldName;
	}

	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}

	public String getTransformationFunctionName() {
		return transformationFunctionName;
	}

	public void setTransformationFunctionName(String transformationFunctionName) {
		this.transformationFunctionName = transformationFunctionName;
	}

	public void addConfigurationParameter(String parameterName, String parameterValue) {
		configurationParameters.put(parameterName, parameterValue);
	}
	
	public boolean hasConfigurationParameters() {
		if (configurationParameters.keySet().size() > 0) {
			return true;
		}
		return false;
	}
	
	public Map<String, String> getConfigurationParameters() {
		return configurationParameters;
	}

	public void setConfigurationParameters(Map<String, String> configurationParameters) {
		this.configurationParameters = configurationParameters;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof CustomField))
			return false;
		CustomField castOther = (CustomField) other;
		return new EqualsBuilder().append(fieldName, castOther.fieldName).append(sourceFieldName,
				castOther.sourceFieldName).append(transformationFunctionName, castOther.transformationFunctionName).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(fieldName).append(sourceFieldName).append(transformationFunctionName).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(super.toString())
				.append("sourceFieldName", sourceFieldName)
				.append("transformationFunctionName", transformationFunctionName)
				.toString();
	}

}
