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
package org.openhie.openempi.transformation;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.transformation.function.SubstringFunction;

public class SubstringTransformationFunctionTest extends BaseServiceTestCase
{
	public void testDateConversion() {
		String dateStr = "Wed Nov 13 00:00:00 EST 1901";
		Person person = new Person();
		person.setDateOfBirth(new java.util.Date());
		Object obj;
		try {
			obj = PropertyUtils.getProperty(person, "dateOfBirth");
			log.debug("The date is: " + obj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testSubstringNoParameters() {
		SubstringFunction function = new SubstringFunction();
		Object field = getTestString()[0];
		Object value = function.transform(field);
		assertEquals("Function didn't return the original string.", field, value);
	}
	
	public void testSubstringValidParameters() {
		SubstringFunction function = new SubstringFunction();
		Object field = getTestString()[0];
		function.setParameter(SubstringFunction.STARTING_POSITION, "0");
		function.setParameter(SubstringFunction.ENDING_POSITION, "2");
		Object value = function.transform(field);
		assertEquals("Function didn't return the original string.", "Op", value);
	}
	
	public void testSubstringOnlyStart() {
		SubstringFunction function = new SubstringFunction();
		Object field = getTestString()[0];
		function.setParameter(SubstringFunction.STARTING_POSITION, "2");
		Object value = function.transform(field);
		assertEquals("Function didn't return the original string.", "enEMPI", value);
	}
	
	public void testSubstringOnlyEnd() {
		SubstringFunction function = new SubstringFunction();
		Object field = getTestString()[0];
		function.setParameter(SubstringFunction.ENDING_POSITION, "2");
		Object value = function.transform(field);
		assertEquals("Function didn't return the original string.", "Op", value);
	}
	
	public void testSubstringLongEnd() {
		SubstringFunction function = new SubstringFunction();
		Object field = getTestString()[0];
		function.setParameter(SubstringFunction.ENDING_POSITION, "25");
		Object value = function.transform(field);
		assertEquals("Function didn't return the original string.", field, value);
	}
	
	public void testSubstringInvalidParams() {
		SubstringFunction function = new SubstringFunction();
		Object field = getTestString()[0];
		function.setParameter(SubstringFunction.ENDING_POSITION, "ABCD");
		Object value = function.transform(field);
		assertEquals("Function didn't return the original string.", field, value);
	}

	public String[] getTestString() {
		String[] testStrings = { "OpenEMPI" };
		return testStrings;
	}	
}
