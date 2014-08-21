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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.audit.TypeValuePair;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.MessageHeader;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openexchange.utils.ExceptionUtil;
import org.openhealthtools.openpixpdq.api.IJMXEventNotifier;
import org.openhealthtools.openpixpdq.api.IPixManagerAdapter;
import org.openhealthtools.openpixpdq.api.MessageStore;
import org.openhealthtools.openpixpdq.api.PixManagerException;
import org.openhealthtools.openpixpdq.common.AssigningAuthorityUtil;
import org.openhealthtools.openpixpdq.common.BaseHandler;
import org.openhealthtools.openpixpdq.common.Constants;
import org.openhealthtools.openpixpdq.common.PixPdqException;
import org.openhealthtools.openpixpdq.common.PixPdqFactory;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7Header;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7Util;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7v25;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.GenericComposite;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.QBP_Q21;
import ca.uhn.hl7v2.model.v25.message.RSP_K23;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.QPD;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;


/**
 * This class processes PIX Query message in HL7 v2.5 format. It 
 * handles the PIX Query transaction of the PIX profile.  
 * The supported message type includes QBP^Q23.
 * 
 * @author Wenzhi Li 
 * @version 1.0 Oct 22, 2008
 * 
 */
class PixQueryHandler extends BaseHandler implements Application {

    private static Logger log = Logger.getLogger(PixQueryHandler.class);
	
	private PixManager actor = null;
	private IPixManagerAdapter pixAdapter = null;
	private IJMXEventNotifier eventBean = null;

	/**
	 * Constructor
	 * 
	 * @param actor the {@link PixManager} actor
	 */
	PixQueryHandler(PixManager actor) {
		super();
		this.actor = actor;
		this.pixAdapter = PixPdqFactory.getPixManagerAdapter();
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
         if (theIn instanceof QBP_Q21)
            return true;
         else
            return false;
    }

