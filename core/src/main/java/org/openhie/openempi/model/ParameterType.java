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
package org.openhie.openempi.model;

import java.util.Arrays;

public class ParameterType extends BaseObject
{
	private static final long serialVersionUID = -871692721916993525L;

	private String name;
	private String displayName;
	private FormEntryDisplayType displayType;
	private Object[] valueList;

	public ParameterType(String name, String displayName, FormEntryDisplayType displayType, Object[] values) {
		this.name = name;
		this.displayName = displayName;
		this.displayType = displayType;
	}
	
	public ParameterType(String name, String displayName, FormEntryDisplayType displayType) {
		this.name = name;
		this.displayName = displayName;
		this.displayType = displayType;
	}
	
	public ParameterType(String name, FormEntryDisplayType displayType, Object[] values) {
		this.name = name;
		this.displayType = displayType;
	}

	public ParameterType(String name, FormEntryDisplayType displayType) {
		this.name = name;
		this.displayType = displayType;
	}

	public ParameterType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public FormEntryDisplayType getDisplayType() {
		return displayType;
	}

	public void setDisplayType(FormEntryDisplayType displayType) {
		this.displayType = displayType;
	}

	public Object[] getValueList() {
		return valueList;
	}

	public void setValueList(Object[] valueList) {
		this.valueList = valueList;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterType other = (ParameterType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ParameterType [name=" + name + ", displayName=" + displayName + ", displayType=" + displayType
				+ ", valueList=" + Arrays.toString(valueList) + "]";
	}

}
