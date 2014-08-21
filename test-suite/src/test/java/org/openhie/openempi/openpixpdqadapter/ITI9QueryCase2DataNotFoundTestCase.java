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

public class ITI9QueryCase2DataNotFoundTestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
		NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO
	
		Description:
		Test case ITI-9-Query-Case2-Data-Not-Found covers the PIX Query Case 2. Three patients (LEE, GARDNER and OLESON) are registered in three different domains. Three registration messages are sent to a Cross Reference Manager. A PIX Query is sent to resolve a reference to LEE in a specific domain (NIST2010-2). Patient LEE should not be found. Another PIX Query is sent to resolve a reference to LEE in two different domains (NIST2010-2 and NIST2010-3). Patient LEE should not be found in those two domains. Another PIX Query is sent to resolve a reference to LEE in all domains. Patient LEE should not be found in any domains.

		Test Steps Description:
		Step 1: The NIST PIX Source sends a registration message (ADT^A04) to register patient DAVID LEE in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Patient ID is LD269. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 2: The NIST PIX Source sends a registration message (ADT^A04) to register patient JANICE GARDNER in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO. Patient ID is GJ789. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 3: The NIST PIX Source sends a registration message (ADT^A04) to register patient JOSEPH OLESON in domain NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO. Patient ID is OJ274. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 4: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for DAVID LEE''s ID in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO using his id LD269 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall send a query response back indicating that no data was found.
		Step 5: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for DAVID LEE''s ID in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO and NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO using his id LD269 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall send a query response back indicating that no data was found.
		Step 6: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for DAVID LEE''s ID in all domains using his id LD269 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall send a query response back indicating that no data was found.

*/
	   public void testQueryCase2DataNotFound() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161210||ADT^A04^ADT_A01|NIST-101101161210082|P|2.3.1
		EVN||20101020
		PID|||LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||LEE^DAVID^^^^^L|GANN^^^^^^L|19781010|M|||1582 Hillside Drive^^WOBURN^MA^01801||^PRN^PH^^^339^2981210||||||
		PV1||O
								   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161220||ADT^A04^ADT_A01|NIST-101101161220952|P|2.3.1
		EVN||20101020
		PID|||GJ789^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||GARDNER^JANICE^^^^^L|HILL^^^^^^L|19321031|F|||1348 Chapel Street^^HOUSTON^TX^77002||^PRN^PH^^^281^7202720||||||450-15-0943
		PV1||O
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161231||ADT^A04^ADT_A01|NIST-101101161231822|P|2.3.1
		EVN||20101020
		PID|||OJ274^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO||OLESON^JOSEPH^^^^^L|BRADSHOW^^^^^^L|19390203|M|||3398 Tator Patch Road^^BURR RIDGE^IL^60527||^PRN^PH^^^312^9412464||||||347-28-8213
		PV1||O
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161233||QBP^Q23^QBP_Q21|NIST-101101161233087|D|2.5
		QPD|IHE PIX Query|QRY148948848468|LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
		RCP|I
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161234||QBP^Q23^QBP_Q21|NIST-101101161234134|T|2.5
		QPD|IHE PIX Query|QRY121484189494|LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO
		RCP|I
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161235||QBP^Q23^QBP_Q21|NIST-101101161235258|D|2.5
		QPD|IHE PIX Query|QRY12434588489874|LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		RCP|I

*/
			try {
				//Step 1: Register Patient LEE^DAVID with ID LD269 in domain NIST2010
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161210||ADT^A04^ADT_A01|NIST-101101161210082|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||LEE^DAVID^^^^^L|GANN^^^^^^L|19781010|M|||1582 Hillside Drive^^WOBURN^MA^01801||^PRN^PH^^^339^2981210||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161210082", msa.getMessageControlID().getValue());
				
				//Step 2: Register Patient GARDNER^JANICE with ID GJ789 in domain NIST2010-2
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161220||ADT^A04^ADT_A01|NIST-101101161220952|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||GJ789^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||GARDNER^JANICE^^^^^L|HILL^^^^^^L|19321031|F|||1348 Chapel Street^^HOUSTON^TX^77002||^PRN^PH^^^281^7202720||||||450-15-0943||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161220952", msa.getMessageControlID().getValue());				
				

				//Step 3: Register Patient OLESON^JOSEPH with ID OJ274 in domain NIST2010-3
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161231||ADT^A04^ADT_A01|NIST-101101161231822|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||OJ274^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO||OLESON^JOSEPH^^^^^L|BRADSHOW^^^^^^L|19390203|M|||3398 Tator Patch Road^^BURR RIDGE^IL^60527||^PRN^PH^^^312^9412464||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161231822", msa.getMessageControlID().getValue());	
				
				//Step 4: Query for corresponding ids in NIST2010-2 for the patient with ID LD269 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161233||QBP^Q23^QBP_Q21|NIST-101101161233087|D|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY148948848468|LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161233087", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("NF", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY148948848468", qak.getQueryTag().getValue());
				
				
				//Step 5: Query for corresponding ids in NIST2010-2 and NIST2010-3 for the patient with ID LD269 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161234||QBP^Q23^QBP_Q21|NIST-101101161234134|T|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY121484189494|LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161234134", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("NF", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY121484189494", qak.getQueryTag().getValue());

				
				//Step 6: Query for corresponding ids in all domains for the patient with ID LD269 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161235||QBP^Q23^QBP_Q21|NIST-101101161235258|D|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY12434588489874|LD269^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161235258", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("NF", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY12434588489874", qak.getQueryTag().getValue());
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test QueryCase2DataNotFound.");
			}					
		   
	   }
}
