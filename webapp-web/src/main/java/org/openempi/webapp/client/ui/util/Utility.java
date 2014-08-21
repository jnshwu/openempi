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
package org.openempi.webapp.client.ui.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

public class Utility
{
	public static String convertToDescription(String name) {
		if (name == null) {
			return "";
		}
		String description = name.substring(0, 1).toUpperCase();
		name = name.substring(1);
			
		// Iterate over the characters in the forward direction
		for (int i=0; i<name.length(); i++) {
	           if ( Character.isDigit(name.charAt(i))) {	        	   
	        	   if( Character.isDigit(name.charAt(i-1)) || name.charAt(i-1) == 'V' )
	    	           description += name.charAt(i);  
	        	   else
		        	   description = description+" "+name.charAt(i);	
	        	   
	           } else if ( Character.isUpperCase(name.charAt(i))) {
	        	   description = description+" "+name.charAt(i);	        	   
	           } else {
	        	   description += name.charAt(i);  
	           }
		}		
		
		return description;		
	}
	
	
	public static String prefixWithZeros(String binary, int n) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < n - binary.length(); i++) {
	        sb.append('0');
	    }
	    return sb.append(binary).toString();
	}
	
	public static String DateToString(Date date) {
		if( date == null)
			return "";
		
//		return DateTimeFormat.getShortDateFormat().format(date);
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
		return dtf.format(date);
		
	}
	
	public static Date StringToDate(String strDate) {
		Date date = null;
		if( strDate != null && !strDate.isEmpty() ) {
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
			date = dtf.parseStrict(strDate);
		}
		return date;
	}
	
	public static String DateTimeToString(Date date) {
		if( date == null)
			return "";
		
//		return DateTimeFormat.getShortDateTimeFormat().format(date);
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.S");
		return dtf.format(date);
	}
	
	public static Date StringToDateTime(String strDate) {
		Date date = null;
		if( strDate != null && !strDate.isEmpty() ) {
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.S");
			date =  dtf.parseStrict(strDate);
		}
		return date;
	}
}
