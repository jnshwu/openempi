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

public class ITI8FeedUnknownDomainA01TestCase extends AbstractPixTest
{
/*
		Pre-condition: 	
		No precondition for this test
	
		Description:
		The purpose of this test is to check that a PIX Manager does not accept a feed message (ADT^A01^ADT_A01) from an unknown domain. An unknown domain is a valid domain but it is not recognized by the PIX Manager.
	    The patient SARAH ROGERS with ID SR00064 in the unknown domain UNKNOWNDOMAIN. The PIX Manager is expected to return an ACK with the code AE or AR.
*/
	   public void testFeedUnknownDomainA01() {
/*
 *		Messages:
 * 
		   MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160503||ADT^A01^ADT_A01|NIST-101101160503833|P|2.3.1
		   EVN||20101020
		   PID|||SR00064^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO||ROGERS^SARAH^^^^^L|BUSCH^^^^^^L|19590110|F|||2912 Fantages Way^^SHERMAN MILLS^ME^04776||^PRN^PH^^^207^7384120||||||006-28-2170
		   PV1||I
		   
		   MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160514||ADT^A01^ADT_A01|NIST-101101160514516|P|2.3.1
		   EVN||20101020
		   PID|||SR00064^^^UNKNOWNDOMAIN||ROGERS^SARAH^^^^^L|BUSCH^^^^^^L|19590110|F|||2912 Fantages Way^^SHERMAN MILLS^ME^04776||^PRN^PH^^^207^7384120||||||006-28-2170
		   PV1||I
		   
		   MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160525||ADT^A01^ADT_A01|NIST-101101160525355|P|2.3.1
		   EVN||20101020
		   PID|||SR00064^^^&2.16.840.1.113883.3.72.5.9.99&ISO||ROGERS^SARAH^^^^^L|BUSCH^^^^^^L|19590110|F|||2912 Fantages Way^^SHERMAN MILLS^ME^04776||^PRN^PH^^^207^7384120||||||006-28-2170
		   PV1||I
*/
			try {
				//Step 1: PIX feed to create one patient from domain UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160503||ADT^A01^ADT_A01|NIST-101101160503833|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||SR00064^^^UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO||ROGERS^SARAH^^^^^L|BUSCH^^^^^^L|19590110|F|||2912 Fantages Way^^SHERMAN MILLS^ME^04776||^PRN^PH^^^207^7384120||||||006-28-2170||||||||||||||||||||\r" +
					      "PV1||I||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160503833", msa.getMessageControlID().getValue());
				
				
				
				//Step 2: PIX feed to create one patient from domain UNKNOWNDOMAIN
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160514||ADT^A01^ADT_A01|NIST-101101160514516|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||SR00064^^^UNKNOWNDOMAIN||ROGERS^SARAH^^^^^L|BUSCH^^^^^^L|19590110|F|||2912 Fantages Way^^SHERMAN MILLS^ME^04776||^PRN^PH^^^207^7384120||||||006-28-2170||||||||||||||||||||\r" +
					      "PV1||I||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160514516", msa.getMessageControlID().getValue());
				
				
				//Step 3: PIX feed to create one patient from domain 2.16.840.1.113883.3.72.5.9.99&ISO
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101160525||ADT^A01^ADT_A01|NIST-101101160525355|P|2.3.1||||||||\r" + 
					      "EVN||20101020||||\r" +
					      "PID|||SR00064^^^&2.16.840.1.113883.3.72.5.9.99&ISO||ROGERS^SARAH^^^^^L|BUSCH^^^^^^L|19590110|F|||2912 Fantages Way^^SHERMAN MILLS^ME^04776||^PRN^PH^^^207^7384120||||||006-28-2170||||||||||||||||||||\r" +
					      "PV1||I||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa = (MSA)response.get("MSA");
				assertEquals("AE", msa.getAcknowledgementCode().getValue());
				assertEquals("NIST-101101160525355", msa.getMessageControlID().getValue());				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test FeedUnknownDomainA01.");
			}					
		   
	   }
}
