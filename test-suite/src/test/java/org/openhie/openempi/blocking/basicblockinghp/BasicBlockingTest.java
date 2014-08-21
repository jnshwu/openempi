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
package org.openhie.openempi.blocking.basicblockinghp;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.blocking.basicblockinghp.dao.BlockingDao;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;

public class BasicBlockingTest extends BaseServiceTestCase
{
	static {
		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-basic-blocking-hp.properties");
		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config-basic-blocking-hp.xml");
	}

	public void testRecordPairSource() {
		Context.startup();
		BlockingService service = Context.getBlockingService();
		RecordPairSource source = service.getRecordPairSource();
		int pairCount=0;
		for (RecordPairIterator iterator = source.iterator(); iterator.hasNext(); ) {
			RecordPair pair = (RecordPair) iterator.next();
			pairCount++;
//			log.debug(pair);
		}
		log.debug("Generated " + pairCount +  " record pairs.");
	}

	public void testBasicBlocking() {
		BlockingService service = Context.getBlockingService();
		System.out.println("Obtained the service: " + service);
		Person person = new Person();
		person.setCustom1("ALF");
		person.setCustom2("ALN");
		Record record = new Record(person);
		List<RecordPair> recordPairs = service.findCandidates(record);
		for (RecordPair pair : recordPairs) {
			log.debug("Generated record pair: <" + pair + ">");
		}
	}
	
	public void testBasicBlockingDao() {
		List<String> fields = new ArrayList<String>(1);
		fields.add("custom1");
		fields.add("custom2");
		BlockingDao dao = (BlockingDao) Context.getApplicationContext().getBean("blockingHpDao");
		List<NameValuePair> pairs  = dao.getDistinctKeyValuePairs(fields);
		assertTrue(pairs.size() >= 0);
		for (NameValuePair value : pairs) {
			log.debug("Blocking key value: <" + value + ">");
		}
	}
}
