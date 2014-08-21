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
package org.openhie.openempi.blocking.basicblocking;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.dao.BlockingDao;
import org.openhie.openempi.model.NameValuePair;

public class BasicRecordPairSource implements RecordPairSource
{
	protected final Log log = LogFactory.getLog(getClass());
	private List<BlockingRound> blockingRounds;
	private BlockingDao blockingDao;
	private List<List<NameValuePair>> valueList;
	
	public void init() {
		log.trace("Initializing the Record Pair Source");
		valueList = new ArrayList<List<NameValuePair>>();
		for (BlockingRound round : blockingRounds) {
			List<BaseField> fields = round.getFields();
			List<List<NameValuePair>> values = blockingDao.getDistinctValues(getBlockingFieldList(fields));
			valueList.addAll(values);
		}		
	}

	private List<String> getBlockingFieldList(List<BaseField> fields) {
		List<String> fieldList  = new java.util.ArrayList<String>(fields.size());
		for (BaseField field : fields) {
			fieldList.add(field.getFieldName());
		}
		return fieldList;
	}

	public List<List<NameValuePair>> getBlockingValueList() {
		return valueList;
	}

	public RecordPairIterator iterator() {
		BasicRecordPairIterator iterator = new BasicRecordPairIterator(this);
		return iterator;
	}

	public List<BlockingRound> getBlockingRounds() {
		return blockingRounds;
	}

	public void setBlockingRounds(List<BlockingRound> blockingRounds) {
		this.blockingRounds = blockingRounds;
	}
	
	public BlockingDao getBlockingDao() {
		return blockingDao;
	}

	public void setBlockingDao(BlockingDao blockingDao) {
		this.blockingDao = blockingDao;
	}
}
