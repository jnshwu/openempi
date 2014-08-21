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
package org.openhealthtools.openpixpdq.impl.v2.hl7;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IBaseDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openpixpdq.api.IPixPdqAdapter;
import org.openhealthtools.openpixpdq.common.AssigningAuthorityUtil;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.GenericComposite;
import ca.uhn.hl7v2.model.GenericPrimitive;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v25.datatype.DTM;
import ca.uhn.hl7v2.model.v25.datatype.ERL;
import ca.uhn.hl7v2.model.v25.datatype.HD;
import ca.uhn.hl7v2.model.v25.segment.DSC;
import ca.uhn.hl7v2.model.v25.segment.ERR;
import ca.uhn.hl7v2.model.v25.segment.MSA;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.QAK;

/**
 * The utility class for HL7 v2.5 messages.
 * 
 * @author Jim Firby
 * @version 1.0 - Nov 22, 2005
 */
public class HL7v25 {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
    public static final String birhtdateFormat = "yyyyMMdd";
  /**
   * Populates an HL7 v2.5 MSH segment according to the IHE standard
   *
   * @param msh the MSH segment
   * @param type the type of message the segment belongs to
   * @param event the event that triggered this message
   * @param id the message control ID for this message
   * @throws DataTypeException When supplied data is inappropriate
   * @throws IheConfigurationException When the connection to which this will be sent if not configured properly
   */
    public static void populateMSH(MSH msh, String type, String event, String id, IConnectionDescription connection) throws DataTypeException, IheConfigurationException {
        // MSH-1
        msh.getFieldSeparator().setValue("|");
        // MSH-2
        msh.getEncodingCharacters().setValue("^~\\&");
        // MSH-3
        Identifier identifier = Configuration.getIdentifier(connection, "SendingApplication", true);
        HD hd = msh.getSendingApplication();
        hd.getNamespaceID().setValue(identifier.getNamespaceId());
        hd.getUniversalID().setValue(identifier.getUniversalId());
        hd.getUniversalIDType().setValue(identifier.getUniversalIdType());
        // MSH-4
        identifier = Configuration.getIdentifier(connection, "SendingFacility", true);
        hd = msh.getSendingFacility();
        hd.getNamespaceID().setValue(identifier.getNamespaceId());
        hd.getUniversalID().setValue(identifier.getUniversalId());
        hd.getUniversalIDType().setValue(identifier.getUniversalIdType());
        // MSH-5
        identifier = Configuration.getIdentifier(connection, "ReceivingApplication", true);
        hd = msh.getReceivingApplication();
        hd.getNamespaceID().setValue(identifier.getNamespaceId());
        hd.getUniversalID().setValue(identifier.getUniversalId());
        hd.getUniversalIDType().setValue(identifier.getUniversalIdType());
        // MSH-6
        identifier = Configuration.getIdentifier(connection, "ReceivingFacility", true);
        hd = msh.getReceivingFacility();
        hd.getNamespaceID().setValue(identifier.getNamespaceId());
        hd.getUniversalID().setValue(identifier.getUniversalId());
        hd.getUniversalIDType().setValue(identifier.getUniversalIdType());
        // MSH-7
        msh.getDateTimeOfMessage().getTime().setValue(formatter.format(new Date()));
        // MSH-9
        msh.getMessageType().getMessageCode().setValue(type);
        msh.getMessageType().getTriggerEvent().setValue(event);
        // MSH-10
        msh.getMessageControlID().setValue(id);
        // MSH-11
        msh.getProcessingID().getProcessingID().setValue("P");
        // MSH-12
        msh.getVersionID().getVersionID().setValue("2.5");
    }

