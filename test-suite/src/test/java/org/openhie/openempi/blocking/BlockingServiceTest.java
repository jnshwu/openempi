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
package org.openhie.openempi.blocking;
import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;


public class BlockingServiceTest extends BaseServiceTestCase
{
	public void testGetRecordPairsWithConfiguration() {
		try {
			BlockingService blockingService = Context.getBlockingService();
			BlockingRound round = new BlockingRound();
			round.addField(new BaseField("custom1"));
			round.addField(new BaseField("custom2"));
			List<BlockingRound> rounds = new ArrayList<BlockingRound>(1);
			rounds.add(round);
			RecordPairSource recordPairSource = blockingService.getRecordPairSource(rounds);
			int i=0;
			for (RecordPairIterator iter = recordPairSource.iterator(); iter.hasNext(); ) {
				RecordPair pair = iter.next();
				log.trace("Comparing records " + pair.getLeftRecord().getRecordId() + " and " + pair.getRightRecord().getRecordId());
				i++;
			}
			System.out.println("Loaded " + i + " record pairs.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	public void testGetRecordPairs() {
		try {
			BlockingService blockingService = Context.getBlockingService();
			RecordPairSource recordPairSource = blockingService.getRecordPairSource();
			int i=0;
			for (RecordPairIterator iter = recordPairSource.iterator(); iter.hasNext(); ) {
				RecordPair pair = iter.next();
				log.trace("Comparing records " + pair.getLeftRecord().getRecordId() + " and " + pair.getRightRecord().getRecordId());
				i++;
			}
			System.out.println("Loaded " + i + " record pairs.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}	
}
