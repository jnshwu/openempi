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

public class PixMesa10511TestCase extends AbstractPixTest
{
	public void testPixMesa10511() {
		try {
			//Step 1: PIX Feed ALPHA^ALAN in the domain HIMSS2005
			String message=null;
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527141702||ADT^A01^ADT_A01|10506101-990001|P|2.3.1||||||||\r" + 
			  "EVN|A01|20090527\r" +
			  "PID|||PIX10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||ALPHA^ALAN|Barnes|19781208|M|||820 JORIE BLVD^^New York City^NY^10503||(112)555-1234|(112)555-6789|||||153-12-5432\r" +
			  "PV1||V";
			Message response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			MSA msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("10506101-990001", msa.getMessageControlID().getValue());

			//Step 2: PIX Feed ALPHA^ALAN in the domain XREF2005
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527141708||ADT^A01^ADT_A01|10506101-990002|A|2.3.1||||||||\r" +
			  "EVN|A01|20090527\r" +
			  "PID|||XYZ10501^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI||ALPHA^ALAN|Barnes|19781208|M|||820 JORIE BLVD^^New York City^NY^10503||(112)555-1234|(112)555-6789|||||153-12-5432\r" +
			  "PV1||V";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("10506101-990002", msa.getMessageControlID().getValue());
			
			// Step 3: PIX Feed SIMPSON^CARL in the domain HIMSS2005
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527141714||ADT^A01^ADT_A01|10506101-990003|A|2.3.1||||||||\r" +
			  "EVN|A01|20090527\r" +
			  "PID|||ABC10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||SIMPSON^CARL|AMSTRONG|19781209|M|||820 OREL BLVD^^CHICAGO^IL^60523||(312)555-1234|(312)555-6789|||||163-12-5432\r" +
			  "PV1||V";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("10506101-990003", msa.getMessageControlID().getValue());
			
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527142638||QBP^Q23^QBP_Q21|10506101-990004|D|2.5\r" +
			  "QPD|IHE PIX QUERY|QRY1243448797905|PIX10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO|XYZ10501^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO\r" +
			  "RCP|I";
//			MSH|^~\&|MESA_XREF|XYZ_HOSPITAL|MESA_ADT|DOMAIN1_ADMITTING|20100220153726-0500||RSP^K23|a000095126ed1959e11|P|2.5
//			MSA|AA|10506101-990004
//			QAK|QRY1243448797905|OK
//			QPD|IHE PIX QUERY|QRY1243448797905|PIX10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO|XYZ10501^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO
//			PID|||XYZ10501^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO^PI||~^^^^^^S
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
			assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
			assertEquals("10506101-990004", msa25.getMessageControlID().getValue());
			ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
			assertEquals("OK", qak.getQueryResponseStatus().getValue());
			assertEquals("QRY1243448797905", qak.getQueryTag().getValue());
			RSP_K23_QUERY_RESPONSE qrs = ((RSP_K23)response).getQUERY_RESPONSE();
			PID pid = qrs.getPID();
			assertEquals("XYZ10501", pid.getPatientIdentifierList(0).getIDNumber().getValue());
			assertEquals("XREF2005", pid.getPatientIdentifierList(0).getAssigningAuthority().getNamespaceID().getValue());
			assertEquals("1.3.6.1.4.1.21367.2005.1.2", pid.getPatientIdentifierList(0).getAssigningAuthority().getUniversalID().getValue());
			
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527142639||QBP^Q23^QBP_Q21|10506101-990005|D|2.5\r" +
			  "QPD|IHE PIX QUERY|QRY1243448799280|ABC10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|text^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO\r" +
			  "RCP|I";
//			MSH|^~\&|MESA_XREF|XYZ_HOSPITAL|MESA_ADT|DOMAIN1_ADMITTING|20100220154438-0500||RSP^K23|a000095126ed1ff0281|P|2.5
//			MSA|AA|10506101-990005
//			QAK|QRY1243448799280|OK
//			QPD|IHE PIX QUERY|QRY1243448799280|ABC10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|text^^^XREF2005&1.3.6.1.4.1.21367.2005.1.2&ISO			
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
			assertEquals("AA", msa25.getAcknowledgmentCode().getValue());
			assertEquals("10506101-990005", msa25.getMessageControlID().getValue());
			qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
			assertEquals("NF", qak.getQueryResponseStatus().getValue());
			assertEquals("QRY1243448799280", qak.getQueryTag().getValue());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Fail to test PIX Mesa 10506 PIX Update");
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
