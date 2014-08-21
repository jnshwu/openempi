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
public class TripleTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		Triple<String, Integer, Boolean> triple = new Triple<String, Integer, Boolean>("John", Integer.parseInt("28"), Boolean.TRUE);
		Triple<String, Integer, Boolean> triple2 = new Triple<String, Integer, Boolean>("John", Integer.parseInt("28"), Boolean.TRUE);
		//two equals objects should return two equal hashcodes
		assertEquals(triple, triple2);
		assertEquals(triple.hashCode(), triple2.hashCode());
	}

	@Test
	public void testTriple() {
		Triple<String, Integer, Boolean> triple = new Triple<String, Integer, Boolean>("John", Integer.parseInt("28"), Boolean.TRUE);
		assertEquals("John", triple.first);
		assertEquals(28, triple.second.intValue());
		assertTrue(triple.third);
	}

	@Test
	public void testEqualsObject() {
		Triple<String, Integer, Boolean> triple = new Triple<String, Integer, Boolean>("John", Integer.parseInt("28"), Boolean.TRUE);
		Triple<String, Integer, Boolean> triple2 = new Triple<String, Integer, Boolean>("John", Integer.parseInt("28"), Boolean.TRUE);
		assertEquals(triple, triple2);
	}

	@Test
	public void testToString() {
		Triple<String, Integer, Boolean> triple = new Triple<String, Integer, Boolean>("John", Integer.parseInt("28"), Boolean.TRUE);
		assertEquals("John,28,true", triple.toString());
	}

}
