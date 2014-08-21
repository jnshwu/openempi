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

public class ITI9QueryCase1DataFoundTestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
		NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO
	
		Description:
		Test case ITI-9-Query-Case1-Data-Found covers the PIX Query Case 1. One patient (ALPHA) is registered in three different domains. Three registration messages are sent to a Cross Reference Manager. A PIX Query is sent to resolve a reference to ALPHA in a specific domain (NIST2010-2). Patient ALPHA should be found. Another PIX Query is sent to resolve a reference to ALPHA in two different domains (NIST2010-2 and NIST2010-3). Patient ALPHA should be found in those two domains. Another PIX Query is sent to resolve a reference to ALPHA in all domains. Patient ALPHA should be found in two domains (NIST2010-2 and NIST2010-3).

		Test Steps Description:
		Step 1: The NIST PIX Source sends a registration message (ADT^A04) to register patient ALAN ALPHA in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Patient ID is PIX10501. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 2: The NIST PIX Source sends a registration message (ADT^A04) to register patient ALAN ALPHA in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO. Patient ID is XYZ10501. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 3: The NIST PIX Source sends a registration message (ADT^A04) to register patient ALAN ALPHA in domain NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO. Patient ID is ABC10501. Your PIX Manager shall register the patient and send a correct ACK message back.
		Step 4: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for ALAN ALPHA''s ID in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.1&ISO using his id PIX10501 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall answer correctly to the query with ALAN ALPHA''s ID XYZ10501 in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 5: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for ALAN ALPHA''s ID in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.1&ISO and NIST2010-3&2.16.840.1.113883.3.72.5.9.1&ISO using his id PIX10501 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall answer correctly to the query with ALAN ALPHA''s ID XYZ10501 and ABC10501 in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.1&ISO and NIST2010-3&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 6: The NIST PIX Consumer sends a query message (QBP^Q23) to ask for ALAN ALPHA''s ID in all domains using his id PIX10501 in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO. Your PIX Manager shall answer correctly to the query with ALAN ALPHA''s ID XYZ10501 and ABC10501 in domain NIST2010-2&2.16.840.1.113883.3.72.5.9.1&ISO and NIST2010-3&2.16.840.1.113883.3.72.5.9.1&ISO.

*/
	   public void testQueryCase1DataFound() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161134||ADT^A04^ADT_A01|NIST-101101161134113|P|2.3.1
		EVN||20101020
		PID|||PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||ALPHA^ALAN^^^^^L|HOUSE^^^^^^L|19690908|M|||338 Apple Lane^^LITTLETON^IL^61452||^PRN^PH^^^309^2576057||||||357-56-4113
		PV1||O
						   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161145||ADT^A04^ADT_A01|NIST-101101161144999|P|2.3.1
		EVN||20101020
		PID|||XYZ10501^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||ALPHA^ALAN^^^^^L|HOUSE^^^^^^L|19690908|M|||338 Apple Lane^^LITTLETON^IL^61452||^PRN^PH^^^309^2576057||||||357-56-4113
		PV1||O
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161155||ADT^A04^ADT_A01|NIST-101101161155901|P|2.3.1
		EVN||20101020
		PID|||ABC10501^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO||ALPHA^ALAN^^^^^L|HOUSE^^^^^^L|19690908|M|||338 Apple Lane^^LITTLETON^IL^61452||^PRN^PH^^^309^2576057||||||357-56-4113
		PV1||O
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161157||QBP^Q23^QBP_Q21|NIST-101101161157166|P|2.5
		QPD|IHE PIX Query|QRY1248968460880|PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
		RCP|I
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161158||QBP^Q23^QBP_Q21|NIST-101101161158306|P|2.5
		QPD|IHE PIX Query|QRY124895648648|PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO
		RCP|I
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161159||QBP^Q23^QBP_Q21|NIST-101101161159430|P|2.5
		QPD|IHE PIX Query|QRY1243359573282|PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		RCP|I

*/
			try {
				//Step 1: Register Patient ALPHA^ALAN with ID PIX10515 in domain NIST2010
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161134||ADT^A04^ADT_A01|NIST-101101161134113|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||ALPHA^ALAN^^^^^L|HOUSE^^^^^^L|19690908|M|||338 Apple Lane^^LITTLETON^IL^61452||^PRN^PH^^^309^2576057||||||357-56-4113||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161134113", msa.getMessageControlID().getValue());
				
				//Step 2: Register Patient ALPHA^ALAN with ID XYZ10515 in domain NIST2010-2
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161145||ADT^A04^ADT_A01|NIST-101101161144999|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||XYZ10501^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||ALPHA^ALAN^^^^^L|HOUSE^^^^^^L|19690908|M|||338 Apple Lane^^LITTLETON^IL^61452||^PRN^PH^^^309^2576057||||||357-56-4113||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161144999", msa.getMessageControlID().getValue());				
				

				//Step 3: Register Patient ALPHA^ALAN with ID ABC10515 in domain NIST2010-3
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161155||ADT^A04^ADT_A01|NIST-101101161155901|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||ABC10501^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO||ALPHA^ALAN^^^^^L|HOUSE^^^^^^L|19690908|M|||338 Apple Lane^^LITTLETON^IL^61452||^PRN^PH^^^309^2576057||||||357-56-4113||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161155901", msa.getMessageControlID().getValue());	
				
				//Step 4: Query for corresponding ids in NIST2010-2 for the patient with ID PIX10501 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161157||QBP^Q23^QBP_Q21|NIST-101101161157166|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1248968460880|PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161157166", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1248968460880", qak.getQueryTag().getValue());
				RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
					System.out.println(pid.getPatientIdentifierList(i).getIDNumber().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());	
				}
				
				
				//Step 5: Query for corresponding ids in NIST2010-2 and NIST2010-3 for the patient with ID PIX10501 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161158||QBP^Q23^QBP_Q21|NIST-101101161158306|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY124895648648|PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO~^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161158306", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY124895648648", qak.getQueryTag().getValue());
				qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
					System.out.println(pid.getPatientIdentifierList(i).getIDNumber().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());	
				}
				
				//Step 6: Query for corresponding ids in all domains for the patient with ID PIX10501 in domain NIST2010
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161159||QBP^Q23^QBP_Q21|NIST-101101161159430|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY1243359573282|PIX10501^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161159430", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1243359573282", qak.getQueryTag().getValue());
				qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
					System.out.println(pid.getPatientIdentifierList(i).getIDNumber().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					System.out.println(pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());	
				}							
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test QueryCase1DataFound.");
			}					
		   
	   }
}
