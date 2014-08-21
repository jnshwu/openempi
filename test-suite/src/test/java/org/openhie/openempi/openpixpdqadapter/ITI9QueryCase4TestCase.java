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

public class ITI9QueryCase4TestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
		NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO
	
		Description:
		Test case ITI-9-Query-Case4 covers the PIX Query Case 4. A PIX Query is sent to find corresponding ids in domain NIST2010-2 for the patient DAMIEN WRONG with ID DWRONG-0184 in domain UNKNOWNDOMAIN. The PIX Manager does not recognize the domain UNKNOWNDOMAIN. The same query is sent but the requested domains are NIST2010-2 and NIST2010-3. Another one is sent but this time it is for all the domains. In all cases the PIX Manager shoud return a response with the specific error.
		
		Test Steps Description:
		Step 1: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for DAMIEN WRONG''s ID in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO using his id DWRONG-0184 in domain UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO. Your PIX Manager shall send a query response back indicating that an error occured.
		Step 2: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for DAMIEN WRONG''s ID in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO and NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO using his id DWRONG-0184 in domain UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO. Your PIX Manager shall send a query response back indicating that an error occured.
		Step 3: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for DAMIEN WRONG''s ID in all domains using his id DWRONG-0184 in domain UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO. Your PIX Manager shall send a query response back indicating that an error occured.

*/
	   public void testQueryCase4() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161239||QBP^Q23^QBP_Q21|NIST-101101161239600|P|2.5
		QPD|IHE PIX Query|QRY1243418844848646|DWRONG-0184^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
		RCP|I
												   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161240||QBP^Q23^QBP_Q21|NIST-101101161240834|P|2.5
		QPD|IHE PIX Query|QRY1243448461681864|DWRONG-0184^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO
		RCP|I
								
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161242||QBP^Q23^QBP_Q21|NIST-101101161242068|P|2.5
		QPD|IHE PIX Query|QRY1243484846181004|DWRONG-0184^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO
		RCP|I
				

*/
			try {
				
				//Step 1: Query for corresponding ids in NIST2010-2 for the patient with ID DWRONG-0184 in domain UNKNOWNDOMAIN
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161239||QBP^Q23^QBP_Q21|NIST-101101161239600|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1243418844848646|DWRONG-0184^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO\r" +
						  "RCP|I||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161239600", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("AE", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1243418844848646", qak.getQueryTag().getValue());
				
				
				//Step 2: Query for corresponding ids in NIST2010-2 and NIST2010-3 for the patient with ID DWRONG-0184 in domain UNKNOWNDOMAIN
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161240||QBP^Q23^QBP_Q21|NIST-101101161240834|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1243448461681864|DWRONG-0184^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161240834", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("AE", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1243448461681864", qak.getQueryTag().getValue());

				
				//Step 3: Query for corresponding ids in all domains for the patient with ID DWRONG-0184 in domain UNKNOWNDOMAIN
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161242||QBP^Q23^QBP_Q21|NIST-101101161242068|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1243484846181004|DWRONG-0184^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161242068", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("AE", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1243484846181004", qak.getQueryTag().getValue());
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test QueryCase4.");
			}					
		   
	   }
}
