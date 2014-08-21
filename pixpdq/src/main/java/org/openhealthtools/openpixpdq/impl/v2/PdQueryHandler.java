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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import ca.uhn.hl7v2.model.v25.message.*;
import ca.uhn.hl7v2.model.v25.segment.*;
import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.audit.TypeValuePair;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.datamodel.Address;
import org.openhealthtools.openexchange.datamodel.DriversLicense;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.MessageHeader;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openexchange.datamodel.PersonName;
import org.openhealthtools.openexchange.datamodel.PhoneNumber;
import org.openhealthtools.openexchange.datamodel.SharedEnums;
import org.openhealthtools.openexchange.datamodel.SharedEnums.PhoneType;
import org.openhealthtools.openexchange.utils.DateUtil;
import org.openhealthtools.openexchange.utils.ExceptionUtil;
import org.openhealthtools.openexchange.utils.StringUtil;
import org.openhealthtools.openpixpdq.api.IJMXEventNotifier;
import org.openhealthtools.openpixpdq.api.IPdSupplierAdapter;
import org.openhealthtools.openpixpdq.api.MessageStore;
import org.openhealthtools.openpixpdq.api.PatientException;
import org.openhealthtools.openpixpdq.api.PdSupplierException;
import org.openhealthtools.openpixpdq.api.PdqQuery;
import org.openhealthtools.openpixpdq.api.PdqResult;
import org.openhealthtools.openpixpdq.common.AssigningAuthorityUtil;
import org.openhealthtools.openpixpdq.common.BaseHandler;
import org.openhealthtools.openpixpdq.common.Constants;
import org.openhealthtools.openpixpdq.common.ContinuationPointer;
import org.openhealthtools.openpixpdq.common.PixPdqException;
import org.openhealthtools.openpixpdq.common.PixPdqFactory;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7Header;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7Util;
import org.openhealthtools.openpixpdq.impl.v2.hl7.HL7v25;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.GenericComposite;
import ca.uhn.hl7v2.model.GenericPrimitive;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v25.datatype.CE;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.datatype.HD;
import ca.uhn.hl7v2.model.v25.datatype.XAD;
import ca.uhn.hl7v2.model.v25.datatype.XPN;
import ca.uhn.hl7v2.model.v25.datatype.XTN;
import ca.uhn.hl7v2.model.v25.group.RSP_K21_QUERY_RESPONSE;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

/**
 * This class processes PDQ query message in HL7 v2.5 format. It 
 * handles the PDQ transaction of the PDQ profile.  
 * The supported message type includes QBP^Q22 and QCN^J01.
 *   
 * @author Wenzhi Li
 * @version 1.0, Oct 23, 2008
 *
 */
class PdQueryHandler extends BaseHandler implements Application {
    /* Logger for problems */
    private static Logger log = Logger.getLogger(PdSupplier.class);

    /** The connection description of the actor for this handler */
	private IConnectionDescription connection;

	private PdSupplier actor = null;
    private IPdSupplierAdapter pdqAdapter = null;
    private IJMXEventNotifier eventBean = null;
    /** Used to store continuation pointer  <String(pointer), ContinuationPointer> */
    private static Hashtable<String, ContinuationPointer> dscMap = new Hashtable<String, ContinuationPointer>();
    
    /**
     * Constructor 
     * 
     * @param actor the {@link PdSupplier} actor
     */
    PdQueryHandler(PdSupplier actor) {
    	super();
    	this.actor = actor;
        this.pdqAdapter = PixPdqFactory.getPdSupplierAdapter();
        this.eventBean = actor.getPixEvent();
        this.connection = actor.getConnection();
		assert this.connection != null;
		assert this.pdqAdapter != null;        
    }

    /**
     * Whether a incoming message can be processed by this handler.
     * 
     * @return <code>true</code> if the incoming message can be processed;
     * otherwise <code>false</code>.
     */
    public boolean canProcess(Message theIn) {
         if (theIn instanceof QBP_Q21 || theIn instanceof QCN_J01)
            return true;
         else
            return false;
    }

