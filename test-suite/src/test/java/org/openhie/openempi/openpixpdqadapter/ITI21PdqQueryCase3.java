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

public class ITI21PdqQueryCase3  extends AbstractPdqTest
{
/*
		Pre-condition: 	
		The PDQ Supplier is configured to send and receive demographics in a single domain: NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
	
		Description:
		Test case ITI-21-Query-Case3 covers the PDQ Query Case 3. The NIST PDQ Consumer will query your PDQ Supplier using an unknown domain in field QPD-8 (What domains returned).
		
		Test Steps Description:
		Step 1 : The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patients with a patient name NXEAL using UNKNOWNDOMAIN&2.16.840.1.113883.3.72.5.9.99&ISO in QPD-8. Your PDQ Supplier shall send an ERR segment for the domain that was not recognized.
		
*/
	   public void testPdqQueryCase3() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161404||QBP^Q22^QBP_Q21|NIST-101101161404422|D|2.5
		QPD|IHE PDQ Query|QRY121548499947|@PID.5.1.1^NXEAL|||||^^^UNKNOWNDOMAIN
		RCP|I
				
*/
			try {
				
				//Step 1: Query for demographics with criteria PID-5.1.1=NXEAL using UNKNOWNDOMAIN in QPD-8			
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101101161404||QBP^Q22^QBP_Q21|NIST-101101161404422|D|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY121548499947|@PID.5.1.1^NXEAL|||||^^^UNKNOWNDOMAIN\r" +
					      "RCP|I||||||";
				
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101101161404422", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("AE", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY121548499947", qak.getQueryTag().getValue());
				
			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test PdqQueryCase3.");
			}					
		   
	   }
}
