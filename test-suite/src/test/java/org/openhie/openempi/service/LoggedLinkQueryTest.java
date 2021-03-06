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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.LoggedLink;
import org.openhie.openempi.model.ReviewRecordPair;

public class LoggedLinkQueryTest extends BaseServiceTestCase
{
	public void testQueryLoggedLinks() {
		PersonQueryService queryService = Context.getPersonQueryService();
		int count = queryService.getLoggedLinksCount(3);
		if (count == 0) {
			return;
		}
		List<LoggedLink> links = queryService.getLoggedLinks(3, 0, count);
		assertNotNull("The list should not be null if the count " + count, links);
		assertEquals("List size should match count", count, links.size());
		
		for (LoggedLink link : links) {
			ReviewRecordPair pair = queryService.getLoggedLink(link.getLinkId());
			log.debug("Found link: " + pair);
		}
	}
}
