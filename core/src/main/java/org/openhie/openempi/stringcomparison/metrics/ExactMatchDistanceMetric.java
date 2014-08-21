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
package org.openhie.openempi.stringcomparison.metrics;


public class ExactMatchDistanceMetric extends AbstractDistanceMetric
{
	public final static String TRUNCATION_LENGTH = "truncation-length";
	
	private Integer truncationLength;
	
	public ExactMatchDistanceMetric() {
	}

	public double score(Object value1, Object value2) {
		boolean match = false;
		if (missingValues(value1, value2)) {
			return handleMissingValues(value1, value2);
		} else {
			String string1 = value1.toString();
			String string2 = value2.toString();
			if (truncationLength != null) {
				if (string1.length() > truncationLength) {
					string1 = string1.substring(0,truncationLength.intValue());
				}
				if (string2.length() > truncationLength) {
					string2 = string2.substring(0,truncationLength.intValue());
				}
			}
			match = (string1.equalsIgnoreCase(string2)) ? true : false;
		}
		double distance = 0.0;
		if (match) {
			distance = 1.0;
		}
		log.trace("Computed the distance between :" + value1 + ": and :" + value2 + ": to be " + distance);
		return distance;
	}

	@Override
	public void setParameter(String key, Object value) {
		if (key == null || !key.equalsIgnoreCase(TRUNCATION_LENGTH)) {
			return;
		}
		if (value instanceof String) {
			try {
				truncationLength = Integer.parseInt((String) value);
				log.debug("Set the truncation length to " + truncationLength);
			} catch (NumberFormatException e) {	
				truncationLength = null;
			}
		}
	}
}
