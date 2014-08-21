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
package org.openhie.openempi.openpixpdq.v3.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01ControlActProcess;
import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openpixpdq.api.IMessageStoreLogger;
import org.openhealthtools.openpixpdq.api.IPixManagerAdapter;
import org.openhealthtools.openpixpdq.api.IPixUpdateNotificationRequest;
import org.openhealthtools.openpixpdq.api.MessageStore;
import org.openhealthtools.openpixpdq.api.PixManagerException;
import org.openhie.openempi.openpixpdq.v3.util.HL7AckTransforms;
import org.openhie.openempi.openpixpdq.v3.util.HL7Constants;
import org.openhie.openempi.openpixpdq.v3.util.HL7DataTransformHelper;
import org.openhie.openempi.openpixpdq.v3.util.HL7MessageIdGenerator;
import org.openhie.openempi.openpixpdq.v3.util.HL7ReceiverTransforms;
import org.openhie.openempi.openpixpdq.v3.util.HL7SenderTransforms;
import org.openhie.openempi.openpixpdq.v3.util.Utilities;

public class PixUpdateNotificationRequestV3 implements IPixUpdateNotificationRequest
{
	private static Logger log = Logger.getLogger(PixUpdateNotificationRequestV3.class);

	private PixManagerV3 actor = null;
	private Patient patient;
	private List<PatientIdentifier> pids = null;
	private IPixManagerAdapter pixAdapter;
	
	/**
	 * Constructor
	 * 
	 * @param actor the PIX Manager actor for this PIX Update Notification request
	 * @param sender 
	 * @param pids a list of patient identifiers to be notified
	 */
	PixUpdateNotificationRequestV3(PixManagerV3 actor, IPixManagerAdapter pixAdapter, Patient patient, List<PatientIdentifier> pids) {
		super();
		this.actor = actor;
		this.patient = patient;
		this.pixAdapter = pixAdapter;
		this.pids = pids;
	}


	public void execute() {
		if (pids==null || pids.size()==0)
			return ;

		for (IConnectionDescription connection : actor.getPixConsumerConnections()) {
			boolean doNotNotify = Boolean.parseBoolean(connection.getProperty("DoNotNotify"));
			if (doNotNotify)
				continue ;

			List<PatientIdentifier> idsToBeUpdated = new ArrayList<PatientIdentifier>();
			Set<Identifier> defaultDomains = Configuration.getAllDomains(actor.getActorDescription());
			Set<Identifier> domainsOfInterest = pixAdapter.getDomainIdentifiers(defaultDomains);
			for (PatientIdentifier pid : pids) {
				if (domainsOfInterest.contains(pid.getAssigningAuthority())) {
					idsToBeUpdated.add(pid);
				}				
			}
			if (idsToBeUpdated.size() == 0) 
				continue;

			MessageStore store = null;

			try {
				PRPAIN201302UV02 updateNotificationMessage = createUpdateNotificationMessage(actor.getActorDescription(), connection, patient,
						idsToBeUpdated);
				log.debug(updateNotificationMessage.toString());
				store = initMessageStore(updateNotificationMessage.toString(), actor.getStoreLogger(), false);

				log.info("Sending PIX Update Notification to "+ connection.getDescription());	

				HL7ChannelV3 channel = new PixConsumerChannel(connection);
				Object replyMessage = channel.sendMessage(updateNotificationMessage);
				String reply = Utilities.objectMessageToString(replyMessage);
				if (store != null) {  
					store.setInMessage(reply);			
				}						
				boolean ok = processPixUpdateNotificationResponse(replyMessage, connection);

				if (ok) auditLog(idsToBeUpdated, connection);

			} catch(Exception e) {
				//Do not re-throw exception to let notification message continue to send to other PIX Consumers.
				String errorMsg = "Cannot send patient update notification to PIX Consumer: " + connection.getDescription();
				errorMsg += " Error Message:"+e.getMessage();
				log.error(errorMsg);
				if (store != null) {
					store.setErrorMessage( errorMsg );
				}							
			} finally {
				//Persist the message
				if (actor.getStoreLogger() != null && store != null) {  
					actor.getStoreLogger().saveLog(store);			
				}						
			}
		}
	}

