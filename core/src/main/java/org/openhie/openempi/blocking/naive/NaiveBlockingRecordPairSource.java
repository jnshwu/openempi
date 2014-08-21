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
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.dao.PersonDao;

public class NaiveBlockingRecordPairSource implements RecordPairSource
{
	private Logger log = Logger.getLogger(getClass());
	
	private PersonDao personDao;
	private List<Integer> personIds;
	
	public NaiveBlockingRecordPairSource(PersonDao personDao) {
		this.personDao = personDao;
	}
	
	public void init() {
		personIds = personDao.getAllPersons();
		if (log.isDebugEnabled()) {
			log.debug("Initialized a record pair source with " + personIds.size() + " entries for the naive blocking service.");
		}
	}

	public RecordPairIterator iterator() {
		if (personIds.size() < 1) {
			if (log.isDebugEnabled()) {
				log.debug("Cannot generate record pairs with less than two records available.");
			}
			throw new RuntimeException("Unable to generate record pairs as there aren't enough records available in the repository.");
		}
		return new NaiveBlockingRecordPairIterator(personDao, personIds);
	}

}
