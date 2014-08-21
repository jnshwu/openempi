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

import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Agent;
import org.hl7.v3.MCCIMT000200UV01Device;
import org.hl7.v3.MCCIMT000200UV01Organization;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Receiver;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7ReceiverTransforms {
    
    private static Log log = LogFactory.getLog(HL7DataTransformHelper.class);
    
    public static MCCIMT000200UV01Receiver createMCCIMT000200UV01Receiver(String OID) {
        MCCIMT000200UV01Receiver receiver = new MCCIMT000200UV01Receiver();

        // Check the input parameter
        if (OID == null) {
            OID="";
        }

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000200UV01Device receiverDevice = new MCCIMT000200UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        receiverDevice.setClassCode(EntityClassDevice.DEV);
        log.debug("Setting receiver application to 1.2.345.678.999");
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory("1.2.345.678.999"));

        MCCIMT000200UV01Agent agent = new MCCIMT000200UV01Agent();
        MCCIMT000200UV01Organization org = new MCCIMT000200UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(OID);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000200UV01Organization> orgElem = new JAXBElement<MCCIMT000200UV01Organization>(xmlqnameorg, MCCIMT000200UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000200UV01Agent> agentElem = new JAXBElement<MCCIMT000200UV01Agent>(xmlqnameagent, MCCIMT000200UV01Agent.class, agent);

        receiverDevice.setAsAgent(agentElem);

        receiver.setDevice(receiverDevice);

        return receiver;
    }
    
    public static MCCIMT000200UV01Receiver createMCCIMT000200UV01Receiver(String applicationOid, String facilityOid) {
        MCCIMT000200UV01Receiver receiver = new MCCIMT000200UV01Receiver();

        // Check the input parameters
        if (facilityOid == null) {
            facilityOid="";
        }
        if (applicationOid == null) {
            applicationOid="";
        }

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000200UV01Device receiverDevice = new MCCIMT000200UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        receiverDevice.setClassCode(EntityClassDevice.DEV);
        log.debug("Setting receiver application to " + applicationOid);
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory(applicationOid));

        if (facilityOid != null && facilityOid.length() > 0) {
	        MCCIMT000200UV01Agent agent = new MCCIMT000200UV01Agent();
	        MCCIMT000200UV01Organization org = new MCCIMT000200UV01Organization();
	        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
	        org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
	        II id = HL7DataTransformHelper.IIFactory(facilityOid);
	        org.getId().add(id);
	
	        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
	        JAXBElement<MCCIMT000200UV01Organization> orgElem = new JAXBElement<MCCIMT000200UV01Organization>(xmlqnameorg, MCCIMT000200UV01Organization.class, org);
	        agent.setRepresentedOrganization(orgElem);
	        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);
	
	        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
	        JAXBElement<MCCIMT000200UV01Agent> agentElem = new JAXBElement<MCCIMT000200UV01Agent>(xmlqnameagent, MCCIMT000200UV01Agent.class, agent);
	
	        receiverDevice.setAsAgent(agentElem);
        }
        
        receiver.setDevice(receiverDevice);

        return receiver;
    }
    
    public static MCCIMT000100UV01Receiver createMCCIMT000100UV01Receiver(String applicationOid, String facilityOid) {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000100UV01Device receiverDevice = new MCCIMT000100UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        receiverDevice.setClassCode(EntityClassDevice.DEV);
        log.debug("Setting receiver application to " + applicationOid);
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory(applicationOid));

        MCCIMT000100UV01Agent agent = new MCCIMT000100UV01Agent();
        MCCIMT000100UV01Organization org = new MCCIMT000100UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(facilityOid);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000100UV01Organization> orgElem = new JAXBElement<MCCIMT000100UV01Organization>(xmlqnameorg, MCCIMT000100UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000100UV01Agent> agentElem = new JAXBElement<MCCIMT000100UV01Agent>(xmlqnameagent, MCCIMT000100UV01Agent.class, agent);

        receiverDevice.setAsAgent(agentElem);

        receiver.setDevice(receiverDevice);

        return receiver;
    }

    public static MCCIMT000300UV01Receiver createMCCIMT000300UV01Receiver(String receiverAppOid, String receiverFacilityOid) {
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000300UV01Device receiverDevice = new MCCIMT000300UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        receiverDevice.setClassCode(EntityClassDevice.DEV);
        log.debug("Setting receiver application to " + receiverAppOid);
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory(receiverAppOid));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(receiverFacilityOid);
        org.getId().add(id);

        if (Utilities.isNotNullish(receiverFacilityOid)) {
	        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
	        JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
	        agent.setRepresentedOrganization(orgElem);
	        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);
	
	        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
	        JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);
	
	        receiverDevice.setAsAgent(agentElem);
        }
        receiver.setDevice(receiverDevice);
        
        return receiver;
    }

}
