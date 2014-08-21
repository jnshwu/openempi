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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.ConnectionFactory;
import org.openhealthtools.openexchange.actorconfig.net.IConnection;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.AuditCodeMappings;
import org.openhealthtools.openexchange.audit.AuditCodeMappings.EventActionCode;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.audit.TypeValuePair;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.MessageHeader;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openexchange.utils.ExceptionUtil;
import org.openhealthtools.openpixpdq.api.IJMXEventNotifier;
import org.openhealthtools.openpixpdq.api.IPixManagerAdapter;
import org.openhealthtools.openpixpdq.api.IPixUpdateNotificationRequest;
import org.openhealthtools.openpixpdq.api.MessageStore;
import org.openhealthtools.openpixpdq.api.PixManagerException;
import org.openhealthtools.openpixpdq.common.AssigningAuthorityUtil;
import org.openhealthtools.openpixpdq.common.BaseHandler;
import org.openhealthtools.openpixpdq.common.Constants;
import org.openhealthtools.openpixpdq.common.PixPdqException;
import org.openhealthtools.openpixpdq.common.PixPdqFactory;
import org.openhealthtools.openpixpdq.common.PixUpdateNotifier;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7Channel;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7Header;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7v231;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7v231ToBaseConvertor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.datatype.CE;
import ca.uhn.hl7v2.model.v231.datatype.CX;
import ca.uhn.hl7v2.model.v231.datatype.ELD;
import ca.uhn.hl7v2.model.v231.datatype.ST;
import ca.uhn.hl7v2.model.v231.group.ADT_A39_PIDPD1MRGPV1;
import ca.uhn.hl7v2.model.v231.message.ACK;
import ca.uhn.hl7v2.model.v231.message.ADT_A01;
import ca.uhn.hl7v2.model.v231.message.ADT_A04;
import ca.uhn.hl7v2.model.v231.message.ADT_A05;
import ca.uhn.hl7v2.model.v231.message.ADT_A08;
import ca.uhn.hl7v2.model.v231.message.ADT_A39;
import ca.uhn.hl7v2.model.v231.segment.MRG;
import ca.uhn.hl7v2.model.v231.segment.PID;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * This class processes PIX Feed message in HL7 v2.3.1 format. It 
 * handles the PIX Feed transaction (including also PIX Update 
 * and PIX Merge transactions) of the PIX profile.  
 * The supported message type includes ADT^A01, ADT^A04, ADT^A05, 
 * ADT^A08 and ADT^A40.
 *
 * @author Wenzhi Li 
 * @version 1.0 Oct 22, 2008
 * 
 */
class PixFeedHandler extends BaseHandler implements Application {

    private static Logger log = Logger.getLogger(PixFeedHandler.class);
	private PixManager actor = null;
	
	private IPixManagerAdapter pixAdapter = null;
	/** Keep an instance of v25 handler for message redirection */
	private PixFeedHandlerV25 handlerV25 = null;
	private Connection hl7Connection = null;
	private IJMXEventNotifier eventBean = null;
	/**
	 * Constructor
	 * 
	 * @param actor the {@link PixManager} actor
	 */
	PixFeedHandler(PixManager actor) {
		super();
		this.actor = actor;
		this.pixAdapter = PixPdqFactory.getPixManagerAdapter(); 
		this.handlerV25 = new PixFeedHandlerV25(actor);
		this.eventBean = actor.getPixEvent();
		assert this.pixAdapter != null;
	}

    /**
     * Whether a incoming message can be processed by this handler.
     * 
     * @return <code>true</code> if the incoming message can be processed;
     * otherwise <code>false</code>.
     */
	public boolean canProcess(Message theIn) {
		if (theIn instanceof ADT_A01 || theIn instanceof ADT_A04 ||
		    theIn instanceof ADT_A05 || theIn instanceof ADT_A08 ||
		    theIn instanceof ADT_A39 ||
		    theIn instanceof ca.uhn.hl7v2.model.v25.message.ADT_A01 ||
		    theIn instanceof ca.uhn.hl7v2.model.v25.message.ADT_A05)
			return true;
		else
			return false;
	}
	 
