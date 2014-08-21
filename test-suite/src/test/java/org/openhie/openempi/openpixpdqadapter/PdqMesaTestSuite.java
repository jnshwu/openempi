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

public class PdqMesaTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for PDQ Interface - MESA Tests");
		suite.addTestSuite(PdqMesa11311TestCase.class);
		suite.addTestSuite(PdqMesa11312TestCase.class);
		suite.addTestSuite(PdqMesa11315TestCase.class);
		suite.addTestSuite(PdqMesa11320TestCase.class);
		suite.addTestSuite(PdqMesa11325TestCase.class);
		suite.addTestSuite(PdqMesa11335TestCase.class);
		suite.addTestSuite(PdqMesa11340TestCase.class);
		suite.addTestSuite(PdqMesa11350TestCase.class);
		return suite;
	}

}
