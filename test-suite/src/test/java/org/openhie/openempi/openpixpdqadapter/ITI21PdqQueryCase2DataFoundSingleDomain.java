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

public class ITI21PdqQueryCase2DataFoundSingleDomain  extends AbstractPdqTest
{
/*
		Pre-condition: 	
		1. The PDQ Supplier is configured to send and receive demographics in a single domain: NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO
		2. The following patients have been loaded into the PDQ Supplier (via Load PDQ Supplier Data) in the domain given in pre-condition (1) above:
		JERMAINO NXEAL with ID JN-018
		BARBARA LXEIGHTON with ID BL-180
		SHAQUILLO NXEAL with ID SN-032
		RUTH NXEAL with ID RN-480
		DAVID VASXQUEZ with ID DV-301
		KATHRINE MARXCHANT with ID KM-375
	
		Description:
		Test case ITI-21-Query-Case2-Data-Found-Single-Domain addresses PDQ Query Case 2. You need to have loaded the set of patients indicated in the pre-conditions into your system before running this test. The purpose of the test is to confirm that your PDQ Supplier supports the multiple query parameters. QPD-8 is populated with the domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.
		The following queries are tested:
		1. Query on the patient ID
		2. Query on the patient name
		3. Query on the patient name and the date of birth
		4. Query on the patient name and the administrative sex
		5. Query on the address (street, city, state and zip code)
		6. Query on the patient account number
		
		Test Steps Description:
		Step 1: The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patient in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO with a patient ID BL-180. Your PDQ Supplier shall answer correctly to the query with the demographics of patient BARBARA LXEIGHTON in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 2: The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patient in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO with a patient name JERMAINO NXEAL. Your PDQ Supplier shall answer correctly to the query with the demographics of patient JERMAINO NXEAL in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 3: The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patient in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO with a patient name NXEAL and DOB 19720306. Your PDQ Supplier shall answer correctly to the query with the demographics of patient SHAQUILLO NXEAL in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 4: The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patient in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO with a patient name NXEAL and feminine sex. Your PDQ Supplier shall answer correctly to the query with the demographics of patient RUTH NXEAL in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 5: The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patient in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO with a patient living at 4670 Raver Croft Drive HAMPTON, TN 37658. Your PDQ Supplier shall answer correctly to the query with the demographics of patient KATHRINE MARXCHANT in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.
		Step 6: The NIST PDQ Consumer sends a query message (QBP^Q22) to ask for the demographics of all patient in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO with a patient account number ID 0007240. Your PDQ Supplier shall answer correctly to the query with the demographics of patient DAVID VASXQUEZ in domain NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO.

*/
	   public void testPdqQueryCase1DataFoundSingleDomain() {
/*
 *		Messages:
 * 
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152150||QBP^Q22^QBP_Q21|NIST-101103152150738|T|2.5
		QPD|IHE PDQ Query|QRY12481848949|@PID.3.1^BL-180|||||^^^NIST2010
		RCP|I
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152152||QBP^Q22^QBP_Q21|NIST-101103152152016|P|2.5
		QPD|IHE PDQ Query|QRY11849105448994|@PID.5.1.1^NXEAL~@PID.5.2^JERMAINO|||||^^^NIST2010
		RCP|I
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152153||QBP^Q22^QBP_Q21|NIST-101103152153310|D|2.5
		QPD|IHE PDQ Query|QRY11011048808|@PID.5.1.1^NXEAL~@PID.7.1^19720306|||||^^^NIST2010
		RCP|I
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152154||QBP^Q22^QBP_Q21|NIST-101103152154572|P|2.5
		QPD|IHE PDQ Query|QRY12400480880077|@PID.5.1.1^NXEAL~@PID.8^F|||||^^^NIST2010
		RCP|I
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152156||QBP^Q22^QBP_Q21|NIST-101103152156365|T|2.5
		QPD|IHE PDQ Query|QRY12489878110556|@PID.11.1.1^4670 Raver Croft Drive~@PID.11.3^HAMPTON~@PID.11.4^TN~@PID.11.5^37658|||||^^^NIST2010
		RCP|I
				
		MSH|^~\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152158||QBP^Q22^QBP_Q21|NIST-101103152158282|P|2.5
		QPD|IHE PDQ Query|QRY581897987987897|@PID.18.1^1007240|||||^^^NIST2010
		RCP|I
										   
				
*/
			try {
				
				//Step 1: Query for demographics with criteria PID.3.1=BL-180 using domain NIST2010 in QPD-8	
				String message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152150||QBP^Q22^QBP_Q21|NIST-101103152150738|T|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY12481848949|@PID.3.1^BL-180|||||^^^NIST2010\r" +
					      "RCP|I||||||";
				
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101103152150738", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY12481848949", qak.getQueryTag().getValue());
				RSP_K21_QUERY_RESPONSE qrs = ((RSP_K21)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				int index = -1;
				for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
					if ("BL-180".equalsIgnoreCase(pid.getPatientIdentifierList(i).getIDNumber().getValue())) {
						index = i;
					}
				}
				// If one of the identifiers matches; then the index points to the entry that does otherwise it will still be -1
				assertTrue(index >= 0);
				assertEquals("LXEIGHTON", pid.getPatientName(0).getFamilyName().getSurname().getValue());
				assertEquals("BARBARA", pid.getPatientName(0).getGivenName().getValue());				
				
				
				//Step 2: Query for demographics with criteria PID.5.1.1=NXEAL and PID.5.2=JERMAINO using domain NIST2010 in QPD-8
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152152||QBP^Q22^QBP_Q21|NIST-101103152152016|P|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY11849105448994|@PID.5.1.1^NXEAL~@PID.5.2^JERMAINO|||||^^^NIST2010\r" +
					      "RCP|I||||||";
				
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101103152152016", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY11849105448994", qak.getQueryTag().getValue());
				qrs = ((RSP_K21)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				assertEquals("NXEAL", pid.getPatientName(0).getFamilyName().getSurname().getValue());
				assertEquals("JERMAINO", pid.getPatientName(0).getGivenName().getValue());				
				
				//Step 3: Query for demographics with criteria PID.5.1.1=NXEAL and PID.7.1=19720306 using domain NIST2010 in QPD-8 
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152153||QBP^Q22^QBP_Q21|NIST-101103152153310|D|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY11011048808|@PID.5.1.1^NXEAL~@PID.7.1^19720306|||||^^^NIST2010\r" +
					      "RCP|I||||||";
				
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101103152153310", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY11011048808", qak.getQueryTag().getValue());
				qrs = ((RSP_K21)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				assertEquals("NXEAL", pid.getPatientName(0).getFamilyName().getSurname().getValue());
				assertEquals("SHAQUILLO", pid.getPatientName(0).getGivenName().getValue());				
				
				//Step 4: Query for demographics with criteria PID.5.1.1=NXEAL and PID.8=F using domain NIST2010 in QPD-8 
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152154||QBP^Q22^QBP_Q21|NIST-101103152154572|P|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY12400480880077|@PID.5.1.1^NXEAL~@PID.8^F|||||^^^NIST2010\r" +
					      "RCP|I||||||";
				
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101103152154572", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY12400480880077", qak.getQueryTag().getValue());
				qrs = ((RSP_K21)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				assertEquals("NXEAL", pid.getPatientName(0).getFamilyName().getSurname().getValue());
				assertEquals("RUTH", pid.getPatientName(0).getGivenName().getValue());		
				
				//Step 5: Query for demographics with criteria PID.11.1.1=4670 Raver Croft Drive and PID.11.3=HAMPTON and PID.11.4=TN and PID.11.5=37658 using domain NIST2010 in QPD-8 
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152156||QBP^Q22^QBP_Q21|NIST-101103152156365|T|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY12489878110556|@PID.11.1.1^4670 Raver Croft Drive~@PID.11.3^HAMPTON~@PID.11.4^TN~@PID.11.5^37658|||||^^^NIST2010\r" +
					      "RCP|I||||||";
				
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101103152156365", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY12489878110556", qak.getQueryTag().getValue());
				qrs = ((RSP_K21)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				assertEquals("MARXCHANT", pid.getPatientName(0).getFamilyName().getSurname().getValue());
				assertEquals("KATHRINE", pid.getPatientName(0).getGivenName().getValue());		
		
				//Step 6: Query for demographics with criteria PID.18.1=0007240 using domain NIST2010 in QPD-8 
				message = "MSH|^~\\&|NIST_SENDER^^|NIST^^|NIST_RECEIVER^^|NIST^^|20101103152158||QBP^Q22^QBP_Q21|NIST-101103152158282|P|2.5||||||||\r" + 
					      "QPD|IHE PDQ Query|QRY581897987987897|@PID.18.1^1007240|||||^^^NIST2010\r" +
					      "RCP|I||||||";
				
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("NIST-101103152158282", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY581897987987897", qak.getQueryTag().getValue());
				qrs = ((RSP_K21)response).getQUERY_RESPONSE();
				pid = qrs.getPID();
				assertEquals("VASXQUEZ", pid.getPatientName(0).getFamilyName().getSurname().getValue());
				assertEquals("DAVID", pid.getPatientName(0).getGivenName().getValue());						

			} catch (Exception e) {
				e.printStackTrace();
				fail("Fail to test PdqQueryCase1DataFoundSingleDomain.");
			}					
		   
	   }
}
