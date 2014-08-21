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

import org.apache.commons.codec.language.Soundex;

public class SoundexFunction extends AbstractTransformationFunction
{
	private Soundex soundex;
	
	public SoundexFunction() {
		super();
		soundex = new Soundex();
	}
	
	public Object transform(Object field) {
		log.debug("Applying the soundex transform to field with value: " + field);
		if (field == null) {
			return null;
		}
		String encodedValue = soundex.encode(field.toString());
		log.debug("The soundex value for field: '" + field + "' is '" + encodedValue + "'");
		return encodedValue;
	}
}
