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
package org.openhie.openempi.service;

import java.util.Date;
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;

import com.ibm.icu.text.SimpleDateFormat;

public class FindMatchingPersonTest extends BaseServiceTestCase
{
	public void testGetPersonByAttributes() {
		try {
			Person personOne = new Person();
			personOne.setCity("Herdon");
			personOne.setFamilyName("Pentakalos");
			personOne.setGivenName("Sophie");
			String dob = "2001-02-10";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			Date dateOfBirth = sdf.parse(dob);
			personOne.setDateOfBirth(dateOfBirth);
			PersonQueryService personService = Context.getPersonQueryService();
			List<Person> list = personService.findMatchingPersonsByAttributes(personOne);
			for (Person person : list) {
				System.out.println("Found person: " + person);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
