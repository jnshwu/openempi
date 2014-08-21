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

public class ITI8UpdateValidDomainPatientExistTestCase  extends AbstractPixTest
{
/*
		Pre-condition: 	
		The PIX Manager is configured to handle these domains:
		NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO

		Description:
		The purpose of this test is to check that a PIX Manager accepts an update message (ADT^A08^ADT_A01) that has a valid domain. A valid domain is either PID-3.1 only, PID-3.2 and PID-3.3, or all three valued. The patient with ID TD14415 exists.
		We register the patient DIANA TEAGUE in a single domain NIST2010. Then we update the same patient in the same domain. Each time the domain is expressed using one of the valid format. We expect to receive an ACK back with the code AA.

*/
	   public void testUpdateValidDomainPatientExist() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161016||ADT^A04^ADT_A01|NIST-101101161016351|P|2.3.1
		EVN||20101020
		PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881128|M|||2934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998
		PV1||O
																						   
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161026||ADT^A08^ADT_A01|NIST-101101161026909|P|2.3.1
		EVN||20101020
		PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881128|F|||2934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998
		PV1||R
																		
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161037||ADT^A08^ADT_A01|NIST-101101161037467|P|2.3.1
		EVN||20101020
		PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881028|F|||2934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998
		PV1||R
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161048||ADT^A08^ADT_A01|NIST-101101161048071|P|2.3.1
		EVN||20101020
		PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881128|F|||12934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998
		PV1||R

*/
			try {
				//Step 1: Register patient TEAGUE^DIANA with TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161016||ADT^A04^ADT_A01|NIST-101101161016351|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881128|M|||2934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998||||||||||||||||||||\r" +
					      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161016351", msa.getMessageControlID().getValue());
				
				
				//Step 2: Update patient demographics in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161026||ADT^A08^ADT_A01|NIST-101101161026909|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881128|F|||2934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161026909", msa.getMessageControlID().getValue());				
				
				
				//Step 3: Update patient demographics in domain NIST2010&&
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161037||ADT^A08^ADT_A01|NIST-101101161037467|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881028|F|||2934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161037467", msa.getMessageControlID().getValue());		
				
				//Step 4: Update patient demographics in domain &2.16.840.1.113883.3.72.5.9.1&ISO 
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161048||ADT^A08^ADT_A01|NIST-101101161048071|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||TD14415^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||TEAGUE^DIANA^^^^^L|BISHOP^^^^^^L|19881128|F|||12934 Rogers Street^^CINCINNATI^OH^45202||^PRN^PH^^^513^4879531||||||200-36-9998||||||||||||||||||||\r" +
					      "PV1||R||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101161048071", msa.getMessageControlID().getValue());			
				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test UpdateValidDomainPatientExist.");
			}					
		   
	   }
}