    /**
     * Processes the incoming PIX Feed Message. Valid messages 
     * are ADT^A01, ADT^A04, ADT^A05, ADT^A08 and ADT^A40.
     * 
     * @param msgIn the incoming message
     * @throws IOException 
     * @throws IOException 
     */
	public Message processMessage(Message msgIn) throws ApplicationException,
			HL7Exception {		
		Message retMessage = null;
		MessageStore store = MessageStoreHelper.initMessageStore(msgIn, actor.getStoreLogger(), true);
		//String encodedMessage = HapiUtil.encodeMessage(msgIn);
		//log.info("Received message:\n" + encodedMessage + "\n\n");
		try {
			HL7Header hl7Header = new HL7Header(msgIn);			

			//Populate MessageStore to persist the message
			hl7Header.populateMessageStore(store);

			if (msgIn instanceof ADT_A01 || //Admission of in-patient into a facility
				msgIn instanceof ADT_A04 || //Registration of an outpatient for a visit of the facility
				msgIn instanceof ADT_A05) { //Pre-admission of an in-patient 
				retMessage = processCreate(msgIn);
			} else if (msgIn instanceof ADT_A08) { //Update patient information   
				retMessage = processUpdate(msgIn);
			} else if (msgIn instanceof ADT_A39) { //Merge Patients
				retMessage = processMerge(msgIn);
			} else if (msgIn instanceof ca.uhn.hl7v2.model.v25.message.ADT_A01 || //Admission of in-patient into a facility
					msgIn instanceof ca.uhn.hl7v2.model.v25.message.ADT_A05) { //Pre-admission of an in-patient 
					retMessage = handlerV25.processCreate(msgIn);
			} else {
				String errorMsg = "Unexpected request to PIX Manager server. " 
					+ "Valid message types are ADT^A01, ADT^A04, ADT^A05, ADT^A08 and ADT^A40";

				throw new ApplicationException(errorMsg);
			}
			
		} catch (PixPdqException e) {
			if (store !=null) { 
				store.setErrorMessage( e.getMessage() );
			}
			throw new ApplicationException(ExceptionUtil.strip(e.getMessage()), e);		
		} catch (HL7Exception e) {
			if (store !=null) {
				store.setErrorMessage( e.getMessage() );
			}	
			throw new HL7Exception(ExceptionUtil.strip(e.getMessage()), e);
		} finally {
			//Persist the message
			if (store !=null) { 
				MessageStoreHelper.saveMessageStore(retMessage, actor.getStoreLogger(), false, store);			
			}
			if(eventBean != null)
				eventBean.notifyMessageReceived();
		}
		return retMessage;
	}

