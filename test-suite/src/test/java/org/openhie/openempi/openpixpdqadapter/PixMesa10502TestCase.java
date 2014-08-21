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
import ca.uhn.hl7v2.model.v25.segment.ERR;

public class PixMesa10502TestCase extends AbstractPixTest
{
	public void testPixMesa10502() {
		try {
			//Step 1: PIX feed to create one patient
			String message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10502102|P|2.3.1||||||||\r" + 
		      "EVN||200310011100||||200310011043\r" +
		      "PID|||PIX10502^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||BETA^BETTY||19570131|F||AI|2815 JORIE BLVD^^OAK BROOK^IL^60523|||||||10502-101|||||||||||||||||||||\r" +
		      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
			Message response = sendMessage(message);
			System.out.println("Received response:\n" + getResponseString(response));
			MSA msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("10502102", msa.getMessageControlID().getValue());
			
			//Step 2: PIX feed to create another patient
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10502104|P|2.3.1||||||||\r" + 
		      "EVN||200310011100||||200310011043\r" +
		      "PID|||XYZ10502^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||CROSS^KEN||19801223|M||WH|10034 CLAYTON RD^^LADUE^MO^63124|||||||10502-201|||||||||||||||||||||\r" +
		      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";
			response = sendMessage(message);
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("10502104", msa.getMessageControlID().getValue());
			
			//Step 3: Pix Query a patient which is not registered previously
			//Request:
 			message = "MSH|^~\\&|MESA_PIX_CLIENT|MESA_DEPARTMENT|MESA_XREF|XYZ_HOSPITAL|200603121200||QBP^Q23|10502106|P|2.5||||||||\r" + 
		      "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10502106|ABC10502^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|||||\r" +
		      "RCP|I||||||";
			//Response:	
// 			MSH|^~\&|MESA_XREF|XYZ_HOSPITAL|MESA_PIX_CLIENT|MESA_DEPARTMENT|20090123232148-0500||RSP^K23|PIXPDQ_348549|P|2.5
// 			MSA|AE|10502106
// 			ERR||QPD^1^3^1^1|204^Unknown Key Identifier|E
// 			QAK|QRY10502106|AE
// 			QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10502106|ABC10502^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI
			response = sendMessage(message);
			System.out.println("Received response:\n" + getResponseString(response));
			ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
			assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
			assertEquals("10502106", msa25.getMessageControlID().getValue());
			ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
			assertEquals("AE", qak.getQueryResponseStatus().getValue());
			assertEquals("QRY10502106", qak.getQueryTag().getValue());
			ERR err = (ERR)response.get("ERR");
			assertEquals("3", err.getErrorLocation(0).getFieldPosition().getValue());
			assertEquals("1", err.getErrorLocation(0).getComponentNumber().getValue());
			assertEquals("204", err.getHL7ErrorCode().getIdentifier().getValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Fail to test PIX Mesa 10502 PIX Feed and Query for unregistered patient.");
		}		
	}
	
	   
	   @Override
	protected void tearDown() throws Exception {
		try {
			Person person = new Person();
			person.setGivenName("BETTY");
			person.setFamilyName("BETA");
			deletePerson(person);
			
			person = new Person();
			person.setGivenName("KEN");
			person.setFamilyName("CROSS");
			deletePerson(person);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.tearDown();
	}	
}
