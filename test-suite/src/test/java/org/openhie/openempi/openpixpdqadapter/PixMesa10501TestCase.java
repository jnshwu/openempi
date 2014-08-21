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

public class PixMesa10501TestCase  extends AbstractPixTest
{
	   public void testPixMesa10501() {
			try {
				//Step 1: PIX Feed one patient in HIMSS2005
				String message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10501102|P|2.3.1||||||||\r" + 
			      "EVN||200310011100||||200310011043\r" +
			      "PID|||PIX10501^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO^PI||ALPHA^ALAN||19380224|M||WH|1 PINETREE^^WEBSTER^MO^63119|||||||10501-101|||||||||||||||||||||\r" +
			      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				MSA msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("10501102", msa.getMessageControlID().getValue());

				//Step 2: PIX Feed another patient in HIMSS2005
				message = "MSH|^~\\&|MESA_ADT|DOMAIN2_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10501106|P|2.3.1||||||||\r" + 
			      "EVN||200310011100||||200310011043\r" +
			      "PID|||XYZ10501^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI||ALPHA^ALAN||19380224|M||WH|1 PINETREE LN^^WEBSTER GROVES^MO^63119|||||||10501-201|||||||||||||||||||||\r" +
			      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				System.out.println("MessageControlID is " + msa.getMessageControlID().getValue());
				assertEquals("10501106", msa.getMessageControlID().getValue());

				//Step 3: PIX Feed a third patient which can be matched with the first patient
				message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10501104|P|2.3.1||||||||\r" + 
			      "EVN||200310011100||||200310011043\r" +
			      "PID|||ABC10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||SIMPSON^CARL||19380224|M||BL|5 PINETREE^^WEBSTER^MO^63119|||||||10501-102|||||||||||||||||||||\r" +
			      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
				response = sendMessage(message);
				msa = (MSA)response.get("MSA");
				assertEquals("AA", msa.getAcknowledgementCode().getValue());
				assertEquals("10501104", msa.getMessageControlID().getValue());
				
	            //Step 4: PIX Query Search, and found one matched patient		
				//Request:
				message = "MSH|^~\\&|MESA_PIX_CLIENT|MESA_DEPARTMENT|MESA_XREF|XYZ_HOSPITAL|200603121200||QBP^Q23|10501108|P|2.5||||||||\r" + 
			      "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10501108|PIX10501^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO^PI|^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO\r" +
			      "RCP|I||||||";
				//Response:	
//				MSH|^~\&|MESA_XREF|XYZ_HOSPITAL|MESA_PIX_CLIENT|MESA_DEPARTMENT|20100219114544-0500||RSP^K23|c0a801a3126e71edcd31|P|2.5
//				MSA|AA|10501108
//				QAK|QRY10501108|OK
//				QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10501108|PIX10501^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO^PI|^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO
//				PID|||XYZ10501^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI||~^^^^^^S
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("10501108", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("OK", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY10501108", qak.getQueryTag().getValue());
				RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
				PID pid = qrs.getPID();
				System.out.println(pid.getPatientIdentifierList(0).getIDNumber().getValue());
				assertEquals("XYZ10501", pid.getPatientIdentifierList(0).getIDNumber().getValue());
				assertEquals("XREF2005", pid.getPatientIdentifierList(0).getAssigningAuthority().getNamespaceID().getValue());
				assertEquals("1.3.6.1.4.1.21367.2005.1.2", pid.getPatientIdentifierList(0).getAssigningAuthority().getUniversalID().getValue());

				//Step 5: PIX Query Search, and found no matched patient		
				//Request:
				message = "MSH|^~\\&|MESA_PIX_CLIENT|MESA_DEPARTMENT|MESA_XREF|XYZ_HOSPITAL|200603121200||QBP^Q23|10501110|P|2.5||||||||\r" + 
			      "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10501110|ABC10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO||||\r" +
			      "RCP|I||||||";
				//Response:
//				MSH|^~\&|MESA_XREF|XYZ_HOSPITAL|MESA_PIX_CLIENT|MESA_DEPARTMENT|||RSP^K23|MESA49541943|P|2.5||||||||
//				MSA|AA|10501110||||
//				QAK|QRY10501110|NF
//				QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10501110|ABC10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|||||
				response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
				assertEquals("10501110", msa25.getMessageControlID().getValue());
				qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
//				assertEquals("NF", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY10501110", qak.getQueryTag().getValue());
			} catch(Exception e) {
				e.printStackTrace();
				fail("Fail to test PIX Mesa 10501 PIX Feed and Query");
			}
	   }
	   
	   @Override
	protected void tearDown() throws Exception {
		try {
			Person person = new Person();
			person.setGivenName("ALAN");
			person.setFamilyName("ALPHA");
			deletePerson(person);
			
			person = new Person();
			person.setGivenName("CARL");
			person.setFamilyName("SIMPSON");
			deletePerson(person);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.tearDown();
	}
}
