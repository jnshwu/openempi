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
package org.openhie.openempi.blocking.naive;

import java.util.Iterator;

import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;

public class NaiveBlockingServiceTest extends BaseServiceTestCase
{
	static {
		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-core-only.properties");
		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config-core-only.xml");
	}

	public void testNaiveBlocking() {
		BlockingService service = (BlockingService)
				Context.getApplicationContext().getBean("naiveBlockingService");
		System.out.println("Obtained the service: " + service);
		Person person = new Person();
		person.setCustom1("ALF");
		person.setCustom2("ALN");
		Record record = new Record(person);
//		List<RecordPair> recordPairs = service.findCandidates(record);
//		for (RecordPair pair : recordPairs) {
//			log.debug("Generated record pair: <" + pair + ">");
//		}
//		log.debug("Generated " + recordPairs.size() + " pairs.");
	}
	
	public void testNaiveBlockingServiceRecordSource() {
		BlockingService blockingService = (BlockingService)
				Context.getApplicationContext().getBean("naiveBlockingService");
		RecordPairSource source = blockingService.getRecordPairSource();
		assertNotNull(source);
		Iterator<RecordPair> iterator = source.iterator();
		assertNotNull(iterator);
		int count=0;
//		for (Iterator<RecordPair> iter = iterator; iter.hasNext(); ) {
//			RecordPair pair = iter.next();
//			count++;
////			System.out.println("Considering pair: (" + pair.getLeftRecord().getRecordId() + "," + pair.getRightRecord().getRecordId() + ")");
//		}
//		System.out.println("Blocking service generated: " + count + " record pairs.");
	}
}
