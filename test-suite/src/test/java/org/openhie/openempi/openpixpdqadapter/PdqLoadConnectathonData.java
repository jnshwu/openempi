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
package org.openhie.openempi.openpixpdqadapter;

import java.io.File;

import org.openhie.openempi.loader.FileLoaderManager;
import org.openhie.openempi.loader.NominalSetFileLoader;

public class PdqLoadConnectathonData extends AbstractPdqTest
{
	private FileLoaderManager fileLoaderManager = new FileLoaderManager();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		try {
			fileLoaderManager.setUp();
        } catch (Exception e) {
        	log.error("Failed to setup the file loader manager: " + e.getMessage());
        	throw e;
        }
	}
	
	public void testLoadTestData() {
		File testData = new File("../../data/pixpdq_connectathon_patients.csv");
		try {
			fileLoaderManager.loadFile(testData.getAbsolutePath(), NominalSetFileLoader.LOADER_ALIAS);
		} catch (Exception e) {			
        	log.error("Failed to load the PDQ test data from file " + testData.getAbsolutePath() + " due to: " + e.getMessage(), e);
		}
	}
}