	/**
	 * Processes PIX Feed Create Patient message in HL72.3.1.
	 * 
	 * @param msgIn the PIX Feed request message
	 * @return a response message for PIX Feed
	 * @throws PixPdqException If Application has trouble
	 * @throws HL7Exception if something is wrong with HL7 message 
	 */
	private Message processCreate(Message msgIn) 
	throws PixPdqException, HL7Exception {

		assert msgIn instanceof ADT_A01 || 
			   msgIn instanceof ADT_A04 || 
			   msgIn instanceof ADT_A05;

		HL7Header hl7Header = new HL7Header(msgIn);
		
		//If it is for A08, we redirect it to processUpdate.
		if(hl7Header.getTriggerEvent().equals("A08")) {
			return processUpdate(msgIn);
		}
			
		//Create Acknowledgment and its Header
		ACK reply = initAcknowledgment(hl7Header);

		//Validate incoming message first
		PID pid = (PID)msgIn.get("PID");
		PatientIdentifier patientId = getPatientIdentifiers(pid);				
		boolean isValidMessage = validateMessage(reply, hl7Header, patientId, null, true);
		if (!isValidMessage) return reply;
		
		//Invoke eMPI function
		MessageHeader header = hl7Header.toMessageHeader();
		Patient patient = getPatient(msgIn);
		try {
            List<PatientIdentifier> matching = null;
            if (!actor.isNotification()){
                matching = pixAdapter.createPatient(patient, header);
            }
			
			//Send PIX Update Notification
			if (matching != null && matching.size() > 0) {
				IPixUpdateNotificationRequest request = 
					new PixUpdateNotificationRequest(actor, matching);
				PixUpdateNotifier.getInstance().accept(request);
			}			
		}catch (PixManagerException e) {
			throw new PixPdqException(e);
		} 
		
		HL7v231.populateMSA(reply.getMSA(), "AA", hl7Header.getMessageControlId());

		//Forward this PIX Feed (Merge) message to the XDS Registry
		forwardToXdsRegistry(msgIn, patientId);
		
		//Finally, Audit Log PIX Feed Success 
	    auditLog(hl7Header, patient, AuditCodeMappings.EventActionCode.Create);

	    return reply; 
	}
	
	
	/**
	 * Processes PIX Feed Update Patient message.
	 * 
	 * @param msgIn the PIX Feed request message
	 * @return a response message for PIX Feed
	 * @throws PixPdqException If Application has trouble
	 * @throws HL7Exception if something is wrong with HL7 message 
	 */
	private Message processUpdate(Message msgIn) throws PixPdqException,
			HL7Exception {
		assert msgIn instanceof ADT_A01 ||
			   msgIn instanceof ADT_A08 ;
		
		HL7Header hl7Header = new HL7Header(msgIn);
		
		//Create Acknowledgment and its Header
		ACK reply = initAcknowledgment(hl7Header);

		//Validate incoming message first
		PID pid = (PID)msgIn.get("PID");
		PatientIdentifier patientId = getPatientIdentifiers(pid);
		
		boolean isValidMessage = validateMessage(reply, hl7Header, patientId, null, false);
		if (!isValidMessage) return reply;
		
		//Invoke eMPI function
		MessageHeader header = hl7Header.toMessageHeader();
		Patient patient = getPatient(msgIn);
		try {			
			//Ids before update
			List<PatientIdentifier> oldMatchings = pixAdapter.findPatientIds(patientId, null);
			
			//Update Patient
			List<List<PatientIdentifier>> matchingList = null;
            if (!actor.isNotification()){
                matchingList = pixAdapter.updatePatient(patient, header);
            }			
	
			//PIX Update Notification to PIX consumers
			if (matchingList != null) {
				for (List<PatientIdentifier> matching : matchingList) {
					IPixUpdateNotificationRequest matchingRequest = 
						new PixUpdateNotificationRequest(actor, matching);
					PixUpdateNotifier.getInstance().accept(matchingRequest);
				}
			}

			//XAD-PID link change Notification to XDS		
			if (actor.getXdsRegistryConnection()!= null && matchingList != null) {
				
	            String XDSAffinityDomain = actor.getXdsRegistryConnection().getProperty("XDSAffinityDomainPatientIdetifier");
	            
				List<PatientIdentifier> oldXADMatchings = new ArrayList<PatientIdentifier>();
				if ( oldMatchings!=null ) {
					for (PatientIdentifier oldPid : oldMatchings) {
						if (oldPid.getAssigningAuthority().getNamespaceId().equals(XDSAffinityDomain)) {
							oldXADMatchings.add(oldPid);
						}
					}					
				}
				
				for (List<PatientIdentifier> matching : matchingList) {						
					// match list contain the id of updated person
					if (matching.contains(patientId) ) {
						
						// has XADS-PID Domain
						boolean XADNotification = false;
						for (PatientIdentifier matchPid : matching) {
							if (matchPid.getAssigningAuthority().getNamespaceId().equals(XDSAffinityDomain)) {
								XADNotification = true;
							}
						}											
						
						if(XADNotification ) {
							IPixUpdateNotificationRequest matchingRequest = new PixXADUpdateNotificationRequest(actor, matching, oldXADMatchings);
							PixUpdateNotifier.getInstance().accept(matchingRequest);
						}
					}
				}						
			}
			
			
		} catch (PixManagerException e) {
			throw new PixPdqException(e);
		}

		HL7v231.populateMSA(reply.getMSA(), "AA", hl7Header.getMessageControlId());
    	
		//Forward this PIX Feed (Merge) message to the XDS Registry
		forwardToXdsRegistry(msgIn, patientId);
		
		//Finally, Audit Log PIX Feed Success 
	    auditLog(hl7Header, patient, AuditCodeMappings.EventActionCode.Update);

    	return reply;
	}

	/**
	 * Processes PIX Feed Merge Patient message.
	 * 
	 * @param msgIn the PIX Feed request message
	 * @return a response message for PIX Feed
	 * @throws PixPdqException If Application has trouble
	 * @throws HL7Exception if something is wrong with HL7 message 
	 */
	private Message processMerge(Message msgIn) 
	throws PixPdqException, HL7Exception {

		assert msgIn instanceof ADT_A39;
		
		HL7Header hl7Header = new HL7Header(msgIn);

		//Create Acknowledgment and its Header
		ACK reply = initAcknowledgment(hl7Header);

		//Validate incoming message first
		ADT_A39_PIDPD1MRGPV1 requestId = ((ADT_A39)msgIn).getPIDPD1MRGPV1();
		PatientIdentifier patientId = getPatientIdentifiers(requestId.getPID());
		PatientIdentifier mrgPatientId = getMrgPatientIdentifiers(requestId.getMRG());
		boolean isValidMessage = validateMessage(reply, hl7Header, patientId, mrgPatientId, false);
		if (!isValidMessage) return reply;

		//Invoke eMPI function
		MessageHeader header = hl7Header.toMessageHeader();
		Patient patient = getPatient(msgIn);
		Patient mrgPatient = getMrgPatient(msgIn);
		try {
			//Merge Patients
			List<List<PatientIdentifier>> matchingList = null;
            if (!actor.isNotification()){
                matchingList = pixAdapter.mergePatients(patient, mrgPatient, header);
            }
			//PIX Update Notification to PIX consumers
			if (matchingList != null) {
				for (List<PatientIdentifier> matching : matchingList) {
					IPixUpdateNotificationRequest matchingRequest = 
						new PixUpdateNotificationRequest(actor, matching);
					PixUpdateNotifier.getInstance().accept(matchingRequest);					
				}
			}
		}catch (PixManagerException e) {
			throw new PixPdqException(e);
		}

		HL7v231.populateMSA(reply.getMSA(), "AA", hl7Header.getMessageControlId());

		//Forward this PIX Feed (Merge) message to the XDS Registry
		forwardToXdsRegistry(msgIn, patientId);

		//Finally, Audit Log PIX Feed Success 
	    auditLog(hl7Header, patient, AuditCodeMappings.EventActionCode.Update);
	    auditLog(hl7Header, mrgPatient, AuditCodeMappings.EventActionCode.Delete);

		return reply;
	}

