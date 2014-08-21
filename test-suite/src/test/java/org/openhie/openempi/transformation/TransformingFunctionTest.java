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

import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.transformation.function.DoubleMetaphoneFunction;
import org.openhie.openempi.transformation.function.MetaphoneFunction;
import org.openhie.openempi.transformation.function.NysiisFunction;
import org.openhie.openempi.transformation.function.RefinedSoundexFunction;
import org.openhie.openempi.transformation.function.SoundexFunction;
import org.openhie.openempi.transformation.function.TransformationFunction;

public class TransformingFunctionTest extends BaseServiceTestCase
{	
	public void testMetaphone() {
		MetaphoneFunction function = new MetaphoneFunction();
		for (String name : getTestString()) {
			log.debug("Metaphone encoding of " + name + " is " + function.transform(name));
		}
	}
	
	public void testDoubleMetaphone() {
		DoubleMetaphoneFunction function = new DoubleMetaphoneFunction();
		for (String name : getTestString()) {
			log.debug("DoubleMetaphone encoding of " + name + " is " + function.transform(name));
		}
	}
	
	public void testSoundex () {
		SoundexFunction function = new SoundexFunction();
		for (String name : getTestString()) {
			log.debug("Soundex encoding of " + name + " is " + function.transform(name));
		}
	}

	public void testRefinedSoundex () {
		RefinedSoundexFunction function = new RefinedSoundexFunction();
		for (String name : getTestString()) {
			log.debug("Refined Soundex encoding of " + name + " is " + function.transform(name));
		}
	}
	
	public void testNysiis() {
//		NysiisFunction function = new NysiisFunction();
		TransformationService transformService = Context.getTransformationService();
		java.util.List<String> functions = transformService.getTransformationFunctionNames();
		for (String name : functions) {
			log.debug("Transformation function: " + name);
		}
		TransformationFunctionType type = transformService.getTransformationFunctionType("Nysiis");
		TransformationFunction function = type.getTransformationFunction();
		for (String name : getTestString()) {
			log.debug("NYSIIS encoding of " + name + " is " + function.transform(name));
		}
	}
	
	public String[] getTestString() {
		String[] testStrings = { "Odysseas", "Odysseus", "John", "Bob", "Frank", "James", "Sophia", "Sofia", "Jensen",
				"Jonassen", "Jonasen", "Jacobsen", "Jakobsen", "Fischer", "Fisher", "Gustavsen", "Gustafsson",
				"Gustafsen", "Handeland", "Javier", "Havier", "Martinez", "Marteenez" };
		return testStrings;
	}	
}
