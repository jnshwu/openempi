/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openexchange.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class PairTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.openhealthtools.openexchange.utils.Pair#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		Pair<String, Integer> pair = new Pair<String, Integer>("John", Integer.parseInt("28"));
		Pair<String, Integer> pair2 = new Pair<String, Integer>("John", Integer.parseInt("28"));
		//two equals objects should return two equal hashcodes
		assertEquals(pair, pair2);
		assertEquals(pair.hashCode(), pair2.hashCode());
	}

	/**
	 * Test method for {@link org.openhealthtools.openexchange.utils.Pair#Pair(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testPair() {
		Pair<String, Integer> pair = new Pair<String, Integer>("John", Integer.parseInt("28"));
		assertEquals("John", pair.first);
		assertEquals(28, pair.second.intValue());
	}

	/**
	 * Test method for {@link org.openhealthtools.openexchange.utils.Pair#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		Pair<String, Integer> pair = new Pair<String, Integer>("John", Integer.parseInt("28"));
		Pair<String, Integer> pair2 = new Pair<String, Integer>("John", Integer.parseInt("28"));
		assertEquals(pair, pair2);
	}

	/**
	 * Test method for {@link org.openhealthtools.openexchange.utils.Pair#toString()}.
	 */
	@Test
	public void testToString() {
		Pair<String, Integer> pair = new Pair<String, Integer>("John", Integer.parseInt("28"));
		assertEquals("John,28", pair.toString());
	}

}
