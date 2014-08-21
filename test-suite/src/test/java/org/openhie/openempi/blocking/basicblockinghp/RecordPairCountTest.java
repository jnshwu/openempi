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
import org.openhie.openempi.blocking.basicblockinghp.dao.BlockingDao;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.dao.BaseDaoTestCase;

public class RecordPairCountTest extends BaseDaoTestCase
{
	static {
		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-basic-blocking-hp.properties");
		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config-basic-blocking-hp.xml");
	}

    private BlockingDao dao;
    
    public void setBlockingHpDao(BlockingDao dao) {
        this.dao = dao;
    }

    @SuppressWarnings("unchecked")
	public void testRecordPairCount() {
		List<BaseField> fields = new ArrayList<BaseField>(2);
		fields.add(new BaseField("givenName"));
		fields.add(new BaseField("familyName"));
		BlockingRound round = new BlockingRound();
		round.setFields(fields);
		round.setName("round.0");
		long recordCount = dao.getRecordPairCount(round);
		log.debug("Count of record pairs: <" + recordCount + ">");
    }
}
