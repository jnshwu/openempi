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
package org.openhie.openempi.matching;

import java.util.List;

import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;

public class BlockingCountsTest extends BaseServiceTestCase
{
	public void testInitialization() {
		BlockingService blockingService = Context.getBlockingService();
		try {
			List<Long> recordPairCount = blockingService.getRecordPairCount();
			int index=0;
			for (Long count : recordPairCount) {
				System.out.println("Blocking round " + index + " generated " + count + " pairs.");
				index++;
			}
		} catch (Exception e) {
			log.error("Failed while initializing the blocking service index: " + e, e);
		}
	}
}