    /**
     * Processes the incoming PIX Query Message. Valid message  
     * is QBP^Q23.
     * 
     * @param msgIn the incoming message
     */
    public Message processMessage(Message msgIn) throws ApplicationException, HL7Exception {

          //String encodedMessage = new PipeParser().encode(msgIn);
          //log.info("Received message:\n" + encodedMessage + "\n\n");
   		Message retMessage = null;
    	MessageStore store = MessageStoreHelper.initMessageStore(msgIn, actor.getStoreLogger(), true);

    	try {
    		HL7Header hl7Header = new HL7Header(msgIn);
    		
    		//Populate MessageStore to persist the message
    		hl7Header.populateMessageStore(store);
    		
	        if(msgIn instanceof QBP_Q21) {
	            QBP_Q21 message = (QBP_Q21)msgIn;
	            retMessage = processQuery( message );
	        } else {
	           throw new ApplicationException( "Unexpected PIX Query to PIX Manager server. Valid message is QBP^Q23.");
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
            if (eventBean != null) {
				eventBean.notifyMessageReceived();
            }
		}
		return retMessage;

    }
    
	/**
	 * Processes the incoming PIX Query message.
	 * 
	 * @param msgIn the PIX Query request message
	 * @return a response message for PIX Query
	 * @throws PixPdqException If Application has trouble
	 * @throws HL7Exception if something is wrong with HL7 message 
	 */
    Message processQuery(QBP_Q21 msgIn) 
    throws PixPdqException, HL7Exception {
    
    	HL7Header hl7Header = new HL7Header(msgIn);    	

        String messageControlId = hl7Header.getMessageControlId();    
        String event = hl7Header.getTriggerEvent();
        if (event.equals("Q23") ) {
            //Q23 is for PIX Query: OK, we will continue process the message
        } else {
            throw new PixPdqException("Unexpeced HL7 event. The supported event is Q23.");
        }

        //Process QPD
        QPD qpd = msgIn.getQPD();	
        String queryTag = qpd.getQueryTag().getValue();
        //Get the Patient ID
        GenericComposite composite = (GenericComposite)qpd.getUserParametersInsuccessivefields().getData();

        PatientIdentifier requestPatientId = HL7v25.extractId(composite, actor.getActorDescription(), pixAdapter );

        //Init response and generate its header 
        RSP_K23 reply = initResponse(hl7Header);
        
        Terser inTerser = new Terser( msgIn );
        Terser outTerser = new Terser( reply );
        boolean isValid = validateReceivingFacilityApplication(reply, hl7Header, queryTag);
        if (!isValid) {
           HL7Util.echoQPD(outTerser, inTerser);    	   
    	   return reply;
        }       
  
        //Process ID in the following 5 scenarios:See ITI Technical Framework Section 3.9.4.2.2.6
        //1. validate request ID domain  (Case #4 in PIX specs request ID Domain is not valid)
        //2. validate request ID itself  (Case #3 in PIX specs request ID is not valid)
        //3. validate return ID domain (Case #5 in PIX specs return ID domain is not valid)
        //4. Found ID            (Case #1 and Case #6 in PIX specs: ID found excluding request ID)
        //5. No ID found         (Case #2 in PIX specs: ID is not found)

        //1. validate ID domain  (Case #4 in PIX specs request ID Domain is not valid)
        Identifier requestDomain = (Identifier)requestPatientId.getAssigningAuthority();
        boolean requestDomainOk = AssigningAuthorityUtil.validateDomain( requestDomain, actor.getActorDescription(), pixAdapter);
        if (!requestDomainOk) {
            HL7v25.populateMSA(reply.getMSA(), "AE", messageControlId);
            HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
            HL7Util.echoQPD(outTerser, inTerser);
            //segmentId=QPD, sequence=1, fieldPosition=3, fieldRepetition=1, componentNubmer=4
            HL7v25.populateERR(reply.getERR(), "QPD", "1", "3", "1", "4", "204", "Unknown Key Identifier");
            return reply;
        }

        //2. validate ID itself  (Case #3 in PIX specs request ID is not valid)
		MessageHeader header = hl7Header.toMessageHeader();
		boolean validPatient;
		try {
			validPatient = pixAdapter.isValidPatient( requestPatientId,  header );
		} catch (PixManagerException e) {
			throw new PixPdqException(e);
		}
        if ( !validPatient ) {
            HL7v25.populateMSA(reply.getMSA(), "AE", messageControlId);
            HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
            HL7Util.echoQPD(outTerser, inTerser);
            //segmentId=QPD, sequence=1, fieldPosition=3, fieldRepetition=1, componentNubmer=1
            HL7v25.populateERR(reply.getERR(), "QPD", "1", "3", "1", "1", "204", "Unknown Key Identifier");
            return reply;
        }

        //3. Validate return ID domains (Case #5 in PIX specs return ID domain is valid or not)
        List<Identifier> returnDomains = getReturnDomain(qpd, reply, 
        		hl7Header.getMessageControlId(), queryTag, outTerser, inTerser);
        if (returnDomains == null) {
        	//if return domains is null, then it means at least one return domain is not recognized.
        	//So just stop with a reply which contains an error message.
        	return reply;
        }
        
        //4. Found ID (Case #1 and Case #6 in PIX specs: ID found excluding request ID)
        //5. No ID found         (Case #2 in PIX specs: ID is not found)
        //AA (application accept) is returned in MSA-1.
        //OK (data found, no errors) is returned in QAK-2.

        //Invoke eMPI function
        List<PatientIdentifier> ids = null;
        try {
       	    ids = pixAdapter.findPatientIds( requestPatientId, header );       	 
        }catch(PixManagerException e) {
        	throw new PixPdqException(e);
        }
                
        if (ids.size() >= 1) {
        	List<PatientIdentifier> finalIds = null; 
        	if (returnDomains.size() == 0) {
        		finalIds = ids;
        	} else {
        		finalIds = new ArrayList<PatientIdentifier>();
	            for (PatientIdentifier pid : ids) {
	            	//filter out patient id by return domains            
	                if (returnDomains.contains( pid.getAssigningAuthority() )) {
	                	finalIds.add( pid );
	                }                	  
	            }
        	}
        	
            Map<Identifier, List<String>> map = new HashMap<Identifier, List<String>>();
            for (PatientIdentifier pid : finalIds) {
                 //authority might be partial (either namespaceId or universalId),
                 //so need to map to the one used in the configuration. 
                 Identifier authority = AssigningAuthorityUtil.reconcileIdentifier( 
                		 pid.getAssigningAuthority(), actor.getActorDescription(), pixAdapter );
                 //In case of multiple IDs for a certain authority, we need to group them together.
                 List<String> idList = map.get( authority );
                 if (idList == null) {
                     idList = new ArrayList<String>();
                     map.put( authority, idList);
                 }
                 idList.add( pid.getId() );
            }

            HL7v25.populateMSA(reply.getMSA(), "AA", messageControlId);
            HL7Util.echoQPD(outTerser, inTerser);
            if (finalIds.size() == 0) {
            	HL7v25.populateQAK(reply.getQAK(), queryTag, "NF");
            }else {
            	HL7v25.populateQAK(reply.getQAK(), queryTag, "OK");

	            PID pid = reply.getQUERY_RESPONSE().getPID();
	            Set<Identifier> authorities = map.keySet();
	            int index = 0;
	            for (Identifier authority : authorities) {
	                List<String> idNumbers = map.get( authority );
	                for (String idNumber : idNumbers) {
	                    CX patientIdentifier = pid.getPatientIdentifierList(index++);
	                    patientIdentifier.getIDNumber().setValue( idNumber );
	                    patientIdentifier.getAssigningAuthority().getNamespaceID().setValue( authority.getNamespaceId() );
	                    patientIdentifier.getAssigningAuthority().getUniversalID().setValue( authority.getUniversalId() );
	                    patientIdentifier.getAssigningAuthority().getUniversalIDType().setValue( authority.getUniversalIdType() );
	                    patientIdentifier.getIdentifierTypeCode().setValue("PI");
	                    //For the second repetition, set the Name Type code to be "S"
	                    pid.getPatientName(0).getGivenName().setValue("");
	                    pid.getPatientName(1).getNameTypeCode().setValue("S");
	                }
	            }
            }
        } else {
            HL7v25.populateMSA(reply.getMSA(), "AA", messageControlId);
            HL7v25.populateQAK(reply.getQAK(), queryTag, "NF");
            HL7Util.echoQPD(outTerser, inTerser);
        }

        //Audit Log
        auditLog(ids, hl7Header, queryTag, msgIn);
        
        return reply;
    }
    
    /**
     * Audit Logging of PDQ Query Message.
     * 
     * @param patientIds the list of patient Identifiers to log
     * @param hl7Header the message header from the request
     * @param queryTag the query tag from the MSA segment of the PIX Query message
     * @param qpd the QPD segment of the PIX Query message
     */
    private void auditLog(List<PatientIdentifier> patientIds, HL7Header hl7Header, String queryTag, QBP_Q21 message) {
        if (actor.getAuditTrail() == null)
        	 return;
    
		//Source Application
    	String userId = hl7Header.getSendingFacility().getNamespaceId() + "|" +
						hl7Header.getSendingApplication().getNamespaceId();
		String messageId = hl7Header.getMessageControlId();
		//TODO: Get the ip address of the source application
		String sourceIp = "127.0.0.1";
		ActiveParticipant source = new ActiveParticipant(userId, messageId, sourceIp);
		//Patient Info
		ParticipantObject po = new ParticipantObject();
		po.setId(patientIds);
		//Query Info
		ParticipantObject queryObj = new ParticipantObject();
		queryObj.setId(queryTag);
		try {
			queryObj.setQuery(PipeParser.encode(message, new EncodingCharacters('|', "^~\\&")));
		} catch (HL7Exception e) {
			log.warn("Failed to encode PIX query for auditting", e);
		} 
		queryObj.addDetail(new TypeValuePair("MSH-10", messageId));		
	
		//Finally Log it.
		actor.getAuditTrail().logPixQuery(source, po, queryObj);
    }
    
    /**
     * Initiates a PIX Query response instance. 
     * 
     * @param hl7Header the message header of the incoming message
     * @return a PIX Query response instance
    * @throws HL7Exception if something is wrong with the HL7 message format
    * @throws PixPdqException if something is wrong with the application
     */
	private RSP_K23 initResponse(HL7Header hl7Header) throws HL7Exception, 
	 PixPdqException {
        RSP_K23 reply = new RSP_K23();
 
        //For the response message, the ReceivingApplication and ReceivingFacility 
		//will become the sendingApplication and sendingFacility;
		//Also the sendingApplication and sendingFacility will become the 
		//receivingApplication and receivingFacility.
        Identifier serverApplication = getServerApplication(actor.getConnection());
        Identifier serverFacility = getServerFacility(actor.getConnection());
        Identifier idSendingApplication   = hl7Header.getSendingApplication();
        Identifier idSendingFacility      = hl7Header.getSendingFacility();

        HL7v25.populateMSH(reply.getMSH(), "RSP", "K23", "RSP_K23", getMessageControlId(), serverApplication, serverFacility, idSendingApplication, idSendingFacility );
		return reply;
	}

   /**
    * Validates the receiving facility and receiving application of an incoming message.
    * 
    * @param reply the reply message to be populated if a validation is failed
    * @param hl7Header the incoming message header
    * @param queryTag the query tag of the incoming PDQ query message
    * @return <code>true</code> if validation is passed; otherwise <code>false</code>.
    * @throws HL7Exception if anything is wrong with the HL7 message format
    * @throws PixPdqException if anything is wrong the application
    */
	private boolean validateReceivingFacilityApplication(RSP_K23 reply, HL7Header hl7Header, String queryTag) 
	throws HL7Exception, PixPdqException {

		Identifier receivingApplication = hl7Header.getReceivingApplication();
        Identifier receivingFacility    = hl7Header.getReceivingFacility();
        
        //Validate ReceivingApplication and ReceivingFacility.
        //Currently we are not validating SendingApplication and SendingFacility
		boolean validateApplication = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_APPLICATION);
		if (validateApplication && !receivingApplication.equals(getServerApplication(actor.getConnection())) ) {
            String incomingMessageId = hl7Header.getMessageControlId();
            HL7v25.populateMSA(reply.getMSA(), "AE", incomingMessageId);
            HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
            //segmentId=MSH, sequence=1, fieldPosition=5, fieldRepetition=1, componentNubmer=1
            HL7v25.populateERR(reply.getERR(), "MSH", "1", "5", "1", "1", null, "Unknown Receiving Application");
            
            return false;
		}
		
		boolean validateFacility = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_FACILITY);
		if (validateFacility && !receivingFacility.equals(getServerFacility(actor.getConnection())) ) {
            String incomingMessageId = hl7Header.getMessageControlId();
            HL7v25.populateMSA(reply.getMSA(), "AE", incomingMessageId);
            HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
            //segmentId=MSH, sequence=1, fieldPosition=6, fieldRepetition=1, componentNubmer=1
            HL7v25.populateERR(reply.getERR(), "MSH", "1", "6", "1", "1", null, "Unknown Receiving Facility");
            
            return false;
		}

