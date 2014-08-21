/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
package org.openhealthtools.openpixpdq.impl.v2.hl7;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.PhoneNumber;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v231.datatype.ELD;
import ca.uhn.hl7v2.model.v231.datatype.HD;
import ca.uhn.hl7v2.model.v231.segment.ERR;
import ca.uhn.hl7v2.model.v231.segment.MSA;
import ca.uhn.hl7v2.model.v231.segment.MSH;

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
   * Populates an HL7 v2.3.1 MSH segment according to the IHE standard
   * 
   * @param msh The MSH segment
   * @param type The type of message the segment belongs to
   * @param event The event that triggered this message
   * @param id The message control ID for this message
   * @param sendingApplication The sending application
   * @param sendingFacility The sending facility
   * @param receivingApplication The receiving application
   * @param receivingFacility The receiving facility
   * @throws DataTypeException When supplied data is inappropriate
   * @throws IheConfigurationException When the connection to which this will be sent if not configured properly
   */
	public static void populateMSH(MSH msh, String type, String event, String id, Identifier sendingApplication, Identifier sendingFacility,
            Identifier receivingApplication, Identifier receivingFacility) throws DataTypeException, IheConfigurationException {
		// MSH-1
		msh.getFieldSeparator().setValue("|");
		// MSH-2
		msh.getEncodingCharacters().setValue("^~\\&");
		// MSH-3
		HD hd = msh.getSendingApplication();
	    hd.getNamespaceID().setValue( sendingApplication.getNamespaceId() );
	    hd.getUniversalID().setValue( sendingApplication.getUniversalId() );
	    hd.getUniversalIDType().setValue( sendingApplication.getUniversalIdType() );
	        // MSH-4
	    hd = msh.getSendingFacility();
	    hd.getNamespaceID().setValue( sendingFacility.getNamespaceId() );
	    hd.getUniversalID().setValue( sendingFacility.getUniversalId() );
	    hd.getUniversalIDType().setValue( sendingFacility.getUniversalIdType() );
	        // MSH-5
	    hd = msh.getReceivingApplication();
	    hd.getNamespaceID().setValue( receivingApplication.getNamespaceId() );
	    hd.getUniversalID().setValue( receivingApplication.getUniversalId() );
	    hd.getUniversalIDType().setValue( receivingApplication.getUniversalIdType() );
	        // MSH-6
	    hd = msh.getReceivingFacility();
	    hd.getNamespaceID().setValue( receivingFacility.getNamespaceId() );
	    hd.getUniversalID().setValue( receivingFacility.getUniversalId() );
	    hd.getUniversalIDType().setValue( receivingFacility.getUniversalIdType());
		// MSH-7
		msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(formatDateTime(new Date()));
		// MSH-9
		msh.getMessageType().getMessageType().setValue(type);
		msh.getMessageType().getTriggerEvent().setValue(event);
		// MSH-10
		msh.getMessageControlID().setValue(id);
		// MSH-11
		msh.getProcessingID().getProcessingID().setValue("P");
		// MSH-12
		msh.getVersionID().getVersionID().setValue("2.3.1");
	}
	
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
	 * Parses an HL7 v2.3.1 style formatted phone number into a PhoneNumber
	 * object.
	 * 
	 * @param phone The phone number object to populate from the string
	 * @param theNumber The phone number string to be parsed
	 */
	public static void parsePhoneNumber(PhoneNumber phone, String theNumber) {
		if (theNumber != null) {
			int end = theNumber.length();
			// First, grab the note off the end
			int i = theNumber.indexOf("C");
			if (0 <= i) {
				end = i;
				if (i < theNumber.length()) {
					phone.setNote(theNumber.substring(i+1));
				} 
			}
			// Second, grab the extension
			i = theNumber.indexOf("X");
			if ((0 <= i) && (i < end)) {
				phone.setExtension(theNumber.substring(i+1, end).trim());
				end = i;
			}
			// Everything else is really a phone number
			i = theNumber.indexOf("(");
			if ((0 <= i) && (i < end)) {
				// Case 1: ccc (aaa) nnn-nnnn
				if (i > 0) {
					phone.setCountryCode(theNumber.substring(0, i).trim());
					i = i + 1;
				} else {
					i = 1;
				}
				int j = theNumber.indexOf(")");
				if ((0 <= j) && (j < end)) {
					phone.setAreaCode(theNumber.substring(i,j).trim());
					i = j + 1;
				}
				// At this point, i = the index of the character after the area code
				if (i < end) {
					phone.setNumber(theNumber.substring(i, end).trim());
				}
			} else {
				// Case 2: ccc nnn-nnnn
				i = theNumber.indexOf(" ");
				if ((0 <= i) && (i < end)) {
					phone.setCountryCode(theNumber.substring(0, i).trim());
					i = i + 1;
					if (i < end) {
						phone.setNumber(theNumber.substring(i, end).trim());
					}
				} else if (0 < end) {
					// Case 3: nnn-nnnn
					phone.setNumber(theNumber.substring(0, end).trim());
				}
			}
		}
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
	
	/**
     * Populates MSA segment, used by, for example, PIX Query response
     *
     * @param msa The Message Acknowledgment segment
     * @param acknowledgmentCode The two letter of acknowledgment code
     * @param messageControlId The message control Id
     * @throws DataTypeException When MSA segment values cannot be set
     */
    public static void populateMSA(MSA msa, String acknowledgmentCode, String messageControlId) throws DataTypeException {
         //MSA-1
         msa.getAcknowledgementCode().setValue(acknowledgmentCode);
         //MSA-2
         msa.getMessageControlID().setValue( messageControlId );
    }

    /**
     * Populates ERR segment.
     *
     * @param err The ERR segment to be populated
     * @param segmentId The id of the segment that caused the error
     * @param sequence The sequence of the segment
     * @param fieldPosition The field position where the error is
     * @param fieldRepetition The repetition of the error field
     * @param componentNumber The component number in the error field
     * @param hl7ErrorCode The HL7 error code
     * @param hl7ErrorText The HL7 error text
     * @throws DataTypeException When ERR segment values cannot be set.
     * @throws HL7Exception When HL7 related issue happens
     */
    public static void populateERR(ERR err, String segmentId, String sequence, String fieldPosition, String fieldRepetition,
            String componentNumber, String hl7ErrorCode, String hl7ErrorText) throws DataTypeException, HL7Exception {
//    	Components:  <segment ID (ST)> ^ <sequence (NM)> ^ <field position (NM)> ^ <code identifying error (CE)> // see hl7231 , ch2fin.doc - 2.24.3
    	ELD erl = err.getErrorCodeAndLocation(0);    
        erl.getSegmentID().setValue( segmentId );
        erl.getSequence().setValue(sequence);
        erl.getFieldPosition().setValue(fieldPosition);              
		erl.getCodeIdentifyingError().getIdentifier().setValue(hl7ErrorCode);
		erl.getCodeIdentifyingError().getText().setValue(hl7ErrorText);
	}
    
}
