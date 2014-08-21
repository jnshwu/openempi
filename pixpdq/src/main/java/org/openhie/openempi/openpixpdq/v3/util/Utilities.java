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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.MCCIMT000200UV01Agent;
import org.hl7.v3.MCCIMT000200UV01Device;
import org.hl7.v3.MCCIMT000200UV01Organization;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.hl7.v3.MCCIMT000200UV01TargetMessage;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01AcknowledgementDetail;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.MCCIMT000300UV01TargetMessage;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.STExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PhoneNumber;

public class Utilities
{
	private static final Log log = LogFactory.getLog(Utilities.class);

	public static TSExplicit generateCreationTime() {
		String timestamp = "";
		TSExplicit creationTime = new TSExplicit();

		try {
			GregorianCalendar today = new GregorianCalendar(TimeZone
					.getTimeZone("GMT"));

			timestamp = String.valueOf(today.get(GregorianCalendar.YEAR))
					+ String.valueOf(today.get(GregorianCalendar.MONTH) + 1)
					+ String.valueOf(today.get(GregorianCalendar.DAY_OF_MONTH))
					+ String.valueOf(today.get(GregorianCalendar.HOUR_OF_DAY))
					+ String.valueOf(today.get(GregorianCalendar.MINUTE))
					+ String.valueOf(today.get(GregorianCalendar.SECOND));
		} catch (Exception e) {
			log.error("Exception when creating Gregorian Date");
			log.error(" message: " + e.getMessage());
		}

		if (isNotNullish(timestamp)) {
			log.debug("Setting the creation timestamp to " + timestamp);
			creationTime.setValue(timestamp);
		}
		return creationTime;
	}

	public static MCCIMT000300UV01Receiver generateMCCIMT00300UV01Receiver(MCCIMT000100UV01Sender sender) {
		
		MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
		II oid = null;

		receiver.setTypeCode(CommunicationFunctionType.RCV);

		if (sender.getDevice() != null &&
				sender.getDevice().getAsAgent() != null &&
				sender.getDevice().getAsAgent().getValue() != null &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
				isNotNullish(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
				isNotNullish(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
			oid = sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0);
		}

		String senderDeviceIdRoot = "1.2.345.678.999";
		if (sender.getDevice() != null &&
				sender.getDevice().getId() != null &&
				sender.getDevice().getId().size() > 0 &&
				sender.getDevice().getId().get(0) != null &&
				sender.getDevice().getId().get(0).getRoot() != null) {
			senderDeviceIdRoot = sender.getDevice().getId().get(0).getRoot();
		}
		
		MCCIMT000300UV01Device receiverDevice = new MCCIMT000300UV01Device();
		receiverDevice.setClassCode(EntityClassDevice.DEV);
		receiverDevice.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
		log.debug("Setting receiver application to " + senderDeviceIdRoot);
		receiverDevice.getId().add(generateHl7Id(senderDeviceIdRoot, null));

		if (oid != null) {
			MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
			MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
			org.setClassCode(ConversionConstants.ENTITY_CLASS_CODE_ORG);
			org.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
			org.getId().add(oid);
	
			javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
			JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
			agent.setRepresentedOrganization(orgElem);
			agent.getClassCode().add(ConversionConstants.ROLE_CLASS_AGENT);
	
			javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
			JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);
	
			receiverDevice.setAsAgent(agentElem);
		}
		receiver.setDevice(receiverDevice);
		return receiver;
	}

