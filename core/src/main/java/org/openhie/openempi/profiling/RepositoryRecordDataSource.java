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
package org.openhie.openempi.profiling;

import java.util.Iterator;
import java.util.List;

import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.model.DataProfileAttribute;
import org.openhie.openempi.model.Record;

public class RepositoryRecordDataSource extends AbstractRecordDataSource
{
	public static final int REPOSITORY_RECORD_DATA_SOURCE_ID = 0;
	
	private Integer recordBlockSize;
	private PersonDao personDao;

	public RepositoryRecordDataSource() {
	}
	
	@Override
	public Iterator<Record> iterator() {
		return new RecordIterator(recordBlockSize);
	}	

	public int getRecordDataSourceId() {
		return REPOSITORY_RECORD_DATA_SOURCE_ID;
	}
	
	public boolean isEmpty()
	{
		boolean isEmpty = false;
		
		if(personDao.getRecordCount() == 0) {
			isEmpty = true;
		}
		
		return isEmpty;
	}


	public List<AttributeMetadata> getAttributeMetadata() {
		java.util.List<AttributeMetadata> metadata = new java.util.ArrayList<AttributeMetadata>();
		metadata.add(new AttributeMetadata("account", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("accountIdentifierDomainId", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("address1", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("address2", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("addressTypeCd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("birthOrder", DataProfileAttribute.INTEGER_DATA_TYPE));
		metadata.add(new AttributeMetadata("birthPlace", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("city", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("country", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("countryCode", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom1", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom2", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom3", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom4", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom5", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom6", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom7", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom8", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom9", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom10", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom11", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom12", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom13", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom14", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom15", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom16", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom17", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom18", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom19", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("custom20", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("dateOfBirth", DataProfileAttribute.DATE_DATA_TYPE));
		metadata.add(new AttributeMetadata("deathInd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("deathTime", DataProfileAttribute.DATE_DATA_TYPE));
		metadata.add(new AttributeMetadata("degree", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("email", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("ethnicGroupCd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("familyName", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("familyName2", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("genderCd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("givenName", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("groupNumber", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("languageCd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("maritalStatusCode", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("middleName", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("mothersMaidenName", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("multipleBirthInd", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("nameTypeCd", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("nationalityCd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("phoneAreaCode", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("phoneCountryCode", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("phoneExt", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("phoneTypeCd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("postalCode", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("prefix", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("raceCd", DataProfileAttribute.STRING_DATA_TYPE));
//		metadata.add(new AttributeMetadata("religionCd", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("ssn", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("state", DataProfileAttribute.STRING_DATA_TYPE));
		metadata.add(new AttributeMetadata("suffix", DataProfileAttribute.STRING_DATA_TYPE));
		return metadata;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
	
	public Integer getRecordBlockSize() {
		return recordBlockSize;
	}

	public void setRecordBlockSize(Integer recordBlockSize) {
		this.recordBlockSize = recordBlockSize;
	}

	private class RecordIterator implements Iterator<Record>
	{
		private int blockSize;
		java.util.List<Record> records;
		int currentIndex;
		int startIndex;
		
		public RecordIterator(int blockSize) {
			this.blockSize = blockSize;
			startIndex = 0;
			currentIndex = -1;
		}
		
		public boolean hasNext() {
			if (records != null && currentIndex < records.size()) {
				return true;
			}
			return loadBlockOfRecords(blockSize);
		}

		private boolean loadBlockOfRecords(int blockSize) {
			try {
				log.debug("Loading records from " + startIndex + " to " + (startIndex + blockSize));
				records = personDao.getRecordsPaged(startIndex, blockSize);
				if (records.size() == 0) {
					return false;
				}
				currentIndex = 0;
				startIndex += blockSize;
				return true;
			} catch (Exception e) {
				log.error("Failed while loading a block of records from the repository: " + e, e);
				return false;
			}
		}

		public Record next() {
			return records.get(currentIndex++);
		}

		public void remove() {
		}
	}

	@Override
	public void close(String message) {
		// TODO Auto-generated method stub		
	}
}
