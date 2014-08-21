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

package org.openhealthtools.openexchange.utils.hl7;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * The utility class for HL7 v2.3.1 messages.
 * 
 * @author Jim Firby
 * @version 1.0 - Nov 22, 2005
 */
public class HL7v231 {
	
	/* HL7 v2.3.1 DateTime format */
	private static SimpleDateFormat DTMformatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
	private static SimpleDateFormat DTformatter = new SimpleDateFormat("yyyyMMdd");
	private static DecimalFormat TZformatter = new DecimalFormat("+0000;-0000");
	
	/**
	 * Formats a date/time according to the HL7 v2.3.1 spec.
	 * 
	 * @param date The date/time to format
	 * @return The formatted data/time as a string
	 */
	public static String formatDateTime(Date date) {
		return DTMformatter.format(date);
	}
	
	/**
	 * Formats a date/time according to the HL7 v2.3.1 spec unless a
	 * custom format string is supplied, then use that.
	 * 
	 * @param date The date/time to format
	 * @param formatString A custom format string, or NULL for the default
	 * @return The formatted data as a string
	 */
	public static String formatDateTime(Date date, String formatString) {
		if (formatString == null) {
			return formatDateTime(date);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat(formatString);
			return formatter.format(date);
		}
	}

	/**
	 * Parses an HL7 v2.3.1 date/time string into a Java
	 * Date object.
	 * 
	 * @param theDate The date/time string to parse
	 * @return The parsed Date
	 */
	public static Date parseDateTime(String theDate) {
		return parseDateTimeString(theDate, true);
	}
	
	/**
	 * Parses an HL7 v2.3.1 date/time string into a Java
	 * Date object.  Use GMT as the default timezone if
	 * one is not supplied in the string.
	 * 
	 * @param theDate The date/time string to parse
	 * @return The parsed Date
	 */	
	public static Date parseDateTimeGMT(String theDate) {
		return parseDateTimeString(theDate, true, "0");
	}
	
	/**
	 * Formats a date according to the HL7 v2.3.1 spec.
	 * 
	 * @param date The date to format
	 * @return The formatted data as a string
	 */
	public static String formatDate(Date date) {
		return DTformatter.format(date);
	}
	
	/**
	 * Formats a date according to the HL7 v2.3.1 spec unless a
	 * custom format string is supplied, then use that.
	 * 
	 * @param date The date to format
	 * @param formatString A custom format string, or NULL for the default
	 * @return The formatted data as a string
	 */
	public static String formatDate(Date date, String formatString) {
		if (formatString == null) {
			return formatDate(date);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat(formatString);
			return formatter.format(date);
		}
	}
	
	/**
	 * Parses an HL7 v2.3.1 date string into a Date object.
	 * 
	 * @param theDate The date string to parse
	 * @return The parsed Date object
	 */
	public static Date parseDate(String theDate) {
		return parseDateTimeString(theDate, false);
	}

	/**
	 * Parses an HL7v231 Date/Time string into a Date object.
	 * 
	 * @param theDate The date/time string to parse
	 * @param useTime True if the time should be included in the returned Date
	 * @return The parsed Date object
	 */
	private static Date parseDateTimeString(String theDate, boolean useTime) {
		return parseDateTimeString(theDate, useTime, null);
	}
	
	/**
	 * Parses an HL7v231 Date/Time string into a Date object.
	 * 
	 * @param theDate The date/time string to parse
	 * @param useTime True if the time should be included in the returned Date
	 * @param defaultTz A string holding the default Timezone offset (as an int)
	 * @return The parsed Date object
	 */
	private static Date parseDateTimeString(String theDate, boolean useTime, String defaultTz) {
		int year = 0;
		int month = 1;
		int day = 1;
		int hour = 0;
		int minute = 0;
		int second = 0;
		int tzIndex = -1;
		int tz = 0;
		boolean useTz = false;
		// See if we have a timezone
		tzIndex = theDate.indexOf("-");
		if (tzIndex < 0) tzIndex = theDate.indexOf("+");
		// Find the last non-timezone character
		int end = tzIndex;
		if (end < 0) end = theDate.length();
		// Now pull out all the pieces we have
		try {
			if (end >= 4) {
				year = Integer.parseInt(theDate.substring(0, 4));
			}
			if (end >= 6) {
				month = Integer.parseInt(theDate.substring(4, 6));
			}
			if (end >= 8) {
				day = Integer.parseInt(theDate.substring(6, 8));
			}
			if (useTime) {
				if (end >= 10) {
					hour = Integer.parseInt(theDate.substring(8, 10));
				}
				if (end >= 12) {
					minute = Integer.parseInt(theDate.substring(10, 12));
				}
				if (end >= 14) {
					second = Integer.parseInt(theDate.substring(12, 14));
				}
			}
		} catch (NumberFormatException e) {
			if (year == 0) return null;
		}
		// Get the timezone, if there is one
		if (useTime) {
			if ((tzIndex >= 0) && (tzIndex < theDate.length())) {
				try {
					tz = Integer.parseInt(theDate.substring(tzIndex));
					useTz = true;
				} catch (NumberFormatException e) {}
			} else if (defaultTz != null) {
				try {
					tz = Integer.parseInt(defaultTz);
					useTz = true;
				} catch (NumberFormatException e) {}
			}
		}
		return buildDateFromInts(year, month, day, hour, minute, second, tz, useTz);
	}

