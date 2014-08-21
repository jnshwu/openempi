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
import ca.uhn.hl7v2.model.v25.group.RSP_K21_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.message.RSP_K21;
import ca.uhn.hl7v2.model.v25.segment.MSA;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.QAK;

public class PdqMesa11320TestCase extends AbstractPixPdqTest
{
	/**
		Description
		Test case 11315 covers a complete ID search where the return domains are unspecified
		by the Patient Demographics Consumer.

		Instructions
		The Consumer is expected to query by exact patient ID. No other keys should be present.

		Validation Criteria
		The Consumer is expected to query by exact patient ID. No other keys should be present.
			
	 */
	public void testPdqMesa11320() {
		try {
			// PDQ Request Message:
			String message = "MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|XYZ_HOSPITAL|||QBP^Q22|11311110|P|2.5||||||||\r" + 
		      "QPD|IHE PDQ Query|QRY11320110|@PID.3.1^PDQ113XX05|||||\r" +
		      "RCP|I|10^RD|||||";

			Message response = sendMessage(getPdqConnection(), message);
			System.out.println("Received response:\n" + getResponseString(response));
			MSA msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgmentCode().getValue());
			QAK qak = (QAK)response.get("QAK");
			assertEquals("OK", qak.getQueryResponseStatus().getValue());
			RSP_K21_QUERY_RESPONSE qrs = ((RSP_K21)response).getQUERY_RESPONSE();
			PID pid = qrs.getPID();
			int index = -1;
			for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
				if ("PDQ113XX05".equalsIgnoreCase(pid.getPatientIdentifierList(i).getIDNumber().getValue())) {
					index = i;
				}
			}
			// If one of the identifiers matches; then the index points to the entry that does otherwise it will still be -1
			assertTrue(index >= 0);
			assertEquals("MOONEY", pid.getPatientName(0).getFamilyName().getSurname().getValue());
			assertEquals("STAN", pid.getPatientName(0).getGivenName().getValue());

			message = "MSH|^~\\&|MESA_ADT|DOMAIN1_ADMITTING|MESA_XREF|XYZ_HOSPITAL|200310011100||ADT^A04^ADT_A01|10506101|P|2.3.1||||||||\r" + 
		      "EVN||200310011100||||200310011043\r" +
		      "PID|||PIX10506^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI||TAU^TERI|RILEY^KARA|19680908|F||WH|15 NORTHBRIDGE^^OAK BROOK^IL^60523|||||||10506-101|499-80-1234||||||||||||||||||||\r" +
		      "PV1||O||||||||||||||||||||||||||||||||||||||||||||||||||";

			response = sendMessage(getPixConnection(), message);	        
			System.out.println("Received response:\n" + getResponseString(response));
			ca.uhn.hl7v2.model.v231.segment.MSA msa2 = (ca.uhn.hl7v2.model.v231.segment.MSA)response.get("MSA");
			assertEquals("AA", msa2.getAcknowledgementCode().getValue());
			assertEquals("10506101", msa2.getMessageControlID().getValue());
			
			message = "MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|XYZ_HOSPITAL|||QBP^Q22|11311110|P|2.5||||||||\r" + 
				"QPD|IHE PIX Query|QRY581897987987897|@PID.18.1^10506-101|||||\r" +
				"RCP|I|10^RD|||||";
			response = sendMessage(getPdqConnection(), message);
			System.out.println("Received response:\n" + getResponseString(response));
			msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgmentCode().getValue());
			qak = (QAK)response.get("QAK");
			assertEquals("OK", qak.getQueryResponseStatus().getValue());
			qrs = ((RSP_K21)response).getQUERY_RESPONSE();
			pid = qrs.getPID();
			index = -1;
			for (int i=0; i < pid.getPatientIdentifierList().length; i++) {
				if ("PIX10506".equalsIgnoreCase(pid.getPatientIdentifierList(i).getIDNumber().getValue())) {
					index = i;
				}
			}
			// If one of the identifiers matches; then the index points to the entry that does otherwise it will still be -1
			assertTrue(index >= 0);			
			assertEquals("TAU", pid.getPatientName(0).getFamilyName().getSurname().getValue());
			assertEquals("TERI", pid.getPatientName(0).getGivenName().getValue());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Fail to test PDQ exact name search");
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