	public static MCCIMT000300UV01Receiver generateMCCIMT00300UV01Receiver(MCCIMT000300UV01Sender sender) {
		
		MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
		II oid = null;

		receiver.setTypeCode(CommunicationFunctionType.RCV);

		if (sender.getDevice() != null &&
				sender.getDevice().getAsAgent() != null &&
				sender.getDevice().getAsAgent().getValue() != null &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
				isNotNullish(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
				isNotNullish(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
			oid = sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0);
		}

		String senderDeviceIdRoot = "1.2.345.678.999";
		if (sender.getDevice() != null &&
				sender.getDevice().getId() != null &&
				sender.getDevice().getId().size() > 0 &&
				sender.getDevice().getId().get(0) != null &&
				sender.getDevice().getId().get(0).getRoot() != null) {
			senderDeviceIdRoot = sender.getDevice().getId().get(0).getRoot();
		}
		
		MCCIMT000300UV01Device receiverDevice = new MCCIMT000300UV01Device();
		receiverDevice.setClassCode(EntityClassDevice.DEV);
		receiverDevice.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
		log.debug("Setting receiver application to " + senderDeviceIdRoot);
		receiverDevice.getId().add(generateHl7Id(senderDeviceIdRoot, null));

		MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
		MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
		org.setClassCode(ConversionConstants.ENTITY_CLASS_CODE_ORG);
		org.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
		org.getId().add(oid);

		javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
		JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
		agent.setRepresentedOrganization(orgElem);
		agent.getClassCode().add(ConversionConstants.ROLE_CLASS_AGENT);

		javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
		JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

		receiverDevice.setAsAgent(agentElem);
		receiver.setDevice(receiverDevice);
		return receiver;
	}

	public static MCCIMT000200UV01Receiver generateMCCIMT00200UV01Receiver(MCCIMT000300UV01Sender sender) {
		
		MCCIMT000200UV01Receiver receiver = new MCCIMT000200UV01Receiver();
		II oid = null;

		receiver.setTypeCode(CommunicationFunctionType.RCV);

		if (sender.getDevice() != null &&
				sender.getDevice().getAsAgent() != null &&
				sender.getDevice().getAsAgent().getValue() != null &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
				isNotNullish(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
				sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
				isNotNullish(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
			oid = sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0);
		}

		String senderDeviceIdRoot = "1.2.345.678.999";
		if (sender.getDevice() != null &&
				sender.getDevice().getId() != null &&
				sender.getDevice().getId().size() > 0 &&
				sender.getDevice().getId().get(0) != null &&
				sender.getDevice().getId().get(0).getRoot() != null) {
			senderDeviceIdRoot = sender.getDevice().getId().get(0).getRoot();
		}
		
		MCCIMT000200UV01Device receiverDevice = new MCCIMT000200UV01Device();
		receiverDevice.setClassCode(EntityClassDevice.DEV);
		receiverDevice.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
		log.debug("Setting receiver application to " + senderDeviceIdRoot);
		receiverDevice.getId().add(generateHl7Id(senderDeviceIdRoot, null));

		MCCIMT000200UV01Agent agent = new MCCIMT000200UV01Agent();
		MCCIMT000200UV01Organization org = new MCCIMT000200UV01Organization();
		org.setClassCode(ConversionConstants.ENTITY_CLASS_CODE_ORG);
		org.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
		org.getId().add(oid);

		javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
		JAXBElement<MCCIMT000200UV01Organization> orgElem = new JAXBElement<MCCIMT000200UV01Organization>(xmlqnameorg, MCCIMT000200UV01Organization.class, org);
		agent.setRepresentedOrganization(orgElem);
		agent.getClassCode().add(ConversionConstants.ROLE_CLASS_AGENT);

		javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
		JAXBElement<MCCIMT000200UV01Agent> agentElem = new JAXBElement<MCCIMT000200UV01Agent>(xmlqnameagent, MCCIMT000200UV01Agent.class, agent);

		receiverDevice.setAsAgent(agentElem);
		receiver.setDevice(receiverDevice);
		return receiver;
	}
	
	public static MCCIMT000300UV01Sender generateMCCIMT00300UV01Sender(List<MCCIMT000100UV01Receiver> receivers) {
		MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
		if (receivers == null || receivers.size() == 0 || receivers.get(0) == null) {
			log.error("Unable to generate message since receiver entry is missing: " + receivers);
			return sender;
		}
		MCCIMT000100UV01Receiver receiver = receivers.get(0);
        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setDeterminerCode("INSTANCE");

        II oid = null;
        if (receiver.getDevice() != null &&
        		receiver.getDevice().getAsAgent() != null &&
        		receiver.getDevice().getAsAgent().getValue() != null &&
        		receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
        		receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                isNotNullish(receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null) {
            oid = receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0);
        }

        MCCIMT000300UV01Device senderDevice = new MCCIMT000300UV01Device();
        senderDevice.setClassCode(EntityClassDevice.DEV);
        senderDevice.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
        String receiverDeviceIdRoot = "1.2.345.678.999";
        if (receiver.getDevice() != null &&
        		receiver.getDevice().getId() != null &&
        		receiver.getDevice().getId().size() > 0 &&
        		receiver.getDevice().getId().get(0) != null &&
        		receiver.getDevice().getId().get(0).getRoot() != null) {
        	receiverDeviceIdRoot = receiver.getDevice().getId().get(0).getRoot();
		}
        log.debug("Setting sender OID to " + receiverDeviceIdRoot);
        senderDevice.getId().add(generateHl7Id(receiverDeviceIdRoot, null));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(ConversionConstants.ENTITY_CLASS_CODE_ORG);
        org.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
        org.getId().add(oid);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(ConversionConstants.CLASS_CODE_AGENT);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);
        sender.setDevice(senderDevice);
		return sender;
	}

	public static MCCIMT000300UV01Sender generateMCCIMT00300UV01SenderByMCCIMT000300UV01Receiver(List<MCCIMT000300UV01Receiver> receivers) {
		MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
		if (receivers == null || receivers.size() == 0 || receivers.get(0) == null) {
			log.error("Unable to generate message since receiver entry is missing: " + receivers);
			return sender;
		}
		MCCIMT000300UV01Receiver receiver = receivers.get(0);
        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setDeterminerCode("INSTANCE");

        II oid = null;
        if (receiver.getDevice() != null &&
        		receiver.getDevice().getAsAgent() != null &&
        		receiver.getDevice().getAsAgent().getValue() != null &&
        		receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
        		receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                isNotNullish(receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null) {
            oid = receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0);
        }

        MCCIMT000300UV01Device senderDevice = new MCCIMT000300UV01Device();
        senderDevice.setClassCode(EntityClassDevice.DEV);
        senderDevice.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
        String receiverDeviceIdRoot = "1.2.345.678.999";
        if (receiver.getDevice() != null &&
        		receiver.getDevice().getId() != null &&
        		receiver.getDevice().getId().size() > 0 &&
        		receiver.getDevice().getId().get(0) != null &&
        		receiver.getDevice().getId().get(0).getRoot() != null) {
        	receiverDeviceIdRoot = receiver.getDevice().getId().get(0).getRoot();
		}
        log.debug("Setting sender OID to " + receiverDeviceIdRoot);
        senderDevice.getId().add(generateHl7Id(receiverDeviceIdRoot, null));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(ConversionConstants.ENTITY_CLASS_CODE_ORG);
        org.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
        org.getId().add(oid);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(ConversionConstants.CLASS_CODE_AGENT);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);
        sender.setDevice(senderDevice);
		return sender;
	}
	
