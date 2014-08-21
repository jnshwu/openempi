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

public class ITI8UpdateAndLinkTestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		1. Ensure that your database is cleared of the patient names and identifiers in the specified domains used in this test case.
		2. Your PIX Manager needs to be configured to support the following assigning authority domains:
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO
		

		Description:
		Test case ITI-8-Update-and-Link covers PIX Patient Feed, the update (ADT^A08) message, and PIX queries. Patient TAU^TERI (ID=TT444) is registered in domain NIST2010 with "correct" demographics. The same patient is then registered in domain IHE2010 with incorrect demographics; the name is spelled wrong (TOW^T; ID=TT888) and the DOB is wrong. The demographics are sufficiently different that a Cross Reference Manager should not link these two records. A query is made to confirm that the records are not linked.
		A patient update message is sent for the patient in domain IHE2010 that should synchronize the demographics as found in NIST2010. That is, patient TOW is updated to TAU with the DOB also corrected. The Cross Reference Manager should now link the two records. A query is made to confirm that the records are linked.


		Test Steps Description:
		Step 1: The NIST PIX Source sends a registration message (ADT^A04) to register patient TERI TAU in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Patient ID is TT444. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 2: The NIST PIX Source sends a registration message (ADT^A04) to register patient T TOW in domain IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO. Patient ID is TT888. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 3: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for T TOW''s ID in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO using her id TT888 in domain IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO. The query should not find patient with id TT444 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 4: Update Patient TOW^T to TAU^TERI. The DOB is also updated. The patients should now be linked.
		Step 5: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for TAU TERI''s ID in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO using her id TT888 in domain IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO. Your PIX Manager shall answer correctly to the query with TAU TERI''s ID TT444 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.		

*/
	   public void testUpdateAndLink() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160829||ADT^A04^ADT_A01|NIST-101101160828977|P|2.3.1
		EVN||20101020
		PID|||TT444^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TAU^TERI^^^^^L||19780515|F|||202 KEN HABOR^^NEW YORK CITY^NY^61000||||||||361-21-2345
		PV1||O
																								   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160839||ADT^A04^ADT_A01|NIST-101101160839347|P|2.3.1
		EVN||20101020
		PID|||TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO||TOW^T^^^^^L||19790515|F|||202 KEN HABOR^^NEW YORK CITY^NY^61000||||||||361-21-2345
		PV1||O
																				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160840||QBP^Q23^QBP_Q21|NIST-101101160840581|P|2.5
		QPD|IHE PIX Query|QRY1243438786881|TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		RCP|I
						
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160850||ADT^A08^ADT_A01|NIST-101101160850701|P|2.3.1
		EVN||20101020
		PID|||TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO||TAU^TERI^^^^^L||19780515|F|||202 KEN HABOR^^NEW YORK CITY^NY^61000||||||||361-21-2345
		PV1||R
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160851||QBP^Q23^QBP_Q21|NIST-101101160851951|D|2.5
		QPD|IHE PIX Query|QRY1243447041583|TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		RCP|I

*/
			try {
				//Step 1: Register Patient TAU^TERI with ID TT444 in domain NIST2010
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160829||ADT^A04^ADT_A01|NIST-101101160828977|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TT444^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TAU^TERI^^^^^L||19780515|F|||202 KEN HABOR^^NEW YORK CITY^NY^61000||||||||361-21-2345||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160828977", msa.getMessageControlID().getValue());
				
				
				//Step 2: Register Patient TOW^T with ID TT888 in domain IHE2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160839||ADT^A04^ADT_A01|NIST-101101160839347|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO||TOW^T^^^^^L||19790515|F|||202 KEN HABOR^^NEW YORK CITY^NY^61000||||||||361-21-2345||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160839347", msa.getMessageControlID().getValue());
				
				
				//Step 3: Query for corresponding ids in NIST2010 for the patient with ID TT888 in domain IHE2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160840||QBP^Q23^QBP_Q21|NIST-101101160840581|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1243438786881|TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101160840581", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("NF", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1243438786881", qak.getQueryTag().getValue());
				
				
				//Step 4: Update Patient TOW^T to TAU^TERI
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160850||ADT^A08^ADT_A01|NIST-101101160850701|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO||TAU^TERI^^^^^L||19780515|F|||202 KEN HABOR^^NEW YORK CITY^NY^61000||||||||361-21-2345||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160850701", msa.getMessageControlID().getValue());	
				
				
				//Step 5: Query for corresponding ids in NIST2010 for the patient with ID TT888 in domain IHE2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160851||QBP^Q23^QBP_Q21|NIST-101101160851951|D|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1243447041583|TT888^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101160851951", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1243447041583", qak.getQueryTag().getValue());
				RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
					System.out.println(pid.getPatientIdentifierList(i).getIDNumber().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());	
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test UpdateAndLink.");
			}					
		   
	   }
}
