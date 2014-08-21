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
import ca.uhn.hl7v2.model.v25.group.RSP_K23_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.message.RSP_K23;
import ca.uhn.hl7v2.model.v25.segment.PID;

public class ITI8UpdateCheckPIDTestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO

		Description:
		The purpose of this test is to check that a PIX Manager does not accept an update message (ADT^A08^ADT_A01) that has a invalid domain. An invalid domain is either PID-3.2 only or PID-3.3 only valued.
		We register the patient CHRISTOPHER ERVIN in a single domain NIST2010. Then we update the same patient in the same domain but the domain is expressed using one of the invalid format. We expect to receive and ACK back with the code AE or AR.

*/
	   public void testUpdateCheckPID() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160902||ADT^A04^ADT_A01|NIST-101101160902493|P|2.3.1
		EVN||20101020
		PID|||EC-015236^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||ERVIN^CHRISTOPHER^^^^^L|PEREZ^^^^^^L|19650109|M|||2452 Emma Street^^COTTON CENTER^TX^79021||^PRN^PH^^^806^8791233||||||462-92-4841
		PV1||O
																		   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160913||ADT^A08^ADT_A01|NIST-101101160913020|P|2.3.1
		EVN||20101020
		PID|||EC-015236^^^&2.16.840.1.113883.3.72.5.9.1||ERVIN^CHRISTOPHER^^^^^L|PEREZ^^^^^^L|19650109|M|||2452 Emma Street^^COTTON CENTER^TX^79021||^PRN^PH^^^806^8791233||||||462-92-4841
		PV1||R
														
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160923||ADT^A08^ADT_A01|NIST-101101160923515|P|2.3.1
		EVN||20101020
		PID|||EC-015236^^^&&ISO||ERVIN^CHRISTOPHER^^^^^L|PEREZ^^^^^^L|19650109|M|||2452 Emma Street^^COTTON CENTER^TX^79021||^PRN^PH^^^806^8791233||||||462-92-4841
		PV1||R
				

*/
			try {
				//Step 1: Register patient ERVIN^CHRISTOPHER with EC-015236^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO 
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160902||ADT^A04^ADT_A01|NIST-101101160902493|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||EC-015236^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||ERVIN^CHRISTOPHER^^^^^L|PEREZ^^^^^^L|19650109|M|||2452 Emma Street^^COTTON CENTER^TX^79021||^PRN^PH^^^806^8791233||||||462-92-4841||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160902493", msa.getMessageControlID().getValue());
				
				
				//Step 2: Update patient demographics in domain &2.16.840.1.113883.3.72.5.9.1& 
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160913||ADT^A08^ADT_A01|NIST-101101160913020|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||EC-015236^^^&2.16.840.1.113883.3.72.5.9.1||ERVIN^CHRISTOPHER^^^^^L|PEREZ^^^^^^L|19650109|M|||2452 Emma Street^^COTTON CENTER^TX^79021||^PRN^PH^^^806^8791233||||||462-92-4841||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160913020", msa.getMessageControlID().getValue());				
				
				
				//Step 3: Update patient demographics in domain &&ISO
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160923||ADT^A08^ADT_A01|NIST-101101160923515|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||EC-015236^^^&&ISO||ERVIN^CHRISTOPHER^^^^^L|PEREZ^^^^^^L|19650109|M|||2452 Emma Street^^COTTON CENTER^TX^79021||^PRN^PH^^^806^8791233||||||462-92-4841||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160923515", msa.getMessageControlID().getValue());			
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test UpdateCheckPID.");
			}					
		   
	   }
}
