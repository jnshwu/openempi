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

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.PersonManagerService;

public class MatchingInitializationTest extends BaseServiceTestCase
{
	static {
//		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-probabilistic-matching.properties");
//		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts.properties");
//		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-sorted-neighborhood-blocking.properties");
//		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-suffix-array-blocking.properties");
//		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config.xml");
//		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config-suffix-array-blocking.xml");
	}

	public void testInitialization() {
		PersonManagerService service = Context.getPersonManagerService();
		try {
			service.initializeRepository();
		} catch (ApplicationException e) {
			log.error("Failed while initializing the repository: " + e, e);
		}
	}
}