	public static MCCIMT000200UV01Sender generateMCCIMT00200UV01Sender(List<MCCIMT000300UV01Receiver> receivers) {
		MCCIMT000200UV01Sender sender = new MCCIMT000200UV01Sender();
		if (receivers == null || receivers.size() == 0 || receivers.get(0) == null) {
			log.error("Unable to generate message since receiver entry is missing: " + receivers);
			return sender;
		}
		MCCIMT000300UV01Receiver receiver = receivers.get(0);
        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setDeterminerCode("INSTANCE");

        II oid = null;
        if (receiver.getDevice() != null &&
        		receiver.getDevice().getAsAgent() != null &&
        		receiver.getDevice().getAsAgent().getValue() != null &&
        		receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
        		receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                isNotNullish(receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null) {
            oid = receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0);
        }

        MCCIMT000200UV01Device senderDevice = new MCCIMT000200UV01Device();
        senderDevice.setClassCode(EntityClassDevice.DEV);
        senderDevice.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
        String receiverDeviceIdRoot = "1.2.345.678.999";
        if (receiver.getDevice() != null &&
        		receiver.getDevice().getId() != null &&
        		receiver.getDevice().getId().size() > 0 &&
        		receiver.getDevice().getId().get(0) != null &&
        		receiver.getDevice().getId().get(0).getRoot() != null) {
        	receiverDeviceIdRoot = receiver.getDevice().getId().get(0).getRoot();
		}
        log.debug("Setting sender OID to " + receiverDeviceIdRoot);
        senderDevice.getId().add(generateHl7Id(receiverDeviceIdRoot, null));

        MCCIMT000200UV01Agent agent = new MCCIMT000200UV01Agent();
        MCCIMT000200UV01Organization org = new MCCIMT000200UV01Organization();
        org.setClassCode(ConversionConstants.ENTITY_CLASS_CODE_ORG);
        org.setDeterminerCode(ConversionConstants.ENTITY_DETERMINER_CODE_INSTANCE);
        org.getId().add(oid);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000200UV01Organization> orgElem = new JAXBElement<MCCIMT000200UV01Organization>(xmlqnameorg, MCCIMT000200UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(ConversionConstants.CLASS_CODE_AGENT);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000200UV01Agent> agentElem = new JAXBElement<MCCIMT000200UV01Agent>(xmlqnameagent, MCCIMT000200UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);
        sender.setDevice(senderDevice);
		return sender;
	}
	
	public static MCCIMT000300UV01Acknowledgement generatedAcknowledgment(II messageId, String code, List<Identifier> invalidDomains) {
        MCCIMT000300UV01Acknowledgement ack = new MCCIMT000300UV01Acknowledgement();

        CS typeCode = new CS();
        typeCode.setCode(code);

        ack.setTypeCode(typeCode);
        
        if (messageId != null) {
        	MCCIMT000300UV01TargetMessage targetMessage = new MCCIMT000300UV01TargetMessage();
        	targetMessage.setId(messageId);
        	ack.setTargetMessage(targetMessage);
        }
        
        if(invalidDomains != null ) {
			for (Identifier identifier : invalidDomains) {
				MCCIMT000300UV01AcknowledgementDetail invalidDomain = HL7AckTransforms.createMCCIMT00300UV01AckDetail("Unknown Key Identifier");
				
	        	if (invalidDomain.getLocation() != null) {
	        		invalidDomain.getLocation().add(Utilities.generateSTExplicit("//controlActProcess/queryByParameter/parameterList/OtherIDsScopingOrgannization/value/@root="+identifier.getUniversalId()));
	        	}
	        	
	        	invalidDomain.setCode(Utilities.generateCe("204"));
	        	
	            ack.getAcknowledgementDetail().add(invalidDomain);       
		    }        	
        }
        
        return ack;
	}

	public static MCCIMT000200UV01Acknowledgement generatedAcknowledgment(II messageId, String code) {
        MCCIMT000200UV01Acknowledgement ack = new MCCIMT000200UV01Acknowledgement();

        CS typeCode = new CS();
        typeCode.setCode(code);

        ack.setTypeCode(typeCode);
        
        if (messageId != null) {
        	MCCIMT000200UV01TargetMessage targetMessage = new MCCIMT000200UV01TargetMessage();
        	targetMessage.setId(messageId);
        	ack.setTargetMessage(targetMessage);
        }
        
        return ack;
	}
	
	public static II generateHl7MessageId(String myDeviceId) {
		II messageId = new II();

		if (isNullish(myDeviceId)) {
			myDeviceId = getDefaultLocalDeviceId();
		}

		log.debug("Using local device id " + myDeviceId);
		messageId.setRoot(myDeviceId);
		messageId.setExtension(generateMessageId());
		return messageId;
	}

	public static CD generateCd(String code, String codeSystem) {
		CD cd = new CD();
		
		if (isNotNullish(code)) {
			cd.setCode(code);
		}
		
		if (isNotNullish(codeSystem)) {
			cd.setCodeSystem(codeSystem);
		}
		return cd;
	}

	public static PNExplicit generatePnExplicit(String firstName, String lastName) {
        log.debug("begin CreatePNExplicit");
        log.debug("firstName = " + firstName + "; lastName = " + lastName);
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = (PNExplicit) (factory.createPNExplicit());
        
        if (isNotNullish(lastName)) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);
            log.info("Setting Patient Lastname: " + lastName);
            name.getContent().add(factory.createPNExplicitFamily(familyName));
        }
        
