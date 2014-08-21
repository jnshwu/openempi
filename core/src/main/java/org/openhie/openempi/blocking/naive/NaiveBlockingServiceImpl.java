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

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.InitializationException;
import org.openhie.openempi.blocking.AbstractBlockingLifecycleObserver;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.model.Criteria;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.util.ConvertUtil;

public class NaiveBlockingServiceImpl extends AbstractBlockingLifecycleObserver implements BlockingService
{
	private PersonDao personDao;

	public void startup() throws InitializationException {
		log.trace("Starting the Naive Blocking Service");
	}

	public void shutdown() {
		log.trace("Stopping the Naive Blocking Service");
	}

	public void rebuildIndex() throws InitializationException {
		// TODO Need to implement the rebuildIndex method here.		
	}
	
	public RecordPairSource getRecordPairSource() {
		RecordPairSource source = new NaiveBlockingRecordPairSource(personDao);
		source.init();
		return source;
	}

	public RecordPairSource getRecordPairSource(Object parameters) {
		return getRecordPairSource();
	}

	// The naive blocking service pairs up the record selected with every other record in the system
	public List<RecordPair> findCandidates(Record record) {
		List<RecordPair> pairs = new ArrayList<RecordPair>();
		if (record == null) {
			return pairs;
		}
		List<Record> firstRecords = loadFirstRecords(record);
		if (firstRecords == null || firstRecords.size() == 0) {
			return pairs;
		}
		List<Integer> personIds = personDao.getAllPersons();
		for (Record firstRecord : firstRecords) {
			for (Integer id : personIds) {
				if (id == firstRecord.getRecordId().intValue()) {
					continue;
				}
				Person secondPerson = personDao.loadPerson(id);
				if (secondPerson == null) {
					continue;
				}
				RecordPair pair = new RecordPair(firstRecord, buildRecord(secondPerson));
				pairs.add(pair);
			}
		}
		return pairs;
	}

	private List<Record> loadFirstRecords(Record record) {
		Criteria criteria = ConvertUtil.buildCriteriaFromProperties(record);
		ConvertUtil.addIndirectCriteria((Person) record.getObject(), criteria);
		List<Person> persons = personDao.getPersons(criteria);
		List<Record> records = new ArrayList<Record>(persons.size());
		for (Person person : persons) {
			records.add(buildRecord(person));
		}
		return records;
	}

	private Record buildRecord(Person person) {
		Record record = new Record(person);
		record.setRecordId(person.getPersonId().longValue());
		return record;
	}

	public List<Long> getRecordPairCount() {
		double recordCount = (double) personDao.getRecordCount();
		double recordPairCount = recordCount * (recordCount-1.0) / 2.0;
		List<Long> counts = new ArrayList<Long>(1);
		counts.add((long) recordPairCount);
		return counts;
	}
	
	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
}
