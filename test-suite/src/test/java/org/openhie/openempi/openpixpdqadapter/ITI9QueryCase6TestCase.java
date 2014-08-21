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

public class ITI9QueryCase6TestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO

		Description:
		Test case ITI-9-Query-Case6 covers the PIX Query Case 6. One patient (TRIPLET) is registered twice with different ids in the same domain NIST2010. These two patients are not linked because they are in the same domain. This patient is also registered in another domain NIST2010-2. Three registration messages are sent to a Cross Reference Manager. A PIX Query is sent to find corresponding ids in domain NIST2010 for the patient MEGAN TRIPLET with ID MT-100-003 in domain NIST2010-2. Patient TRIPLET should be found in domain NIST2010. Your PIX Manager shall answer correctly to the query returning one of identifiers or both in domain NIST2010.

		Test Steps Description:
		Step 1: The NIST PIX Source sends a registration message (ADT^A04) to register patient MEGAN TRIPLET in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Patient ID is MT-100-001. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 2: The NIST PIX Source sends a registration message (ADT^A04) to register patient MEGAN TRIPLET in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Patient ID is MT-100-002. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 3: The NIST PIX Source sends a registration message (ADT^A04) to register patient MEGAN TRIPLET in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO. Patient ID is MT-100-003. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 4: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for MEGAN TRIPLET''s ID in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO using her id MT-100-003 in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO. Your PIX Manager shall answer correctly to the query with MEGAN TRIPLET''s IDs MT-100-001 or MT-100-002 or both in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.

*/
	   public void testQueryCase6() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161322||ADT^A04^ADT_A01|NIST-101101161322503|P|2.3.1
		EVN||20101020
		PID|||MT-100-001^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TRIPLET^MEGAN^^^^^L|RICH^^^^^^L|19321219|F|||2266 Station Street^^RICHMOND^CA^94801||^PRN^PH^^^510^9658426||||||626-21-6397
		PV1||O
																   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161334||ADT^A04^ADT_A01|NIST-101101161334232|P|2.3.1
		EVN||20101020
		PID|||MT-100-002^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TRIPLET^MEGAN^^^^^L|RICH^^^^^^L|19321219|F|||2266 Station Street^^RICHMOND^CA^94801||^PRN^PH^^^510^9658426||||||626-21-6397
		PV1||O
												
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161346||ADT^A04^ADT_A01|NIST-101101161346633|P|2.3.1
		EVN||20101020
		PID|||MT-100-003^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||TRIPLET^MEGAN^^^^^L|RICH^^^^^^L|19321219|F|||2266 Station Street^^RICHMOND^CA^94801||^PRN^PH^^^510^9658426||||||626-21-6397
		PV1||O
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161348||QBP^Q23^QBP_Q21|NIST-101101161348023|P|2.5
		QPD|IHE PIX Query|QRY184861681|MT-100-003^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		RCP|I
						

*/
			try {
				//Step 1: Register Patient TRIPLET^MEGAN with ID MT-100-001 in domain NIST2010
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161322||ADT^A04^ADT_A01|NIST-101101161322503|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||MT-100-001^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TRIPLET^MEGAN^^^^^L|RICH^^^^^^L|19321219|F|||2266 Station Street^^RICHMOND^CA^94801||^PRN^PH^^^510^9658426||||||626-21-6397||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161322503", msa.getMessageControlID().getValue());
				
				//Step 2: Register Patient TRIPLET^MEGAN with ID MT-100-002 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161334||ADT^A04^ADT_A01|NIST-101101161334232|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||MT-100-002^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TRIPLET^MEGAN^^^^^L|RICH^^^^^^L|19321219|F|||2266 Station Street^^RICHMOND^CA^94801||^PRN^PH^^^510^9658426||||||626-21-6397||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161334232", msa.getMessageControlID().getValue());				
				
				
				//Step 3: Register Patient TRIPLET^MEGAN with ID MT-100-003 in domain NIST2010-2
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161346||ADT^A04^ADT_A01|NIST-101101161346633|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||MT-100-003^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||TRIPLET^MEGAN^^^^^L|RICH^^^^^^L|19321219|F|||2266 Station Street^^RICHMOND^CA^94801||^PRN^PH^^^510^9658426||||||626-21-6397||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161346633", msa.getMessageControlID().getValue());				
				
				
				//Step 4: Query for corresponding ids in NIST2010 for the patient with ID MT-100-003 in domain NIST2010-2
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161348||QBP^Q23^QBP_Q21|NIST-101101161348023|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY184861681|MT-100-003^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161348023", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY184861681", qak.getQueryTag().getValue());
				RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
					System.out.println(pid.getPatientIdentifierList(i).getIDNumber().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());	
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test QueryCase6.");
			}					
		   
	   }
}
