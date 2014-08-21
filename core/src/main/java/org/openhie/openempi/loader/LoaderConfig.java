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
package org.openhie.openempi.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class LoaderConfig
{
	private boolean headerLinePresent;
	private List<LoaderDataField> dataFields;

	public LoaderConfig() {
		dataFields = new ArrayList<LoaderDataField>();
	}

	public boolean getHeaderLinePresent() {
		return headerLinePresent;
	}

	public void setHeaderLinePresent(boolean headerLinePresent) {
		this.headerLinePresent = headerLinePresent;
	}

	public void addDataField(LoaderDataField dataField) {
		dataFields.add(dataField);
	}

	public List<LoaderDataField> getDataFields() {
		return dataFields;
	}

	public void setDataFields(List<LoaderDataField> dataFields) {
		this.dataFields = dataFields;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(
				"headerLinePresent", headerLinePresent).append("dataFields", dataFields).toString();
	}
}
