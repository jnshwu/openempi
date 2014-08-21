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
package org.openhie.openempi.openpixpdq.v3.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.AcknowledgementDetailType;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.MCCIMT000200UV01AcknowledgementDetail;
import org.hl7.v3.MCCIMT000200UV01TargetMessage;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01AcknowledgementDetail;
import org.hl7.v3.MCCIMT000300UV01TargetMessage;
import org.hl7.v3.MFMIMT700701UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.hl7.v3.ParticipationTargetSubject;
import org.hl7.v3.XActMoodIntentEvent;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openpixpdq.api.IheActorException;
import org.openhie.openempi.openpixpdq.v3.impl.PixManagerV3;

public class HL7AckTransforms
{
    private static Log log = LogFactory.getLog(HL7AckTransforms.class);

    public static MCCIIN000002UV01 createAckFrom201305(PRPAIN201305UV02 request, String ackMsgText) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null) {
            // Extract the message id
            if (request.getId() != null) {
                msgId = request.getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (Utilities.isNotNullish(request.getReceiver()) &&
                    request.getReceiver().get(0) != null &&
                    request.getReceiver().get(0).getDevice() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    Utilities.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    Utilities.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                senderOID = request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
    	    
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    request.getSender().getDevice().getAsAgent() != null &&
                    request.getSender().getDevice().getAsAgent().getValue() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    Utilities.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    Utilities.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                receiverOID = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Create the ack message
            ack = HL7AckTransforms.createAckMessage(null, msgId, ackMsgText, senderOID, receiverOID);
        }

        return ack;
    }

    public static MCCIIN000002UV01 createAckFrom201306(PRPAIN201306UV02 request, String ackMsgText) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null) {
            // Extract the message id
            if (request.getId() != null) {
                msgId = request.getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (Utilities.isNotNullish(request.getReceiver()) &&
                    request.getReceiver().get(0) != null &&
                    request.getReceiver().get(0).getDevice() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    Utilities.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    Utilities.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                senderOID = request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    request.getSender().getDevice().getAsAgent() != null &&
                    request.getSender().getDevice().getAsAgent().getValue() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    Utilities.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    Utilities.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                receiverOID = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Create the ack message
            ack = HL7AckTransforms.createAckMessage(null, msgId, ackMsgText, senderOID, receiverOID);
        }

        return ack;
    }

    public static MCCIIN000002UV01 createAckMessage(String localDeviceId, II origMsgId, String msgText, String senderOID, String receiverOID) {
        MCCIIN000002UV01 ackMsg = new MCCIIN000002UV01();

        // Validate input parameters
        if (Utilities.isNullish(senderOID)) {
            log.error("Failed to specify a sender OID");
            return null;
        }

        if (Utilities.isNullish(receiverOID)) {
            log.error("Failed to specify a receiver OID");
            return null;
        }

        // Create the Ack message header fields
        ackMsg.setITSVersion(HL7Constants.ITS_VERSION);
        ackMsg.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        ackMsg.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        ackMsg.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "MCCI_IN000002UV01"));
        ackMsg.setProcessingCode(HL7DataTransformHelper.CSFactory("T"));
        ackMsg.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        ackMsg.setAcceptAckCode(HL7DataTransformHelper.CSFactory("NE"));

        // Create the Sender
        ackMsg.setSender(HL7SenderTransforms.createMCCIMT000200UV01Sender(senderOID));

        // Create the Receiver
        ackMsg.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000200UV01Receiver(receiverOID));

        // Create Acknowledgement section if an original message id or message text was specified
        if (Utilities.isNotNullish(msgText) ||
                (origMsgId != null && Utilities.isNotNullish(origMsgId.getRoot()) && Utilities.isNotNullish(origMsgId.getExtension()))) {
            log.debug("Adding Acknowledgement Section");
            ackMsg.getAcknowledgement().add(createAcknowledgement(origMsgId, msgText));
        }

        return ackMsg;
    }

    public static PRPAIN201310UV02 createQueryResponse(II origMsgId, String senderAppOid, String senderFacilityOid,
    		String receiverAppOid, String receiverFacilityOid, IheActorException e) {

    	PRPAIN201310UV02 msg = new PRPAIN201310UV02();
    	
    	// Create the query response message header fields
    	msg.setITSVersion(HL7Constants.ITS_VERSION);
        msg.setId(HL7MessageIdGenerator.GenerateHL7MessageId());
        msg.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        msg.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201310UV02"));
        msg.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        msg.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        msg.setAcceptAckCode(HL7DataTransformHelper.CSFactory("NE"));

        // Create the Sender
        msg.setSender(HL7SenderTransforms.createMCCIMT000300UV01Sender(senderAppOid, senderFacilityOid));
        // Create the Receiver
        msg.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(receiverAppOid, receiverFacilityOid));

        // Create Acknowledgement section if an original message id or message text was specified
        if (Utilities.isNotNullish(origMsgId.getRoot())) {
            log.debug("Adding Acknowledgement Section");
            if (e == null) {
            	msg.getAcknowledgement().add(createMCCIMT000300UV01Acknowledgement(origMsgId,
            			HL7Constants.MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_APPLICATION_ACCEPT));
            } else {
            	msg.getAcknowledgement().add(createMCCIMT000300UV01Acknowledgement(origMsgId,
            			HL7Constants.MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_APPLICATION_ERROR, e));
            }
        }
        return msg;
    }

	public static MCCIIN000002UV01 createAckMessage(II origMsgId, String msgText, String senderOID, String senderAppOid, 
    		String receiverOID, String receiverAppOid) {
        return createAckMessage(origMsgId, msgText, senderOID, senderAppOid, receiverOID, receiverAppOid, null);
    }
    
    public static MCCIIN000002UV01 createAckMessage(II origMsgId, String msgText, String senderOID, String senderAppOid, 
    		String receiverOID, String receiverAppOid, IheActorException e) {
        MCCIIN000002UV01 ackMsg = new MCCIIN000002UV01();

        // Create the Ack message header fields
        ackMsg.setITSVersion(HL7Constants.ITS_VERSION);
        ackMsg.setId(HL7MessageIdGenerator.GenerateHL7MessageId());
        ackMsg.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        ackMsg.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "MCCI_IN000002UV01"));
        ackMsg.setProcessingCode(HL7DataTransformHelper.CSFactory("T"));
        ackMsg.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        ackMsg.setAcceptAckCode(HL7DataTransformHelper.CSFactory("NE"));

        // Create the Sender
        ackMsg.setSender(HL7SenderTransforms.createMCCIMT000200UV01Sender(senderAppOid, senderOID));

        // Create the Receiver
        ackMsg.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000200UV01Receiver(receiverAppOid, receiverOID));

        // Create Acknowledgement section if an original message id or message text was specified
        if (Utilities.isNotNullish(origMsgId.getRoot())) {
            log.debug("Adding Acknowledgement Section");
            if (e == null) {
            	ackMsg.getAcknowledgement().add(createAcknowledgement(origMsgId, "", HL7Constants.MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_COMMIT_ACCEPT));
            } else {
            	ackMsg.getAcknowledgement().add(createAcknowledgement(origMsgId, HL7Constants.MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_COMMIT_ERROR,
            			e));
            }
        }

        return ackMsg;
    }

    private static MCCIMT000300UV01Acknowledgement createMCCIMT000300UV01Acknowledgement(II msgId, String typeCode) {
    	MCCIMT000300UV01Acknowledgement ack = new MCCIMT000300UV01Acknowledgement();
    	ack.setTypeCode(HL7DataTransformHelper.CSFactory(typeCode));
        if (msgId != null) {
            ack.setTargetMessage(createMCCIMT000300UV01TargetMessage(msgId));
        }
		return ack;
	}

    private static MCCIMT000300UV01Acknowledgement createMCCIMT000300UV01Acknowledgement(II msgId, String typeCode, IheActorException e) {
    	MCCIMT000300UV01Acknowledgement ack = new MCCIMT000300UV01Acknowledgement();
    	ack.setTypeCode(HL7DataTransformHelper.CSFactory(typeCode));
        if (msgId != null) {
            ack.setTargetMessage(createMCCIMT000300UV01TargetMessage(msgId));
        }
        if (e != null) {
        	if (e.getData() == null || !(e.getData() instanceof List<?>)) {
	        	MCCIMT000300UV01AcknowledgementDetail error = HL7AckTransforms.createMCCIMT00300UV01AckDetail(e.getMessage());
	        	if (e.getLocation() != null) {
	        		error.getLocation().add(Utilities.generateSTExplicit(e.getLocation()));	
	        	}
	        	if (e.getCode() != null) {
	        		error.setCode(Utilities.generateCe(e.getCode()));
	        	}
	            ack.getAcknowledgementDetail().add(error);
	        // This is support for Case #5 where we did not recognize one or more of the dataSource
	        // domains requested by the user.
        	} else {
        		@SuppressWarnings("unchecked")
				List<String> domains = (List<String>) e.getData();
        		for (String domain : domains) {
    	        	MCCIMT000300UV01AcknowledgementDetail error = HL7AckTransforms.createMCCIMT00300UV01AckDetail(e.getMessage() + ":" + domain);
    	        	if (e.getLocation() != null) {
    	        		error.getLocation().add(Utilities.generateSTExplicit(e.getLocation() + domain + "'"));
    	        	}
    	        	if (e.getCode() != null) {
    	        		error.setCode(Utilities.generateCe(e.getCode()));
    	        	}
    	            ack.getAcknowledgementDetail().add(error);        			
        		}
        	}
        }
		return ack;
	}

	public static MCCIMT000200UV01Acknowledgement createAcknowledgement(II msgId, String typeCode, IheActorException e) {
        MCCIMT000200UV01Acknowledgement ack = new MCCIMT000200UV01Acknowledgement();

        ack.setTypeCode(HL7DataTransformHelper.CSFactory(typeCode));

        if (msgId != null) {
            ack.setTargetMessage(createTargetMessage(msgId));
        }

        if (e != null) {
        	MCCIMT000200UV01AcknowledgementDetail error = HL7AckTransforms.createAckDetail(e.getMessage());
        	if (e.getLocation() != null) {
        		error.getLocation().add(Utilities.generateSTExplicit(e.getLocation()));	
        	}
        	if (e.getCode() != null) {
        		error.setCode(Utilities.generateCe(e.getCode()));
        	}
            ack.getAcknowledgementDetail().add(error);
        }

        return ack;
    }

    public static MCCIMT000200UV01Acknowledgement createAcknowledgement(II msgId, String msgText, String typeCode) {
        MCCIMT000200UV01Acknowledgement ack = new MCCIMT000200UV01Acknowledgement();

        ack.setTypeCode(HL7DataTransformHelper.CSFactory(typeCode));

        if (msgId != null) {
            ack.setTargetMessage(createTargetMessage(msgId));
        }

        if (msgText != null) {
            ack.getAcknowledgementDetail().add(createAckDetail(msgText));
        }

        return ack;
    }

    public static MCCIMT000200UV01Acknowledgement createAcknowledgement(II msgId, String msgText) {
        MCCIMT000200UV01Acknowledgement ack = new MCCIMT000200UV01Acknowledgement();

        ack.setTypeCode(HL7DataTransformHelper.CSFactory("CA"));

        if (msgId != null) {
            ack.setTargetMessage(createTargetMessage(msgId));
        }

        if (msgText != null) {
            ack.getAcknowledgementDetail().add(createAckDetail(msgText));
        }

        return ack;
    }

    public static MCCIMT000200UV01TargetMessage createTargetMessage(II msgId) {
        MCCIMT000200UV01TargetMessage targetMsg = new MCCIMT000200UV01TargetMessage();

        if (msgId != null) {
            log.debug("Setting original message id, root: " + msgId.getRoot() + ", extension: " + msgId.getExtension());
            targetMsg.setId(msgId);
        }

        return targetMsg;
    }
    
    private static MCCIMT000300UV01TargetMessage createMCCIMT000300UV01TargetMessage(II msgId) {
    	MCCIMT000300UV01TargetMessage targetMsg = new MCCIMT000300UV01TargetMessage();
    	
        if (msgId != null) {
            log.debug("Setting original message id, root: " + msgId.getRoot() + ", extension: " + msgId.getExtension());
            targetMsg.setId(msgId);
        }

        return targetMsg;
	}

	public static MCCIMT000300UV01AcknowledgementDetail createMCCIMT00300UV01AckDetail(String error) {
		MCCIMT000300UV01AcknowledgementDetail detail = new MCCIMT000300UV01AcknowledgementDetail();
		detail.setTypeCode(AcknowledgementDetailType.E);
        if (Utilities.isNotNullish(error)) {
            // Set the acknowledge message text
            EDExplicit msg = new EDExplicit();
            log.debug("Setting ack message text: " + error);
            msg.getContent().add(error);
            detail.setText(msg);
        }
		return detail;
	}

    public static MCCIMT000200UV01AcknowledgementDetail createAckDetail(String msgText, String location) {
    	if (location == null) {
    		return createAckDetail(msgText);
    	}
    	
        MCCIMT000200UV01AcknowledgementDetail ackDetail = new MCCIMT000200UV01AcknowledgementDetail();

        if (Utilities.isNotNullish(msgText)) {
            // Set the acknowledge message text
            EDExplicit msg = new EDExplicit();
            log.debug("Setting ack message text: " + msgText);
            msg.getContent().add(msgText);
            ackDetail.setText(msg);
        }
        ackDetail.getLocation().add(Utilities.generateSTExplicit(location));
        return ackDetail;
    }

    public static MCCIMT000200UV01AcknowledgementDetail createAckDetail(String msgText) {
        MCCIMT000200UV01AcknowledgementDetail ackDetail = new MCCIMT000200UV01AcknowledgementDetail();

        if (Utilities.isNotNullish(msgText)) {
            // Set the acknowledge message text
            EDExplicit msg = new EDExplicit();

            log.debug("Setting ack message text: " + msgText);
            msg.getContent().add(msgText);
            ackDetail.setText(msg);
        }
        return ackDetail;
    }
    
	public static PRPAIN201310UV02MFMIMT700711UV01ControlActProcess createQueryResponseControlActProcess(IActorDescription actor, List<PatientIdentifier> ids, PRPAMT201307UV02QueryByParameter queryByParameter, 
			II queryId, String responseCode) {
		ObjectFactory factory = new ObjectFactory();
		PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = factory.createPRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
		controlActProcess.setClassCode(ActClassControlAct.CACT);
		controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
		controlActProcess.setCode(Utilities.generateCd("PRPA_TE201310UV02", null));
		controlActProcess.setQueryByParameter(factory.createPRPAIN201309UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryByParameter));
		if (ids != null && ids.size() > 0) {
			PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
			subject.getTypeCode().add(HL7Constants.TYPE_CODE_SUBJ);
			PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registration = new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();
			subject.setRegistrationEvent(registration);
			registration.getClassCode().add(HL7Constants.CLASS_CODE_REGISTRATION);
			registration.getMoodCode().add(HL7Constants.MOOD_COODE_EVN);
			II regId = new II();
			regId.getNullFlavor().add(HL7Constants.NULL_FLAVOR);
			registration.getId().add(regId);
			registration.setStatusCode(Utilities.generateCs(HL7Constants.STATUS_CODE_ACTIVE));
			PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = new PRPAIN201310UV02MFMIMT700711UV01Subject2();
			subject1.setTypeCode(ParticipationTargetSubject.SBJ);
			registration.setSubject1(subject1);
			MFMIMT700711UV01Custodian custodian = new MFMIMT700711UV01Custodian();
			custodian.getTypeCode().add(HL7Constants.TYPE_CODE_CST);
			COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
			assignedEntity.setClassCode(HL7Constants.CLASS_CODE_ASSIGNED);
			II assignedEntityId = new II();
			assignedEntityId.setRoot(actor.getIdentifier("ReceivingFacility").getUniversalId());
			assignedEntity.getId().add(assignedEntityId);
			custodian.setAssignedEntity(assignedEntity);
			registration.setCustodian(custodian);
			subject1.setPatient(HL7PatientTransforms.create201304Patient(ids));
			controlActProcess.getSubject().add(subject);
		}
		MFMIMT700711UV01QueryAck queryAck = new MFMIMT700711UV01QueryAck();
		queryAck.setQueryId(queryId);
		queryAck.setQueryResponseCode(Utilities.generateCs(responseCode));
		controlActProcess.setQueryAck(queryAck);
		return controlActProcess;
	}

	public static PRPAIN201302UV02MFMIMT700701UV01ControlActProcess createUpdateNotificationControlActProcess(
			PixManagerV3 actor, Patient patient, List<PatientIdentifier> ids) {
		ObjectFactory factory = new ObjectFactory();
		PRPAIN201302UV02MFMIMT700701UV01ControlActProcess controlActProcess = factory.createPRPAIN201302UV02MFMIMT700701UV01ControlActProcess();
		controlActProcess.setClassCode(ActClassControlAct.CACT);
		controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
		controlActProcess.setCode(Utilities.generateCd(HL7Constants.UPDATE_NOTIFICATION_TRIGGER_EVENT_CODE, null));
		if (ids != null && ids.size() > 0) {
			PRPAIN201302UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201302UV02MFMIMT700701UV01Subject1();
			subject.getTypeCode().add(HL7Constants.TYPE_CODE_SUBJ);
			PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent registration = new PRPAIN201302UV02MFMIMT700701UV01RegistrationEvent();
			subject.setRegistrationEvent(registration);
			registration.getClassCode().add(HL7Constants.CLASS_CODE_REGISTRATION);
			registration.getMoodCode().add(HL7Constants.MOOD_COODE_EVN);
//			II regId = new II();
//			regId.getNullFlavor().add(HL7Constants.NULL_FLAVOR);
//			registration.getId().add(regId);
			registration.setStatusCode(Utilities.generateCs(HL7Constants.STATUS_CODE_ACTIVE));
			PRPAIN201302UV02MFMIMT700701UV01Subject2 subject1 = new PRPAIN201302UV02MFMIMT700701UV01Subject2();
			subject1.setTypeCode(ParticipationTargetSubject.SBJ);
			registration.setSubject1(subject1);
			MFMIMT700701UV01Custodian custodian = new MFMIMT700701UV01Custodian();
			custodian.getTypeCode().add(HL7Constants.TYPE_CODE_CST);
			COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
			assignedEntity.setClassCode(HL7Constants.CLASS_CODE_ASSIGNED);
			II assignedEntityId = new II();
			assignedEntityId.setRoot(actor.getActorDescription().getIdentifier("ReceivingFacility").getUniversalId());
			assignedEntity.getId().add(assignedEntityId);
			custodian.setAssignedEntity(assignedEntity);
			registration.setCustodian(custodian);
			subject1.setPatient(HL7PatientTransforms.create201302Patient(patient, ids));
			controlActProcess.getSubject().add(subject);
		}
		return controlActProcess;
	}
}