        if (isNotNullish(firstName)) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);
            log.info("Setting Patient Firstname: " + firstName);
            name.getContent().add(factory.createPNExplicitGiven(givenName));
        }
        log.debug("end CreatePNExplicit");
        return name;		
	}
	
	public static ADExplicit generateADExplicit(boolean notOrdered, String street, String city, String state, String zip) {

		if (isNullish(street) && isNullish(city) && isNullish(state) && isNullish(zip)) {
			return null;
		}

		org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
		ADExplicit address = (ADExplicit) (factory.createADExplicit());
		List<Serializable> addrlist = address.getContent();

		if (isNotNullish(street)) {
			AdxpExplicitStreetAddressLine streetHl7 = new AdxpExplicitStreetAddressLine();
			streetHl7.setContent(street);
			addrlist.add(factory.createADExplicitStreetAddressLine(streetHl7));
		}

		if (isNotNullish(city)) {
			AdxpExplicitCity cityHl7 = new AdxpExplicitCity();
			cityHl7.setContent(city);
			addrlist.add(factory.createADExplicitCity(cityHl7));
		}

		if (isNotNullish(state)) {
			AdxpExplicitState stateHl7 = new AdxpExplicitState();
			stateHl7.setContent(state);
			addrlist.add(factory.createADExplicitState(stateHl7));
		}

		if (isNotNullish(zip)) {
			AdxpExplicitPostalCode zipHl7 = new AdxpExplicitPostalCode();
			zipHl7.setContent(zip);
			addrlist.add(factory.createADExplicitPostalCode(zipHl7));
		}

		return address;
	}
	
	public static INT generateINT(int value) {
		INT iType = new INT();
		String strValue = Integer.toString(value);
		BigInteger bigInt = new BigInteger(strValue);
		iType.setValue(bigInt);
		return iType;
	}
	
    public static ADExplicit generateADExplicit(String street, String city, String state, String zip) {
        return generateADExplicit(false, street, city, state, zip);
    }
    
	public static TELExplicit generateTELExplicit(Patient person) {
		TELExplicit phone = new TELExplicit();
		StringBuffer number = new StringBuffer(ConversionConstants.TELECOM_URL_SCHEME);
		PhoneNumber phoneNumber = person.getPhoneNumbers().get(0);
		if (phoneNumber.getCountryCode() != null) {
			number.append("+").append(phoneNumber.getCountryCode()).append("-");
		}
		if (phoneNumber.getAreaCode() != null) {
			number.append(phoneNumber.getAreaCode()).append("-");
		}		
		number.append(phoneNumber.getNumber());
		
		if (phoneNumber.getExtension() != null) {
			number.append(";ext=").append(phoneNumber.getExtension());
		}
		phone.setValue(number.toString());
		
		phone.getUse().add("H");
		return phone;
	}

	public static CS generateCs(String code) {
		CS cs = new CS();

		if (isNotNullish(code)) {
			log.debug("Setting the code attribute of CS " + code);
			cs.setCode(code);
		}

		return cs;
	}

	public static CE generateCe(String code) {
		return generateCe(code, null);
	}
	
	public static CE generateCe(String code, String codeSystem) {
		CE ce = new CE();
		if (code == null) {
			ce.getNullFlavor().add(ConversionConstants.NULL_FLAVOR);
		} else {
			ce.setCode(code);
		}
		if (isNotNullish(codeSystem)) {
			ce.setCodeSystem(codeSystem);
		}
		return ce;
	}

	public static II generateHl7Id(String root, String extension) {
		return generateHl7Id(root, extension, null);
	}
	
	public static STExplicit generateSTExplicit(String message) {
		STExplicit st = new STExplicit();
		st.getContent().add(message);
		return st;
	}
	
	public static II generateHl7Id(String root, String extension, String nullFlavor) {
		II id = new II();

		if (isNotNullish(root)) {
			id.setRoot(root);
		}

		if (isNotNullish(extension)) {
			id.setExtension(extension);
		}

		if (isNotNullish(nullFlavor)) {
			id.getNullFlavor().add(nullFlavor);
		}
		
		return id;
	}

	public static TSExplicit generateTSExplicit(Date dateOfBirth) {
		TSExplicit time = new TSExplicit();
		if (dateOfBirth == null) {
			return time;
		}
		StringBuffer buffer = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateOfBirth);
		buffer.append(cal.get(Calendar.YEAR));
		int month = cal.get(Calendar.MONTH) + 1;
		// Zero pad the month if it is one digit long
		if (month < 10) {
			buffer.append("0");
		}
		buffer.append(month);
		// Zero pad the day if it is one digit long
		if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
			buffer.append("0");
		}
		buffer.append(cal.get(Calendar.DAY_OF_MONTH));
		time.setValue(buffer.toString());
		log.debug("Converted " + dateOfBirth + " to " + time.getValue());
		return time;
	}
	
	public static II generateHl7MessageId() {
		String deviceId = getDefaultLocalDeviceId();
		return generateHl7MessageId(deviceId);
	}

	private static String getDefaultLocalDeviceId() {
		return ConversionConstants.DEFAULT_LOCAL_DEVICE_ID;
	}

	public static String generateMessageId() {
		java.rmi.server.UID uid = new java.rmi.server.UID();
		log.debug("generated message id=" + uid.toString());
		return uid.toString();
	}

	public static boolean isNullish(String value) {
		boolean result = false;
		if ((value == null) || (value.contentEquals(""))) {
			result = true;
		}
		return result;
	}

	public static boolean isNotNullish(String value) {
		return (!isNullish(value));
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNullish(java.util.List value) {
		boolean result = false;
		if ((value == null) || (value.size() == 0)) {
			result = true;
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNotNullish(java.util.List value) {
		return (!isNullish(value));
	}
	
	public static String objectMessageToString(Object message) {
		if (message == null) {
			return "";
		}
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(message.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();	 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			OutputStream outputStream = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(message, outputStream);
			return outputStream.toString();
		} catch (JAXBException e) {
			log.warn("Unable to serialize a message into a string: " + e, e);
			throw new RuntimeException(e);
		}
		
	}
	public static void logMessageObject(Object message) {
		String messageString = objectMessageToString(message);
		log.info("Generated message:\n" + messageString.toString());
	}
}
