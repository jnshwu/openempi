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
package org.openhie.openempi.transformation.function;

import java.text.SimpleDateFormat;

public class DateTransformationFunction extends AbstractTransformationFunction
{
	public final static String DATE_FORMAT = "dateFormat";
	public final static String DEFAULT_DATE_FORMAT = "yyyy.MM.dd";
	
	public DateTransformationFunction() {
		super();
	}
	
	@Override
	public Object transform(Object field) {
		if (field == null || !(field instanceof java.util.Date)) {
			log.warn("Field " + field + " is not of type Date.");
			return field;
		}
		
		String dateFormat = getParameter(DATE_FORMAT, DEFAULT_DATE_FORMAT);
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format((java.util.Date) field);
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { DATE_FORMAT };
	}	
}
