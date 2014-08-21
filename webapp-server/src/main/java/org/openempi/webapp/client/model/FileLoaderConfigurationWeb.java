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
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.user.client.ui.Widget;

public class FileLoaderConfigurationWeb extends BaseModelData implements Serializable
{ 
	@SuppressWarnings("unused")
    private ParameterTypeWeb unusedParameterTypeWeb;
	
	public FileLoaderConfigurationWeb() {
	}
	
	public java.lang.String getLoaderName() {
		return get("loaderName");
	}

	public void setLoaderName(java.lang.String loaderName) {
		set("loaderName", loaderName);
	}
	
	public List<ParameterTypeWeb> getParameterTypes() {
		return get("parameterTypes");
	}

	public void setParameterTypes(List<ParameterTypeWeb> parameterTypes) {
		set("parameterTypes", parameterTypes);
	}	
	
	public List<Field<?>> getFields() {
		return get("fields");
	}

	public void setFields(List<Field<?>> fields) {
		set("fields", fields);
	}	
}
