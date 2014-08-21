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
package org.openempi.webapp.server;

import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;
import org.openhie.openempi.service.BaseServiceTestCase;

public class ReviewRecordPairTest extends BaseServiceTestCase
{
	public void testRetrieveUnreviewedRecordPairs() {
		PersonDataServiceImpl personService = new PersonDataServiceImpl();
		try {
			java.util.List<ReviewRecordPairWeb> dtos = personService.getUnreviewedReviewRecordPairs();
			for (ReviewRecordPairWeb dto : dtos) {
				System.out.print("Found review record pair entry: Date Created: " + dto.getDateCreated() + ", Weight: " + dto.getWeight());
				PersonWeb person = dto.getLeftPerson();
				System.out.print(" L: Name: " + person.getGivenName() + " " + person.getFamilyName() + ", Birth Date: " + person.getDateOfBirth());
				person = dto.getRightPerson();
				System.out.println(" R: Name: " + person.getGivenName() + " " + person.getFamilyName() + ", Birth Date: " + person.getDateOfBirth());
			}
		} catch (Exception e) {
			log.error("Test failed: " + e);
		}			
	}
	
	public void testMatchUnreviewedRecordPairs() {
		PersonDataServiceImpl personService = new PersonDataServiceImpl();
		try {
			java.util.List<ReviewRecordPairWeb> dtos = personService.getUnreviewedReviewRecordPairs();
			for (ReviewRecordPairWeb dto : dtos) {
				System.out.print("Found review record pair entry: Date Created: " + dto.getDateCreated() + ", Weight: " + dto.getWeight());
				PersonWeb person = dto.getLeftPerson();
				System.out.print(" L: Name: " + person.getGivenName() + " " + person.getFamilyName() + ", Birth Date: " + person.getDateOfBirth());
				person = dto.getRightPerson();
				System.out.println(" R: Name: " + person.getGivenName() + " " + person.getFamilyName() + ", Birth Date: " + person.getDateOfBirth());
				dto.setRecordsMatch(false);
				personService.matchReviewRecordPairs(dto);
			}
		} catch (Exception e) {
			log.error("Test failed: " + e);
		}		
		
	}

}
