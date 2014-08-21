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

import java.util.List;

import org.apache.log4j.Logger;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;

public class NaiveBlockingRecordPairIterator implements RecordPairIterator
{
	private Logger log = Logger.getLogger(getClass());
	
	private PersonDao personDao;
	private List<Integer> personIds;
	private Person firstPerson;
	private int indexFirst;
	private int indexSecond;
	
	public NaiveBlockingRecordPairIterator(PersonDao personDao, List<Integer> personIds) {
		this.personDao = personDao;
		this.personIds = personIds;
		indexFirst = 0;
		indexSecond = 1;
		Integer id = personIds.get(indexFirst);
		firstPerson = personDao.loadPerson(id);
		if (firstPerson == null) {
			log.warn("Unable to locate person in record pair iterator with identifier: " + id);
			throw new RuntimeException("Unable to load the referenced record from the repository.");
		}
	}
	
	public boolean hasNext() {
		return indexFirst < (personIds.size()-2);
	}

	public RecordPair next() {
		Integer id = personIds.get(indexSecond);
		Person secondPerson = personDao.loadPerson(id);
		RecordPair pair = new RecordPair(buildRecord(firstPerson), buildRecord(secondPerson));
		// Prepare the pointers for the next iteration
		indexSecond++;
		if (indexSecond == personIds.size()-1) {
			indexFirst++;
			id = personIds.get(indexFirst);
			firstPerson = personDao.loadPerson(id);
			indexSecond = indexFirst + 1;
		}
		return pair;
	}

	private Record buildRecord(Person person) {
		Record record = new Record(person);
		record.setRecordId(person.getPersonId().longValue());
		return record;
	}

	public void remove() {
		// This is an optional method of the interface and doesn't do
		// anything in this implementation. This is a read-only iterator.
	}
}
