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
import ca.uhn.hl7v2.model.v25.segment.MSA;
import ca.uhn.hl7v2.model.v25.segment.QAK;

public class PdqMesa11312TestCase extends AbstractPdqTest
{
	/**
		Description
		Test case 11311 covers an exact name search by the Patient Demographics Consumer. 
		Several additional new patient messages are sent to the Patient Demographics Supplier.
		Then the PD test Consumer sends a Patient Demographics Query that includes an exact patient name.

		Instructions
		Several Patient Feed messages and a PDQ Query message will be sent to your PD Supplier. 
		The PDQ Query will reflected an exact name search on MOORE CHIP

		Validation Criteria
		The Supplier is expected to provide a valid HL7v2 PDQ Query Response message, 
		correctly populated with the given values. A single PID segment shall be returned, 
		containing the demographics of patient MOORE CHIP in domain 1.
	 */
	public void testPdqMesa11312() {
		try {
			// PDQ Request Message: 
			String message = "MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|XYZ_HOSPITAL|||QBP^Q22|11311110|P|2.5||||||||\r" + 
		      "QPD|IHE PDQ Query|QRY11311110|@PID.5.1.1^MOORE~@PID.5.2^CHIP|||||^^^XREF2005\r" +
		      "RCP|I|10^RD|||||";

			Message response = sendMessage(message);
			System.out.println("Received response:\n" + getResponseString(response));
			MSA msa = (MSA)response.get("MSA");
			assertEquals("AA", msa.getAcknowledgmentCode().getValue());
			QAK qak = (QAK)response.get("QAK");
			assertEquals("NF", qak.getQueryResponseStatus().getValue());

		} catch(Exception e) {
			e.printStackTrace();
			fail("Fail to test PDQ exact name search");
		}		
	}	
}
