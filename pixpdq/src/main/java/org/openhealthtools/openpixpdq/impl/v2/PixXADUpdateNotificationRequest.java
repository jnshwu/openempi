/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */
package org.openhealthtools.openpixpdq.impl.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openpixpdq.api.PixManagerException;
import org.openhealthtools.openpixpdq.common.BaseHandler;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7v25;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.XPN;
import ca.uhn.hl7v2.model.v25.message.ADT_A43;
import ca.uhn.hl7v2.model.v25.segment.MRG;
/**
* The request class of PIX update notification, which is used to notify 
* subscribed PIX Consumers when there is change in a
* set of cross reference patient identifiers for any of the patient
* identifiers belonging to patient identifier domains of interest to 
* the PIX consumers.
* 
*/
class PixXADUpdateNotificationRequest extends PixUpdateNotificationRequest
{
    private List<PatientIdentifier> prePids = null;
    
    /**
     * Constructor
     * 
     * @param actor the PIX Manager actor for this PIX Update Notification request
     * @param pids a list of patient identifiers to be notified
     */
	PixXADUpdateNotificationRequest(PixManager actor, List<PatientIdentifier> pids, List<PatientIdentifier> prePids) {
		super(actor, pids);
		this.prePids = prePids;
		connections = new ArrayList<IConnectionDescription>();
		connections.add(actor.getXdsRegistryConnection());
	}
 
    /**
     * Creates an HL7 Patient Update Notification message.
     * 
  	 * @param patient the patient demographics to be sent
  	 * @param eventTime the time this patient event took place: typically now
  	 * @param authority the assigning Authority
  	 * @return a HL7 ADT^A31 Update Notification message to send  
  	 * @throws IheConfigurationException When this connection is not properly configured to encode messages
  	 * @throws PixManagerException When required patient information is missing
  	 * @throws HL7Exception When the patient information cannot be encoded properly into HL7
     */
  	protected Message createHL7UpdateNotificationMessage(List<PatientIdentifier> pids, IConnectionDescription connection)
  			throws IheConfigurationException, PixManagerException, HL7Exception {
		ADT_A43 message = new ADT_A43();
		// Populate the MSH segment
//		HL7v25.populateMSH(message.getMSH(), "ADT", "A43", BaseHandler.getMessageControlId(), connection);
        Identifier sendAPPIdentifier = Configuration.getIdentifier(connection, "SendingApplication", true);
        Identifier sendFacilityIdentifier = Configuration.getIdentifier(connection, "SendingFacility", true);
        Identifier reciAppReciIdentifier = Configuration.getIdentifier(connection, "ReceivingApplication", true);
        Identifier reciFacilityIdentifier = Configuration.getIdentifier(connection, "ReceivingFacility", true);        
		HL7v25.populateMSH(message.getMSH(), "ADT", "A43", "ADT_A43", BaseHandler.getMessageControlId(), 
				           sendAPPIdentifier, sendFacilityIdentifier, reciAppReciIdentifier, reciFacilityIdentifier);
		
		// Populate the EVN segment		
		populateEVN(message.getEVN(), "A43", new Date());
		// Populate the PID segment
		populatePID(message.getPATIENT().getPID(), pids);		
		// Populate the MRG segment		
		populateMRG(message.getPATIENT().getMRG(), prePids);
		
		return message;
  	}
	
	private void populateMRG(MRG mgr, List<PatientIdentifier> prePids) throws IheConfigurationException, PixManagerException, HL7Exception {
		
		if( prePids != null ) {
			// PID-3 - Preferred ID 
			for (int i=0; i<prePids.size(); i++) {
				PatientIdentifier id = prePids.get(i);
				populateCX(mgr.getPriorPatientIdentifierList(i), id);
			}
			// PID-5 - Patient legal name, 
			// Return a single space charter in PID-5, see ITI-vol2 Section 3.10.4.1.2.3
			XPN xpn = mgr.getPriorPatientName(0);
			xpn.getFamilyName().getSurname().setValue(" ");
		}
	}
}
