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

public class ITI8UpdateUnknownDomainTestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO

		Description:
		The purpose of this test is to check that a PIX Manager does not accept an update message (ADT^A08^ADT_A01) that has an unknown domain. An unknown domain is a valid domain but it is not recognized by the PIX Manager.
		We register the patient THOMPSON MONICA in a single domain NIST2010. Then we update the same patient in an unknown domain. We expect to receive an ACK back with the code AE or AR.

*/
	   public void testUpdateUnknownDomain() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160934||ADT^A04^ADT_A01|NIST-101101160934088|P|2.3.1
		EVN||20101020
		PID|||TM111025^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989
		PV1||O
																				   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160944||ADT^A08^ADT_A01|NIST-101101160944662|P|2.3.1
		EVN||20101020
		PID|||TM111025^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989
		PV1||R
																
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160955||ADT^A08^ADT_A01|NIST-101101160955142|P|2.3.1
		EVN||20101020
		PID|||TM111025^^^UNKNOWNDOMAIN||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989
		PV1||R
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161005||ADT^A08^ADT_A01|NIST-101101161005715|P|2.3.1
		EVN||20101020
		PID|||TM111025^^^&2.16.840.1.113883.3.72.5.9.99&ISO||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989
		PV1||R

*/
			try {
				//Step 1: Register patient THOMPSON^MONICA with TM111025^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO 
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160934||ADT^A04^ADT_A01|NIST-101101160934088|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TM111025^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160934088", msa.getMessageControlID().getValue());
				
				
				//Step 2: Update patient demographics in domain UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160944||ADT^A08^ADT_A01|NIST-101101160944662|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TM111025^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160944662", msa.getMessageControlID().getValue());				
				
				
				//Step 3: Update patient demographics in domain UNKNOWNDOMAIN&&
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160955||ADT^A08^ADT_A01|NIST-101101160955142|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TM111025^^^UNKNOWNDOMAIN||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160955142", msa.getMessageControlID().getValue());		
				
				//Step 4: Update patient demographics in domain &2.16.840.1.113883.3.72.5.9.99&ISO
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161005||ADT^A08^ADT_A01|NIST-101101161005715|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TM111025^^^&2.16.840.1.113883.3.72.5.9.99&ISO||THOMPSON^MONICA^^^^^L|HOLMQUIST^^^^^^L|19730208|F|||4709 McDonald Avenue^^MAITLAND^FL^32751||^PRN^PH^^^407^8856248||||||846-42-6989||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161005715", msa.getMessageControlID().getValue());			
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test UpdateUnknownDomain.");
			}					
		   
	   }
}
