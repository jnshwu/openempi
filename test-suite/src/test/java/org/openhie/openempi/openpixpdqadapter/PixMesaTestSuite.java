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

import junit.framework.Test;
import junit.framework.TestSuite;

public class PixMesaTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for PIX Interface - MESA Tests");
		suite.addTestSuite(PixMesa10501TestCase.class);
		suite.addTestSuite(PixMesa10502TestCase.class);
		suite.addTestSuite(PixMesa10503TestCase.class);
		suite.addTestSuite(PixMesa10506TestCase.class);
		suite.addTestSuite(PixMesa10511TestCase.class);
		suite.addTestSuite(PixMesa10512TestCase.class);
		suite.addTestSuite(PixMesa10515TestCase.class);		
		return suite;
	}

}
