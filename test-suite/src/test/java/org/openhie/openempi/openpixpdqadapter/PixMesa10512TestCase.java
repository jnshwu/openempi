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

public class PixMesa10512TestCase extends AbstractPixTest
{
	public void testPixMesa10512() {
		try {
			//Step 1: PIX Feed EPSILON^ELLIE in the domain HIMSS2005
			String message=null;
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527144212||ADT^A04^ADT_A01|NIST-090527144209435|1|2.3.1\r" + 
			  "EVN|A04|20090527\r" +
			  "PID|||PIX10512^^^HIMSS2005||EPSILON^ELLIE|JACK|19380224|F|||1 PINETREE^^WEBSTER^MO^63119||(314)555-1234|(314)555-4444|||||163-12-5432\r" +
			  "PV1||V";
			Message response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			MSA msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("NIST-090527144209435", msa.getMessageControlID().getValue());

			//Step 2: PIX Feed EPSILON^ELLIE in the domain XREF2005
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527144846||ADT^A04^ADT_A01|NIST-090527144842416|1|2.3.1\r" +
			  "EVN|A04|20090527\r" +
			  "PID|||PIX10512^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||EPSILON^ELLIE|JACK|19380224|F|||1 PINETREE^^WEBSTER^MO^63119||(314)555-1234|(314)555-4444|||||163-12-5432\r" +
			  "PV1||V";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("NIST-090527144842416", msa.getMessageControlID().getValue());
			
			// Step 3: PIX Feed EPSILON^ELLIE in the domain HIMSS2005
			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|20090527162732||ADT^A04^ADT_A01|NIST-090527162729385|1|2.3.1\r" +
			  "EVN|A04|20090527\r" +
			  "PID|||PIX10512^^^&1.3.6.1.4.1.21367.2005.1.1&ISO||EPSILON^ELLIE|JACK|19380224|F|||1 PINETREE^^WEBSTER^MO^63119||(314)555-1234|(314)555-4444|||||163-12-5432\r" +
			  "PV1||V";
			response = sendMessage(message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgementCode().getValue());
			assertEquals("NIST-090527162729385", msa.getMessageControlID().getValue());
			
		} catch(Exception e) {
			e.printStackTrace();
			fail("Fail to test PIX Mesa 10506 PIX Update");
		}			
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
			Person person = new Person();
			person.setGivenName("ELLIE");
			person.setFamilyName("EPSILON");
			deletePerson(person);

		} catch (Exception e) {
			e.printStackTrace();
		}
		super.tearDown();
	}
}
