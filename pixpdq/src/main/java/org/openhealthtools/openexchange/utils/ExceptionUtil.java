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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * The utility class for Exceptions.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ExceptionUtil {

	/**
	 * Strips away the exception class path preceding the exception message,
	 * so the message length would be shortened.
	 * 
	 * @param exceptionMessage
	 * @return a shortened exception message without preceding class path
	 */
	public static String strip(String exceptionMessage) {
		int index = exceptionMessage.lastIndexOf("Exception:");
		if (index == -1) 
			return exceptionMessage;
		else
			return exceptionMessage.substring(index+10);
	}

	/**
	 * Gets the details of this exception.
	 * 
	 * @param e the exception where to get the details
	 * @return the String detail
	 */
	public static String getExceptionDetails(Exception e) {
		if (e == null) 
			return "";
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + 
			e.getMessage() + "\n" + new String(baos.toByteArray());
	}
}
