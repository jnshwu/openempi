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
package org.openhie.openempi.matching;

import org.openhie.openempi.configuration.MatchField;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.service.BaseServiceTestCase;

public class ComparisonVectorTest extends BaseServiceTestCase
{
	public void testComparisonVector() {
		MatchField[] fields = new MatchField[4];
		fields[0] = new MatchField();
		fields[0].setFieldName("givenName");
		fields[1] = new MatchField();
		fields[1].setFieldName("familyName");
		fields[2] = new MatchField();
		fields[2].setFieldName("phoneNumber");
		fields[3] = new MatchField();
		fields[3].setFieldName("city");
		fields[0].setMatchThreshold(0.725f);
		fields[1].setMatchThreshold(0.725f);
		fields[2].setMatchThreshold(0.725f);
		fields[3].setMatchThreshold(0.725f);
		
		ComparisonVector vector = new ComparisonVector(fields);
		System.out.println("Initialized the vector as: " + vector);
		
		// The following should build the vector [1 0 1 0] given the thresholds above
		vector.setScore(0, 0.5);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.3);
		vector.setScore(3, 0.9);
		
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(10, vector.getBinaryVectorValue());
		
		vector.setScore(0, 0.8);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.8);
		vector.setScore(3, 0.5);
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(7, vector.getBinaryVectorValue());
	}
	
	public void testComparisonVectorAsList() {
		java.util.List<MatchField> fields = new java.util.ArrayList<MatchField>();
		MatchField m = new MatchField();
		m.setFieldName("givenName");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		m = new MatchField();
		m.setFieldName("familyName");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		m = new MatchField();
		m.setFieldName("phoneNumber");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		m = new MatchField();
		m.setFieldName("city");
		m.setMatchThreshold(0.725f);
		fields.add(m);
		
		ComparisonVector vector = new ComparisonVector(fields);
		System.out.println("Initialized the vector as: " + vector);
		
		// The following should build the vector [1 0 1 0] given the thresholds above
		vector.setScore(0, 0.5);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.3);
		vector.setScore(3, 0.9);
		
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(10, vector.getBinaryVectorValue());
		
		vector.setScore(0, 0.8);
		vector.setScore(1, 0.8);
		vector.setScore(2, 0.8);
		vector.setScore(3, 0.5);
		System.out.println("Binary vector value is " + vector.getBinaryVectorString() + " with value of " + vector.getBinaryVectorValue());
		assertEquals(7, vector.getBinaryVectorValue());
	}
}
