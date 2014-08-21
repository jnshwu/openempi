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

public class LoaderFieldComposition
{
	private int index;
	private String separator;
	private String value;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		if (separator == null)
			return;
		
		if (separator.equals("SPACE"))
			this.separator = " ";
		else if (separator.equals("DASH"))
			this.separator = "-";
		else if (separator.equals("PER"))
			this.separator = "/";
		else if (separator.equals("BACKSLASH"))
			this.separator = "\\";
		else if (separator.equals("COMMA"))
			this.separator = ",";
		else if (separator.equals("PERCENT"))
			this.separator = "%";
		else if (separator.equals("DOLLAR"))
			this.separator = "$";
		else if (separator.equals("HASHMARK"))
			this.separator = "#";
		else if (separator.equals("AT"))
			this.separator = "@";
		else if (separator.equals("CAP"))
			this.separator = "^";
		else if (separator.equals("STAR"))
			this.separator = "*";
		else if (separator.equals("EXCLAMATION"))
			this.separator = "!";
		else if (separator.equals("QUESTION"))
			this.separator = "?";
		else
			this.separator = separator;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