        return true;
	}
	
    /**
     * Gets the return domain from the original PIX Query request.
     *
     * @param qpd the request QPD segment
     * @param reply the response message
     * @param messageControlId the messageControlId from the incoming message (MSH-10)
     * @param queryTag the queryTag of the request message
     * @param outTerser the out response message terser
     * @param inTerser the in request message terser
     * @return return a list of returnDomain if not error. Otherwise, return null if there is an invalid domain or request syntax cannot be recognized.
     * @throws HL7Exception if anything is wrong with the HL7 message format
     */
    private List<Identifier> getReturnDomain(QPD qpd, RSP_K23 reply, String messageControlId, String queryTag, Terser outTerser, Terser inTerser ) throws HL7Exception {
        //1. validate the return domains.
       //Get the return domains
       Type[] typeDomains = qpd.getField(4);
       List<Identifier> returnDomains = new ArrayList<Identifier>();
       for (int i=0; i<typeDomains.length; i++) {
           Varies domain = (Varies)typeDomains[i];
           Type data = domain.getData();
           if (data instanceof GenericComposite) {
               Identifier aa = HL7v25.extractAssigningAuthority( (GenericComposite)data );
               boolean validDomain = AssigningAuthorityUtil.validateDomain( aa, actor.getActorDescription(), pixAdapter);
               if (validDomain) {
                   Identifier reconciledDomain = AssigningAuthorityUtil.reconcileIdentifier(aa, actor.getActorDescription(), pixAdapter);
                   returnDomains.add( reconciledDomain );
               }
               else {
                   //if a domain is invalid, return an error message
                   HL7v25.populateMSA(reply.getMSA(), "AE", messageControlId);
                   HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
                   HL7Util.echoQPD(outTerser, inTerser);
                   //segmentId=QPD, sequence=1, fieldPosition=4, fieldRepetition=i+1, componentNubmer has to be empty
                   HL7v25.populateERR(reply.getERR(), "QPD", "1", "4", Integer.toString(i+1), null, "204", "Unknown Key Identifier");
                   return null;
               }
           } else {
               //return "Data type error"
               HL7v25.populateMSA(reply.getMSA(), "AE", messageControlId);
               HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
               HL7Util.echoQPD(outTerser, inTerser);
               //segmentId=QPD, sequence=1, fieldPosition=4, fieldRepetition=null, componentNubmer=null
               HL7v25.populateERR(reply.getERR(), "QPD", "1", "4", null, null, "102", "Data type error");
               return null;
           }
        }
        return returnDomains;
    }

}