	/**
	 * Constructs a Date object from a set of date/time integer
	 * values.  This auxiliary function is used in some unittests
	 * and in some of the HL7v25 code.
	 * 
	 * @param year The year for the Date
	 * @param month The month (January = 1)
	 * @param day The day of the month
	 * @param hour The hour of the day (0-23)
	 * @param minute The minutes
	 * @param second The seconds
	 * @param tz The timezone offset as an integer
	 * @param useTz True if the timezone offset should be used when encoding the time, False to use the local timezone
	 * @return The Date object built using these parameters
	 */
	public static Date buildDateFromInts(int year, int month, int day, int hour, int minute, int second, int tz, boolean useTz) {
		// Push everything into a calendar
		Calendar calendar = GregorianCalendar.getInstance(); 
		calendar.set(Calendar.YEAR, year);
		if (month >= 1) {
			calendar.set(Calendar.MONTH, month-1);
		} else {
			calendar.set(Calendar.MONTH, 0);
		}
		if (day >= 1) {
			calendar.set(Calendar.DAY_OF_MONTH, day);
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		}
		if (hour >= 0) calendar.set(Calendar.HOUR_OF_DAY, hour);
		if (minute >= 0) calendar.set(Calendar.MINUTE, minute);
		if (second >= 0) calendar.set(Calendar.SECOND, second);
		if (useTz) calendar.setTimeZone(TimeZone.getTimeZone("GMT" + TZformatter.format(tz)));
		// Done
		return calendar.getTime();
	}

	/**
	 * Formats a phone number into the HL7 v2.3.1 spec.
	 * 
	 * @param country The country code
	 * @param area The area code
	 * @param number The number
	 * @param extension The extension
	 * @param note Any note text
	 * @return A formatted North American phone number string
	 */
	public static String formatPhoneNumber(String country, String area, String number, String extension, String note) {
		StringBuffer sb = new StringBuffer();
		// Put in the country code
		if (country != null) {
			if (country.length() > 3) return null;
			sb.append(country);
			if (area == null) sb.append(' ');
		}
		// Next, the area code
		if (area != null) {
			if (area.length() != 3) return null;
			sb.append('(');
			sb.append(area);
			sb.append(')');				
		}
		// Now the number itself
		if (number == null) return null;
		int n = 0;
		for (int i=0; i<number.length(); i++) {
			char c = number.charAt(i);
			if (Character.isDigit(c)) {
				n = n + 1;
				if (n == 4) {
					sb.append('-');
					sb.append(c);
				} else {
					sb.append(c);
				}
			}
		}
		if (n > 7) return null;
		// Next, the extension
		if ((extension != null) && (extension.length() <= 5)) {
			sb.append('X');
			sb.append(extension);
		}
		// And last, the note
		if (note != null) {
			sb.append('C');
			sb.append(note);
		}
		// Done
		return sb.toString();
	}

	/**
	 * Creates a human-readable string our of an HL7 error code.
	 * 
	 * @param code The error code
	 * @return The error string
	 */
	public static String getErrorString(String code) {
		String text = null;
		if (code == null) text = "Unspecified error";
		else if (code.equals("100")) text = "Segment sequence error";
		else if (code.equals("101")) text = "Required segment missing";
		else if (code.equals("102")) text = "Data type error";
		else if (code.equals("103")) text = "Table value not found";
		else if (code.equals("200")) text = "Unsupported message type";
		else if (code.equals("201")) text = "Unsupported event code";
		else if (code.equals("202")) text = "Unsupported processing id";
		else if (code.equals("203")) text = "Unsupported version id";
		else if (code.equals("204")) text = "Unknown key identifier";
		else if (code.equals("205")) text = "Duplicate key identifier";
		else if (code.equals("206")) text = "Application record locked";
		else if (code.equals("207")) text = "Application internal error";
		else	text = "Unspecified error";
		return text;
	}

}
