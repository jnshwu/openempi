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
package org.openhie.openempi.blocking;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;

public class BlockingServiceFindCandidatesTest extends BaseServiceTestCase
{
	public void testFindCandidates() {
		Person person = new Person();
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Oak Hill");
		person.setFamilyName("Pentakalos");
		person.setGivenName("Odysseas");
		Nationality nationality = new Nationality();
		nationality.setNationalityCd(100);
		person.setNationality(nationality);
		person.setDateOfBirth(new java.util.Date());
		
		Record record = new Record(person);
		String[] matchingAttributes = {"givenName", "familyName"};
		for (String attribute : matchingAttributes) {
			System.out.println("The record has a value of " + record.getAsString(attribute) + " for field " + attribute);
		}
		BlockingService blockingService = Context.getBlockingService();
		java.util.List<RecordPair> records = blockingService.findCandidates(record);
		for (RecordPair entry : records) {
			System.out.println("Found candicate matching record pair: " + entry);
		}
	}
}