	/**
	 * Audit Logging of PIX Feed message.
	 * 
	 * @param hl7Header the header message from the source application
	 * @param patient the patient to create, update or merged
	 * @param eventActionCode the {@link EventActionCode}
	 */
	private void auditLog(HL7Header hl7Header, Patient patient, AuditCodeMappings.EventActionCode eventActionCode) {
		if (actor.getAuditTrail() == null)
			return;
		
		String userId = hl7Header.getSendingFacility().getNamespaceId() + "|" +
						hl7Header.getSendingApplication().getNamespaceId();
		String messageId = hl7Header.getMessageControlId();
		//TODO: Get the ip address of the source application
		String sourceIp = "127.0.0.1";

		ActiveParticipant source = new ActiveParticipant(userId, messageId, sourceIp);
		
		ParticipantObject patientObj = new ParticipantObject(patient);
		patientObj.addDetail(new TypeValuePair("MSH-10", hl7Header.getMessageControlId()));
		
		actor.getAuditTrail().logPixFeed(source, patientObj, eventActionCode);		
	}
	
	/**
	 * Initiates an acknowledgment instance for the incoming message.
	 * 
	 * @param hl7Header the message header of the incoming message
	 * @return an {@link ACK} instance
	 * @throws HL7Exception if something is wrong with HL7 message 
	 * @throws PixPdqException If Application has trouble
	 */
	private ACK initAcknowledgment(HL7Header hl7Header) throws HL7Exception, 
		PixPdqException {
		//Send Response
		ACK reply = new ACK();
		
		//For the response message, the ReceivingApplication and ReceivingFacility 
		//will become the sendingApplication and sendingFacility;
		//Also the sendingApplication and sendingFacility will become the 
		//receivingApplication and receivingFacility.
		Identifier serverApplication = getServerApplication(actor.getConnection());
		Identifier serverFacility = getServerFacility(actor.getConnection());
		Identifier sendingApplication = hl7Header.getSendingApplication();
		Identifier sendingFacility = hl7Header.getSendingFacility();
		
		try {
			String event = hl7Header.getTriggerEvent();
			HL7v231.populateMSH(reply.getMSH(), "ACK", event, getMessageControlId(), 
				serverApplication, serverFacility, sendingApplication, sendingFacility);
		} catch (IheConfigurationException e) {
			throw new PixPdqException("Error populating MSH segment", e);
		}
		
		return reply;
	}
		
	/**
	 * Validates a patient identifier domain, namely, assigning authority.
	 * 
	 * @param reply the reply message to be populated if the validation fails
	 * @param patientId the patient id
	 * @param incomingMessageId the incoming message id
	 * @return <code>true</code> if the patient domain is validated successfully;
	 *         otherwise <code>false</code>.
	 * @throws HL7Exception if something is wrong with HL7 message 
	 */
	private boolean validateDomain(ACK reply, PatientIdentifier patientId, String incomingMessageId) 
	throws HL7Exception {
		Identifier domain = patientId.getAssigningAuthority();
		boolean domainOk = AssigningAuthorityUtil.validateDomain(
				domain, actor.getActorDescription(), pixAdapter);
		if (!domainOk) {
			HL7v231.populateMSA(reply.getMSA(), "AE", incomingMessageId);
			//segmentId=PID, sequence=1, fieldPosition=3, fieldRepetition=1,componentNubmer=4
			HL7v231.populateERR(reply.getERR(), "PID", "1", "3", "1", "4",
					"204", "Unknown Key Identifier");
			return false;
		}
		return true;
	}
	
