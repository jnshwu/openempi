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

public class PixMesa10515TestCase extends AbstractPixPdqTest
{
	public void testPixMesa10515() {
		try {
			//Step 1: PIX Feed EPSILON^ELLIE in the domain HIMSS2005
			String message=null;
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090528105925||ADT^A04^ADT_A01|NIST-090528105921882|P|2.3.1\r" +
			  "EVN|A04|20090528\r" + 
			  "PID|||PIX10515W^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||WASHINGTON^MARY|JOHN|19781208|F|||820 JORIE BLVD^^CHICAGO^IL^60523||(312)555-1234|(312)555-6789|||||100-9-1234\r" +
			  "PV1||V";
			Message response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			MSA msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("NIST-090528105921882", msa.getMessageControlID().getValue());
			
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090528105932||ADT^A04^ADT_A01|NIST-090528105928523|P|2.3.1\r" +
			  "EVN|A04|20090528\r" +
			  "PID|||XYZ10515W^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI||WASHINGTON^MARY|JOHN|19781208|F|||820 JORIE BLVD^^CHICAGO^IL^60523||(312)555-1234|(312)555-6789|||||100-9-1234\r" +
			  "PV1||V";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("NIST-090528105928523", msa.getMessageControlID().getValue());
			
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090528105938||ADT^A04^ADT_A01|NIST-090528105934554|P|2.3.1\r" +
			  "EVN|A04|20090528\r" +
			  "PID|||PIX10515L^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||LINCOLN^MARY|JOHN|19781208|F|||JEAN JAQUES BLVD^^New York^NY^60001||(213)455-4321|(213)555-0001|||||100-9-1234\r" +
			  "PV1||V";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("NIST-090528105934554", msa.getMessageControlID().getValue());
			
			//Step 4: Merge patient WASHINTON^MARY with LINCOLN^MARY
			message ="MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20060919004624-0400||ADT^A40^ADT_A39|10515104|P|2.3.1||||||||\r"+
			"EVN|A40|20060919004624||||20060919004340\r"+
			"PID|||PIX10515L^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|\r"+
			"MRG|PIX10515W^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|\r";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200603121200||QBP^Q23|10515105|P|2.5||||||||\r" + 
		      "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10515105|XYZ10515W^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI|||||\r" +
		      "RCP|I||||||";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
			assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
			assertEquals("10515105", msa25.getMessageControlID().getValue());
			ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
			assertEquals("OK", qak.getQueryResponseStatus().getValue());
			assertEquals("QRY10515105", qak.getQueryTag().getValue());
			RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
			PID pid = qrs.getPID();
			boolean found = false;
			for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
				if (pid.getPatientIdentifierList(i).getIDNumber().getValue().equalsIgnoreCase("PIX10515L")) {
					found = true;
					assertEquals("HIMSS2005", pid.getPatientIdentifierList(i).getAssigningAuthority().getNamespaceID().getValue());
					assertEquals("1.3.6.1.4.1.21367.2005.1.1", pid.getPatientIdentifierList(i).getAssigningAuthority().getUniversalID().getValue());					
				}
			}
			assertTrue(found);
			
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|||QBP^Q22|11350110|P|2.5||||||||\r" + 
			  "QPD|IHE PDQ Query|QRY11350110|@PID.5.2^MARY|||||\r" + 
			  "RCP|I|10^RD|||||";
			response = sendMessage(this.getPdqConnection(), message);	        
			System.out.println("Received response:\n" + getResponseString(response));
		} catch(Exception e) {
			e.printStackTrace();
			fail("Fail to test PIX Mesa 10506 PIX Update");
		}	
	}
	
	
	@Override
	protected void tearDown() throws Exception {
		try {
			Person person = new Person();
			person.setGivenName("MARY");
			person.setFamilyName("WASHINGTON");
			deletePerson(person);

			person = new Person();
			person.setGivenName("MARY");
			person.setFamilyName("LINCOLN");
			deletePerson(person);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.tearDown();
	}
}
