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

public class LoaderFieldCompositionWeb extends BaseModelData implements Serializable
{
	public static final String INDEX = "index";
	public static final String SEPARATOR = "separator";

	public LoaderFieldCompositionWeb() {
	}

	public LoaderFieldCompositionWeb(Integer index, String separator) {
		set(INDEX, index);
		set(SEPARATOR, separator);
	}

	public java.lang.Integer getIndex() {
		return get(INDEX);
	}

	public void setIndex(java.lang.Integer index) {
		set(INDEX, index);
	}

	public java.lang.String getSeparator() {
		return get(SEPARATOR);
	}

	public void setSeparator(java.lang.String separator) {
		set(SEPARATOR, separator);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(INDEX + ": ").append(getIndex());
		sb.append("," + SEPARATOR + ": ").append(getSeparator());
		return sb.toString();
	}
}