	/**
     * Validates the receiving facility and receiving application of an incoming message.
	 * 
     * @param reply the reply message to be populated if any validation is failed
	 * @param receivingApplication the receiving application of the incoming message
	 * @param receivingFacility the receiving facility of the incoming message
	 * @param expectedApplication the expected receiving application
	 * @param expectedFacility the expected receiving facility
	 * @param incomingMessageId the incoming message
	 * @return <code>true</code> if validation is passed;
	 *         otherwise <code>false</code>.
	 * @throws HL7Exception if something is wrong with HL7 message 
	 * @throws PixPdqException if something is wrong with the application
	 */
	private boolean validateReceivingFacilityApplication(ACK reply, Identifier receivingApplication,
			Identifier receivingFacility, Identifier expectedApplication, Identifier expectedFacility,
			String incomingMessageId) 
		    throws HL7Exception, PixPdqException
	{
		//Validate ReceivingApplication and ReceivingFacility.
		//Currently we are not validating SendingApplication and SendingFacility
		boolean validateApplication = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_APPLICATION);
		if (validateApplication && !receivingApplication.equals(expectedApplication)) {
			HL7v231.populateMSA(reply.getMSA(), "AE", incomingMessageId);
			//segmentId=MSH, sequence=1, fieldPosition=5, fieldRepetition=1, componentNubmer=1
			HL7v231.populateERR(reply.getERR(), "MSH", "1", "5", "1", "1",
					null, "Unknown Receiving Application");
			return false;
		}
		
