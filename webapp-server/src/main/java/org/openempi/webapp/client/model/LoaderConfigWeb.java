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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LoaderConfigWeb extends BaseModelData implements Serializable
{
	public static final String HEADER_LINE_PRESENT = "headerLinePresent";
	public static final String LOADER_DATA_FIELDS = "loaderDataFields";

	public LoaderDataFieldWeb dummyField;	// This is need so LoaderDataFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file

	public LoaderConfigWeb() {
	}

	public java.lang.Boolean getHeaderLinePresent() {
		return get(HEADER_LINE_PRESENT);
	}

	public void setHeaderLinePresent(java.lang.Boolean headerLinePresent) {
		set(HEADER_LINE_PRESENT, headerLinePresent);
	}

	public List<org.openempi.webapp.client.model.LoaderDataFieldWeb> getLoaderDataFields() {
		return get(LOADER_DATA_FIELDS);
	}

	public void setLoaderDataFields(List<org.openempi.webapp.client.model.LoaderDataFieldWeb> loaderDataFields) {
		set(LOADER_DATA_FIELDS, loaderDataFields);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(HEADER_LINE_PRESENT + ": ").append(getHeaderLinePresent());
		sb.append("," + LOADER_DATA_FIELDS + ": ").append(getLoaderDataFields().toString());
		return sb.toString();
	}
}
