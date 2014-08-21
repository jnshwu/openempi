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
package org.openhie.openempi.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eaio.uuid.UUID;

import junit.framework.TestCase;

public class UUIDGeneratorTest extends TestCase
{
	private final Log log = LogFactory.getLog(UUIDGeneratorTest.class);
	
	public void testGenerateGUID() {
		UUID uuid = new UUID();
		log.trace(uuid.toString());
		
		UUID uuid2 = new UUID();
		log.trace(uuid2.toString());
		
		for (int i=0; i < 100; i++) {
			log.trace(new UUID().toString());
		}
		
		log.trace("The two uuid are the same is a " + uuid.compareTo(uuid2) + " statement.");
	}
}