  /**
   * Populates an HL7 v2.5 MSH segment according to the IHE standard
   *
   * @param msh the MSH segment
   * @param type the type of message the segment belongs to
   * @param event the event that triggered this message
   * @param id the message control ID for this message
   * @param sendingApplication the sending application
   * @param sendingFacility the sending facility
   * @param receivingApplication the receiving application
   * @param receivingFacility the receiving facility
   * @throws DataTypeException When supplied data is inappropriate
   */
    public static void populateMSH(MSH msh, String type, String event, String structure, String id, Identifier sendingApplication, Identifier sendingFacility,
                                   Identifier receivingApplication, Identifier receivingFacility) throws DataTypeException {
        // MSH-1
        msh.getFieldSeparator().setValue("|");
        // MSH-2
        msh.getEncodingCharacters().setValue("^~\\&");
        // MSH-3
        HD hd = msh.getSendingApplication();
        hd.getNamespaceID().setValue( sendingApplication.getNamespaceId() );
        hd.getUniversalID().setValue( sendingApplication.getUniversalId() );
        hd.getUniversalIDType().setValue( sendingApplication.getUniversalIdType() );
        // MSH-4
        hd = msh.getSendingFacility();
        hd.getNamespaceID().setValue( sendingFacility.getNamespaceId() );
        hd.getUniversalID().setValue( sendingFacility.getUniversalId() );
        hd.getUniversalIDType().setValue( sendingFacility.getUniversalIdType() );
        // MSH-5
        hd = msh.getReceivingApplication();
        hd.getNamespaceID().setValue( receivingApplication.getNamespaceId() );
        hd.getUniversalID().setValue( receivingApplication.getUniversalId() );
        hd.getUniversalIDType().setValue( receivingApplication.getUniversalIdType() );
        // MSH-6
        hd = msh.getReceivingFacility();
        hd.getNamespaceID().setValue( receivingFacility.getNamespaceId() );
        hd.getUniversalID().setValue( receivingFacility.getUniversalId() );
        hd.getUniversalIDType().setValue( receivingFacility.getUniversalIdType());
        // MSH-7
        msh.getDateTimeOfMessage().getTime().setValue(formatter.format(new Date()));
        // MSH-9
        msh.getMessageType().getMessageCode().setValue(type);
        msh.getMessageType().getTriggerEvent().setValue(event);
        msh.getMessageType().getMessageStructure().setValue(structure);
        // MSH-10
        msh.getMessageControlID().setValue(id);
        // MSH-11
        msh.getProcessingID().getProcessingID().setValue("P");
        // MSH-12
        msh.getVersionID().getVersionID().setValue("2.5");
    }

    /**
     * Updates an HL7 v2.5 message header for a repeat use.
     *
     * @param msh the message header to update
     * @param id the new message control id for this header
     * @throws DataTypeException When the date/time cannot be set
     */
    public static void updateMSH(MSH msh, String id) throws DataTypeException {
        // MSH-7
        msh.getDateTimeOfMessage().getTime().setValue(formatter.format(new Date()));
        // MSH-10
        msh.getMessageControlID().setValue(id);
    }

    /**
     * Creates a human-readable string our of an HL7 ERR segment.
     *
     * @param err the ERR segment
     * @param type use either "PIX" or "PDQ";
     * @return the error string
     */
    public static String getErrorString(ERR err, String type) {
        if (err != null) {
            String code = err.getHL7ErrorCode().getIdentifier().getValue();
            String text = err.getHL7ErrorCode().getText().getValue();
            String user = err.getUserMessage().getValue();
            String severity = err.getSeverity().getValue();
            if (text == null) {
                if (code == null) text = "Unspecified error";
                else if (code.equals("100")) text = "Segment sequence error";
                else if (code.equals("101")) text = "Required segment missing";
                else if (code.equals("102")) text = "Data type error";
                else if (code.equals("103")) text = "Table value not found";
                else if (code.equals("200")) text = "Unsupported message type";
                else if (code.equals("201")) text = "Unsupported event code";
                else if (code.equals("202")) text = "Unsupported processing id";
                else if (code.equals("203")) text = "Unsupported version id";
                else if (code.equals("204")) text = "Unknown key identifier";
                else if (code.equals("205")) text = "Duplicate key identifier";
                else if (code.equals("206")) text = "Application record locked";
                else if (code.equals("207")) text = "Application internal error";
                else	text = "Unspecified error";
            }
            String error = null;
            if ((severity == null) || (severity.equalsIgnoreCase("E"))) error = type + " Error ";
            else if (severity.equalsIgnoreCase("W")) error = type + " Warning ";
            else error = "";
            if (code != null) error = error + "(" + code + ") ";
            if (text != null) error = error + text;
            if (user != null) error = error + ": " + user;
            return error;
        } else {
            return "Unknown error";
        }
    }

