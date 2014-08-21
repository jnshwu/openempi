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

public class SubstringFunction extends AbstractTransformationFunction
{
	public final static String STARTING_POSITION = "start";
	public final static String ENDING_POSITION = "end";
	
	public SubstringFunction() {
		super();
	}
	
	@Override
	public Object transform(Object field) {
		if (field == null || field.toString().isEmpty()) {
			return field;
		}
		if (getConfiguration() == null ||
			(getConfiguration().get(STARTING_POSITION) == null && getConfiguration().get(ENDING_POSITION) == null)) {
			log.warn("The substring transformation function has not been configured propoerly; no starting or ending position has been defined.");
			return field;
		}
		Integer startPos = getParameterAsInteger(STARTING_POSITION);
		if (startPos == null) {
			startPos = new Integer(0);
		}
		Integer endPos = getParameterAsInteger(ENDING_POSITION);
		String value = field.toString();
		if (endPos == null || endPos.intValue() > value.length()) {
			endPos = value.length();
		}
		return value.substring(startPos, endPos);
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { STARTING_POSITION, ENDING_POSITION };
	}
}
