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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01AcknowledgementDetail;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UV02OtherIDs;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201302UV02OtherIDs;
import org.hl7.v3.PRPAMT201302UV02OtherIDsId;
import org.hl7.v3.PRPAMT201302UV02PatientId;
import org.hl7.v3.PRPAMT201302UV02PatientPatientPerson;
import org.hl7.v3.PRPAMT201303UV02OtherIDs;
import org.hl7.v3.PRPAMT201303UV02Person;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.datamodel.Address;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openexchange.datamodel.PersonName;
import org.openhealthtools.openexchange.datamodel.PhoneNumber;
import org.openhealthtools.openexchange.datamodel.SharedEnums.SexType;
import org.openhealthtools.openexchange.datamodel.SharedEnums.TelecomUseCode;
import org.openhealthtools.openpixpdq.api.Hl7MessageValidationException;
import org.openhealthtools.openpixpdq.api.IMessageStoreLogger;
import org.openhealthtools.openpixpdq.api.IheActorException;
import org.openhealthtools.openpixpdq.api.MessageStore;
import org.openhealthtools.openpixpdq.common.BaseHandler;
import org.openhealthtools.openpixpdq.common.Constants;
import org.openhealthtools.openpixpdq.common.PixPdqConfigurationLoader;
import org.openhie.openempi.openpixpdq.v3.util.HL7AckTransforms;
import org.openhie.openempi.openpixpdq.v3.util.HL7Constants;
import org.openhie.openempi.openpixpdq.v3.util.HL7Extractors;
import org.openhie.openempi.openpixpdq.v3.util.Utilities;

public class AbstractIheService extends BaseHandler
{
	protected final Log log = LogFactory.getLog(getClass());
    private static SimpleDateFormat hl7DateFormat = new SimpleDateFormat("yyyyMMdd");

	private String configurationFilename;
	private String actorName;

	public void init() {
		if (configurationFilename == null) {
			log.warn("Have not set a configuration file for the PIX/PDQ Managers; unable to initialize the system.");
			return;
		}
		PixPdqConfigurationLoader loader = PixPdqConfigurationLoader.getInstance();
		loader.loadProperties(new String[] {"openpixpdq.properties"} );
		if (!loader.isInitialized()) {
			try {
				loader.loadConfiguration(configurationFilename, true);
			} catch (IheConfigurationException e) {
				log.warn("Unable to initialize the configuration of the PIX/PDQ Managers: " + e, e);
			}
		}
	}
	
