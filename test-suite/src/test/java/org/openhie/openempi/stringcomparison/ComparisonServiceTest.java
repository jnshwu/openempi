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
package org.openhie.openempi.stringcomparison;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ComparisonServiceTest extends BaseServiceTestCase
{
	private StringPair[] pairs = {
		new StringPair("odysseas", "odysseus"),
		new StringPair("odysseas", "odysseas"),
		new StringPair("testing", "hesting"),
		new StringPair("test", "TEST"),
		new StringPair("John", "Jon"),
		new StringPair("John", "Jane"),
		new StringPair(null, null),
		new StringPair(null, "test"),
		new StringPair("test", null),
		new StringPair("Javier", "Havier"),
		new StringPair("Martinez", "Marteenez")
	};
	
	public void testDistanceMetrics() {
		try {
			StringComparisonService service = Context.getStringComparisonService();
			DistanceMetricType[] types = service.getDistanceMetricTypes();
			for (int i=0; i < types.length; i++) {
				DistanceMetricType metricType = service.getDistanceMetricType(types[i].getName());
				System.out.println("=== Testing distance metric " + metricType + " ===");
				for (StringPair pair : pairs) {
					System.out.println("Distance between " + pair.getString1() + " and " + pair.getString2() + " is " +
							service.score(metricType.getName(), pair.getString1(), pair.getString2()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class StringPair
	{
		private String string1;
		private String string2;
		
		public String getString1() {
			return string1;
		}

		public void setString1(String string1) {
			this.string1 = string1;
		}

		public String getString2() {
			return string2;
		}

		public void setString2(String string2) {
			this.string2 = string2;
		}

		public StringPair(String string1, String string2) {
			this.string1 = string1;
			this.string2 = string2;
		}

		@Override
		public boolean equals(final Object other) {
			if (!(other instanceof StringPair))
				return false;
			StringPair castOther = (StringPair) other;
			return new EqualsBuilder().append(string1, castOther.string1).append(string2, castOther.string2).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(string1).append(string2).toHashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("string1", string1).append("string2", string2).toString();
		}
		
	}
}
