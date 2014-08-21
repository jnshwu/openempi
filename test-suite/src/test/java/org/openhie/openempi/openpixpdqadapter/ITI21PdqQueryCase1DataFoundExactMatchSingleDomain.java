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
import ca.uhn.hl7v2.model.v25.group.RSP_K21_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.group.RSP_K23_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;
import ca.uhn.hl7v2.model.v25.message.RSP_K23;
import ca.uhn.hl7v2.model.v25.segment.PID;

public class ITI21PdqQueryCase1DataFoundExactMatchSingleDomain  extends AbstractPdqTest
{
/*
		Pre-condition: 	
		1. The PDQ Supplier is configured to send and receive demographics in a single domain: NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		2. The following patient has been loaded into the PDQ Supplier (via Load PDQ Supplier Data) in the domain given in pre-condition (1) above:
		GERALD BXRACK with ID GB-481
	
		Description:
		Test case ITI-21-Query-Case1-Data-Found-Exact-Match-Single-Domain covers the PDQ Query Case 1. You need to have loaded the set of patients indicated in the pre-conditions into your system before running this test. The purpose of the test is to confirm that your PDQ Supplier supports the multiple query parameters.
		The following query is tested:
		1. Query on the patient first and last name
		
		Test Steps Description:
		Step 1: The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patients in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO with a patient name GERALD BXRACK . Your PDQ Supplier shall answer correctly to the query with the demographics of patient GERALD BXRACK in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.

*/
	   public void testPdqQueryCase1DataFoundExactMatchSingleDomain() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152222||QBP^Q22^QBP_Q21|NIST-101103152222814|T|2.5
		QPD|IHE PDQ Query|QRY1184848949494|@PID.5.1.1^BXRACK~@PID.5.2^GERALD
		RCP|I
										   
				
*/
			try {
				
				//Step 1: Query for demographics with criteria PID.5.1.1=BXRACK and PID.5.2=GERALD				
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152222||QBP^Q22^QBP_Q21|NIST-101103152222814|T|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY1184848949494|@PID.5.1.1^BXRACK~@PID.5.2^GERALD|||||\r" +
					      "RCP|I||||||";
				
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101103152222814", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY1184848949494", qak.getQueryTag().getValue());
				RSP_K21_QUERY_RESPONSE qrs = ((RSP_K21)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				assertEquals("BXRACK", pid.getPatientName(0).getFamilyName().getSurname().getValue());
				assertEquals("GERALD", pid.getPatientName(0).getGivenName().getValue());				
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test PdqQueryCase1DataFoundExactMatchSingleDomain.");
			}					
		   
	   }
}