	private PRPAIN201302UV02 createUpdateNotificationMessage(IActorDescription actorDescription, IConnectionDescription connection, Patient patient, List<PatientIdentifier> ids) {
    	PRPAIN201302UV02 msg = new PRPAIN201302UV02();

    	// Create the update notification message header fields
    	msg.setITSVersion(HL7Constants.ITS_VERSION);
        msg.setId(HL7MessageIdGenerator.GenerateHL7MessageId());
        msg.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        msg.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201302UV02"));
        msg.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        msg.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        msg.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));

        // Create the Sender
        // Within a given actor, the sender of the update notification in this case is the receiver 
        Identifier applicationId = connection.getIdentifier("SendingApplication");
        Identifier facilityId = connection.getIdentifier("SendingFacility");
        msg.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(applicationId.getUniversalId(), facilityId.getUniversalId()));
        
        // Create the Receiver
        // For a given PIX Consumer connection, the recipient of the update notification, is the receiver 
        // for the connection.
        Identifier receiverAppId = connection.getIdentifier("ReceivingApplication");
        Identifier receiverFacId = connection.getIdentifier("ReceivingFacility");
        msg.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverAppId.getUniversalId(),
        		receiverFacId.getUniversalId()));

        PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = HL7AckTransforms.
        		createUpdateNotificationControlActProcess(actor, patient, ids);
        msg.setControlActProcess(controlActProcess);
        Utilities.logMessageObject(msg);
		return msg;
	}


	/**
	 * Audits this PIX Update Notification message request.
	 * 
	 * @param pids the list of patient identifiers to notify the subscribed PIX Consumers.
	 * @param connection the connection description
	 */
	private void auditLog(List<PatientIdentifier> pids, IConnectionDescription connection) {
		if (actor.getAuditTrail() == null) return;

		try {
			ActiveParticipant destination = new ActiveParticipant();
			Identifier receivingApplication = Configuration.getIdentifier(connection, "ReceivingApplication", true);
			Identifier receivingFacility = Configuration.getIdentifier(connection, "ReceivingFacility", true);
			destination.setUserId(receivingFacility.getAuthorityNameString()+"|"+receivingApplication.getAuthorityNameString());
			destination.setAccessPointId(Configuration.getPropertyValue(connection, "hostname", true));
			ParticipantObject patientObject = new ParticipantObject();
			patientObject.setId(pids);
			//TODO
			//patientObject.addDetail(new TypeValuePair("MSH-10", header.getMessageControlId()));
			actor.getAuditTrail().logPixUpdateNotification(destination, patientObject);		
		} catch(Exception e) {
			log.error("Fail to audit log Pix Update Notification: " +e.getMessage());
		}
	}

	private boolean processPixUpdateNotificationResponse(Object replyMessage, IConnectionDescription connection) throws PixManagerException {

		if (!(replyMessage instanceof MCCIIN000002UV01)) {
			throw new PixManagerException("Unexpected response from Patient Identity consumer \"" + connection.getDescription());			
		}
		MCCIIN000002UV01 reply = (MCCIIN000002UV01) replyMessage;
		if (reply.getInteractionId() == null ||
				reply.getInteractionId().getExtension() == null ||
				!reply.getInteractionId().getExtension().equalsIgnoreCase(HL7Constants.INTERACTION_EXTENSION_MCCIIN000002UV01)) {
			throw new PixManagerException("Unexpected interaction extension from Patient Identity consumer \"" + connection.getDescription());
		}

		String status = getAcknowledgmentTypeCode(reply);
		if (status == null || (!status.equalsIgnoreCase(HL7Constants.MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_APPLICATION_ACCEPT) 
				&& !status.equalsIgnoreCase(HL7Constants.MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_COMMIT_ACCEPT))) {
			//actor.logHL7MessageError(log, message, error);
			throw new PixManagerException("Invalid acknowledgement type code of " + status + 
					" from Patient Identity consumer \"" + connection.getDescription());
		}
		return true;
	}

	private MessageStore initMessageStore(String message, IMessageStoreLogger storeLogger, boolean isInbound) {
		if (storeLogger == null)
			return null;

		MessageStore ret = new MessageStore(); 
		if (message != null) {
			if (isInbound)
				ret.setInMessage( message );
			else 
				ret.setOutMessage( message );
		}
		return ret;
	}

	private String getAcknowledgmentTypeCode(MCCIIN000002UV01 reply) {
		String value = "";
		if (reply.getAcknowledgement() == null ||
				reply.getAcknowledgement().size() == 0 ||
				reply.getAcknowledgement().get(0).getTypeCode() == null ||
						reply.getAcknowledgement().get(0).getTypeCode().getCode() == null) {
			return value;
		}
		return reply.getAcknowledgement().get(0).getTypeCode().getCode();
	}
}