		boolean validateFacility = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_FACILITY);
		if (validateFacility && !receivingFacility.equals(expectedFacility)) {
			HL7v231.populateMSA(reply.getMSA(), "AE", incomingMessageId);
			//segmentId=MSH, sequence=1, fieldPosition=6, fieldRepetition=1, componentNubmer=1
			HL7v231.populateERR(reply.getERR(), "MSH", "1", "6", "1", "1",
					null, "Unknown Receiving Facility");
			return false;
		}
		
		return true;
	}

	/**
	 * Validates the incoming Message in this order:
	 * 
	 * <ul>
	 * <li> Validate Receiving Facility and Receiving Application</li>
	 * <li> Validate Domain </li>
	 * <li> Validate patient Id <li>		 
	 * <li> Validate merge patient Id if applicable<li> 
	 * </ul>
	 * 
     * @param reply the reply message to be populated if any validation is failed
	 * @param hl7Header the message header of the incoming message
	 * @param patientId the id of the patient to be validated
	 * @param mrgPatientId the id of the patient to be merged
	 * @param isPixCreate Whether this validation is for PIX patient creation
	 * @return <code>true</code> if the message is correct; <code>false</code>otherwise.
	 * @throws HL7Exception if something is wrong with HL7 message 
	 * @throws PixPdqException if something is wrong with the application
	 */
	private boolean validateMessage(ACK reply, HL7Header hl7Header, PatientIdentifier patientId, PatientIdentifier mrgPatientId, boolean isPixCreate) 
	throws HL7Exception, PixPdqException {
		Identifier serverApplication = getServerApplication(actor.getConnection());
		Identifier serverFacility = getServerFacility(actor.getConnection());
		Identifier receivingApplication = hl7Header.getReceivingApplication();
		Identifier receivingFacility = hl7Header.getReceivingFacility();
		String incomingMessageId = hl7Header.getMessageControlId();
		//1. validate receiving facility and receiving application
		boolean isValidFacilityApplication = validateReceivingFacilityApplication(reply, 
				receivingApplication, receivingFacility, 
				serverApplication, serverFacility, incomingMessageId);
		if (!isValidFacilityApplication) return false;		
		
		//2.validate the domain
		boolean isValidDomain = validateDomain(reply, patientId, incomingMessageId);
		if (!isValidDomain) return false;
		
		//3. validate ID itself 
		if (!isPixCreate) { 
			//Do not valid patient id for PIX patient creation
			boolean isValidPid = validatePatientId(reply, patientId, hl7Header.toMessageHeader(), false, incomingMessageId);
			if (!isValidPid) return false;
		}
		
		//4. validate mrgPatientId
		if (mrgPatientId != null) {
			boolean isValidMrgPid = validatePatientId(reply, mrgPatientId, hl7Header.toMessageHeader(), true, incomingMessageId);
			if (!isValidMrgPid) return false;
		}
		
		//Finally, it must be true when it reaches here
		return true;
	}

	/**
	 * Checks the given whether the given patient id is a valid patient id.
	 * 
     * @param reply the reply message to be populated if any validation is failed
	 * @param patientId the patient id to be checked
	 * @param header the incoming message header 
	 * @param isMrgPatientId whether the patient id to be checked is a merge patient id.
	 * @param incomingMessageId the incoming message id.
	 * @return <code>true</code> if the patientId is valid; otherwise <code>false</code>.
	 * @throws HL7Exception if something is wrong with HL7 message 
	 * @throws PixPdqException if something is wrong with the application
	 */
	private boolean validatePatientId(ACK reply, PatientIdentifier patientId, 
			MessageHeader header, boolean isMrgPatientId, String incomingMessageId)
	throws HL7Exception, PixPdqException{
		boolean validPatient;
		try {
			validPatient = pixAdapter.isValidPatient(patientId, header);
		} catch (PixManagerException e) {
			throw new PixPdqException(e);
		}
		if (!validPatient) {
			HL7v231.populateMSA(reply.getMSA(), "AE", incomingMessageId);
			if (isMrgPatientId){
				//segmentId=MRG, sequence=1, fieldPosition=1, fieldRepetition=1, componentNubmer=1
				HL7v231.populateERR(reply.getERR(), "MRG", "1", "1", "1", "1",
						"204", "Unknown Key Identifier");
			} else {
				//segmentId=PID, sequence=1, fieldPosition=3, fieldRepetition=1, componentNubmer=1
				HL7v231.populateERR(reply.getERR(), "PID", "1", "3", "1", "1",
						"204", "Unknown Key Identifier");
			}
		}
		return validPatient;
	}
	
	/**
	 * Converts a PIX Feed Patient message to a {@link Patient} object.
	 * 
	 * @param msgIn the incoming PIX Feed message
	 * @return a {@link Patient} object
	 * @throws PixPdqException if something is wrong with the application
	 */
	private Patient getPatient(Message msgIn) throws PixPdqException,HL7Exception {
		HL7v231ToBaseConvertor convertor = null;
		if (msgIn.getVersion().equals("2.3.1")) {
			convertor = new HL7v231ToBaseConvertor(msgIn, actor.getActorDescription(), pixAdapter);
		} else {
			throw new PixPdqException("Unexpected HL7 version");
		}
		Patient patientDesc = new Patient();
		patientDesc.setPatientIds(convertor.getPatientIds());
		patientDesc.setPatientName(convertor.getPatientName());
		patientDesc.setMothersMaidenName(convertor.getMotherMaidenName());
		patientDesc.setBirthDateTime(convertor.getBirthDate());
		patientDesc.setAdministrativeSex(convertor.getSexType());
		patientDesc.setPatientAliases(convertor.getPatientAliases());
		patientDesc.setRace(convertor.getRace());
		patientDesc.setPrimaryLanguage(convertor.getPrimaryLanguage());
		patientDesc.setMaritalStatus(convertor.getMaritalStatus());
		patientDesc.setReligion(convertor.getReligion());
		patientDesc.setPatientAccountNumber(convertor.getpatientAccountNumber());
		patientDesc.setSsn(convertor.getSsn());
		patientDesc.setDriversLicense(convertor.getDriversLicense());
		patientDesc.setMothersId(convertor.getMothersId());
		patientDesc.setEthnicGroup(convertor.getEthnicGroup());
		patientDesc.setBirthPlace(convertor.getBirthPlace());
		patientDesc.setBirthOrder(convertor.getBirthOrder());
		patientDesc.setCitizenship(convertor.getCitizenShip());
		patientDesc.setDeathDate(convertor.getDeathDate());
		patientDesc.setDeathIndicator(convertor.getDeathIndicator());
		patientDesc.setPhoneNumbers(convertor.getPhoneList());
		patientDesc.setAddresses(convertor.getAddressList());
		patientDesc.setVisits(convertor.getVisitList());
		patientDesc.setVipIndicator(convertor.getVipIndicator());
        patientDesc.setNextOfKin(convertor.getNextOfKin());
		return patientDesc;
	}
	
	

	/**
	 * Extracts the merge patient out of a PIX Merge Patient message.
	 * 
	 * @param msgIn the incoming PIX Merge message
	 * @return a {@link Patient} object that represents the merge patient
	 * @throws HL7Exception if something is wrong with the application
	 */
	private Patient getMrgPatient(Message msgIn) throws HL7Exception {
		HL7v231ToBaseConvertor convertor = null;		
		convertor = new HL7v231ToBaseConvertor(msgIn, actor.getActorDescription(), pixAdapter);
		Patient patientDesc = new Patient();
		patientDesc.setPatientIds(convertor.getMrgPatientIds());
		patientDesc.setPatientName(convertor.getMrgPatientName());
		patientDesc.setPatientAccountNumber(convertor
				.getMrgpatientAccountNumber());
		patientDesc.setVisits(convertor.getMrgVisitList());
		return patientDesc;
	}

	/**
	 * Gets the patient identifier from a Patient PID segment.
	 * 
	 * @param pid the PID segment
	 * @return a {@link PatientIdentifier}
	 */
	private PatientIdentifier getPatientIdentifiers(PID pid) {
		PatientIdentifier identifier = new PatientIdentifier();
		CX[] cxs = pid.getPatientIdentifierList();
		for (CX cx : cxs) {
			Identifier assignAuth = new Identifier(cx.getAssigningAuthority()
					.getNamespaceID().getValue(), cx.getAssigningAuthority()
					.getUniversalID().getValue(), cx.getAssigningAuthority()
					.getUniversalIDType().getValue());
			Identifier assignFac = new Identifier(cx.getAssigningFacility()
					.getNamespaceID().getValue(), cx.getAssigningFacility()
					.getUniversalID().getValue(), cx.getAssigningFacility()
					.getUniversalIDType().getValue());
			identifier.setAssigningAuthority(AssigningAuthorityUtil.reconcileIdentifier(assignAuth, actor.getActorDescription(), pixAdapter));
			identifier.setAssigningFacility(assignFac);
			identifier.setId(cx.getID().getValue());
			identifier.setIdentifierTypeCode(cx.getIdentifierTypeCode()
					.getValue());
		}
		return identifier;
	}

	/**
	 * Gets the merge patient identifier out of a MRG segment.
	 * 
	 * @param MRG segment the merge segment
	 * @return a {@link PatientIdentifier} 
	 */
	private PatientIdentifier getMrgPatientIdentifiers(MRG mrg) {
		PatientIdentifier identifier = new PatientIdentifier();
		CX[] cxs = mrg.getPriorPatientIdentifierList();
		for (CX cx : cxs) {
			Identifier assignAuth = new Identifier(cx.getAssigningAuthority()
					.getNamespaceID().getValue(), cx.getAssigningAuthority()
					.getUniversalID().getValue(), cx.getAssigningAuthority()
					.getUniversalIDType().getValue());
			Identifier assignFac = new Identifier(cx.getAssigningFacility()
					.getNamespaceID().getValue(), cx.getAssigningFacility()
					.getUniversalID().getValue(), cx.getAssigningFacility()
					.getUniversalIDType().getValue());
			identifier.setAssigningAuthority(AssigningAuthorityUtil.reconcileIdentifier(assignAuth, actor.getActorDescription(), pixAdapter));
			identifier.setAssigningFacility(assignFac);
			identifier.setId(cx.getID().getValue());
			identifier.setIdentifierTypeCode(cx.getIdentifierTypeCode().getValue());
		}
		return identifier;
	}
		
	/**
	 * Forwards this PIX Feed message to the XDS Registry in the affinity domain. The XDS registry
	 * in the affinity domain is interested in patient IDs in only the global (master) assigning
	 * authority (domain). So messages for non-global patient IDs are filtered out. Also, be sure
	 * to configure XDS Registry connection in the Actor configuration. See the relevant actor 
	 * configuration documentation.
	 * 
	 * @param msgIn the incoming PIX Feed message to be forwarded to the XDS Registry 
	 * @param patientId the ID of the patient of PIX Feed. For patient creation
	 * and update, it is the main patient ID; for patient merge, it is the surviving
	 * patient ID.
	 */
	private void forwardToXdsRegistry(Message msgIn, PatientIdentifier patientId) {
		//Ignore it if XDS registry is not configured
		IConnectionDescription registryConnection = actor.getXdsRegistryConnection();
		if (registryConnection == null) 
			return ;
		
			
		MessageStore store = null;
		try {
			//Forward to XDS Registry only those messages associated with global patients
			Identifier defaultGlobalDomain = Configuration.getGlobalDomain(actor.getActorDescription(), false);			
			Identifier globalDomain = pixAdapter.getGlobalDomainIdentifier(defaultGlobalDomain);
						
			if ( !patientId.getAssigningAuthority().equals(globalDomain) ) 
				return ;
			
			log.info("Forward the PIX Feed to the XDS Registry"+ registryConnection);		            

			store = MessageStoreHelper.initMessageStore(msgIn, actor.getStoreLogger(), false);
			HL7Header header = new HL7Header(msgIn);
			header.populateMessageStore(store);

			HL7Channel channel = new HL7Channel(registryConnection);
			Message ack = channel.sendMessage(msgIn);
		    boolean ok = processPixFeedResponse(ack, registryConnection);
		    
		} catch(Exception e) {
			String errorMsg = "Cannot send PIX Feed to XDS Registry: " + registryConnection.getDescription();
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
	
	
  /**
   * Checks the response to the patient identity feed to ensure that it was
   * a success.
   * 
   * @param response the response from the patient identity feed consumer
   * @param connection the connection from which the response is from
   * @return <code>true</code> if the PIX Feed message was accepted
   */
   private boolean processPixFeedResponse(Message response, IConnectionDescription connection) throws PixManagerException {
		// Make sure the response is the right type of message
		ACK message = null;
		if (response instanceof ACK) {
			message = (ACK) response;
		} else {
			actor.logHL7MessageError(log, message, "Unexpected response");
			throw new PixManagerException("Unexpected response from \"" + connection.getDescription());
		}
		// Check the MSA segment ...
		String status = message.getMSA().getAcknowledgementCode().getValue();
		if ((status == null) || (!status.equalsIgnoreCase("AA") && !status.equalsIgnoreCase("CA"))) {
			// The server has rejected our request, or generated an error
			String mtext = message.getMSA().getTextMessage().getValue();
			String code = message.getMSA().getErrorCondition().getIdentifier().getValue();
			String etext = message.getMSA().getErrorCondition().getText().getValue();
			String error = null;
			if (code != null) error = "(" + code + ") " + HL7v231.getErrorString(code);
			if (mtext != null) error = error + " - " + mtext;
			if (etext != null) error = " [" + etext + "]";
            if (error == null) {
            	message.getERR();
            	ca.uhn.hl7v2.model.v231.segment.ERR err = message.getERR();
              if (err != null) {
                  // Message = err.getMessage().get;
                   try {
                       ELD eld = err.getErrorCodeAndLocation(0);
                       if (eld != null) {
                           CE ce = eld.getCodeIdentifyingError();
                           if (ce != null) {
                               ST errorcode = ce.getIdentifier();
                               if (errorcode != null) {
                                   error = "(" + errorcode.getValue() + ") " + HL7v231.getErrorString(errorcode.getValue());
                               }
                               ST text = ce.getText();
                               if (text != null) error = error + "-" + text.getValue();
                           }
                       }
                   } catch (HL7Exception e) { //do nothing if we cannot get anything from ERR.
                   }
               }
            }
            if (error == null) error ="Unspecified error";
            
			error = "Error response from \"" + connection.getDescription() + "\": " + error; 
			actor.logHL7MessageError(log, message, error);
			throw new PixManagerException(error);
		}
		// Okay, we're good
		return true;
	}
   
	/**
	 * Send an HL7 message over this channel to the XDS Registry and
	 * return the response.
	 * 
	 * @param message The HL7 message to be sent
	 * @return The HL7 message that came back as a response
	 * @throws IOException When there is a problem communicating with the server
	 */
	public Message sendMessage(Message message) throws IOException {
			// Open the XDS Registry TCP connection
			IConnection conn = ConnectionFactory.getConnection(actor.getXdsRegistryConnection());
			if (!conn.isConnectionValid()) {
				throw new IOException("Cannot open XDS Registry connection to \"" + actor.getXdsRegistryConnection().getDescription() + "\"");
			}
			// Create the HL7 connection using this TCP connection
			try {
                PipeParser parser = new PipeParser();
                parser.setValidationContext(new MessageValidation());
				hl7Connection = new Connection(parser, new MinLowerLayerProtocol(), conn.getSocket());
			} catch (LLPException e) {
				// Error in HAPI configuration
				log.error("Cannot find HL7 low level protocol implementation", e);
				conn.closeConnection();
				return null;
			} catch (IOException e) {
				// Error communicating over socket
				conn.closeConnection();
				throw e;
			}
		// Get an initator to send the message
		Initiator initiator = hl7Connection.getInitiator();
		// Okay, send the message
		Message response = null;
		try {
			response = initiator.sendAndReceive(message);
		} catch (HL7Exception e) {
			// Improper HL7 message formatting (on send or receive)
           
           log.error("Improper HL7 message formatting", e);
			hl7Connection.close();
			hl7Connection = null;
			return null;
		} catch (LLPException e) {
			// Can't use Lower Level Protocol for some reason
			log.error("HL7 protocol error", e);
			hl7Connection.close();
			hl7Connection = null;
			return null;
		} catch (IOException e) {
			// Can't communicate over socket
			hl7Connection.close();
			hl7Connection = null;
			throw e;
		}
			hl7Connection.close();
			hl7Connection = null;
		return response;
	}
	
}
