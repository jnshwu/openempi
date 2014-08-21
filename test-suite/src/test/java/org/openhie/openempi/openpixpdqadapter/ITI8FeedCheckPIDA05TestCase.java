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

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.segment.MSA;

public class ITI8FeedCheckPIDA05TestCase extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
	
		Description:
		The purpose of this test is to check that a PIX Manager does not accept a feed message (ADT^A05^ADT_A01) that specifies the assigning authority of the patient identifier incorrectly. The PIX Manager shall only accept an assigning authority with values populated in PID-3.4.1, PID-3.4.2 and PID-3.4.3, or all three components. 
		We register the same patient ROBERT JOHNSTON with ID RJ-438 in the domain NIST2010 but the domain is expressed using an invalid format. The PIX Manager is expected to return an ACK with the code AE or AR.

*/
	   public void testFeedCheckPIDA05() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160442||ADT^A05^ADT_A01|NIST-101101160442327|P|2.3.1
		EVN||20101020
		PID|||RJ-438^^^&2.16.840.1.113883.3.72.5.9.1||JOHNSTON^ROBERT^^^^^L|MURRAY^^^^^^L|19830205|M|||1220 Centennial Farm Road^^ELLIOTT^IA^51532||^PRN^PH^^^712^7670867||||||481-27-4185
		PV1||P
						   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160453||ADT^A05^ADT_A01|NIST-101101160453134|P|2.3.1
		EVN||20101020
		PID|||RJ-438^^^&&ISO||JOHNSTON^ROBERT^^^^^L|MURRAY^^^^^^L|19830205|M|||1220 Centennial Farm Road^^ELLIOTT^IA^51532||^PRN^PH^^^712^7670867||||||481-27-4185
		PV1||P

*/
			try {
				//Step 1: Register patient JOHNSTON^ROBERT with RJ-438^^^&2.16.840.1.113883.3.72.5.9.1&
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160442||ADT^A05^ADT_A01|NIST-101101160442327|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||RJ-438^^^&2.16.840.1.113883.3.72.5.9.1||JOHNSTON^ROBERT^^^^^L|MURRAY^^^^^^L|19830205|M|||1220 Centennial Farm Road^^ELLIOTT^IA^51532||^PRN^PH^^^712^7670867||||||481-27-4185||||||||||||||||||||\r" +
					      "PV1||P||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160442327", msa.getMessageControlID().getValue());
				
				
				
				//Step 2: Register patient JOHNSTON^ROBERT with RJ-438^^^&&ISO 
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160453||ADT^A05^ADT_A01|NIST-101101160453134|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||RJ-438^^^&&ISO||JOHNSTON^ROBERT^^^^^L|MURRAY^^^^^^L|19830205|M|||1220 Centennial Farm Road^^ELLIOTT^IA^51532||^PRN^PH^^^712^7670867||||||481-27-4185||||||||||||||||||||\r" +
					      "PV1||P||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160453134", msa.getMessageControlID().getValue());	
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test FeedCheckPIDA05.");
			}					
		   
	   }
}
