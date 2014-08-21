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

public class PixPdqTestCase extends AbstractPixPdqTest
{
	public void testPixPdq() {
		try {
			//Step 1: PIX Feed TAU^TERI in the domain HIMSS2005
			String message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10506101|P|2.3.1||||||||\r" + 
		      "EVN||200310011100||||200310011043\r" +
		      "PID|||PIX10506^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||TAU^TERI|RILEY^KARA|19680908|F||WH|15 NORTHBRIDGE^^OAK BROOK^IL^60523|||||||10506-101|499-80-1234||||||||||||||||||||\r" +
		      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";

			Message response = sendMessage(getPixConnection(), message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			MSA msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("10506101", msa.getMessageControlID().getValue());

			//Step 2: PIX Feed TOW^T in the XREF2005 domain
			message = "MSH|^~\\&|MESA_ADT|DOMAIN2_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10506102|P|2.3.1||||||||\r" + 
		      "EVN||200310011100||||200310011043\r" +
		      "PID|||ABC10506^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI||TOW^T|ORILEY^KAREN|19680908|F||WH|15 NORTHMOOR^^CHICAGO^IL^60523|||||||10506-201|501-80-5678||||||||||||||||||||\r" +
		      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
			response = sendMessage(getPixConnection(), message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());

			//Step 3: PIX Query Search for TAU^TERI in XREF2005 domain should find no link		
			//Request:
			message = "MSH|^~\\&|MESA_PIX_CLIENT|MESA_DEPARTMENT|MESA_XREF|XYZ_HOSPITAL|200603121200||QBP^Q23|10506103|P|2.5||||||||\r" + 
		      "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10506103|PIX10506^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO||||\r" +
		      "RCP|I||||||";
			//Response:	
			response = sendMessage(getPixConnection(), message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
			assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
			assertEquals("10506103", msa25.getMessageControlID().getValue());
			ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
			assertEquals("NF", qak.getQueryResponseStatus().getValue());
			assertEquals("QRY10506103", qak.getQueryTag().getValue());

			//Step 4: PIX Update TOW^T in the domain HIMSS2005
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A08|10506104|P|2.3.1||||||||\r" + 
		      "EVN||200310011100||||200310011043\r" +
		      "PID|||ABC10506^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI||TAU^TERI|RILEY^KARA|19680908|F||WH|NORTHBRIDGE^^OAK BROOK^IL^60523|||||||10506-201|499-80-1234||||||||||||||||||||\r" +
		      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
			response = sendMessage(getPixConnection(), message);
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());

			//Step 5: PIX Query Search for TAU^TERI in XREF2005 domain should now find the link		
			//Request:
 			message = "MSH|^~\\&|MESA_PIX_CLIENT|MESA_DEPARTMENT|MESA_XREF|XYZ_HOSPITAL|200603121200||QBP^Q23|10506105|P|2.5||||||||\r" + 
		      "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10506105|ABC10506^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI|||||\r" +
		      "RCP|I||||||";
			//Response:			
			response = sendMessage(getPixConnection(), message);
			System.out.println("Received response:\n" + getResponseString(response));
			msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
			assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
			assertEquals("10506105", msa25.getMessageControlID().getValue());
			qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
			assertEquals("OK", qak.getQueryResponseStatus().getValue());
			assertEquals("QRY10506105", qak.getQueryTag().getValue());
			RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
			PID pid = qrs.getPID();
			boolean found = false;
			for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
				if (pid.getPatientIdentifierList(i).getIDNumber().getValue().equalsIgnoreCase("PIX10506")) {
					found = true;
					assertEquals("HIMSS2005", pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					assertEquals("1.3.6.1.4.1.21367.2005.1.1", pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());					
				}
			}
			assertTrue(found);

			// PDQ Request Message: 
			message = "MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|XYZ_HOSPITAL|||QBP^Q22|11311110|P|2.5||||||||\r" + 
		      "QPD|IHE PDQ Query|QRY11311110|@PID.7.1^19680908|||||\r" +
		      "RCP|I|10^RD|||||";

			response = sendMessage(getPdqConnection(), message);
			System.out.println("Received response:\n" + getResponseString(response));
//			MSA msa = (MSA)response.get("MSA");
//			assertEquals("AA", msa.getAcknowledgmentCode().getValue());
//			QAK qak = (QAK)response.get("QAK");
//			assertEquals("OK", qak.getQueryResponseStatus().getValue());
//			RSP_K21_QUERY_RESPONSE qrs = ((RSP_K21)response).getQUERY_RESPONSE();
//			PID pid = qrs.getPID();
//			assertEquals("MOORE", pid.getPatientName(0).getFamilyName().getSurname().getValue());
//			assertEquals("CHIP", pid.getPatientName(0).getGivenName().getValue());

		} catch(Exception e) {
			e.printStackTrace();
			fail("Fail to test PIX Mesa 10506 PIX Update");
		}
	}
	
	
	@Override
	protected void tearDown() throws Exception {
		try {
			Person person = new Person();
			person.setGivenName("TERI");
			person.setFamilyName("TAU");
			deletePerson(person);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.tearDown();
	}
}