    /**
     * Processes the incoming PDQ Query Message. Valid messages 
     * are QBP^Q22 and QCN^J01.
     * 
     * @param msgIn the incoming PDQ query message
     */
    public Message processMessage(Message msgIn) throws ApplicationException, HL7Exception {
   		Message retMessage = null;
    	MessageStore store = MessageStoreHelper.initMessageStore(msgIn, actor.getStoreLogger(), true);
   		try {
   			HL7Header hl7Header = new HL7Header(msgIn);
   			
   			//Populate MessageStore to persist the message
   			hl7Header.populateMessageStore(store);
   			
            if (msgIn instanceof QBP_Q21) {
            	retMessage = processQuery(msgIn);
            }
            else if (msgIn instanceof QCN_J01) {
            	retMessage = processQCN_J01((QCN_J01)msgIn);
            }
            else if (msgIn instanceof QRY_A19 ||
                    msgIn instanceof QRY_Q01 ||
                    msgIn instanceof QRY_Q02) {
              retMessage = processQRY_A19(msgIn);
            }
            else {
                throw new ApplicationException( "Unexpected request to PD Supplier server. Valid messages are QBP/^Q22 and QCN/^J01");
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
   * This is method to process in coming QRY_A19 message
   * and return Message ADR_A19
   * @param msgIn
   * @return Message (ADR_A19)
   * @throws PixPdqException
   * @throws HL7Exception
   */
  private Message processQRY_A19(Message msgIn) throws PixPdqException, HL7Exception{
    HL7Header hl7Header = new HL7Header(msgIn);
    ADR_A19 replyMessage = new ADR_A19();
    processMSH(hl7Header, replyMessage.getMSH(),
            "ADR", "A19", "ADR_A19", getServerApplication(connection), getServerFacility(connection));

    // QRY_A19 as in message
    QRY_Q01 message = (QRY_Q01)msgIn;

    QRD qrd = message.getQRD();

    String queryTag = qrd.getQueryFormatCode().getValue();
    Terser inTerser = new Terser( message );
    Terser outTerser = new Terser( replyMessage );

    //Validate the incoming message first
    /*boolean isValidMessage = validateQRYA19Message(replyMessage, hl7Header, queryTag, inTerser, outTerser);
*/


    return replyMessage;
  }



//  /**
//   *
//   * @param replyMessage
//   * @param hl7Header
//   * @param queryTag
//   * @param inTerser
//   * @param outTerser
//   * @return boolean if
//   */
//  private boolean validateQRYA19Message(ADR_A19 replyMessage, HL7Header hl7Header, String queryTag, Terser inTerser, Terser outTerser)
//          throws HL7Exception {
//    //1. Valid triggering event:Q22 is for PDQ Query
//    if ( !hl7Header.getTriggerEvent().equals("") ) {
//      HL7v25.populateMSA(replyMessage.getMSA(), "AE", hl7Header.getMessageControlId());
//      HL7v25.populateQAK(replyMessage.getQAK(), queryTag, "AE");
//      HL7Util.echoQPD(outTerser, inTerser);
//      //segmentId=MSH, sequence=1, fieldPosition=9, fieldRepetition=1, componentNubmer=2
//      HL7v25.populateERR(replyMessage.getERR(), "MSH", "1", "9", "1", "2", "201", "Unsupported event code");
//      return false;
//    }
//    //2. Validate receiving facility and receiving application
//    boolean isValid = validateReceivingFacilityApplication(replyMessage, hl7Header, queryTag);
//    if (!isValid) {
//      HL7Util.echoQPD(outTerser, inTerser);
//      return false;
//    }
//    return false;
//  }



  /**
     * Main logics to process QBP^Q22 patient demographics query.
     * 
     * @param msgIn the incoming PDQ message
     * @return the response message associated with the incoming query message
     * @throws PixPdqException if anything wrong with the application
     * @throws HL7Exception if anything wrong with the HL7 message format
     */
    private Message processQuery(Message msgIn) throws PixPdqException, HL7Exception {

    	 //String encodedMessage = new PipeParser().encode(msgIn);
         //log.info("Received message:\n" + encodedMessage + "\n\n");

         HL7Header hl7Header = new HL7Header(msgIn);

         RSP_K21 reply = new RSP_K21();
    	 processMSH(hl7Header, reply.getMSH(), "RSP", "K22", "RSP_K21", getServerApplication(connection), getServerFacility(connection));

         //Now, it must be QBP_Q21 message
         QBP_Q21 message = (QBP_Q21)msgIn;
         //Process QPD
         QPD qpd = message.getQPD();
         
         String queryTag = qpd.getQueryTag().getValue();         
         Terser inTerser = new Terser( message );
         Terser outTerser = new Terser( reply );

 	     //Validate the incoming message first         
 		 boolean isValidMessage = validateMessage(reply, hl7Header, queryTag, inTerser, outTerser);
	     if (!isValidMessage) return reply;

         //Process ID in the following cenarios:See ITI Technical Framework Section 3.21.4.2.2.8
         //0. If use DSC (Continuation pointer), we retrieve it from the memory
         //1. Validate the return domains. If there is a domain not recognized, return an error message.
         //        (Case #3 The Patient Demographics Supplier Actor does not recognize one or more of the
         //         domains in QPD-8-What Domains Returned.)
         //2. If the return domain (QPD-8) is not specified, return all found records. If it exceeds the max num needed,
         //  then just return that number, and construct a DSC segment (Case #1).
         //3. If one or more domains are specified, and there is at least one patient record found, each found
         //   patient (the PID segment) will have the PID-3 field containing each id repetition for each domain. If an
         //   identifier does not exist for a domain that was specified on QPD-8, nothing is returned in the Id list.
         //   (Case #2).
         
         //1. validate the return domains
         List<Identifier> returnDomains = getReturnDomain(qpd, reply, hl7Header.getMessageControlId(), queryTag, outTerser, inTerser);
         //If the returnDomains is null, the reply will contain error message
         if (returnDomains == null)
            return reply;

         //Process RCP segment
         int recordRequestNumber = getRecordRequestNumber(message);
         //Process DSC
         String pointer = getContinuationPointer(message);

         //2. and 3.
         List<List<Patient>>  finalPatients = new ArrayList<List<Patient>>();  
         String newPointer = null;
         PdqResult pdqResult;
         int totalNumber = -1;
         int remainingNumber = -1;
         if(isContinuationQueryByOpenPixPdq()){
        	 if (StringUtil.goodString(pointer)) {
                 //Get the patients from Cache Pointer
                 if ( !dscMap.containsKey(pointer) ) {
                     HL7v25.populateMSA(reply.getMSA(), "AE", hl7Header.getMessageControlId());
                     HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
                     HL7Util.echoQPD(outTerser, inTerser);
                     //segmentId=DSC, sequence=1, fieldPosition=1, fieldRepetition=1, componentNubmer has to be empty
                     HL7v25.populateERR(reply.getERR(), "DSC", "1", "1", "1", null, null, "Unknown Continuation Pointer");
                     return reply;
                 }
                 ContinuationPointer cp = dscMap.get( pointer );
                 totalNumber = cp.getTotalRecords();
                 returnDomains = cp.getReturnDomain();
                 List<List<Patient>> allPatients = cp.getPatients();

                 if (recordRequestNumber < 0 || recordRequestNumber >= allPatients.size() ) {
                    finalPatients = allPatients;
                    //remove continuation pointer if no more patients available
                    synchronized(dscMap) {
                    	dscMap.remove( pointer );
                    }
                    remainingNumber = 0;
                 }
                 else {
                    finalPatients = getSubList(0, recordRequestNumber,allPatients);
                    List<List<Patient>> remainingPatients = getSubList(recordRequestNumber, allPatients.size(),allPatients);
                    cp.setPatients(remainingPatients);
                    cp.setLastRequestTime(System.currentTimeMillis());
                    newPointer = pointer;
                    remainingNumber = remainingPatients.size();
                 }
             }
             else { //This is the first time query, so get patients directly from EMPI. 
                List<List<Patient>> allPatients =new ArrayList<List<Patient>>();
                pdqResult  = getPdqResult(qpd, reply, hl7Header, outTerser, inTerser, 
                		pointer, recordRequestNumber, false, returnDomains);
                if (pdqResult == null) 
                	return reply;
              
                //Filter out patients by return domains.
                allPatients = getPatientList(pdqResult, returnDomains); 
                totalNumber = allPatients.size();
                if (recordRequestNumber <= 0 || recordRequestNumber >= allPatients.size()) {
                   finalPatients = allPatients;
                   remainingNumber = 0;
                } else {
                    finalPatients = getSubList(0, recordRequestNumber,allPatients);
                    //add continuation pointer
                    List<List<Patient>> remainingPatients = getSubList(recordRequestNumber, allPatients.size(),allPatients);
                    String pointerControlId = queryTag  + ":" + getMessageControlId();
                    ContinuationPointer cp = new ContinuationPointer();
                    cp.setPointer(pointerControlId);
                    cp.setReturnDomain( returnDomains );
                    cp.setPatients( remainingPatients );
                    cp.setLastRequestTime(System.currentTimeMillis());
                    cp.setTotalRecords(allPatients.size());
                    cp.setQueryTag(queryTag);
                    synchronized(dscMap) {
                    	dscMap.put(pointerControlId, cp);
                    }
                    newPointer = pointerControlId;
                    remainingNumber = remainingPatients.size();
                }
             }
         }else{
        	//Otherwise Handle Continuation Query by EMPI 
        	pdqResult = getPdqResult(qpd, reply, hl7Header, outTerser, inTerser, 
        			pointer, recordRequestNumber, true, returnDomains);                 
            if (pdqResult == null) return reply;
            finalPatients = getPatientList(pdqResult, returnDomains);            
            newPointer = pdqResult.getContinuationPointer();
            //TODO: calculate totalNumber and remainingNumber
         }
         
         HL7v25.populateMSA(reply.getMSA(), "AA", hl7Header.getMessageControlId());
         if (finalPatients.size() == 0) {
             HL7v25.populateQAK(reply.getQAK(), queryTag, "NF");          	 
         } else {
        	 if (totalNumber == -1 || remainingNumber == -1)
        		 HL7v25.populateQAK(reply.getQAK(), queryTag, "OK");
        	 else 
        		 HL7v25.populateQAK(reply.getQAK(), queryTag, "OK", 
        		        totalNumber, finalPatients.size(), remainingNumber);
         }
         
         HL7Util.echoQPD(outTerser, inTerser);

         if (finalPatients.size() >= 1) {
        	 popluatePIDGroup(reply, finalPatients, returnDomains);
         }//end if found patient
         
         //Populate DSC segment if appropriate
         if (newPointer != null)
             HL7v25.populateDSC(reply.getDSC(), newPointer);

         //Audit Log
         auditLog(finalPatients, hl7Header, queryTag, message);
 
         return reply;
    }

   /**
    * Processes QCN_J01 cancel query message.
    *
    * @param qcnMsg the incoming QCN query cancel message
    * @return Message the acknowledge to the incoming QCN message
    * @throws DataTypeException if there is any data type error
    * @throws PixPdqException if there is any application error
    */
   private Message processQCN_J01(QCN_J01 qcnMsg) throws DataTypeException, PixPdqException {

	   HL7Header hl7Header = new HL7Header(qcnMsg);

       ACK reply = new ACK();
       MSH replyMsh = reply.getMSH();
	   processMSH(hl7Header, replyMsh, "ACK", "J01", "ACK", getServerApplication(connection), getServerFacility(connection));
    	   
       String messageControlId = hl7Header.getMessageControlId();
       HL7v25.populateMSA(reply.getMSA(), "AA", messageControlId);
       //Remove the query result for this query tag
       QID qid = qcnMsg.getQID();
       String queryTag = qid.getQueryTag().getValue();
       
       String messageQueryName= qid.getMessageQueryName().getIdentifier().getValue();
       if(isContinuationQueryByOpenPixPdq()){ 
	       long timeout = 600000; //defaults to 600000 millieseconds (10 minutes)
	       try {
	           timeout = Long.parseLong( Configuration.getPropertySetValue(connection, "QueryProperties", "ContinuationPointerTimeout", false) );
	       } catch (IheConfigurationException e) {
	           throw new PixPdqException(e);
	       }
	
	       List<String> removeList = new ArrayList<String>();
	       synchronized(dscMap) {
		       Set<String> keys = dscMap.keySet();
		       for (String key : keys) {
		           ContinuationPointer cp = dscMap.get(key);
		           // Cancel the query matched by the queryTag
		           if (cp == null || queryTag.equals(cp.getQueryTag())) {
		        	   //Remove the key with invalid value as well as
		        	   //the key whose query to be canceled
		               removeList.add(key);
		        	   continue;
		           }
		
		           //Also, clean up timed out entries. Set time out to be 10 minutes.
	               long lastTime = cp.getLastRequestTime();
	               if ((System.currentTimeMillis() - lastTime) > timeout )
		               removeList.add(key);
		       }
		       if (removeList.size() > 0) {
		    	   for (String key : removeList) {
		    		   if(dscMap.containsKey(key))
		    		        dscMap.remove(key);
		    	   }
		       }
	       }
       }
       else{
            try {
            	 pdqAdapter.cancelQuery(queryTag, messageQueryName);	
			} catch (PdSupplierException e) {
				throw new PixPdqException(e);
			}
       }	
       return reply;
   }

	/**
	 * Validates the incoming Message. It validates in this order:
	 * <p>
	 * <ul>
	 * <li> Validate Triggering Event</li>
	 * <li> Validate Receiving Facility and Receiving Application</li>
	 * </ul>
	 * 
	 * @param reply
	 * @param hl7Header
	 * @param queryTag
	 * @throws HL7Exception
	 * @throws PixPdqException
	 */
	private boolean validateMessage(RSP_K21 reply, HL7Header hl7Header, String queryTag, Terser inTerser, Terser outTerser) 
	throws HL7Exception, PixPdqException {
		
        //1. Valid triggering event:Q22 is for PDQ Query
        if ( !hl7Header.getTriggerEvent().equals("Q22") ) {
            HL7v25.populateMSA(reply.getMSA(), "AE", hl7Header.getMessageControlId());
            HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
            HL7Util.echoQPD(outTerser, inTerser);
            //segmentId=MSH, sequence=1, fieldPosition=9, fieldRepetition=1, componentNubmer=2
            HL7v25.populateERR(reply.getERR(), "MSH", "1", "9", "1", "2", "201", "Unsupported event code");
            return false;
        }
		//2. Validate receiving facility and receiving application
        boolean isValid = validateReceivingFacilityApplication(reply, hl7Header, queryTag);
        if (!isValid) {
            HL7Util.echoQPD(outTerser, inTerser);        	
        	return false;
        }
 		
		//Finally, it must be true when it reaches here
		return true;
	}

	/**
	 * Creates a PID group for each matching patient which will be 
	 * returned back to the PDQ consumer.
	 * 
	 * @param reply the PDQ response message
	 * @param finalPatients the final list of patients to be returned
	 * @param returnDomains the patient domains the PDQ consumer is interested
	 * @throws HL7Exception if any HL7 related message is wrong
	 * @throws PixPdqException if application encounter any problem
	 */
	private void popluatePIDGroup(RSP_K21 reply, List<List<Patient>> finalPatients, List<Identifier> returnDomains) 
	throws HL7Exception, PixPdqException {
        boolean useFirstPID = true;
        for (int patientIndex=0; patientIndex<finalPatients.size(); patientIndex++) {
        	List<Patient> patientRecord = finalPatients.get(patientIndex);
            //We grab the first patient descriptor as the patient demograhpics.
            Patient patient = patientRecord.get(0);
            RSP_K21_QUERY_RESPONSE qr = reply.getQUERY_RESPONSE();
            //RSP_K21 only contains one PID, so we need to add our own PID segment if more than one are needed
            PID pid = null;
            if (useFirstPID) {
                pid = qr.getPID();   
                useFirstPID = false;
            }
            else {
                //Add a new PID segment
                String name = qr.add(PID.class, false, true);
                pid = (PID)qr.get(name);
            }
            try {
                int idIndex = populatePID(pid, patient, patientIndex+1, returnDomains);
                //For subsequent patient record, we only retrieve its patient id
                for (int i=1; i<patientRecord.size(); i++) { //has to start with the second one, the first was handled above
                    Patient pd = (Patient)patientRecord.get(i);                        
                    List<PatientIdentifier> patientIds = pd.getPatientIds();
                    for (PatientIdentifier patientId : patientIds) {
                    	String id = patientId.getId();
                    	Identifier assigningAuthority = patientId.getAssigningAuthority();
                    	assigningAuthority = AssigningAuthorityUtil.reconcileIdentifier( assigningAuthority, actor.getActorDescription(), pdqAdapter );
                    	if ( returnDomains.size() >=1 && !returnDomains.contains(assigningAuthority) )
                    		continue;
                        // PID-3 - Preferred ID
                        populateCX(pid.getPatientIdentifierList(idIndex++), id, assigningAuthority);
                    }
                }

            } catch (IheConfigurationException e) {
                throw new PixPdqException("Failed to populate PID segment", e);
            } catch (PatientException e) {
                throw new PixPdqException("Failed to populate PID segment", e);
            }
        }
	}
	
   /**
    * Audit Logging of PDQ Query Message.
    * 
    * @param patients the patients returned
    * @param hl7Header the message header from the request
    * @param queryTag the query tag from the MSA segment of the PDQ request
    * @param message the QPD segment of the PDQ request
    */
   private void auditLog(List<List<Patient>> patients, HL7Header hl7Header, String queryTag, QBP_Q21 message) {
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
		Collection<ParticipantObject> pos = new ArrayList<ParticipantObject>();
		for (Collection<Patient> patientList : patients) {
			for(Patient patient : patientList) {
				ParticipantObject patientObj = new ParticipantObject(patient);
				pos.add(patientObj);				
			}
		}
		//Query Info
		ParticipantObject queryObj = new ParticipantObject();
		queryObj.setId(queryTag);
		try {
			queryObj.setQuery(PipeParser.encode(message, new EncodingCharacters('|', "^~\\&"))); 
		} catch (HL7Exception e) {
			log.warn("Failed to encode PDQ query for auditting", e);
		} 
		queryObj.addDetail(new TypeValuePair("MSH-10", messageId));		
	
		//Finally Log it.
		actor.getAuditTrail().logPdqQuery(source, pos, queryObj);
   }

   /**
    * Processes the MSH segment.
    *
    * @param requestMsh the request MSH segment
    * @param replyMsh the reply MSH segment
    * @param messageType the message type
    * @param event  the event id
    * @param receivingApplication it is expected to be the application of this PDQ server
    * @param receivingFacility  it is expected to be the facility of this PDQ server
    * @throws DataTypeException
    */
   private void processMSH(HL7Header requestMsh, MSH replyMsh, String messageType, String event, String structure, Identifier receivingApplication, Identifier receivingFacility) throws DataTypeException {
        Identifier idSendingApplication   = requestMsh.getSendingApplication();
        Identifier idSendingFacility      = requestMsh.getSendingFacility();
        //For the response message, the ReceivingApplication and ReceivingFacility will become the sendingApplication and sendingFacility,
        //Also the sendingApplication and sendingFacility will become the receivingApplication and receivingFacility.
        HL7v25.populateMSH(replyMsh, messageType, event, structure, getMessageControlId(), receivingApplication, receivingFacility, idSendingApplication, idSendingFacility );

   }

   /**
    * Validates the receiving facility and receiving application of an incoming message.
    * 
    * @param reply the reply message to be populated if any validation is failed
    * @param inMsg the incoming message
    * @param queryTag the query tag of the incoming PDQ query message
    * @return <code>true</code> if validation is passed; otherwise <code>false</code>.
    * @throws HL7Exception if anything is wrong with the HL7 message format
    * @throws PixPdqException if anything is wrong the application
    */
   private boolean validateReceivingFacilityApplication(RSP_K21 reply, HL7Header inMsg, String queryTag) 
   throws HL7Exception, PixPdqException {
	   //Validate ReceivingApplication and ReceivingFacility.
       //Currently we are not validating SendingApplication and SendingFacility
       Identifier idReceivingApplication = inMsg.getReceivingApplication();
       Identifier idReceivingFacility    = inMsg.getReceivingFacility();

	   boolean validateApplication = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_APPLICATION);
	   if (validateApplication && !idReceivingApplication.equals(getServerApplication(connection)) ) {
	       String inMsgControlId = inMsg.getMessageControlId();
           HL7v25.populateMSA(reply.getMSA(), "AE", inMsgControlId);
           HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
           //segmentId=MSH, sequence=1, fieldPosition=5, fieldRepetition=1, componentNubmer=1
           HL7v25.populateERR(reply.getERR(), "MSH", "1", "5", "1", "1", "204", "Unknown Receiving Application");
           return false;
	   }
	   
	   boolean validateFacility = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_FACILITY);
	   if (validateFacility && !idReceivingFacility.equals(getServerFacility(connection)) ) {
	       String inMsgControlId = inMsg.getMessageControlId();
           HL7v25.populateMSA(reply.getMSA(), "AE", inMsgControlId);
           HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
           //segmentId=MSH, sequence=1, fieldPosition=6, fieldRepetition=1, componentNubmer=1
           HL7v25.populateERR(reply.getERR(), "MSH", "1", "6", "1", "1", "204", "Unknown Receiving Facility");
           return false;
       }
       return true;
   }
   
   /**
    * Gets the record request number from RCP segment.
    *
    * @param message the QBP_Q21 request message
    * @return the record number for the request. <code>-1</code> if there is no record number limit.
    */
   private int getRecordRequestNumber(QBP_Q21 message) {
         RCP rcp = message.getRCP();
         String number = rcp.getQuantityLimitedRequest().getQuantity().getValue();
         int recordRequestNumber = -1;  //-1 indicates no limit
         if (StringUtil.goodString(number)) {
             try {
                 recordRequestNumber = Integer.parseInt(number);
             } catch (NumberFormatException e) {
            	 log.warn("Invalid RCP record request number");
             }
         }
         return recordRequestNumber;
   }

   /**
    * Gets the continuation pointer from DSC segment.
    *
    * @param message the QBP_Q21 request message
    * @return the continuation pointer
    */
   private String getContinuationPointer(QBP_Q21 message) {
         DSC dsc = message.getDSC();
         String pointer = dsc.getContinuationPointer().getValue();
         return pointer;
   }


   /**
    * Gets the value of a given component
    *
    * @param data the {@link GenericComposite}
    * @param componentNum the component number in the composite data
    * @return
    * @throws DataTypeException
    */
    private Varies getComponentValue(GenericComposite data, int componentNum) throws DataTypeException {
            return (Varies)data.getComponent(componentNum);
        }

   /**
    * Gets the return domain from the original PDQ request.
    *
    * @param qpd the request QPD segment
    * @param reply the response message
    * @param messageControlId the messageControlId for the response message
    * @param queryTag the queryTag of the request message
    * @param outTerser the out response message terser
    * @param inTerser the in request message terser
    * @return return a list of returnDomain if not error. Otherwise, return null if there is an invalid domain or request syntax cannot be recognized.
    * @throws HL7Exception if there is anything wrong with the HL7 message format
    */
   private List<Identifier> getReturnDomain(QPD qpd, RSP_K21 reply, String messageControlId, String queryTag, Terser outTerser, Terser inTerser ) throws HL7Exception {
       //1. validate the return domains.
      //Get the return domains
      Type[] typeDomains = qpd.getField(8);
      List<Identifier> returnDomains = new ArrayList<Identifier>();
      for (int i=0; i<typeDomains.length; i++) {
          Varies domain = (Varies)typeDomains[i];
          Type data = domain.getData();
          if (data instanceof GenericComposite) {
              Identifier aa = HL7v25.extractAssigningAuthority( (GenericComposite)data );
              boolean validDomain = AssigningAuthorityUtil.validateDomain( aa, actor.getActorDescription(), pdqAdapter);
              if (validDomain) {
                  Identifier reconciledDomain = AssigningAuthorityUtil.reconcileIdentifier(aa, actor.getActorDescription(), pdqAdapter);
                  returnDomains.add( reconciledDomain );
              }
              else {
                  //if a domain is invalid, return an error message
                  HL7v25.populateMSA(reply.getMSA(), "AE", messageControlId);
                  HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
                  HL7Util.echoQPD(outTerser, inTerser);
                  //segmentId=QPD, sequence=1, fieldPosition=8, fieldRepetition=i+1, componentNubmer has to be empty
                  HL7v25.populateERR(reply.getERR(), "QPD", "1", "8", Integer.toString(i+1), null, "204", "Unknown Key Identifier");
                  return null;
              }
          } else {
              //return "Data type error"
              HL7v25.populateMSA(reply.getMSA(), "AE", messageControlId);
              HL7v25.populateQAK(reply.getQAK(), queryTag, "AE");
              HL7Util.echoQPD(outTerser, inTerser);
              //segmentId=QPD, sequence=1, fieldPosition=8, fieldRepetition=null, componentNubmer=null
              HL7v25.populateERR(reply.getERR(), "QPD", "1", "8", null, null, "102", "Data type error");
              return null;
          }
       }
       return returnDomains;
   }

   /**
    * Gets raw {@link PdqResult} from the EMPI.
    *
    * @param qpd the request QPD segment
    * @param reply the response message
    * @param hl7Header the incoming message header 
    * @param outTerser the out response message terser
    * @param inTerser the in request message terser
    * @param pointer continuation pointer of the matching patient
    * @param requestNo the number of patient record to be returned for each request   
    * @param isContinatuation <code>true</code> - if continuation query is handled by OpenPIXPDQ; 
    *        <code>false</code> - if continuation query  is handled by EMPI. 
    * @param returnDomains the domains in which patients are returned
    * @return return a {@link PdqResult} object that contains a list of patients 
    * if not error. Otherwise, return null if the request syntax cannot be recognized.
    * @throws HL7Exception
    * @throws PixPdqException
    */
   private PdqResult getPdqResult(QPD qpd, RSP_K21 reply, HL7Header hl7Header, Terser outTerser, 
		   Terser inTerser ,String pointer,int requestNo, boolean isContinuation, List<Identifier> returnDomains) 
   		   throws PixPdqException, HL7Exception {

       //First, process query parameters
       Type[] queries = qpd.getField(3);  //The third one is the query parameters
       if (queries.length == 0) {
	       HL7v25.populateMSA(reply.getMSA(), "AE", hl7Header.getMessageControlId());
	       HL7v25.populateQAK(reply.getQAK(), qpd.getQueryTag().getValue(), "AE");
	       HL7Util.echoQPD(outTerser, inTerser);
	       //segmentId=QPD, sequence=1, fieldPosition=3, fieldRepetition=null, componentNubmer=null
           HL7v25.populateERR(reply.getERR(), "QPD", "1", "3", null, null, "102", "Data type error");
	       return null;
       }
       
       HashMap<String, String> parameters = new HashMap<String, String>();       
       for (int i=0; i < queries.length; i++) {
           Varies parameter = (Varies)queries[i];
           Type data = parameter.getData();
           boolean validData = false;
           if (data instanceof GenericComposite) {
               //The first component is parameter name
               Varies field = getComponentValue((GenericComposite)data, 0);
               String sField = ((GenericPrimitive)field.getData()).getValue();
               //The second component is parameter value
               Varies value = getComponentValue((GenericComposite)data, 1);
               String sValue = ((GenericPrimitive)value.getData()).getValue();
               if (sField != null) {
            	   parameters.put( sField, sValue );
            	   validData = true;
               }
           } 
           
           if (!validData) {
              //return "Data type error"
              HL7v25.populateMSA(reply.getMSA(), "AE", hl7Header.getMessageControlId());
              HL7v25.populateQAK(reply.getQAK(), qpd.getQueryTag().getValue(), "AE");
              HL7Util.echoQPD(outTerser, inTerser);
              //segmentId=QPD, sequence=1, fieldPosition=3, fieldRepetition=null, componentNubmer=null
              HL7v25.populateERR(reply.getERR(), "QPD", "1", "3", null, null, "102", "Data type error");
              return null;
           }
       }
       
       PdqQuery query = processQuery(parameters);
       query.setQueryName(qpd.getMessageQueryName().getText().getValue());
       query.setQueryTag(qpd.getQueryTag().getValue());
       query.addReturnDomains(returnDomains);
       if(isContinuation){
	       query.setContinuationPointer(pointer);
	       query.setHowMany(requestNo);
       }
       MessageHeader header = hl7Header.toMessageHeader();
	   PdqResult pdqResult =null;	
       try {
    	   pdqResult = pdqAdapter.findPatients( query, header );
       } catch (PdSupplierException e) {
    	    log.error(e.getMessage());
			throw new PixPdqException(e);
       }

       if (pdqResult == null || pdqResult.getPatients() == null) {
    	   log.error("Failed to get valid PDQ result.");
    	   throw new PixPdqException("Failed to get valid PDQ result.");
       }
       
       return pdqResult;
   }

   /**
    * Populates {@link PdqQuery} object with the query request parameters.
    *
    * @param parameters the PDQ query parameters
    * @return the {@link PdqQuery} object
    */
    private PdqQuery processQuery(HashMap<String, String> parameters) throws PixPdqException {
        PdqQuery ret = new PdqQuery();
       try {
           ret.setPrefix( Configuration.getPropertySetValue(connection, "QueryProperties", "WildcardBefore", false) );
           ret.setSuffix( Configuration.getPropertySetValue(connection, "QueryProperties", "WildcardAfter", false) );
       } catch (IheConfigurationException e) {
           throw new PixPdqException(e);
       }
        Address address = null;
        PhoneNumber phone = null;        
        PersonName personName = null;
        PersonName maidenName = null;
        DriversLicense driversLicense = null;
        PatientIdentifier patientAccountNumber = null;
        PatientIdentifier patientIdentifier = null;
        
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            String value = parameters.get( key );
            // PID-3 - Patient Identifier List
            if (key.equalsIgnoreCase("@PID.3.1")) {
            	if (patientIdentifier==null) patientIdentifier = new PatientIdentifier();
            	patientIdentifier.setId(value);
            }
            else if(key.equalsIgnoreCase("@PID.3.4")) {
            	if (patientIdentifier==null) patientIdentifier = new PatientIdentifier();
            	patientIdentifier.setAssigningAuthority(new Identifier(value,null,null));
            }
            else if(key.equalsIgnoreCase("@PID.3.4.1")) {
            	if (patientIdentifier==null) patientIdentifier = new PatientIdentifier();
            	Identifier aa = patientIdentifier.getAssigningAuthority();
            	if (aa == null) {
            		aa = new Identifier(value, null, null);
            	} else {
            		aa.setNamespaceId(value);
            	}
            	patientIdentifier.setAssigningAuthority(aa);
            }
            else if(key.equalsIgnoreCase("@PID.3.4.2")) {
            	if (patientIdentifier==null) patientIdentifier = new PatientIdentifier();
            	Identifier aa = patientIdentifier.getAssigningAuthority();
            	if (aa == null) {
            		aa = new Identifier(null, value, null);
            	} else {
            		aa.setUniversalId(value);
            	}
            	patientIdentifier.setAssigningAuthority(aa);
            }
            else if(key.equalsIgnoreCase("@PID.3.4.3")) {
            	if (patientIdentifier==null) patientIdentifier = new PatientIdentifier();
            	Identifier aa = patientIdentifier.getAssigningAuthority();
            	if (aa == null) {
            		aa = new Identifier(null, null, value);
            	} else {
            		aa.setUniversalIdType(value);
            	}
            	patientIdentifier.setAssigningAuthority(aa);
            }
            else if(key.equalsIgnoreCase("@PID.3.5")) {
            	if (patientIdentifier==null) patientIdentifier = new PatientIdentifier();
            	patientIdentifier.setIdentifierTypeCode(value);
            }
            else if(key.equalsIgnoreCase("@PID.3.6")) {
            	if (patientIdentifier==null) patientIdentifier = new PatientIdentifier();
            	patientIdentifier.setAssigningFacility(new Identifier(value,null,null));           
            }
           // PID-5 - Patient Name
            /*Subcomponents for Family Name (FN): 
             * <Surname (ST)> & <Own Surname Prefix (ST)> & <Own
             * Surname (ST)> & <Surname Prefix From Partner/Spouse (ST)> & <Surname From
             * Partner/Spouse (ST)> */
            else if (key.equalsIgnoreCase("@PID.5.1") ||
            		 key.equalsIgnoreCase("@PID.5.1.1")) {
            	if (personName == null) personName = new PersonName();
            	personName.setLastName(value);            
            }
            else if (key.equalsIgnoreCase("@PID.5.2")) {
            	if (personName == null) personName = new PersonName();
            	personName.setFirstName(value);
            }
            else if (key.equalsIgnoreCase("@PID.5.3")) {
            	if (personName == null) personName = new PersonName();
            	personName.setSecondName(value);
            }
            else if (key.equalsIgnoreCase("@PID.5.4")) {
            	if (personName == null) personName = new PersonName();
            	personName.setSuffix(value);
            }
            else if (key.equalsIgnoreCase("@PID.5.5")) {
            	if (personName == null) personName = new PersonName();
            	personName.setPrefix(value);
            }
            else if (key.equalsIgnoreCase("@PID.5.6")) {
            	if (personName == null) personName = new PersonName();
            	personName.setDegree(value);
            }
            // PID-6 - Maiden Name
            else if (key.equalsIgnoreCase("@PID.6.1") ) {
            	if (maidenName == null) maidenName = new PersonName();
            	maidenName.setLastName(value);
            }
            else if (key.equalsIgnoreCase("@PID.6.2")) {
            	if (maidenName == null) maidenName = new PersonName();
            	maidenName.setFirstName(value);
            }
            	if (maidenName == null) maidenName = new PersonName();
            else if (key.equalsIgnoreCase("@PID.6.3")) {
            	maidenName.setSecondName(value);
            }
            else if (key.equalsIgnoreCase("@PID.6.4")) {
            	if (maidenName == null) maidenName = new PersonName();
            	maidenName.setSuffix(value);
            }
            else if (key.equalsIgnoreCase("@PID.6.5")) {
            	if (maidenName == null) maidenName = new PersonName();
            	maidenName.setPrefix(value);
            }
            // PID-7 - Birth date
            	
            else if (key.equalsIgnoreCase("@PID.7.1") )  {
                try {
                    String birthdateFormat = HL7v25.birhtdateFormat;
                    String formatFromProperty = Configuration.getPropertySetValue(connection, "DateTimeFormat", "Birthdate", false);
                    if (StringUtil.goodString(formatFromProperty)) {
                        birthdateFormat = formatFromProperty;
                    }
                    Calendar birthdate = DateUtil.parseCalendar(value, birthdateFormat);
                    ret.setBirthDate(birthdate);
                } catch (IheConfigurationException e) {
                    throw new PixPdqException(e);
                } catch (ParseException e) {
                    throw new PixPdqException(e);
                }
            }
            // PID-8 - Gender
            else if (key.equalsIgnoreCase("@PID.8"))
               ret.setSex(SharedEnums.SexType.getByString(value));
            // PID-11 - Address
            else if (key.equalsIgnoreCase("@PID.11.1") || 
            		 key.equalsIgnoreCase("@PID.11.1.1") ) {
                if (address == null ) address = new Address();
                address.setAddLine1(value);
            }
            else if (key.equalsIgnoreCase("@PID.11.2")) {
                if (address == null ) address = new Address();
                address.setAddLine2(value);
            }
            else if (key.equalsIgnoreCase("@PID.11.3")) {
                if (address == null ) address = new Address();
                address.setAddCity(value);
            }
            else if (key.equalsIgnoreCase("@PID.11.4")) {
                if (address == null ) address = new Address();
                address.setAddState(value);
            }
            else if (key.equalsIgnoreCase("@PID.11.5")) {
                if (address == null ) address = new Address();
                address.setAddZip(value);
            }
            else if (key.equalsIgnoreCase("@PID.11.6")) {
                if (address == null ) address = new Address();
                address.setAddCountry(value);
            }
            else if (key.equalsIgnoreCase("@PID.11.9")) {
                if (address == null ) address = new Address();
                address.setAddCounty(value);
            }
            // PID-14 - Phone Number
//Commented out, there is no specific format for this field
//            else if (key.equalsIgnoreCase("@PID.14.1")) {
//                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.WORK);
//                phone.setNumber( value );
//            }
            else if (key.equalsIgnoreCase("@PID.14.5")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.WORK);
                phone.setCountryCode(value);
            }
            else if (key.equalsIgnoreCase("@PID.14.6")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.WORK);
                phone.setAreaCode(value);
            }
            else if (key.equalsIgnoreCase("@PID.14.1")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.WORK);
                phone.setNumber(value);
            }
           else if (key.equalsIgnoreCase("@PID.14.8")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.WORK);
                phone.setExtension(value);
            }
            else if (key.equalsIgnoreCase("@PID.14.9")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.WORK);
                phone.setNote(value);
            }
            else if (key.equalsIgnoreCase("@PID.13.5")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.HOME);
                phone.setCountryCode( value );
            }
            else if (key.equalsIgnoreCase("@PID.13.6")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.HOME);
                phone.setAreaCode( value );
            }
            else if (key.equalsIgnoreCase("@PID.13.1")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.HOME);
                phone.setNumber(value);
            }
            else if (key.equalsIgnoreCase("@PID.13.8")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.HOME);
                phone.setExtension(value);
            }
            else if (key.equalsIgnoreCase("@PID.13.9")) {
                if (phone == null) phone = new PhoneNumber(SharedEnums.PhoneType.HOME);
                phone.setNote(value);
            }
            // PID-18 - Patient Account Number
            else if (key.equalsIgnoreCase("@PID.18.1")) {
            	if (patientAccountNumber==null) patientAccountNumber = new PatientIdentifier();
            	patientAccountNumber.setId(value);
            }
            else if(key.equalsIgnoreCase("@PID.18.4")) {
            	if (patientAccountNumber==null) patientAccountNumber = new PatientIdentifier();
            	patientAccountNumber.setAssigningAuthority(new Identifier(value, null, null));
            }
            else if(key.equalsIgnoreCase("@PID.18.4.1")) {
            	if (patientAccountNumber==null) patientAccountNumber = new PatientIdentifier();
            	Identifier aa = patientAccountNumber.getAssigningAuthority();
            	if (aa == null) {
            		aa = new Identifier(value, null, null);
            	} else {
            		aa.setNamespaceId(value);
            	}
            	patientAccountNumber.setAssigningAuthority(aa);
            }
            else if(key.equalsIgnoreCase("@PID.18.4.2")) {
            	if (patientAccountNumber==null) patientAccountNumber = new PatientIdentifier();
            	Identifier aa = patientAccountNumber.getAssigningAuthority();
            	if (aa == null) {
            		aa = new Identifier(null, value, null);
            	} else {
            		aa.setUniversalId(value);
            	}
            	patientAccountNumber.setAssigningAuthority(aa);
            }
            else if(key.equalsIgnoreCase("@PID.18.4.3")) {
            	if (patientAccountNumber==null) patientAccountNumber = new PatientIdentifier();
            	Identifier aa = patientAccountNumber.getAssigningAuthority();
            	if (aa == null) {
            		aa = new Identifier(null, null, value);
            	} else {
            		aa.setUniversalIdType(value);
            	}
            	patientAccountNumber.setAssigningAuthority(aa);
            }
            else if(key.equalsIgnoreCase("@PID.18.5")) {
            	if (patientAccountNumber==null) patientAccountNumber = new PatientIdentifier();
            	patientAccountNumber.setIdentifierTypeCode(value);
            }
            else if(key.equalsIgnoreCase("@PID.18.6")) {
            	if (patientAccountNumber==null) patientAccountNumber = new PatientIdentifier();
            	patientAccountNumber.setAssigningFacility(new Identifier(value, null, null));
            }
            //PID-19 SSN
            else if (key.equalsIgnoreCase("@PID.19")) {
                ret.setSsn(value);
            }
            //PID-20 Driver's License
            else if (key.equalsIgnoreCase("@PID.20") || key.equalsIgnoreCase("@PID.20.1")) {
            	if (driversLicense==null) driversLicense = new DriversLicense();
            	driversLicense.setLicenseNumber(value);
            }else if (key.equalsIgnoreCase("@PID.20.2")){
            	if (driversLicense==null) driversLicense = new DriversLicense();
            	driversLicense.setIssuingState(value);                            	
            }else if(key.equalsIgnoreCase("@PID.20.3")){
            	try {
                    String birthdateFormat = HL7v25.birhtdateFormat;
                    Calendar birthdate = DateUtil.parseCalendar(value, birthdateFormat);
                	if (driversLicense==null) driversLicense = new DriversLicense();
                	driversLicense.setExpirationDate(birthdate);                    
                } catch (ParseException e) {
                    throw new PixPdqException(e);
                }
            }
            
        } //end for
        if(patientIdentifier != null) ret.setPatientIdentifier(patientIdentifier);
        if(patientAccountNumber != null) ret.setPatientAccountNumber(patientAccountNumber);
        if(personName != null ) ret.setPersonName(personName);
        if(maidenName != null ) ret.setMotherMaidenName(maidenName);
        if (address != null) ret.setAddress( address );
        if (phone != null) ret.setPhone( phone );
        if(driversLicense != null ) ret.setDriversLicense(driversLicense);       
        return ret;
    }

   /**
    * Populates the PID segment of the Patient Demographics Query Response.
    * <p>
    * See IHE ITI-TF vol 2, section 3.21.4.2.2.5 and HL7 v2.5 chapter 3
    *
    * @param pid the PID segment to be populated
    * @param patient the patient demographic data
    * @param returnDomains a list of domains whose patient record to be returned
    * @return the number of matching IDs in this PID segment
    * @throws IheConfigurationException When this connection is not properly configured to encode messages
    * @throws PatientException When required patient information is missing
    * @throws HL7Exception When the patient information cannot be encoded properly into HL7
    */
    private int populatePID(PID pid, Patient patient, int setId, List<Identifier> returnDomains) throws HL7Exception, IheConfigurationException, PatientException {
        int numberOfPatientId = 0;
        
        List<PatientIdentifier> pids = patient.getPatientIds();
        // PID-1 Set ID - PID (SI) 00104
        if (setId >= 1)
        	pid.getSetIDPID().setValue(Integer.toString(setId));
        // PID-2 - Deprecated ID, our authority makes this ID > 20 characters, the allowed limit
        //populateCX(pid.getPatientID(), patientId, authority);
        // PID-3 - Preferred ID
        for (int i=0; i<pids.size(); i++) {
        	PatientIdentifier id = pids.get(i);
        	Identifier authority = id.getAssigningAuthority();
        	authority = AssigningAuthorityUtil.reconcileIdentifier( authority, actor.getActorDescription(), pdqAdapter );
        	if ( returnDomains.size() >=1 && !returnDomains.contains(authority) )
        		continue;
        	populateCX(pid.getPatientIdentifierList(numberOfPatientId++), id.getId(), authority);
        }
        // PID-5 - Patient legal name
        XPN xpn = pid.getPatientName(0);
        populateXPN(xpn, patient.getPatientName());
        // PID-6 - Mother's maiden name, we have only last name
        if (null != patient.getMothersMaidenName()) {
            xpn = pid.getMotherSMaidenName(0);
            PersonName name = patient.getMothersMaidenName();
            populateXPN(xpn, name);
        }
        // PID-7 - Birth date
        if (patient.getBirthDateTime() != null) {
            String birthdateFormat = HL7v25.birhtdateFormat;
            String formatFromProperty = Configuration.getPropertySetValue(connection, "DateTimeFormat", "Birthdate", false);
            if (StringUtil.goodString(formatFromProperty)) {
                birthdateFormat = formatFromProperty;
            }
            String date = new SimpleDateFormat(birthdateFormat).format(patient.getBirthDateTime().getTime());
            pid.getDateTimeOfBirth().getTime().setValue(date);
        }
        // PID-8 - Gender
        String gender = "U";
        if (patient.getAdministrativeSex() == SharedEnums.SexType.MALE) gender = "M";
        else if (patient.getAdministrativeSex() == SharedEnums.SexType.FEMALE) gender = "F";
        else if (patient.getAdministrativeSex() == SharedEnums.SexType.OTHER) gender = "O";
        pid.getAdministrativeSex().setValue(gender);
        int i = 0;
        // PID-9 - Patient name aliases
        if (patient.getPatientAliases() != null) {
            for (PersonName alias : patient.getPatientAliases()){
                populateXPN(pid.getPatientAlias(i++), alias);
            }
        }
        // PID-11 - Addresses
        i = 0;  
        List<Address> addresses = patient.getAddresses();
        if (addresses != null) {
	        for (Address address : addresses) {
	        	populateXAD(pid.getPatientAddress(i++), address);
	        }
        }
        i = 0; 
        int j = 0;
        List<PhoneNumber> phones = patient.getPhoneNumbers();
        if (phones != null) {
	        for (PhoneNumber phone : phones) {
	        	PhoneType type = phone.getType();
	            // PID-13 - Home phone
	        	if (type == SharedEnums.PhoneType.HOME ||
	        	    type == SharedEnums.PhoneType.CELL ||
	        	    type == SharedEnums.PhoneType.EMERGENCY)
	        		populateXTN(pid.getPhoneNumberHome(i++), phone);
	            // PID-14 - Work phone
	        	else if (type == SharedEnums.PhoneType.WORK ||
	        			 type == SharedEnums.PhoneType.SERVICE ||
	        			 type == SharedEnums.PhoneType.FAX) 
	        		populateXTN(pid.getPhoneNumberBusiness(j++), phone);
	        }
        }   
        // PID-15 - Marital Status
        if (patient.getMaritalStatus() != null)
            populateCE(pid.getMaritalStatus(), patient.getMaritalStatus());
                     	
        //PID-18 Patient account number
        // not known from PatientDescriptor
        PatientIdentifier accountNumber = patient.getPatientAccountNumber();
        if (accountNumber != null) {
        	if (accountNumber.getAssigningAuthority() == null)
        		pid.getPatientAccountNumber().getIDNumber().setValue(accountNumber.getId());
        	else
        		populateCX(pid.getPatientAccountNumber(), accountNumber.getId(), accountNumber.getAssigningAuthority() );
        }

        // PID-19 - SSN
        if (patient.getSsn() != null)
            pid.getSSNNumberPatient().setValue(clip(patient.getSsn(), 16));
        // PID-20 - Driver's License
        if (patient.getDriversLicense() != null) {
        	DriversLicense license = patient.getDriversLicense();
        	if (license.getIssuingState() != null)
        		pid.getDriverSLicenseNumberPatient().getIssuingStateProvinceCountry().setValue(license.getIssuingState());
            if (license.getLicenseNumber() != null)
            	pid.getDriverSLicenseNumberPatient().getLicenseNumber().setValue(clip(license.getLicenseNumber(), 25));
            if (license.getExpirationDate() != null) {
	            Calendar expDate = license.getExpirationDate();
	            int year = expDate.get(Calendar.YEAR);
	            int month = expDate.get(Calendar.MONTH) + 1;
	            int date = expDate.get(Calendar.DATE);
	            pid.getDriverSLicenseNumberPatient().getExpirationDate().setYearMonthDayPrecision(year, month, date);
            }
        }

        //PID-26 - Citizenship Info
        pid.getCitizenship(0).getIdentifier().setValue(patient.getCitizenship());

        //PID-29 - Date of death / death indicator
         if (patient.getDeathDate() != null) {
            String birthdateFormat = HL7v25.birhtdateFormat;
            String formatFromProperty = Configuration.getPropertySetValue(connection, "DateTimeFormat", "Birthdate", false);
            if (StringUtil.goodString(formatFromProperty)) {
                birthdateFormat = formatFromProperty;
            }
            String date = new SimpleDateFormat(birthdateFormat).format(patient.getDeathDate().getTime());
            pid.getPatientDeathDateAndTime().getTime().setValue(date);
        }
        //PID-30 - Death indicator
        pid.getPatientDeathIndicator().setValue(patient.getDeathIndicator());
        // Done
        return numberOfPatientId;
    }

     /**
     * Populates a CX component with an ID and assigning authority.
     *
     * @param cx the CX component to populate
     * @param id the id to put in
     * @param authority the assigning authority for that id
     * @throws DataTypeException When the component cannot be encoded in HL7
     * @throws IheConfigurationException When this connection is not properly configured to translate Patient Feed messages
     */
    private void populateCE(CE ce, String id) throws DataTypeException, IheConfigurationException {
        // PID 15.1 -- The id
        ce.getIdentifier().setValue(id);
    }

    /**
     * Populates a CX component with an ID and assigning authority.
     *
     * @param cx the CX component to populate
     * @param id the id to put in
     * @param authority the assigning authority for that id
     * @throws DataTypeException When the component cannot be encoded in HL7
     * @throws IheConfigurationException When this connection is not properly configured to translate Patient Feed messages
     */
    private void populateCX(CX cx, String id, Identifier authority) throws DataTypeException, IheConfigurationException {
        // PID 3.1 -- The id
        cx.getIDNumber().setValue(id);
        // PID 3.4 -- The assigning authority
        boolean okay = false;
        HD hd = cx.getAssigningAuthority();
        if (authority.getNamespaceId() != null) {
            hd.getNamespaceID().setValue(authority.getNamespaceId());
            okay = true;
        }
        if (authority.getUniversalId() != null) {
            hd.getUniversalID().setValue(authority.getUniversalId());
            if (authority.getUniversalIdType() != null) {
                hd.getUniversalIDType().setValue(authority.getUniversalIdType());
                okay = true;
            }
        }
        // If the assigning authority does not enough pieces, throw an exception
        if (!okay)
            throw new IheConfigurationException("Invalid Assigning Authority identifer encountered by connection \"" + connection.getDescription() + "\"");
        // PID 3.5 -- The id type code, see HL7 table 0203 - "PI" stands for Patient Internal PersonIdentifier
        cx.getIdentifierTypeCode().setValue("PI");
    }

    /**
     * Populates an XPN component with a patient name.
     *
     * @param xpn the XPN component to populate
     * @param patientName the patient name
     * @throws PatientException If this patient has no name
     * @throws DataTypeException When the name cannot be encoded into valid HL7
     */
    private void populateXPN(XPN xpn, PersonName patientName) throws DataTypeException, PatientException {
        if ((patientName.getFirstName() == null) && (patientName.getSecondName() == null) && (patientName.getLastName() == null))
            throwPatientException("Patient has no name.");
        if (patientName.getFirstName() != null) xpn.getGivenName().setValue(patientName.getFirstName());
        if (patientName.getSecondName() != null) xpn.getSecondAndFurtherGivenNamesOrInitialsThereof().setValue(patientName.getSecondName());
        if (patientName.getLastName() != null) xpn.getFamilyName().getSurname().setValue(patientName.getLastName());
        if (patientName.getPrefix() != null) xpn.getPrefixEgDR().setValue(patientName.getPrefix());
        if (patientName.getSuffix() != null) xpn.getSuffixEgJRorIII().setValue(patientName.getSuffix());
        if (patientName.getNameTypeCode() != null) xpn.getNameTypeCode().setValue(patientName.getNameTypeCode());
    }

    /**
     * Populates an XAD component with an address.
     *
     * @param xad the XAD component to populate
     * @param address the address to get the data from
     * @throws DataTypeException When the component cannot be encoded in HL7
     */
    private void populateXAD(XAD xad, Address address) throws DataTypeException {
        // Fill in all the address parts
        if (address.getAddLine1() != null) {
            xad.getStreetAddress().getStreetOrMailingAddress().setValue(address.getAddLine1());
        }
        if (address.getAddLine2() != null) {
            xad.getOtherDesignation().setValue(address.getAddLine2());
        }
        if (address.getAddCity() != null) {
            xad.getCity().setValue(address.getAddCity());
        }
        if (address.getAddState() != null) {
            xad.getStateOrProvince().setValue(address.getAddState());
        }
        if (address.getAddZip() != null) {
            xad.getZipOrPostalCode().setValue(address.getAddZip());
        }
        if (address.getAddCountry() != null) {
            xad.getCountry().setValue(address.getAddCountry());
        }
        if (address.getAddCounty() != null) {
            xad.getCountyParishCode().setValue(address.getAddCounty());
        }
        // The address type is optional
        if (address.getAddType() != null) {
        	xad.getAddressType().setValue(address.getAddType().getHL7Value());
        }
    }

   /**
    * Populates an XTN component with a phone number.
    * <p>
    * This method uses a combined interpretation of the v2.3.1 and the v2.5 spec to
    * require only digits in phone numbers and impose limits on the field lengths.
    *
    * @param xtn the XTN component to populate
    * @param phone the phone number to put into the component
    * @throws DataTypeException When the component cannot be encoded in HL7
    */
   private void populateXTN(XTN xtn, PhoneNumber phone) throws DataTypeException{
       // First the separate pieces
       String country = null;
       if (phone.getCountryCode() != null) {
           country = clipNumber(phone.getCountryCode(), 3);
           xtn.getCountryCode().setValue(country);
       }
       String area = null;
       if (phone.getAreaCode() != null) {
           area = clipNumber(phone.getAreaCode(), 5);
           xtn.getAreaCityCode().setValue(area);
       }
       String number = null;
       if (phone.getNumber() != null) {
           number = clipNumber(phone.getNumber(), 9);
           xtn.getLocalNumber().setValue(number);
       }
       String extension = null;
       if (phone.getExtension() != null) {
           extension = clipNumber(phone.getExtension(), 5);
           xtn.getExtension().setValue(extension);
       }
       if (phone.getNote() != null) xtn.getAnyText().setValue(phone.getNote());
       // Next the telecommunications types (adapted fom our ENUM types)
       if (phone.getType() == SharedEnums.PhoneType.HOME) {
           xtn.getTelecommunicationUseCode().setValue("PRN");
           xtn.getTelecommunicationEquipmentType().setValue("PH");
       } else if (phone.getType() == SharedEnums.PhoneType.WORK) {
           xtn.getTelecommunicationUseCode().setValue("WPN");
           xtn.getTelecommunicationEquipmentType().setValue("PH");
       } else if (phone.getType() == SharedEnums.PhoneType.CELL) {
           xtn.getTelecommunicationUseCode().setValue("PRN");
           xtn.getTelecommunicationEquipmentType().setValue("CP");
       } else if (phone.getType() == SharedEnums.PhoneType.EMERGENCY) {
           xtn.getTelecommunicationUseCode().setValue("EMR");
           xtn.getTelecommunicationEquipmentType().setValue("PH");
       } else if (phone.getType() == SharedEnums.PhoneType.SERVICE) {
           xtn.getTelecommunicationUseCode().setValue("ASN");
           xtn.getTelecommunicationEquipmentType().setValue("PH");
       } else if (phone.getType() == SharedEnums.PhoneType.FAX) {
           xtn.getTelecommunicationUseCode().setValue("WPN");
           xtn.getTelecommunicationEquipmentType().setValue("FX");
       }
       if (phone.getEmail() != null) {
           xtn.getEmailAddress().setValue(phone.getEmail());
       }
       if (phone.getRawValue() != null) {
           xtn.getTelephoneNumber().setValue(phone.getRawValue());
       }
   }

    /**
     * Extracts only digits from a string and then clip it to a specific length.
     *
     * @param value the string to make numeric and clip
     * @param size the maximum length allowed
     * @return The clipped string
     */
    private String clipNumber(String value, int size) {
        try {
            Integer.parseInt(value);
            return clip(value, size);
        } catch (NumberFormatException e) {
            log.warn("Extracting digits from non-numeric value '" + value + "'");
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<value.length(); i++) {
                char c = value.charAt(i);
                if (Character.isDigit(c)) sb.append(c);
            }
            return sb.toString();
        }
    }

   /**
    * Clips a string to a specific length.
    *
    * @param value the string to clip
    * @param size the maximum length allowed
    * @return The clipped string
    */
   private String clip(String value, int size) {
       if (value == null) return null;
       if (value.length() <= size) return value;
       log.warn("Clipping the value '" + value + "' to length " + size);
       return value.substring(0, size);
   }


	/**
	 * Throws a new patient exception and log it as well.
	 *
	 * @param message the exception message to log
	 * @throws PatientException
	 */
	private void throwPatientException(String message) throws PatientException {
	    log.error(message);
	    throw new PatientException(message);
	}
	
	/* 
	 * Whether to handle Pagination (or Continuation Query) by OpenPIXPDQ or EMPI. 
	 * 
	 * @return <code>true</code> if continuation query is processed by OpenPIXPDQ;
	 *         otherwise <code>false</code>. Defaults to true (namely, by OpenPIXPDQ).
	 * @throw IheConfigureException if anything wrong with configuration
	 */
	private boolean isContinuationQueryByOpenPixPdq(){
		boolean  pagingByOpenPixPdq = true; //defaults to true(by OpenPIXPDQ). 
		  try {
			  String pagination =  Configuration.getPropertySetValue(connection, "QueryProperties","ContinuationQueryByOpenPIXPDQ" , false);
			  pagingByOpenPixPdq = Boolean.parseBoolean(pagination); 
			} catch (IheConfigurationException e) {
				System.out.println(e);
			}
		return pagingByOpenPixPdq;
	}
	
	/*
	 * Return the patient list for specified return domain otherwise it return 
	 * all patient list.
	 * 
	 * @param pdqresult contains all matching patients
	 * @param returnDomains the domains whose patient demographics to be 
	 *        returned to the PDQ consumer
	 * @return List<List<Patient>> a list of filtered patient by return domains. The
	 * first list for different logic patients, while the second list is for the same 
	 * logic patient in different domains.
	 */
	private List<List<Patient>> getPatientList(PdqResult pdqResult,List<Identifier> returnDomains){
		List<List<Patient>>  finalPatients = new ArrayList<List<Patient>>();
		
		  //pdqResult can never be null, otherwise exception would be thrown. 
        List<List<Patient>> allPatients = pdqResult.getPatients();
        // List<List<Patient>> finalPatients = new ArrayList<List<Patient>>();
        if (returnDomains.size() == 0) {
            //If no return domain is specified, we consider all patients
           finalPatients= allPatients;
        }
        else {
            //Find a list of final patients that have ids in the return domain.
            for (List<Patient> lpatients : allPatients) {
                List<Patient> filteredPatients = new ArrayList<Patient>();
                for (Patient patient : lpatients) {
                   List<PatientIdentifier> pids = patient.getPatientIds();
                   for (PatientIdentifier pid : pids) {
                 	  Identifier authority = pid.getAssigningAuthority();
                       //authority might be partial (either namespaceId or universalId),
                       //so need to map to the one used in the configuration. 
                       authority = AssigningAuthorityUtil.reconcileIdentifier( authority, actor.getActorDescription(), pdqAdapter );
                       if (returnDomains.contains( authority )) {
                           filteredPatients.add( patient );
                           break;
                       }                	  
                   }
                }
                //We don't want an empty list of patient 
                if (filteredPatients.size() > 0)
                   finalPatients.add( filteredPatients );
            }
        }
		return finalPatients;
	}
	
	/**
	 * Gets the sub list from the parent list.
	 * 
	 * @param start the start index
	 * @param end the end index
	 * @param parentList the parent list
	 * @return a sublit with the given start and end indexes
	 */
	private List<List<Patient>> getSubList(int start, int end, List<List<Patient>> parentList){
    	List<List<Patient>> returnList = new ArrayList<List<Patient>>();
    	for(int j = start ; j<end; j++){
    	     List<Patient> list = parentList.get(j);
    	     returnList.add(list);
    	}
    	return returnList;
	}
}

