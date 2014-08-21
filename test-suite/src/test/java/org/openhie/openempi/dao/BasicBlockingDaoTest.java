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
package org.openhie.openempi.dao;

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.blocking.basicblockinghp.dao.BlockingDao;

/**
 * This class tests the current BlockingDao implementation class
 * @author Odysseas
 */
public class BasicBlockingDaoTest extends BaseDaoTestCase {
    private BlockingDao dao;
    
    public void setBlockingHpDao(BlockingDao dao) {
        this.dao = dao;
    }

    @SuppressWarnings("unchecked")
	public void testGetBlockingKeyValues() {
		List<String> fields = new ArrayList<String>(1);
		fields.add("custom1");
		fields.add("familyName");
		List<String> blockingKeyValueList = dao.getBlockingKeyValues(fields);
        assertTrue(blockingKeyValueList.size() >= 0);
		for (String value : blockingKeyValueList) {
			log.debug("Blocking key value: <" + value + ">");
		}
    }
}
