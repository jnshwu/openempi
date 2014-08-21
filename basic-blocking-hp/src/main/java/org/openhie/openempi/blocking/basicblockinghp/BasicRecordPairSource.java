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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.blocking.basicblockinghp.cache.BlockingServiceCache;
import org.openhie.openempi.blocking.basicblockinghp.dao.BlockingDao;
import org.openhie.openempi.configuration.BlockingRound;

public class BasicRecordPairSource implements RecordPairSource
{
	protected final Log log = LogFactory.getLog(getClass());
	private List<BlockingRound> blockingRounds;
	private BlockingDao blockingDao;
	private BlockingServiceCache cache;
	
	public void init() {
		log.trace("Initializing the Record Pair Source");
	}

	public RecordPairIterator iterator() {
		BasicRecordPairIterator iterator = new BasicRecordPairIterator(this);
		this.setBlockingDao(cache.getBlockingDao());
		iterator.setRecordIds(blockingDao.getAllRecordIds());
		return iterator;
	}

	public List<BlockingRound> getBlockingRounds() {
		return blockingRounds;
	}

	public void setBlockingRounds(List<BlockingRound> blockingRounds) {
		this.blockingRounds = blockingRounds;
	}
	
	public BlockingServiceCache getCache() {
		return cache;
	}

	public void setCache(BlockingServiceCache cache) {
		this.cache = cache;
	}

	public BlockingDao getBlockingDao() {
		return blockingDao;
	}

	public void setBlockingDao(BlockingDao blockingDao) {
		this.blockingDao = blockingDao;
	}
}
