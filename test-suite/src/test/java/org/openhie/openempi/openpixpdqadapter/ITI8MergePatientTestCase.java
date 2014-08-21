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

public class ITI8MergePatientTestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		1. Ensure that your database is cleared of the patient names and identifiers in the specified domains used in this test case.
		2. Your PIX Manager needs to be configured to support the following assigning authority domains:
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO
		

		Description:
		The purpose is to check that your PIX Manager can merge two existing patient when it receives a merge message (ADT^A40^ADT_A39).
		The patient WASHINGTON^MARY registers in domain NIST2010 with the id MW-10001. The same patient, with the same demographics registers in domain IHE2010 with the id MW-20002. Later, the patient LINCOLN^MARY registers in domain NIST2010 with the id ML-30003. LINCOLN MARY and WASHINGTON MARY are in fact the same person: WASHINGTON is a maiden name. Someone notices that WASHINGTON and LINCOLN refer to the same person in domain NIST2010 and a merge message (ADT^A40^ADT_A39) is sent to correct that. WASHINGTON is merged with LINCOLN, LINCOLN is the remaining record. The domain IHE2010 has no direct knowledge of the merge. Your PIX Manager should maintain the link for this patient between domains NIST2010 and IHE2010. The NIST PIX Consumer sends a query to request corresponding id for patient WASHINGTON MARY (MW-20002). Your PIX Manager shall return the remaining id for LINCOLN (ML-30003) in domain NIST2010.


		Test Steps Description:
		Step 1: WASHINGTON registers in domain NIST2010 with ID MW-10001
		Step 2: WASHINGTON registers in domain IHE2010 with ID MW-20002
		Step 3: WASHINGTON marries, takes on the name LINCOLN and changes address at the same time. A new A04 registration message is sent in domain NIST2010 with ID ML-30003 (because she forgot to tell the administrator of her name change). This registration message is for LINCOLN with a new address but same DOB and other demographics.
		Step 4: Someone recognizes the mistake and merges the records MW-10001 and ML-30003 in domain NIST2010. LINCOLN (ML-30003) is the remaining record. The PIX Manager should maintain the link between WASHINGTON in domain IHE2010 and LINCOLN in domain NIST2010.
		Step 5: The domain IHE2010 has no direct knowledge of the merge. A query from domain IHE2010 is made with the ID for WASHINGTON (MW-20002). The PIX manager shall return the remaining ID for LINCOLN (ML-30003) in domain NIST2010.

*/
	   public void testUpdateAndLink() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161058||ADT^A04^ADT_A01|NIST-101101161058473|P|2.3.1
		EVN||20101020
		PID|||MW-10001^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||WASHINGTON^MARY^^^^^L||19771208|F|||100 JORIE BLVD^^CHICAGO^IL^60523||||||||100-09-1234
		PV1||O
																										   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161108||ADT^A04^ADT_A01|NIST-101101161108875|P|2.3.1
		EVN||20101020
		PID|||MW-20002^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO||WASHINGTON^MARY^^^^^L||19771208|F|||100 JORIE BLVD^^CHICAGO^IL^60523||||||||100-09-1234
		PV1||O
																						
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161119||ADT^A04^ADT_A01|NIST-101101161119698|P|2.3.1
		EVN||20101020
		PID|||ML-30003^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||LINCOLN^MARY^^^^^L||19771208|F|||JEAN JAQUES BLVD^^NEW YORK^NY^60001||||||||100-09-1234
		PV1||O
								
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161122||ADT^A40^ADT_A39|NIST-101101161122806|P|2.3.1
		EVN||20101020
		PID|||ML-30003^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||LINCOLN^MARY^^^^^L||19771208|F|||JEAN JAQUES BLVD^^NEW YORK^NY^60001||||||||100-09-1234
		MRG|MW-10001^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161123||QBP^Q23^QBP_Q21|NIST-101101161123790|P|2.5
		QPD|IHE PIX Query|QRY1243523037937|MW-20002^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		RCP|I


*/
			try {
				//Step 1: Register Patient WASHINGTON^MARY with ID MW-10001 in domain NIST2010
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161058||ADT^A04^ADT_A01|NIST-101101161058473|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||MW-10001^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||WASHINGTON^MARY^^^^^L||19771208|F|||100 JORIE BLVD^^CHICAGO^IL^60523||||||||100-09-1234||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161058473", msa.getMessageControlID().getValue());
				
				
				//Step 2: Register Patient WASHINGTON^MARY with ID MW-20002 in domain IHE2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161108||ADT^A04^ADT_A01|NIST-101101161108875|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||MW-20002^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO||WASHINGTON^MARY^^^^^L||19771208|F|||100 JORIE BLVD^^CHICAGO^IL^60523||||||||100-09-1234||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161108875", msa.getMessageControlID().getValue());
				
				
				//Step 3: Register Patient LINCOLN^MARY with ID ML-30003 in domain NIST2010.
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161119||ADT^A04^ADT_A01|NIST-101101161119698|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||ML-30003^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||LINCOLN^MARY^^^^^L||19771208|F|||JEAN JAQUES BLVD^^NEW YORK^NY^60001||||||||100-09-1234||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161119698", msa.getMessageControlID().getValue());
				
				
				//Step 4: Merge Patient WASHINGTON^MARY to LINCOLN^MARY
				message ="MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161122||ADT^A40^ADT_A39|NIST-101101161122806|P|2.3.1||||||||\r"+
				"EVN|A40|20060919004624||||20060919004340\r"+
				"PID|||ML-30003^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||LINCOLN^MARY^^^^^L||19771208|F|||JEAN JAQUES BLVD^^NEW YORK^NY^60001||||||||100-09-1234||||||||||||||||||||\r"+
				"MRG|MW-10001^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|\r";
				response = sendMessage(message);	        
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161122806", msa.getMessageControlID().getValue());
				
				
				//Step 5: Query for corresponding ids in NIST2010 for the patient with ID MW-20002 in domain IHE2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161123||QBP^Q23^QBP_Q21|NIST-101101161123790|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1243523037937|MW-20002^^^IHE2010&1.3.6.1.4.1.21367.2010.1.1&ISO|^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161123790", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1243523037937", qak.getQueryTag().getValue());
				RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
					System.out.println(pid.getPatientIdentifierList(i).getIDNumber().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());	
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test MergePatient.");
			}					
		   
	   }
}
