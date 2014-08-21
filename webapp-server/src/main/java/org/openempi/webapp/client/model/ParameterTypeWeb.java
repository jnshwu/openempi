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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ParameterTypeWeb extends BaseModelData implements Serializable
{ 	
	public ParameterTypeWeb() {
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getDisplayName() {
		return get("displayName");
	}

	public void setDisplayName(java.lang.String displayName) {
		set("displayName", displayName);
	}
	
	public java.lang.String getDisplayType() {
		return get("displayType");
	}

	public void setDisplayType(java.lang.String displayType) {
		set("displayType", displayType);
	}
/*
	public FormEntryDisplayTypeWeb getDisplayType() {
		return get("displayType");
	}

	public void setDisplayType(FormEntryDisplayTypeWeb displayType) {
		set("displayType", displayType);
	}
*/
}
