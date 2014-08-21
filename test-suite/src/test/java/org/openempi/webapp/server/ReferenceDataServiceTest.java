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
package org.openempi.webapp.server;

import java.util.List;

import org.openempi.webapp.client.ReferenceDataService;
import org.openempi.webapp.client.model.IdentifierDomainTypeCodeWeb;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.server.BaseServiceTestCase;

public class ReferenceDataServiceTest extends BaseServiceTestCase
{
	public void testGetIdentifierDomainTypes() {
		ReferenceDataService refService = new ReferenceDataServiceImpl();
		List<IdentifierDomainTypeCodeWeb> codes = refService.getIdentifierDomainTypeCodes();
		for (IdentifierDomainTypeCodeWeb identifierDomainTypeCode : codes) {
			log.debug("Found code: " + identifierDomainTypeCode);
		}
		
		List<IdentifierDomainWeb> domains = refService.getIdentifierDomains();
		for (IdentifierDomainWeb identifierDomain : domains) {
			log.debug("Found code: " + identifierDomain);
		}
	}
}
