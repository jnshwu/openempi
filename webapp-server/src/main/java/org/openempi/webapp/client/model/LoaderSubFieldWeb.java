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

public class LoaderSubFieldWeb extends BaseFieldWeb implements Serializable
{
	public static final String BEGIN_INDEX = "beginIndex";
	public static final String END_INDEX = "endIndex";

	public LoaderSubFieldWeb() {
	}

	public LoaderSubFieldWeb(String fieldName, Integer beginIndex, Integer endIndex) {
		set(FIELD_NAME, fieldName);
		set(BEGIN_INDEX, beginIndex);
		set(END_INDEX, endIndex);
	}
	
	public java.lang.Integer getBeginIndex() {
		return get(BEGIN_INDEX);
	}

	public void setBeginIndex(java.lang.Integer beginIndex) {
		set(BEGIN_INDEX, beginIndex);
	}

	public java.lang.Integer getEndIndex() {
		return get(END_INDEX);
	}

	public void setEndIndex(java.lang.Integer endIndex) {
		set(END_INDEX, endIndex);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(BEGIN_INDEX + ": ").append(getBeginIndex());
		sb.append("," + END_INDEX + ": ").append(getEndIndex());
		return sb.toString();
	}
}
