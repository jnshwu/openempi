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
package org.openhie.openempi.matching.fellegisunter;

public class ExtractBitPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] vectorValues = { 3, 7, 14, 22 };
		int matchFields = 5;
		for (int j=0; j < vectorValues.length; j++) {
			for (int i=0; i < matchFields; i++) {
				int valueAtPosition = getBitAtPosition(vectorValues[j], i);
				System.out.println("At position " + i + " vector value " + vectorValues[j] + " has the bit value of " + valueAtPosition);
			}
		}
	}

	private static int getBitAtPosition(int value, int pos) {
		int mask = 1;
		for (int i=1; i <= pos; i++) {
			mask = mask << 1;
		}
		System.out.println("For position " + pos + " using mask " + mask);
		int valueAtPosition = mask & value;
		if (valueAtPosition > 0) {
			valueAtPosition = 1;
		}
		return valueAtPosition;
	}

}