    /**
     * Turns an HL7 v2.5 DTM structure into a Java Date object.
     *
     * @param dtm the DTM structure
     * @param doTime True if the time should be included in the result
     * @return the Date object holding the DTM date/time
     * @throws DataTypeException When there is a problem extracting the HL7 data from the DTM
     */
    public static Date unformatDTM(DTM dtm, boolean doTime) throws DataTypeException {
        if (dtm == null) return null;
        // Convert to a Date
        if (doTime) {
            return HL7v231.buildDateFromInts(
                    dtm.getYear(), dtm.getMonth(), dtm.getDay(),
                    dtm.getHour(), dtm.getMinute(), dtm.getSecond(),
                    dtm.getGMTOffset(), true);
        } else {
            return HL7v231.buildDateFromInts(
                    dtm.getYear(), dtm.getMonth(), dtm.getDay(),
                    0, 0, 0, 0, false);
        }
    }

    /**
     * Populates MSA segment, used by, for example, PIX Query response
     *
     * @param msa the Message Acknowledgment segment
     * @param acknowledgmentCode the two letter of acknowledgment code
     * @param messageControlId the message control Id
     * @throws DataTypeException When MSA segment values cannot be set
     */
    public static void populateMSA(MSA msa, String acknowledgmentCode, String messageControlId) throws DataTypeException {
         //MSA-1
         msa.getAcknowledgmentCode().setValue(acknowledgmentCode);
         //MSA-2
         msa.getMessageControlID().setValue( messageControlId );
    }
    /**
     * Populates QAK segment.
     *
     * @param qak  the query acknowledgment segment
     * @param queryTag the query tag
     * @param responseStatus the response status
     * @param totalNum the total number of records found by this query
     * @param thisNum the the number of records returned in this pay-load
     * @param remainingNum the remaining number of records yet to be returned
     * @throws DataTypeException When QAK segment values cannot be set.
     */
    public static void populateQAK(QAK qak, String queryTag, String responseStatus,
    		int totalNum, int thisNum, int remainingNum) throws DataTypeException {
         populateQAK(qak, queryTag, responseStatus); 
         //QAK-4 Hit Count Total
         qak.getHitCount().setValue(Integer.toString(totalNum));
         //QAK-5 This Pay-load number
         qak.getThisPayload().setValue(Integer.toString(thisNum));
         //QAK-6 Remaining number
         qak.getHitsRemaining().setValue(Integer.toString(remainingNum));         
    }
    /**
     * Populates QAK segment.
     *
     * @param qak  The query acknowledgment segment
     * @param queryTag The query tag
     * @param responseStatus The response status
     * @throws DataTypeException When QAK segment values cannot be set.
     */
    public static void populateQAK(QAK qak, String queryTag, String responseStatus) throws DataTypeException {
         //QAK-1
         qak.getQueryTag().setValue( queryTag );
         //QAK-2
         qak.getQueryResponseStatus().setValue( responseStatus);
    }

    /**
     * Populates DSC segment
     *
     * @param dsc the DSC segment to be populated
     * @param pointer the pointer id
     * @throws DataTypeException When DSC segment values cannot be set.
     */
    public static void populateDSC(DSC dsc, String pointer) throws DataTypeException {
        //DSC-1
        dsc.getContinuationPointer().setValue(pointer);
        //DSC-2
        dsc.getContinuationStyle().setValue("I");
    }

