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

public class ITI9QueryCase5TestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
	
		Description:
		Test case ITI-9-Query-Case5 covers the PIX Query Case 5. One patient (CRONAN) is registered in two different domains. Two registration messages are sent to a Cross Reference Manager. A PIX Query is sent to find corresponding ids in domain UNKNOWNDOMAIN for the patient KARL CRONAN with ID KC-51-958 in domain NIST2010. The PIX Manager does not recognize the requested domain UNKNOWNDOMAIN. The same query is sent but the requested domains are NIST2010-2 and UNKNOWNDOMAIN. In both cases the PIX Manager shoud return a response with the specific error.

		Test Steps Description:
		Step 1: The NIST PIX Source sends a registration message (ADT^A04) to register patient KARL CRONAN in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Patient ID is KC-51-958. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 2: The NIST PIX Source sends a registration message (ADT^A04) to register patient KARL CRONAN in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO. Patient ID is KC0000145. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 3: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for KARL CRONAN''s ID in domain UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO using his id KC-51-958 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall send a query response back indicating that an error occured.
		Step 4: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for KARL CRONAN’s ID in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO and UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO using his id KC-51-958 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall send a query response back indicating that an error occured.

*/
	   public void testQueryCase5() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161254||ADT^A04^ADT_A01|NIST-101101161254234|P|2.3.1
		EVN||20101020
		PID|||KC-51-958^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||CRONAN^KARL^^^^^L|NEW^^^^^^L|19861005|M|||443 Holly Street^^ELBERTON^GA^30653||^PRN^PH^^^706^2831110||||||259-05-1931
		PV1||O
														   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161308||ADT^A04^ADT_A01|NIST-101101161308603|P|2.3.1
		EVN||20101020
		PID|||KC0000145^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||CRONAN^KARL^^^^^L|NEW^^^^^^L|19861005|M|||443 Holly Street^^ELBERTON^GA^30653||^PRN^PH^^^706^2831110||||||259-05-1931
		PV1||O
										
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161310||QBP^Q23^QBP_Q21|NIST-101101161310009|P|2.5
		QPD|IHE PIX Query|QRY2186485688164|KC-51-958^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO
		RCP|I
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161311||QBP^Q23^QBP_Q21|NIST-101101161311133|P|2.5
		QPD|IHE PIX Query|QRY218841999789|KC-51-958^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO
		RCP|I
				

*/
			try {
				//Step 1: Register Patient CRONAN^KARL with ID KC-51-958 in domain NIST2010
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161254||ADT^A04^ADT_A01|NIST-101101161254234|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||KC-51-958^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||CRONAN^KARL^^^^^L|NEW^^^^^^L|19861005|M|||443 Holly Street^^ELBERTON^GA^30653||^PRN^PH^^^706^2831110||||||259-05-1931||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161254234", msa.getMessageControlID().getValue());
				
				//Step 2: Register Patient CRONAN^KARL with ID KC0000145 in domain NIST2010-2
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161308||ADT^A04^ADT_A01|NIST-101101161308603|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||KC0000145^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||CRONAN^KARL^^^^^L|NEW^^^^^^L|19861005|M|||443 Holly Street^^ELBERTON^GA^30653||^PRN^PH^^^706^2831110||||||259-05-1931||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161308603", msa.getMessageControlID().getValue());				
				
				
				//Step 3: Query for corresponding ids in UNKNOWNDOMAIN for the patient with ID KC-51-958 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161310||QBP^Q23^QBP_Q21|NIST-101101161310009|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY2186485688164|KC-51-958^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161310009", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("AE", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY2186485688164", qak.getQueryTag().getValue());
				
				
				//Step 4: Query for corresponding ids in NIST2010-2 and UNKNOWNDOMAIN for the patient with ID KC-51-958 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161311||QBP^Q23^QBP_Q21|NIST-101101161311133|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY218841999789|KC-51-958^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161311133", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("AE", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY218841999789", qak.getQueryTag().getValue());

				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test QueryCase5.");
			}					
		   
	   }
}
