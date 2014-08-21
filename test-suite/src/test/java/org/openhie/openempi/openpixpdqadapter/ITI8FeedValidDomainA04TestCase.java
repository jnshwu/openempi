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

import org.openhie.openempi.model.Person;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.segment.MSA;
import ca.uhn.hl7v2.model.v25.group.RSP_K23_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.message.RSP_K23;
import ca.uhn.hl7v2.model.v25.segment.PID;

public class ITI8FeedValidDomainA04TestCase extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO
		NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO
	
		Description:
		The purpose of this test is to check that a PIX Manager accepts a feed message (ADT^A04^ADT_A01) that has a correctly specified assigning authority domain for the patient"s identifier. Valid combinations of the patient"s identifier assigning authority subcomponents include either the first subcomponent (Namespace ID), the second (Universal ID) and the third (Universal ID Type), or all three.
		When the PIX Manager is sent a message where the domain is populated with only the first subcomponent (Namespace ID), the PIX Manager shall populate the second (Universal ID) and the third (Universal ID Type) subcomponents in subsequent transactions.
		When the PIX Manager is sent a message where the domain is populated with only the second (Universal ID) and the third (Universal ID Type) subcomponents, the PIX Manager shall populate the first subcomponent (Namespace ID) in subsequent transactions.

		Test Steps Description:
		Step 1: The patient WILLIE MUSTO with ID 14583058 in the domain NIST2010 is registered with all three assigning authority components NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO populated. The PIX Manager is expected to return an ACK with the code AA.
		Step 2: The same patient WILLIE MUSTO with ID WM-9037-93299 in a different domain NIST2010-2 is registered with only the first subcomponent NIST2010-2&& populated. The PIX Manager is expected to return an ACK with the code AA. 
		Step 3: A query is made with WILLIE MUSTO with ID 14583058 in domain NIST2010 requesting his patient ID in domain NIST2010-2. The PIX Manager is expected to return a query response in which the patient WILLIE MUSTO ID is found in domain NIST2010-2. The PIX manager is also expected to fill in the second (PID.3.4.2) and third (PID.3.4.3) subcomponents in the patient''s identifier assigning authority.
		Step 4: The patient WILLIE MUSTO with ID WMUSTO-0001 in the domain NIST2010-3 is registered with &2.16.840.1.113883.3.72.5.9.3&ISO populated. The PIX Manager is expected to return an ACK with the code AA.
		Step 5: A query is made with WILLIE MUSTO with ID 14583058 in domain NIST2010 requesting his patient ID in domain NIST2010-3. The PIX Manager is expected to return a query response in which the patient WILLIE MUSTO ID is found in domain NIST2010-3. The PIX manager is expected to fill in the first (PID.3.4.1) subcomponent in the patient"s identifier assigning authority.

*/
	   public void testFeedValidDomainA04() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160720||ADT^A04^ADT_A01|NIST-101101160720116|P|2.3.1
		EVN||20101020
		PID|||14583058^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||MUSTO^WILLIE^^^^^L|BROWN^^^^^^L|19670217|M|||2516 Maxwell Farm Road^^HARRISONBURG^VA^22801||^PRN^PH^^^540^2084880||||||691-01-6885
		PV1||O
		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160731||ADT^A04^ADT_A01|NIST-101101160731002|P|2.3.1
		EVN||20101020
		PID|||WM-9037-93299^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||MUSTO^WILLIE^^^^^L|BROWN^^^^^^L|19670217|M|||2516 Maxwell Farm Road^^HARRISONBURG^VA^22801||^PRN^PH^^^540^2084880||||||691-01-6885
		PV1||O
						   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160732||QBP^Q23^QBP_Q21|NIST-101101160732252|P|2.5
		QPD|IHE PIX Query|QRY124518648946312|14583058^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^&2.16.840.1.113883.3.72.5.9.2&ISO
		RCP|I
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160742||ADT^A04^ADT_A01|NIST-101101160742669|P|2.3.1
		EVN||20101020
		PID|||WMUSTO-0001^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO||MUSTO^WILLIE^^^^^L|BROWN^^^^^^L|19670217|M|||2516 Maxwell Farm Road^^HARRISONBURG^VA^22801||^PRN^PH^^^540^2084880||||||691-01-6885
		PV1||O
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160743||QBP^Q23^QBP_Q21|NIST-101101160743934|P|2.5
		QPD|IHE PIX Query|QRY124518648946313|14583058^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-3
		RCP|I

*/
			try {
				//Step 1
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160720||ADT^A04^ADT_A01|NIST-101101160720116|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||14583058^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||MUSTO^WILLIE^^^^^L|BROWN^^^^^^L|19670217|M|||2516 Maxwell Farm Road^^HARRISONBURG^VA^22801||^PRN^PH^^^540^2084880||||||691-01-6885||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160720116", msa.getMessageControlID().getValue());
				
				
				
				//Step 2
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160731||ADT^A04^ADT_A01|NIST-101101160731002|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||WM-9037-93299^^^NIST2010-2&2.16.840.1.113883.3.72.5.9.2&ISO||MUSTO^WILLIE^^^^^L|BROWN^^^^^^L|19670217|M|||2516 Maxwell Farm Road^^HARRISONBURG^VA^22801||^PRN^PH^^^540^2084880||||||691-01-6885||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160731002", msa.getMessageControlID().getValue());

				
	            //Step 3:
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160732||QBP^Q23^QBP_Q21|NIST-101101160732252|P|2.5||||||||\r" + 
						  "QPD|IHE PIX Query|QRY124518648946312|14583058^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^&2.16.840.1.113883.3.72.5.9.2&ISO\r" +
						  "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101160732252", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY124518648946312", qak.getQueryTag().getValue());
				RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				System.out.println(pid.getPatientIdentifierList(0).getIDNumber().getValue());
				System.out.println(pid.getPatientIdentifierList(0).getAssigningAuthority().getNamespaceID().getValue());
				System.out.println(pid.getPatientIdentifierList(0).getAssigningAuthority().getUniversalID().getValue());
				
				
				//Step 4
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160742||ADT^A04^ADT_A01|NIST-101101160742669|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||WMUSTO-0001^^^NIST2010-3&2.16.840.1.113883.3.72.5.9.3&ISO||MUSTO^WILLIE^^^^^L|BROWN^^^^^^L|19670217|M|||2516 Maxwell Farm Road^^HARRISONBURG^VA^22801||^PRN^PH^^^540^2084880||||||691-01-6885||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160742669", msa.getMessageControlID().getValue());		
				
				
				//Step 5
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160743||QBP^Q23^QBP_Q21|NIST-101101160743934|P|2.5||||||||\r" + 
					      "QPD|IHE PIX Query|QRY124518648946313|14583058^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO|^^^NIST2010-3\r" +
					      "RCP|I||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101160743934", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY124518648946313", qak.getQueryTag().getValue());
				qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				System.out.println(pid.getPatientIdentifierList(0).getIDNumber().getValue());
				System.out.println(pid.getPatientIdentifierList(0).getAssigningAuthority().getNamespaceID().getValue());
				System.out.println(pid.getPatientIdentifierList(0).getAssigningAuthority().getUniversalID().getValue());	
						
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test FeedValidDomainA04.");
			}					
		   
	   }
	 
		@Override
		protected void tearDown() throws Exception {
			try {
				Person person = new Person();
				person.setGivenName("WILLIE");
				person.setFamilyName("MUSTO");
				deletePerson(person);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.tearDown();
		}
}
