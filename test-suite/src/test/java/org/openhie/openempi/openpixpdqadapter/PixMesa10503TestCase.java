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
import ca.uhn.hl7v2.model.v25.segment.ERR;

public class PixMesa10503TestCase extends AbstractPixTest
{

	   public void testPixMesa10503() {
			try {
				//Step 1: Pix Query a patient whose domain is not registered
				//Request:
				String message = "MSH|^~\\&|MESA_PIX_CLIENT|MESA_DEPARTMENT|MESA_XREF|XYZ_HOSPITAL|200603121200||QBP^Q23|10503102|P|2.5||||||||\r" + 
			      "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10503102|ABC10503^^^XXXX|||||\r" +
			      "RCP|I||||||";
				//Response:	
//				MSH|^~\&|MESA_XREF|XYZ_HOSPITAL|MESA_PIX_CLIENT|MESA_DEPARTMENT|20090124004120-0500||RSP^K23|PIXPDQ_348817|P|2.5
//				MSA|AE|10503102
//				ERR||QPD^1^3^1^4|204^Unknown Key Identifier|E
//				QAK|QRY10503102|AE
//				QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10503102|ABC10503^^^XXXX
				Message response = sendMessage(message);
				System.out.println("Received response:\n" + getResponseString(response));
				ca.uhn.hl7v2.model.v25.segment.MSA msa25 = (ca.uhn.hl7v2.model.v25.segment.MSA)response.get("MSA");
				assertEquals("AE", msa25.getAcknowledgmentCode().getValue());
				assertEquals("10503102", msa25.getMessageControlID().getValue());
				ca.uhn.hl7v2.model.v25.segment.QAK qak = (ca.uhn.hl7v2.model.v25.segment.QAK)response.get("QAK");
				assertEquals("AE", qak.getQueryResponseStatus().getValue());
				assertEquals("QRY10503102", qak.getQueryTag().getValue());
				ERR err = (ERR)response.get("ERR");
				assertEquals("3", err.getErrorLocation(0).getFieldPosition().getValue());
				assertEquals("4", err.getErrorLocation(0).getComponentNumber().getValue());
				assertEquals("204", err.getHL7ErrorCode().getIdentifier().getValue());

			}catch(Exception e) {
				e.printStackTrace();
				fail("Fail to test PIX Mesa 10503 PIX Query for patient with unrecognized domain");
			}
		   
	   }
}