	protected Hl7HeaderAddress extractHeaderAddress(List<MCCIMT000100UV01Receiver> receiver, MCCIMT000100UV01Sender sender) {
		Hl7HeaderAddress addr = new Hl7HeaderAddress();
		String receiverOid="";
    	if (receiver != null && receiver.size() > 0 &&
    			receiver.get(0).getDevice() != null &&
    			receiver.get(0).getDevice().getId() != null && receiver.get(0).getDevice().getId().size() != 0 &&
    			Utilities.isNotNullish(receiver.get(0).getDevice().getId().get(0).getRoot())) {
    		receiverOid = receiver.get(0).getDevice().getId().get(0).getRoot();
    	}
    	addr.setReceiverApplication(receiverOid);

    	String receiverFacilityOid="";
    	if (receiver != null && 
    			receiver.get(0) != null &&
    			receiver.get(0).getDevice() != null) {
        	MCCIMT000100UV01Device device = receiver.get(0).getDevice();
        	if (device.getAsAgent() != null &&
            		device.getAsAgent().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size() > 0 &&
            		Utilities.isNotNullish(device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
        		receiverFacilityOid = device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        	}
    	}
    	addr.setReceiverFacility(receiverFacilityOid);
    	
    	String senderOid = "";
    	if (sender != null &&
    			sender.getDevice() != null &&
    			sender.getDevice().getId() != null && 
    			sender.getDevice().getId().size() != 0 &&
    			Utilities.isNotNullish(sender.getDevice().getId().get(0).getRoot())) {
    		senderOid = sender.getDevice().getId().get(0).getRoot();
    	}
    	addr.setSenderApplication(senderOid);

    	String senderFacilityOid="";
    	if (sender != null && 
    			sender.getDevice() != null) {
        	MCCIMT000100UV01Device device = sender.getDevice();
        	if (device.getAsAgent() != null &&
            		device.getAsAgent().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size() > 0 &&
            		Utilities.isNotNullish(device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
        		senderFacilityOid = device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        	}
    	}
    	addr.setSenderFacility(senderFacilityOid);
    	return addr;
	}
	
	protected Hl7HeaderAddress extractHeaderAddress(List<MCCIMT000300UV01Receiver> receiver, MCCIMT000300UV01Sender sender) {
		Hl7HeaderAddress addr = new Hl7HeaderAddress();
		String receiverOid="";
    	if (receiver != null && receiver.size() > 0 &&
    			receiver.get(0).getDevice() != null &&
    			receiver.get(0).getDevice().getId() != null && receiver.get(0).getDevice().getId().size() != 0 &&
    			Utilities.isNotNullish(receiver.get(0).getDevice().getId().get(0).getRoot())) {
    		receiverOid = receiver.get(0).getDevice().getId().get(0).getRoot();
    	}
    	addr.setReceiverApplication(receiverOid);

    	String receiverFacilityOid="";
    	if (receiver != null && 
    			receiver.get(0) != null &&
    			receiver.get(0).getDevice() != null) {
        	MCCIMT000300UV01Device device = receiver.get(0).getDevice();
        	if (device.getAsAgent() != null &&
            		device.getAsAgent().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size() > 0 &&
            		Utilities.isNotNullish(device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
        		receiverFacilityOid = device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        	}
    	}
    	addr.setReceiverFacility(receiverFacilityOid);
    	
    	String senderOid = "";
    	if (sender != null &&
    			sender.getDevice() != null &&
    			sender.getDevice().getId() != null && 
    			sender.getDevice().getId().size() != 0 &&
    			Utilities.isNotNullish(sender.getDevice().getId().get(0).getRoot())) {
    		senderOid = sender.getDevice().getId().get(0).getRoot();
    	}
    	addr.setSenderApplication(senderOid);

    	String senderFacilityOid="";
    	if (sender != null && 
    			sender.getDevice() != null) {
        	MCCIMT000300UV01Device device = sender.getDevice();
        	if (device.getAsAgent() != null &&
            		device.getAsAgent().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId() != null &&
            		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size() > 0 &&
            		Utilities.isNotNullish(device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
        		senderFacilityOid = device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        	}
    	}
    	addr.setSenderFacility(senderFacilityOid);
    	return addr;
	}

	protected MCCIIN000002UV01 generateAckResponse(II origMesgId, List<MCCIMT000100UV01Receiver> receiver, MCCIMT000100UV01Sender sender, IheActorException e) {
		Hl7HeaderAddress addr = extractHeaderAddress(receiver, sender);
    	MCCIIN000002UV01 ackMsg = HL7AckTransforms.createAckMessage(origMesgId, "", addr.getReceiverFacility(), addr.getReceiverApplication(), 
    			addr.getSenderFacility(), addr.getSenderApplication(), e);
		return ackMsg;
	}

	protected void validateSenderReceivingApplicationAndFacility(Hl7HeaderAddress addr, IActorDescription actorDescription) throws Hl7MessageValidationException {
		// Verify that receiving application OID is not null
		boolean validateApplication = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_APPLICATION);
		if (validateApplication) {
			if (Utilities.isNullish(addr.getReceiverApplication())) {
				Hl7MessageValidationException e = new Hl7MessageValidationException("Missing Receiving Application OID");
				log.warn("Received a message with a missing receiver application OID");
				e.setLocation("//sender/device/id/@root");
				throw e;
			}
	    	String myApplicationId = actorDescription.getIdentifier("ReceivingApplication").getUniversalId();
	    	if (!myApplicationId.equals(addr.getReceiverApplication())) {
				Hl7MessageValidationException e = new Hl7MessageValidationException("Missing Receiving Application OID");
				log.warn("Received a message with an invalid receiver application OID: " + addr.getReceiverApplication());
				e.setLocation("//sender/device/id/@root");
				throw e;
	    	}
		}
		boolean validateFacility = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_FACILITY);
		if (validateFacility) {
			if (Utilities.isNullish(addr.getReceiverFacility())) {
				Hl7MessageValidationException e = new Hl7MessageValidationException("Missing Receiving Facility OID");
				log.warn("Received a message with a missing receiver facility OID");
				e.setLocation("//sender/device/asAgent/representedOrganization/id/@root");
				throw e;
			}
			String myFacilityOid = actorDescription.getIdentifier("ReceivingFacility").getUniversalId();
	    	if (!myFacilityOid.equals(addr.getReceiverFacility())) {
				Hl7MessageValidationException e = new Hl7MessageValidationException("Invalid Receiving Facility OID");
				log.warn("Received a message with an invalid receiver facility OID: " + addr.getReceiverFacility());
				e.setLocation("//sender/device/asAgent/representedOrganization/id/@root");
				throw e;
	    	}			
		}
	}

	protected boolean validateReceivingApplicationAndFacility(List<MCCIMT000100UV01Receiver> receiver,  IActorDescription actorDescription, List<MCCIMT000200UV01AcknowledgementDetail> errors) {
		// Verify that receiving application OID is not null
		boolean validateApplication = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_APPLICATION);
		if (validateApplication) {
	    	if (receiver == null || receiver.size() == 0 ||
	    			receiver.get(0).getDevice() == null ||
	    			receiver.get(0).getDevice().getId() == null || receiver.get(0).getDevice().getId().size() == 0 ||
	    			Utilities.isNullish(receiver.get(0).getDevice().getId().get(0).getRoot())) {
	    		MCCIMT000200UV01AcknowledgementDetail error = 
	    				HL7AckTransforms.createAckDetail("Missing Receiving Application OID", "//sender/device/id/@root");
	    		errors.add(error);
	    		return false;
	    	}
	    	String receiverApplicationOid = receiver.get(0).getDevice().getId().get(0).getRoot();
	    	String myApplicationId = actorDescription.getIdentifier("ReceivingApplication").getUniversalId();
	    	if (!myApplicationId.equals(receiverApplicationOid)) {
	    		MCCIMT000200UV01AcknowledgementDetail error = 
	    				HL7AckTransforms.createAckDetail("Invalid Receiving Application OID", "//sender/device/id/@root");
	    		errors.add(error);
	    		return false;
	    	}
		}
		
		boolean validateFacility = PropertyFacade.getBoolean(Constants.VALIDATE_RECEIVING_FACILITY);
		if (validateFacility) {
	    	// Verify that the receiving facility OID is not null
	    	MCCIMT000100UV01Device device = receiver.get(0).getDevice();
	    	if (device.getAsAgent() == null ||
	    		device.getAsAgent().getValue() == null ||
	    		device.getAsAgent().getValue().getRepresentedOrganization() == null ||
	    		device.getAsAgent().getValue().getRepresentedOrganization().getValue() == null ||
	    		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId() == null ||
	    		device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().size() == 0 ||
	    		Utilities.isNullish(device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
	    		MCCIMT000200UV01AcknowledgementDetail error = HL7AckTransforms.createAckDetail("Missing Receiving Facility OID");
	    		error.getLocation().add(Utilities.generateSTExplicit("//sender/device/asAgent/representedOrganization/id/@root"));
	    		errors.add(error);
	    		return false;
	    	}
	    	String receiverFacilityOid = device.getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
	    	String myFacilityOid = actorDescription.getIdentifier("ReceivingFacility").getUniversalId();
	    	if (!myFacilityOid.equals(receiverFacilityOid)) {
	    		MCCIMT000200UV01AcknowledgementDetail error = HL7AckTransforms.createAckDetail("Invalid Receiving Facility OID");
	    		error.getLocation().add(Utilities.generateSTExplicit("//sender/device/asAgent/representedOrganization/id/@root"));
	    		errors.add(error);
	    		return false;
	    	}
		}
    	return true;
	}

	protected Patient extractPatient(PRPAMT201301UV02Person person) {
		Patient patient = new Patient();
		extractNames(person.getName(), patient);
		patient.setPhoneNumbers(extractPhoneNumber(person.getTelecom()));
		patient.setAdministrativeSex(extractAdministrativeGender(person.getAdministrativeGenderCode()));
		patient.setBirthDateTime(extractDateOfBirth(person.getBirthTime()));
		patient.setAddresses(extractAddresses(person.getAddr()));
		if (person.getAsOtherIDs() != null || person.getAsOtherIDs().size() > 0) {
			List<PatientIdentifier> identifiers = new ArrayList<PatientIdentifier>();
			for (PRPAMT201301UV02OtherIDs otherId : person.getAsOtherIDs()) {
				identifiers.addAll(extractIdentifiers(otherId.getId()));
			}
			extractOtherIds(patient, identifiers);
		}
		return patient;
	}
	
	protected Patient extractPatient(PRPAMT201302UV02PatientPatientPerson person) {
		Patient patient = new Patient();
		extractNames(person.getName(), patient);
		patient.setPhoneNumbers(extractPhoneNumber(person.getTelecom()));
		patient.setAdministrativeSex(extractAdministrativeGender(person.getAdministrativeGenderCode()));
		patient.setBirthDateTime(extractDateOfBirth(person.getBirthTime()));
		patient.setAddresses(extractAddresses(person.getAddr()));
		
		if (person.getAsOtherIDs() != null || person.getAsOtherIDs().size() > 0) {
			List<PatientIdentifier> identifiers = new ArrayList<PatientIdentifier>();
			for (PRPAMT201302UV02OtherIDs otherId : person.getAsOtherIDs()) {
				identifiers.addAll(extractIdentifiers02Other(otherId.getId()));
			}
			extractOtherIds(patient, identifiers);
		}
		return patient;
	}

	protected MessageStore initMessageStore(String message, IMessageStoreLogger storeLogger, boolean isInbound) {
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
	
	protected Patient extractPatient(PRPAMT201303UV02Person person) {
		Patient patient = new Patient();
		extractNames(person.getName(), patient);
		patient.setPhoneNumbers(extractPhoneNumber(person.getTelecom()));
		patient.setAdministrativeSex(extractAdministrativeGender(person.getAdministrativeGenderCode()));
		patient.setBirthDateTime(extractDateOfBirth(person.getBirthTime()));
		patient.setAddresses(extractAddresses(person.getAddr()));
		
		if (person.getAsOtherIDs() != null || person.getAsOtherIDs().size() > 0) {
			List<PatientIdentifier> identifiers = new ArrayList<PatientIdentifier>();
			for (PRPAMT201303UV02OtherIDs otherId : person.getAsOtherIDs()) {
				identifiers.addAll(extractIdentifiers(otherId.getId()));
			}
			extractOtherIds(patient, identifiers);
		}		
		return patient;
	}

	private void extractOtherIds(Patient patient, List<PatientIdentifier> identifiers) {
		boolean asOtherIds = PropertyFacade.getBoolean(Constants.INCLUDE_OTHER_IDS);
		for (PatientIdentifier identifier : identifiers) {
			
			// check if the other id is the social security
			if( identifier.getAssigningAuthority().getUniversalId().equals(Constants.SSN_OID)) {
				patient.setSsn(identifier.getId());
			} else {
			
				// identifier list includes the other ids
				if( asOtherIds ) {
					patient.addPatientId(identifier);
				}
			}
		}
	}
	
	private List<Address> extractAddresses(List<ADExplicit> addr) {
		if (addr == null || addr.size() == 0 || addr.get(0) == null) {
			return null;
		}
		List<Address> addresses = new ArrayList<Address>();
		for (ADExplicit ad : addr) {
			Address address = extractAddress(ad);
			if (address != null) {
				addresses.add(address);
			}
		}
		return addresses;
	}
	
	private Address extractAddress(ADExplicit ad) {

		List<Serializable> addressValue = ad.getContent();
		String nameString = "";
		Address address = new Address();
		for (Iterator<Serializable> iterSerialObjects = addressValue.iterator(); iterSerialObjects.hasNext(); ) {
			log.info("in iterSerialObjects.hasNext() loop");
			Serializable contentItem = iterSerialObjects.next();
			if (contentItem instanceof String) {
				log.debug("contentItem is string");
				String strValue = (String) contentItem;
				
				if (nameString != null) {
					nameString += strValue;
				} else {
					nameString = strValue;
				}
				log.debug("nameString=" + nameString);
			} else if (contentItem instanceof JAXBElement) {
				log.debug("contentItem is JAXBElement");
				@SuppressWarnings("rawtypes")
				JAXBElement oJAXBElement = (JAXBElement) contentItem;
				log.debug("found element of type: " + oJAXBElement.getValue().getClass());
				if (oJAXBElement.getValue() instanceof AdxpExplicitStreetAddressLine) {
					AdxpExplicitStreetAddressLine addressLine = (AdxpExplicitStreetAddressLine) oJAXBElement.getValue();
					if (addressLine.getContent() != null) {
						log.debug("found Address Line element; content=" + addressLine.getContent());
						address.setAddLine1(addressLine.getContent());
					}
				} else if (oJAXBElement.getValue() instanceof AdxpExplicitCity) {
					AdxpExplicitCity city = (AdxpExplicitCity) oJAXBElement.getValue();
					if (city.getContent() != null) {
						log.debug("found city element; content=" + city.getContent());
						address.setAddCity(city.getContent());
					}
				} else if (oJAXBElement.getValue() instanceof AdxpExplicitState) {
					AdxpExplicitState state = (AdxpExplicitState) oJAXBElement.getValue();
					if (state.getContent() != null) {
						log.debug("found state element; content=" + state.getContent());
						address.setAddState(state.getContent());
					}
				} else if (oJAXBElement.getValue() instanceof AdxpExplicitPostalCode) {
					AdxpExplicitPostalCode postalCode = (AdxpExplicitPostalCode) oJAXBElement.getValue();
					if (postalCode.getContent() != null) {
						log.debug("found postalCode element; content=" + postalCode.getContent());
						address.setAddZip(postalCode.getContent());
					}
				} else {
					log.warn("other name part=" + oJAXBElement.getValue());
				}
			} else {
				log.info("contentItem is other");
			}			
			
		}
		return address;
	}
	
	private Calendar extractDateOfBirth(TSExplicit birthday) {
        try {
            java.util.Date dob = hl7DateFormat.parse(birthday.getValue());
        	log.debug("Extracted dob = " + dob.toString());
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(dob);
        	return calendar;
        } catch (Exception ex) {
        	log.warn("Message does not include a valid subject birth time value: " + birthday);
        	return null;
        }
    }

	private SexType extractAdministrativeGender(CE administrativeGenderCode) {
		if (administrativeGenderCode == null || administrativeGenderCode.getCode() == null) {
			return null;
		}
		String code = administrativeGenderCode.getCode();
		if (code.equalsIgnoreCase(HL7Constants.GENDER_CODE_MALE)) {
			return SexType.MALE;
		} else if (code.equalsIgnoreCase(HL7Constants.GENDER_CODE_FEMALE)) {
			return SexType.FEMALE;
		} else if (code.equalsIgnoreCase(HL7Constants.GENDER_CODE_UNKNOWN)) {
			return SexType.UNKNOWN;
		}
		return null;
	}

	private List<PhoneNumber> extractPhoneNumber(List<TELExplicit> phoneList) {
		List<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		if (phoneList != null && phoneList.size() > 0) {
			for (TELExplicit tel : phoneList) {
				PhoneNumber number = new PhoneNumber();
				parseTelecom(tel.getValue(), number);
				if (tel.getUse() != null && tel.getUse().size() > 0 && tel.getUse().get(0) != null) {
					String use = tel.getUse().get(0);
					// TODO Need to add support for other use types but it is not clear which
					// vocabulary set is being used here.
					if (use.equalsIgnoreCase("H")) {
						number.setUseCode(TelecomUseCode.PRN);
					}
				}
				numbers.add(number);
			}
		}
		return numbers;
	}

	/**
	 * Here we assume that the number takes the format: tel:610-220-4354
	 * 
	 * @param value
	 * @param number
	 */
	private void parseTelecom(String value, PhoneNumber number) {
		if (!value.matches("tel:[0-9]{3}-[0-9]{3}-[0-9]{4}")) {
			number.setNumber(value);
			return;
		}
		number.setAreaCode(value.substring(4, 7));
		number.setNumber(value.substring(8));
	}

	private void extractNames(List<PNExplicit> names, Patient patient) {
		if (names == null || names.size() == 0 || names.get(0).getContent() == null) {
			log.warn("Received a message with no patient names.");
			return;
		}
		PersonName personName = HL7Extractors.translatePNListtoPersonNameType(names);
		patient.setPatientName(personName);
	}

	protected List<PatientIdentifier> extractIdentifiers(List<II> ids) {
		List<PatientIdentifier> pids = new ArrayList<PatientIdentifier>();
		if (ids == null || ids.size() == 0) {
			return pids;
		}
		for (II id : ids) {
			PatientIdentifier pid = extractIdentifier(id);
			if (pid != null) {
				pids.add(pid);
			}
		}
		return pids;
	}
	
	protected List<PatientIdentifier> extractIdentifiers02Patient(List<PRPAMT201302UV02PatientId> ids) {
		List<PatientIdentifier> pids = new ArrayList<PatientIdentifier>();
		if (ids == null || ids.size() == 0) {
			return pids;
		}
		for (II id : ids) {
			PatientIdentifier pid = extractIdentifier(id);
			if (pid != null) {
				pids.add(pid);
			}
		}
		return pids;
	}

	protected List<PatientIdentifier> extractIdentifiers02Other(List<PRPAMT201302UV02OtherIDsId> ids) {
		List<PatientIdentifier> pids = new ArrayList<PatientIdentifier>();
		if (ids == null || ids.size() == 0) {
			return pids;
		}
		for (II id : ids) {
			PatientIdentifier pid = extractIdentifier(id);
			if (pid != null) {
				pids.add(pid);
			}
		}
		return pids;
	}
	
	protected PatientIdentifier extractIdentifier(II id) {
		String identifier = id.getExtension();
		String domainName = id.getAssigningAuthorityName();
		String universalIdentifier = id.getRoot();
		PatientIdentifier pid = new PatientIdentifier();
		Identifier domain = new Identifier(domainName, universalIdentifier, HL7Constants.UNIVERSAL_IDENTIFIER_TYPE_CODE_ISO);
		log.debug("Found identifier: <" + identifier + "->(" + domainName + "," + universalIdentifier + "," + 
				HL7Constants.UNIVERSAL_IDENTIFIER_TYPE_CODE_ISO + ")");
		if (Utilities.isNullish(identifier) || Utilities.isNullish(universalIdentifier)) {
			log.debug("Found an invalid identifier; skipping it.");
			return null;
		}
		pid.setAssigningAuthority(domain);
		pid.setId(identifier);
		return pid;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getConfigurationFilename() {
		return configurationFilename;
	}

	public void setConfigurationFilename(String configurationFilename) {
		this.configurationFilename = configurationFilename;
	}
	
	public static void main(String[] args) {
		String tel = "tel:610-220-4354";
		System.out.println("Matches returns " + tel.matches("tel:[0-9]{3}-[0-9]{3}-[0-9]{4}"));
	}
	
	public class Hl7HeaderAddress
	{
		private String senderApplication;
		private String senderFacility;
		private String receiverApplication;
		private String receiverFacility;
		
		public String getSenderApplication() {
			return senderApplication;
		}
		
		public void setSenderApplication(String senderApplication) {
			this.senderApplication = senderApplication;
		}
		
		public String getSenderFacility() {
			return senderFacility;
		}
		
		public void setSenderFacility(String senderFacility) {
			this.senderFacility = senderFacility;
		}
		
		public String getReceiverApplication() {
			return receiverApplication;
		}
		
		public void setReceiverApplication(String receiverApplicationg) {
			this.receiverApplication = receiverApplicationg;
		}
		
		public String getReceiverFacility() {
			return receiverFacility;
		}
		
		public void setReceiverFacility(String receiverFacility) {
			this.receiverFacility = receiverFacility;
		}
	}
}
