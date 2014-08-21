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

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Operation implements Serializable
{
	private static final long serialVersionUID = -8735471015676733746L;

	public static Operation EQ = new Operation("Equals");
	public static Operation IN = new Operation("In Set");
	public static Operation ISNULL = new Operation("Is NULL");
	public static Operation ISNOTNULL = new Operation("Is Not NULL");
	public static Operation LIKE = new Operation("Like");
	public static Operation NE = new Operation("Not Equals");
	
	private String name;
	
	private Operation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}	
}
