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
import java.util.List;

public class LoaderDataFieldWeb extends BaseFieldWeb implements Serializable
{
	public static final String FORMAT_STRING = "formatString";
	public static final String FUNCTION_NAME = "functionName";
	public static final String LOADER_FIELD_COMPOSITION = "loaderFieldComposition";
	public static final String LOADER_SUB_FIELDS = "loaderSubFields";

	public LoaderFieldCompositionWeb dummyField1;	// This is need so LoaderDataFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	public LoaderSubFieldWeb dummyField2;	// This is need so LoaderDataFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public LoaderDataFieldWeb() {
	}

	public LoaderDataFieldWeb(String fieldName) {
		set(FIELD_NAME, fieldName);
	}
	
	public LoaderDataFieldWeb(String fieldName, String formatString, String functionName) {
		set(FIELD_NAME, fieldName);
		set(FORMAT_STRING, formatString);
		set(FUNCTION_NAME, functionName);
	}
	
	public java.lang.String getFormatString() {
		return get(FORMAT_STRING);
	}

	public void setFormatString(java.lang.String formatString) {
		set(FORMAT_STRING, formatString);
	}

	public java.lang.String getFunctionName() {
		return get(FUNCTION_NAME);
	}

	public void setFunctionName(java.lang.String functionName) {
		set(FUNCTION_NAME, functionName);
	}

	public org.openempi.webapp.client.model.LoaderFieldCompositionWeb getLoaderFieldComposition() {
		return get(LOADER_FIELD_COMPOSITION);
	}

	public void setLoaderFieldComposition(org.openempi.webapp.client.model.LoaderFieldCompositionWeb loaderFieldComposition) {
		set(LOADER_FIELD_COMPOSITION, loaderFieldComposition);
	}

	public List<org.openempi.webapp.client.model.LoaderSubFieldWeb> getLoaderSubFields() {
		return get(LOADER_SUB_FIELDS);
	}

	public void setLoaderSubFields(List<org.openempi.webapp.client.model.LoaderSubFieldWeb> loaderSubFields) {
		set(LOADER_SUB_FIELDS, loaderSubFields);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("," + FORMAT_STRING + ": ").append(getFormatString());
		sb.append("," + FUNCTION_NAME + ": ").append(getFunctionName());
		sb.append("," + LOADER_FIELD_COMPOSITION + ": ").append(getLoaderFieldComposition().toString());
		sb.append("," + LOADER_SUB_FIELDS + ": ").append(getLoaderSubFields().toString());
		return sb.toString();
	}
}