    /**
     * Populates ERR segment.
     *
     * @param err the ERR segment to be populated
     * @param segmentId the id of the segment that caused the error
     * @param sequence the sequence of the segment
     * @param fieldPosition the field position where the error is
     * @param fieldRepetition the repetition of the error field
     * @param componentNumber the component number in the error field
     * @param hl7ErrorCode the HL7 error code
     * @param hl7ErrorText the HL7 error text
     * @throws DataTypeException When ERR segment values cannot be set.
     * @throws HL7Exception When HL7 related issue happens
     */
    public static void populateERR(ERR err, String segmentId, String sequence, String fieldPosition, String fieldRepetition,
                                   String componentNumber, String hl7ErrorCode, String hl7ErrorText) throws DataTypeException, HL7Exception {
        ERL erl = err.getErrorLocation(0);
        //ERR-2
        erl.getSegmentID().setValue( segmentId );
        erl.getSegmentSequence().setValue( sequence );
        erl.getFieldPosition().setValue(fieldPosition);
        erl.getFieldRepetition().setValue(fieldRepetition);
        erl.getComponentNumber().setValue(componentNumber);
        //ERR-3
        err.getHL7ErrorCode().getIdentifier().setValue( hl7ErrorCode );
        err.getHL7ErrorCode().getText().setValue( hl7ErrorText );
        //ERR-4
        err.getSeverity().setValue("E");
    }

    /**
     * Extracts ID with assigning authority from a GenericComposite.
     *
     * @param composite a composite data
     * @param description the connection description
     * @return the {@link PatientIdentifier}
     * @throws DataTypeException
     */
    public static PatientIdentifier extractId(GenericComposite composite, IBaseDescription description, IPixPdqAdapter adapter) throws DataTypeException {
    	PatientIdentifier ret = new PatientIdentifier();
        //Get ID number
        Varies varies = (Varies)composite.getComponent(0);
        if (varies != null)
            ret.setId( ((GenericPrimitive)varies.getData()).getValue() );

        //Get assigning authority
        Identifier identifier = null;
        varies = (Varies)composite.getComponent(3);
        if (varies != null) {
            Type data = varies.getData();
            if (data instanceof GenericPrimitive) {
                //If the type is GenericPrimitive, only namespace is provided
                String namespaceId = ((GenericPrimitive)data).getValue();
                identifier = new Identifier(namespaceId, null, null);
            } else {
                varies =  (Varies)((GenericComposite)data).getComponent(0);
                String namespaceId = ((GenericPrimitive)varies.getData()).getValue();

                varies =  (Varies)((GenericComposite)data).getComponent(1);
                String universalId = ((GenericPrimitive)varies.getData()).getValue();

                varies =  (Varies)((GenericComposite)data).getComponent(2);
                String universalIdType = ((GenericPrimitive)varies.getData()).getValue();

                identifier = new Identifier(namespaceId, universalId, universalIdType);
            }
        }
        Identifier reconciled = AssigningAuthorityUtil.reconcileIdentifier(identifier, description, adapter);
        ret.setAssigningAuthority(reconciled);
        return ret;
    }

    /**
     * Extracts assigning authority from a GenericComposite. The GenericComposite has to be either PDQ-8 (in PDQ Query)
     * or PID-4 (in PIX Query).
     *
     * @param composite the composite data
     * @return the assigning authority
     * @throws DataTypeException if there is any data type error
     */
    public static Identifier extractAssigningAuthority(GenericComposite composite) throws DataTypeException {
            //Get assigning authority
            Identifier identifier = null;
            Varies varies = (Varies)composite.getComponent(3);
            if (varies == null) return null;

            Type data = varies.getData();
            if (data instanceof GenericPrimitive) {
                //If the type is GenericPrimitive, only namespace is provided
                String namespaceId = ((GenericPrimitive)data).getValue();
                identifier = new Identifier(namespaceId, null, null);
            } else {
                varies =  (Varies)((GenericComposite)data).getComponent(0);
                String namespaceId = ((GenericPrimitive)varies.getData()).getValue();

                varies =  (Varies)((GenericComposite)data).getComponent(1);
                String universalId = ((GenericPrimitive)varies.getData()).getValue();

                varies =  (Varies)((GenericComposite)data).getComponent(2);
                String universalIdType = ((GenericPrimitive)varies.getData()).getValue();

                identifier = new Identifier(namespaceId, universalId, universalIdType);
            }

            return identifier;
        }
    
}
