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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The date util class
 *
 * @author Wenzhi Li
 * @version 2.0, Jul 3, 2006
 */
public class DateUtil {
	 public final static String FORMAT_yyyyMMdd = "yyyyMMdd";
	 public final static String FORMAT_yyyyMMddHHmm = "yyyyMMddHHmm";
	 public final static String FORMAT_yyyyMMddHHmmssZ = "yyyyMMddHHmmssZ";
	 
	
	 private static SimpleDateFormat hl7formatter1 = new SimpleDateFormat(FORMAT_yyyyMMdd);
	 private static SimpleDateFormat hl7formatter2 = new SimpleDateFormat(FORMAT_yyyyMMddHHmm);
	 private static SimpleDateFormat DTMformatter = new SimpleDateFormat(FORMAT_yyyyMMddHHmmssZ);

    /**
     * Parses a datetime string to its corresponding calendar value
     *
     * @param datetime the string value of the date time
     * @param format   the format of the string datetime
     * @return the Calendar object
     * @throws ParseException if there is a parsing error.
     */
    public static Calendar parseCalendar(String datetime, String format) throws ParseException {
        if (!StringUtil.goodString(datetime)) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(format);
        Date d = null;
        d = df.parse(datetime);
        Calendar date = new GregorianCalendar();
        date.setTime(d);
        return date;
    }

	 /**
	  * Converts the date String into Calender 
	  * 
	  * @param fromDate as String to be converted from
	  * @return Calender
	  * 
	  */
	 public static Calendar convertHL7DateToCalender (String fromDate) {
        if(fromDate == null)
            return null;
        try {
            Date date = null;
            if(fromDate.length() == 8)
                date = hl7formatter1.parse(fromDate);
            else
                date = hl7formatter2.parse(fromDate);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch(ParseException pex) {            
            return null;
        }
	 }
 
	 /*** 
	  * Converts the date String into date format 
	  * 
	  * @param fromDate as String to be converted from
      * @return Date
	  * 
	  */
	public static Date convertHL7Date(String fromDate) {
        if(fromDate == null)
        	return null;

        try {
            Date date = null;
            if(fromDate.length() == 8)
                date = hl7formatter1.parse(fromDate);
            else
                date = hl7formatter2.parse(fromDate);
            
            return date;
        } catch(ParseException pex) {            
            return null;
        }
    }
	/**
	 * Formats a date/time according to the HL7 v2.3.1 spec unless a
	 * custom format string is supplied, then use that.
	 * 
	 * @param date the date/time to format
	 * @param formatString a custom format string, or NULL for the default
	 * @return the formatted data as a string
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
	 * Formats a date/time according to the HL7 v2.3.1 spec.
	 * 
	 * @param date the date/time to format
	 * @return the formatted data/time as a string
	 */
	public static String formatDateTime(Date date) {
		return DTMformatter.format(date);
	}


}
