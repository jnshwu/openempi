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
package org.openhie.openempi.nhinadapter.hl7;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AcknowledgementType;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT150002UV01Organization;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.MFMIMT700711UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.ParticipationTargetSubject;
import org.hl7.v3.QueryResponse;
import org.hl7.v3.XActMoodIntentEvent;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class Hl7ConversionHelper
{
	private static final Log log = LogFactory.getLog(Hl7ConversionHelper.class);
    private static SimpleDateFormat hl7DateFormat = new SimpleDateFormat("yyyyMMdd");
    
    private boolean allowAddressQueryAttribute=false;
    private boolean allowTelecomQueryAttribute=false;
    private boolean allowSocialSecurityNumberQueryAttribute=false;
    private boolean allowAddressResponseAttribute=false;
    private boolean allowTelecomResponseAttribute=false;
    private boolean allowSocialSecurityNumberResponseAttribute=false;
    private boolean allowPartialMatches=false;

    private String assigningAuthorityId;
    private String localHomeCommunityId;
 
    /**
     * 	We parse the request message based on the requirements of the NHIN Patient Discovery
     *  Web Service Interface Specification:
     *  
     *  Required values for the Patient Discovery request:
     *  1.LivingSubjectName Parameter: Both “family” and “given” elements are required.  
     *  	If patients are known by multiple names or have had a name change, the alternative names shall be
     *  	specified as multiple instances of LivingSubjectName.  Inclusion of all current and former names
     *  	increases the likelihood of a correct match but may incur privacy concerns.
     *  	The sequence of given names in the given name list reflects the sequence in which they are
     *  	known – first, second, third etc. The first name is specified in the first “given” element in the list.
     *  	Any middle name is specified in the second “given” element in the list when there are no more than two
     *  	“given” elements.
     *  2.LivingSubjectAdministrativeGender Parameter: Is required. Coded using the HL7 coding for AdministrativeGender;
     *  	namely code equal to one of “M” (Male) or “F “(Female) or “UN” (Undifferentiated).
     *  3.LivingSubjectBirthTime Parameter: Is required. The contents must contain the greatest degree of detail
     *  	as is available.
     */
	public Person extractQueryPerson(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
		Person person = new Person();
		log.debug("Extracting query criteria from message:\n" + findCandidatesRequest);

        if (findCandidatesRequest == null) {
            log.warn("input message was null, no query parameters present in message");
            return null;
        }

        PRPAMT201306UV02ParameterList queryParamList = extractQueryParameterList(findCandidatesRequest);
        if (queryParamList == null) {
        	log.warn("Unable to extract query parameter list from input message.");
        	return null;
        }

        populatePersonIdentifiers(person, queryParamList);
        populatePersonNameAttributes(person, queryParamList);
        populatePersonGender(person, queryParamList);
        populatePersonBirthDate(person, queryParamList);
        if (allowAddressQueryAttribute) {
        	populatePersonAddress(person, queryParamList);
        }
        if (allowTelecomQueryAttribute) {
        	populatePersonPhoneNumber(person, queryParamList);
        }
        return person;
	}

	/**
	 * Required values for the Patient Discovery response:
	 * 1.Person.name element: Both “family” and “given” elements are required.  If patients are known by multiple
	 * 		names or have had a name change, the alternative names shall be specified as multiple Patient.name elements.
	 * 		Inclusion of all current and former names increases the likelihood of a correct match. 
	 * 		The sequence of given names in the given name list reflects the sequence in which they are known – first, second, third etc.
	 * 		The first name is specified in the first “given” element in the list. Any middle name is specified in the second “given”
	 * 		element in the list when there are no more than two “given” elements.
	 * 2.Person.AdministrativeGenderCode element: Is required.  Coded using the HL7 coding for AdministrativeGender;
	 * 		namely code equal to one of “M” (Male) or “F “(Female) or “UN” (Undifferentiated).
	 * 3.Person.BirthTime Parameter: Is required.  The contents must contain the greatest degree of detail as is available.
	 * 
	 * Required values if available and if allowed for the Patient Discovery Request and Response:
	 * 1.Address – The “streetAddressLine”, “city”, “state”, “postalCode” shall be used for elements of the address.
	 * 		Multiple “streetAddressLine” elements may be used if necessary and are specified in order of appearance in the address.
	 * 		For more information about coding of addresses see 
	 * 		htp://www.hl7.org/v3ballot/html/infrastructure/datatypes/datatypes.htm#prop-AD.formatted
	 * 2.PatientTelecom – a single phone number. See section 3.1.5.1 for details regarding coding of phone numbers.
	 * 3.Social Security Number – SSN is specified in a LivingSubjectId element – potentially one of several.
	 * 		When specified within the response, the SSN is specified in an OtherIDs element.
	 * 		SSN is designated using the OID 2.16.840.1.113883.4.1.
	 * 
	 * @param candidates
	 * @return
	 */
    public PRPAIN201306UV02 generateResponseMessage(List<Person> candidates, PRPAIN201305UV02 request, boolean isSystemError) {
    	log.debug("Begin generateResponseMesssage");    	
    	PRPAIN201306UV02 message = new PRPAIN201306UV02();
        buildMessageTransmissionWrapper(request, message, AcknowledgementType.AA.value());
        buildControlActProcess(candidates, request, message, isSystemError);
    	return message;
    }

	private void buildControlActProcess(List<Person> candidates, PRPAIN201305UV02 request, PRPAIN201306UV02 message, boolean isSystemError) {
		message.setControlActProcess(generateMFMIMT700711UV01ControlActProcess(candidates, request, message, isSystemError));
	}

	/**
	 * The IHE Patient Discovery Specification indicates the following response codes should be used based on
	 * the five different response conditions identified by the specification:
	 * 
	 * The Initiating Gateway Actor shall act on the query response as described by the following 5
	 * cases:
	 * Case 1: The Responding Gateway Actor finds exactly one patient record matching the criteria
	 * sent in the query parameters.
	 * AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
	 * OK (data found, no errors) is returned in QueryAck.queryResponseCode (control act wrapper)
	 * One RegistrationEvent (and the associated Patient role, subject of that event) is returned from the
	 * patient information source for the patient record found. The community associated with the
	 * Initiating Gateway may use the patient demographics and identifiers to: a) run an independent
	 * matching algorithm to ensure the quality of the match b) use the designated patient identifier in a
	 * Cross Gateway Query to get information about records related to the patient c) cache the
	 * correlation for future use (see ITI TF-2b: 3.55.4.2.3.1 for more information about caching) d)
	 * use a Patient Location Query transaction to get a list of patient data locations.
	 * 
	 * Case 2: The Responding Gateway Actor finds more than one patient close to matching the
	 * criteria sent in the query parameters and the policy allows returning multiple.
	 * AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
	 * OK (data found, no errors) is returned in QueryAck.queryResponseCode (control act wrapper)
	 * One RegistrationEvent (and the associated Patient role, subject of that event) is returned for each
	 * patient record found. The community associated with the Initiating Gateway may run its own
	 * matching algorithm to select from the list of returned patients. If a correlation is found the
	 * Responding Gateway may continue as if only one entry had been returned. If a correlation is still
	 * not clear it is expected that human intervention is required, depending on the policies of the
	 * Initiating Gateway’s community.
	 * 
	 * Case 3: The Responding Gateway Actor finds more than one patient close to matching the
	 * criteria sent in the query parameters but no matches close enough for the necessary assurance
	 * level and more attributes might allow the Responding Gateway to return a match.
	 * AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
	 * OK (data found, no errors) is returned in QueryAck.queryResponseCode (control act wrapper)
	 * No RegistrationEvent is returned in the response, but the Responding Gateway provides a
	 * suggestion in terms of demographics that may help identify a match. The mechanism for
	 * specifying the suggestion is detailed in ITI TF-2b: 3.55.4.2.2.6 for description of coding of the
	 * response. The Initiating Gateway may use this feedback to initiate a new Cross Gateway Patient
	 * Discovery request including the requested additional attributes.
	 * 
	 * Case 4: The Responding Gateway Actor finds no patients anywhere close to matching the
	 * criteria sent in the query parameters.
	 * AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
	 * OK (data found, no errors) is returned in QueryAck.queryResponseCode (control act wrapper)
	 * There is no RegistrationEvent returned in the response. The Initiating Gateway can assume this
	 * patient has no healthcare information held by the community represented by the Responding
	 * Gateway. This lack of correlation may be cached, see ITI TF-2b: 3.55.4.2.3.1 for more
	 * information about caching.
	 * 
	 * Case 5: The Responding Gateway Actor is unable to satisfy the request. This may be because
	 * the request came synchronously and an asynchronous request may be feasible, or because the
	 * Responding Gateway Actor is overloaded with other requests and does not expect to answer for a
	 * significant period of time. Or the Responding Gateway may need some manuel configuration
	 * update to authorize responder.
	 * AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
	 * QE (application error) is returned in QueryAck.queryResponseCode (control act wrapper)
	 * There is no RegistrationEvent returned in the response. See ITI TF-2b: 3.55.4.2.2.7 for more
	 * information about coding errors for this case.
	 */
	private PRPAIN201306UV02MFMIMT700711UV01ControlActProcess generateMFMIMT700711UV01ControlActProcess(List<Person> candidates, PRPAIN201305UV02 request,
			PRPAIN201306UV02 message, boolean isSystemError) {
		PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setCode(Utilities.generateCd(ConversionConstants.PRPA_TE201306UV02, ConversionConstants.INTERACTION_ID_ROOT));
        
        for (Person person : candidates) {
        	List<PersonIdentifier> uniqueIdentifiers = extractUniqueIdentifiers(person.getPersonIdentifiers());
        	for (PersonIdentifier identifier : uniqueIdentifiers) {
        		controlActProcess.getSubject().add(createSubjectFromPerson(person, identifier, request));
        	}
        }
        
        controlActProcess.setQueryAck(createQueryAck(request, isSystemError));

        // Add in query parameters
        if (request.getControlActProcess() != null &&
        		request.getControlActProcess().getQueryByParameter() != null &&
        		request.getControlActProcess().getQueryByParameter().getValue() != null) {
           controlActProcess.setQueryByParameter(request.getControlActProcess().getQueryByParameter());
        }
		return controlActProcess;
	}

	private MFMIMT700711UV01QueryAck createQueryAck(PRPAIN201305UV02 request, boolean isSystemError) {
        MFMIMT700711UV01QueryAck  result = new MFMIMT700711UV01QueryAck();
        
        if (request.getControlActProcess() != null &&
        		request.getControlActProcess().getQueryByParameter() != null &&
        		request.getControlActProcess().getQueryByParameter().getValue() != null &&
        		request.getControlActProcess().getQueryByParameter().getValue().getQueryId() != null) {
           result.setQueryId(request.getControlActProcess().getQueryByParameter().getValue().getQueryId());
        }
        
        String responseCode = QueryResponse.OK.name();
        if (isSystemError) {
        	responseCode = QueryResponse.QE.name();
        }
        result.setQueryResponseCode(Utilities.generateCs(responseCode));
        
        return result;
	}

	/**
	 * Eliminates the SSN from the list of person identifiers if present since it should be 
	 * added to the response message as an asOtherId entry.
	 * 
	 * @param personIdentifiers
	 * @return
	 */
	private List<PersonIdentifier> extractUniqueIdentifiers(Set<PersonIdentifier> personIdentifiers) {
		List<PersonIdentifier> uniqueIds = new java.util.ArrayList<PersonIdentifier>();
		for (PersonIdentifier identifier : personIdentifiers) {
			if (identifier.getIdentifierDomain() != null &&
					identifier.getIdentifierDomain().getNamespaceIdentifier() != null &&
					identifier.getIdentifierDomain().getNamespaceIdentifier().equalsIgnoreCase(ConversionConstants.SSN_ID_ROOT)) {
				continue;
			}
			uniqueIds.add(identifier);		
		}
		return uniqueIds;
	}

	private void buildMessageTransmissionWrapper(PRPAIN201305UV02 request, PRPAIN201306UV02 message, String acknowledmentCode) {
		message.setITSVersion("XML_1.0");
        message.setId(Utilities.generateHl7MessageId(getLocalHomeCommunityId()));
        message.setCreationTime(Utilities.generateCreationTime());
        message.setInteractionId(Utilities.generateHl7Id(ConversionConstants.INTERACTION_ID_ROOT, ConversionConstants.PRPA_IN201306UV02));
        message.setProcessingCode(Utilities.generateCs(ConversionConstants.PROCESSING_CODE_PRODUCTION));
        message.setProcessingModeCode(Utilities.generateCs(ConversionConstants.PROCESSING_MODE_CODE_INITIAL_LOAD));
        message.setAcceptAckCode(Utilities.generateCs(ConversionConstants.ACCEPT_ACK_CODE_NEVER));
        message.getReceiver().add(Utilities.generateMCCIMT00300UV01Receiver(request.getSender()));
        message.setSender(Utilities.generateMCCIMT00300UV01Sender(request.getReceiver()));
        message.getAcknowledgement().add(Utilities.generatedAcknowledgment(request.getId(), acknowledmentCode));
	}

	private PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubjectFromPerson(Person person, PersonIdentifier identifier, PRPAIN201305UV02 request) {
		PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();
		subject.getTypeCode().add(ConversionConstants.ACT_RELATIONSHIP_TYPE);
		PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
		regEvent.getClassCode().add(ConversionConstants.CLASS_CODE_REGISTRATION_EVENT);
		regEvent.getMoodCode().add(ConversionConstants.MOOD_CODE_EVENT);
		regEvent.getId().add(Utilities.generateHl7Id(null, null, ConversionConstants.NULL_FLAVOR));
		regEvent.setStatusCode(Utilities.generateCs(ConversionConstants.STATUS_CODE_ACTIVE));
		regEvent.setSubject1(createSubject2FromPerson(person, identifier, request));
		regEvent.setCustodian(createCustodian(person, identifier));
		subject.setRegistrationEvent(regEvent);
		return subject;
	}

	private PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject2FromPerson(Person person, PersonIdentifier identifier, PRPAIN201305UV02 request) {
		PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
		subject.setTypeCode(ParticipationTargetSubject.SBJ);
		PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();
		patient.getClassCode().add(ConversionConstants.CLASS_CODE_PATIENT);
		patient.getId().add(generateIdFromPersonIdentifier(identifier));
		patient.setStatusCode(Utilities.generateCs(ConversionConstants.STATUS_CODE_ACTIVE));
		patient.setPatientPerson(createPatientPersonFromPerson(person, request));
		subject.setPatient(patient);
		return subject;
	}

	private JAXBElement<PRPAMT201310UV02Person> createPatientPersonFromPerson(Person person, PRPAIN201305UV02 request) {
		PRPAMT201310UV02Person thePerson = new PRPAMT201310UV02Person();
		PNExplicit name = Utilities.generatePnExplicit(person.getGivenName(), person.getFamilyName());
		thePerson.getName().add(name);
		thePerson.setAdministrativeGenderCode(Utilities.generateCe(getAdministrativeGenderCode(person.getGender())));
		thePerson.setBirthTime(Utilities.generateTSExplicit(person.getDateOfBirth()));
		
		// If the adapter is configured to return the phone number and we have, it add it to the message.
		if (allowTelecomResponseAttribute && person.getPhoneNumber() != null) {
			thePerson.getTelecom().add(Utilities.generateTELExplicit(person));
		}
		
		// If the adapter is configured to return the address and we have it, add it to the message
		if (allowAddressResponseAttribute) {
			ADExplicit address = Utilities.generateADExplicit(person.getAddress1(), person.getCity(), person.getState(), person.getPostalCode());
			if (address != null) {
				thePerson.getAddr().add(address);
			}
		}
		
		if (allowSocialSecurityNumberResponseAttribute && Utilities.isNotNullish(person.getSsn())) {
			thePerson.getAsOtherIDs().add(generateSsnAsOtherId(person));
		}
		
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201310UV02Person> jaxbPerson = new JAXBElement<PRPAMT201310UV02Person>(xmlqname, PRPAMT201310UV02Person.class, thePerson);
        
		return jaxbPerson;
	}

	private PRPAMT201310UV02OtherIDs generateSsnAsOtherId(Person person) {
        PRPAMT201310UV02OtherIDs  otherIds = new PRPAMT201310UV02OtherIDs();
        
        otherIds.getClassCode().add(ConversionConstants.AS_OTHER_IDS_SSN_CLASS_CODE);
        
        // Set the SSN
        II ssn = new II();
        ssn.setExtension(person.getSsn());
        ssn.setRoot(ConversionConstants.SSN_ID_ROOT);
        otherIds.getId().add(ssn);
        
        COCTMT150002UV01Organization scopingOrg = new COCTMT150002UV01Organization();
        scopingOrg.setClassCode(ConversionConstants.CLASS_CODE_ORG);
        scopingOrg.setDeterminerCode(ConversionConstants.DETERMINER_CODE_INSTANCE);
        II orgId = new II();
        orgId.setRoot(ssn.getRoot());
        scopingOrg.getId().add(orgId);
        otherIds.setScopingOrganization(scopingOrg);
        
        return otherIds;
	}

	private String getAdministrativeGenderCode(Gender gender) {
		String code = null;
		if (gender == null || gender.getGenderCode() == null) {
			return null;
		}
		
		String value = gender.getGenderCode();
		if (value.equalsIgnoreCase(ConversionConstants.OPENEMPI_MALE_GENDER_CODE)) {
			code = ConversionConstants.MALE_GENDER_CODE;
        } else if (value.equalsIgnoreCase(ConversionConstants.OPENEMPI_FEMALE_GENDER_CODE)) {
        	code = ConversionConstants.FEMALE_GENDER_CODE;
        } else if (value.equalsIgnoreCase(ConversionConstants.OPENEMPI_UNDIFFERENTIATED_GENDER_CODE)) {
        	code = ConversionConstants.UNDIFFERENTIATED_GENDER_CODE;
        }
		log.debug("Looking up gender code for value: " + value + " resulted in: " + code);
		return code;
	}

	private MFMIMT700711UV01Custodian createCustodian(Person person, PersonIdentifier identifier) {
        MFMIMT700711UV01Custodian result = new MFMIMT700711UV01Custodian();
        result.getTypeCode().add(ConversionConstants.PARTICIPATION_CUSTODIAN);
        
        result.setAssignedEntity(createAssignedEntity(identifier));
        
        return result;
	}
    
    private static COCTMT090003UV01AssignedEntity createAssignedEntity (PersonIdentifier identifier) {
        COCTMT090003UV01AssignedEntity  assignedEntity = new COCTMT090003UV01AssignedEntity();
        
        II id = new II();
		String idRoot = null;
		if (identifier.getIdentifierDomain() != null &&
				identifier.getIdentifierDomain().getNamespaceIdentifier() != null) {
			idRoot = identifier.getIdentifierDomain().getNamespaceIdentifier();
		} else if (identifier.getIdentifierDomain() != null &&
				identifier.getIdentifierDomain().getUniversalIdentifier() != null) {
			idRoot = identifier.getIdentifierDomain().getUniversalIdentifier();
		}
		
		if (idRoot != null) {
			id.setRoot(idRoot);
		}
		assignedEntity.setCode(Utilities.generateCe(ConversionConstants.NO_HEALTH_DATA_LOCATOR_CODE, ConversionConstants.NO_HEALTH_DATA_LOCATOR_CODE_SYSTEM));
		assignedEntity.setClassCode(ConversionConstants.ROLE_CLASS_ASSIGNED);
        assignedEntity.getId().add(id);
        
        return assignedEntity;
    }

	private II generateIdFromPersonIdentifier(PersonIdentifier identifier) {
		II id = new II();
		id.setExtension(identifier.getIdentifier());
		
		String idRoot = null;
		if (identifier.getIdentifierDomain() != null &&
				identifier.getIdentifierDomain().getNamespaceIdentifier() != null) {
			idRoot = identifier.getIdentifierDomain().getNamespaceIdentifier();
		} else if (identifier.getIdentifierDomain() != null &&
				identifier.getIdentifierDomain().getUniversalIdentifier() != null) {
			idRoot = identifier.getIdentifierDomain().getUniversalIdentifier();
		}
		
		if (idRoot != null) {
			id.setRoot(idRoot);
		}
		return id;
	}

	private void populatePersonIdentifiers(Person person, PRPAMT201306UV02ParameterList queryParamList) {
		log.debug("Entering Hl7ConversionHelper.populatePersonIdentifiers method...");
		
		//Extract the patient identifiers in the requesting message
		if (!hasLivingSubjectIds(queryParamList)) {
			log.warn("Message does not include a living subject ID.");
			return;
		}
		
		List<PRPAMT201306UV02LivingSubjectId> idList = queryParamList.getLivingSubjectId();
		for (PRPAMT201306UV02LivingSubjectId id : idList) {
			if (!hasLivingSubjectIdValue(id)) {
				log.warn("Message does not include a living subject ID value.");
				continue;
			}
			
			II identifier = id.getValue().get(0);
			// If this is the SSN then it requires special treatment within OpenEMPI
			// The following text below is quoted from the NHIN Patient Discovery Specification
			// Social Security Number – SSN is specified in a LivingSubjectId element – potentially one of several. 
			// SSN is designated using the OID 2.16.840.1.113883.4.1
			//
			if ((identifier.getRoot().equalsIgnoreCase(ConversionConstants.SSN_OID) && allowSocialSecurityNumberQueryAttribute)) {
				log.debug("Message includes the SSN as a query attribute.");
				person.setSsn(identifier.getExtension());
				continue;
			}
			
			// If the identifier is not the SSN then it is the Patient ID assigned to the patient by the initiating gateway
			IdentifierDomain domain = new IdentifierDomain();
			domain.setUniversalIdentifier(identifier.getRoot());
			domain.setNamespaceIdentifier(identifier.getRoot());
			PersonIdentifier personIdentifier = new PersonIdentifier();
			personIdentifier.setIdentifier(identifier.getExtension());
			personIdentifier.setIdentifierDomain(domain);
			person.addPersonIdentifier(personIdentifier);
			log.debug("Added person identifier of: " + personIdentifier);
		}
	}

	private void populatePersonBirthDate(Person person, PRPAMT201306UV02ParameterList queryParamList) {
        log.debug("Entering Hl7ConversionHelper.populatePersonIdentifiers.populatePersonBirthDate method...");
        
        // Extract the birth time from the query parameters - Assume only one was specified
        if (!hasLivingSubjectBirthTime(queryParamList)) {
        	log.warn("Message does not include a living subject birth time.");
        	return;
        }
        
        PRPAMT201306UV02LivingSubjectBirthTime birthTime = queryParamList.getLivingSubjectBirthTime().get(0);
        if (!hasLivingSubjectBirthTimeValue(birthTime)) {
        	log.warn("Message does not include a valid living subject birth time value.");
        	return;
        }
 
        IVLTSExplicit birthday = birthTime.getValue().get(0);
        log.debug("Found birthTime in query parameters = " + birthday.getValue());

        try {
            java.util.Date dob = hl7DateFormat.parse(birthday.getValue());
        	log.debug("Extracted dob = " + dob.toString());
        	person.setDateOfBirth(dob);
        } catch (Exception ex) {
        	log.warn("Message does not include a valid living subject birth time value.");
        }
	}

	/**
	 * LivingSubjectAdministrativeGender Parameter: Is required. Coded using the HL7 coding for
	 * AdministrativeGender; namely code equal to one of “M” (Male) or “F “(Female) or “UN”
	 * (Undifferentiated).
	 * 
	 * @param person
	 * @param queryParamList
	 */
	private void populatePersonGender(Person person, PRPAMT201306UV02ParameterList queryParamList) {
        // Extract the gender from the query parameters - Assume only one was specified
        if (!hasLivingSubjectAdministrativeGender(queryParamList)) {
        	log.warn("Message does not include a living subject admistrative gender code.");
        	return;
        }

        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender = queryParamList.getLivingSubjectAdministrativeGender().get(0);
        if (!hasLivingSubjectAdministrativeGenderCodeValue(adminGender)) {
        	log.warn("Message does not include a living subject admistrative gender code value.");
        	return;
        }

        CE administrativeGenderCode = adminGender.getValue().get(0);
        String value = administrativeGenderCode.getCode();
        log.debug("Found living subject administrative gender code value of " + value);
        if (value.equalsIgnoreCase(ConversionConstants.MALE_GENDER_CODE)) {
        	Gender gender = new Gender();
        	gender.setGenderCode(ConversionConstants.OPENEMPI_MALE_GENDER_CODE);
        	person.setGender(gender);
        } else if (value.equalsIgnoreCase(ConversionConstants.FEMALE_GENDER_CODE)) {
        	Gender gender = new Gender();
        	gender.setGenderCode(ConversionConstants.OPENEMPI_FEMALE_GENDER_CODE);
        	person.setGender(gender);
        } else if (value.equalsIgnoreCase(ConversionConstants.UNDIFFERENTIATED_GENDER_CODE)) {
        	Gender gender = new Gender();
        	gender.setGenderCode(ConversionConstants.OPENEMPI_UNDIFFERENTIATED_GENDER_CODE);
        	person.setGender(gender);
        } else {
        	log.warn("Found unknown living subject administrative gender code value of " + value);
        }
    }

	@SuppressWarnings("unchecked")
	private void populatePersonAddress(Person person, PRPAMT201306UV02ParameterList queryParamList) {
		// Extract the address of the person from the query parameters - This is an optional attribute
		// so it may not be present in the message
		if (!hasPatientAddress(queryParamList)) {
			log.debug("Message does not include the address as a query criterion.");
			return;
		}
		PRPAMT201306UV02PatientAddress address = queryParamList.getPatientAddress().get(0);

		if (!hasPatientAddressValue(address)) {
			log.debug("Message does not include the address value as a query criterion.");
			return;
		}
		
		List<Serializable> addressValue = address.getValue().get(0).getContent();
		
		String nameString = "";
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
				JAXBElement oJAXBElement = (JAXBElement) contentItem;
				log.debug("found element of type: " + oJAXBElement.getValue().getClass());
				if (oJAXBElement.getValue() instanceof AdxpExplicitStreetAddressLine) {
					AdxpExplicitStreetAddressLine addressLine = (AdxpExplicitStreetAddressLine) oJAXBElement.getValue();
					if (addressLine.getContent() != null) {
						log.debug("found Address Line element; content=" + addressLine.getContent());
						person.setAddress1(addressLine.getContent());
					}
				} else if (oJAXBElement.getValue() instanceof AdxpExplicitCity) {
					AdxpExplicitCity city = (AdxpExplicitCity) oJAXBElement.getValue();
					if (city.getContent() != null) {
						log.debug("found city element; content=" + city.getContent());
						person.setCity(city.getContent());
					}
				} else if (oJAXBElement.getValue() instanceof AdxpExplicitState) {
					AdxpExplicitState state = (AdxpExplicitState) oJAXBElement.getValue();
					if (state.getContent() != null) {
						log.debug("found state element; content=" + state.getContent());
						person.setState(state.getContent());
					}
				} else if (oJAXBElement.getValue() instanceof AdxpExplicitPostalCode) {
					AdxpExplicitPostalCode postalCode = (AdxpExplicitPostalCode) oJAXBElement.getValue();
					if (postalCode.getContent() != null) {
						log.debug("found postalCode element; content=" + postalCode.getContent());
						person.setPostalCode(postalCode.getContent());
					}
				} else {
					log.warn("other name part=" + oJAXBElement.getValue());
				}
			} else {
				log.info("contentItem is other");
			}			
			
		}
	}

	private void populatePersonPhoneNumber(Person person, PRPAMT201306UV02ParameterList queryParamList) {
		// Extract the phone number of the person from the query parameters - This is an optional attribute
		// so it may not be present in the message
		if (!hasPatientPhoneNumber(queryParamList)) {
			log.debug("Message does not include the phone number as a query criterion.");
			return;
		}
		PRPAMT201306UV02PatientTelecom phoneNumber = queryParamList.getPatientTelecom().get(0);

		if (!hasPatientPhoneNumberValue(phoneNumber)) {
			log.debug("Message does not include the phone number value as a query criterion.");
			return;
		}
		
		String value = phoneNumber.getValue().get(0).getValue();
        log.debug("Found patientTelecom in query parameters = " + value);
        if (value.startsWith(ConversionConstants.TELECOM_URL_SCHEME)) {
        	person.setPhoneNumber(value.substring(value.indexOf(ConversionConstants.TELECOM_URL_SCHEME)+ConversionConstants.TELECOM_URL_SCHEME.length()));
        } else {
        	person.setPhoneNumber(value);
        }
	}

	@SuppressWarnings("unchecked")
	private void populatePersonNameAttributes(Person person, PRPAMT201306UV02ParameterList params) {
		log.debug("Populating person name attributes from name.");

		// Extract the name from the query parameters - Assume only one was
		// specified
		if (!hasLivingSubjectName(params)) {
			log.warn("Message does not include a living subject name: " + params);
			return;
		}
		PRPAMT201306UV02LivingSubjectName name = params.getLivingSubjectName().get(0);

		if (!hasLivingSubjectNameValue(name)) {
			log.warn("Message does not include a living subject name: " + params);
			return;
		}
		
		List<Serializable> choice = name.getValue().get(0).getContent();

		String nameString = "";
		int givenNameIndex=0;
		EnExplicitGiven givenName = null;
		EnExplicitGiven middleName = null;
		EnExplicitFamily familyName = null;
		for (Iterator<Serializable> iterSerialObjects = choice.iterator(); iterSerialObjects.hasNext(); ) {
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
				JAXBElement oJAXBElement = (JAXBElement) contentItem;
				if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
					familyName = (EnExplicitFamily) oJAXBElement.getValue();
					log.debug("found lastname element; content=" + familyName.getContent());
				} else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
					if (givenNameIndex == 0) {
						givenName = (EnExplicitGiven) oJAXBElement.getValue();
						log.info("found firstname element; content=" + givenName.getContent());
						givenNameIndex++;
					} else {
						middleName = (EnExplicitGiven) oJAXBElement.getValue();
						log.info("found middlename element; content=" + givenName.getContent());
						givenNameIndex++;
					}
				} else {
					log.warn("other name part=" + oJAXBElement.getValue());
				}
			} else {
				log.info("contentItem is other");
			}
		}

		if ((familyName != null && familyName.getContent() != null) || 
				(givenName != null && givenName.getContent() != null) ||
				(middleName != null && middleName.getContent() != null)) {
			
			if (familyName != null && familyName.getContent() != null) {
				person.setFamilyName(familyName.getContent());
			}
			
			if (givenName != null && givenName.getContent() != null) {
				person.setGivenName(givenName.getContent());
			}
			
			if (middleName != null && middleName.getContent() != null) {
				person.setMiddleName(middleName.getContent());
			}
		} else {

			if (nameString.length() > 0) {
				person.setFamilyName(nameString);
			}
		}
		log.debug("Added person name attributes to the query person: " + person);
	}

    private boolean hasLivingSubjectIds(PRPAMT201306UV02ParameterList params) {
		return params.getLivingSubjectId() != null &&
			params.getLivingSubjectId().size() > 0 &&
			params.getLivingSubjectId().get(0) != null;
	}

	private boolean hasLivingSubjectIdValue(PRPAMT201306UV02LivingSubjectId id) {
		return id.getValue() != null &&
			id.getValue().size() > 0 &&
			id.getValue().get(0) != null &&
			id.getValue().get(0).getExtension() != null &&
			id.getValue().get(0).getRoot() != null;
	}

	private static boolean hasLivingSubjectBirthTimeValue(
			PRPAMT201306UV02LivingSubjectBirthTime birthTime) {
		return birthTime.getValue() != null &&
		        birthTime.getValue().size() > 0 &&
		        birthTime.getValue().get(0) != null;
	}

	private boolean hasLivingSubjectAdministrativeGenderCodeValue(PRPAMT201306UV02LivingSubjectAdministrativeGender gender) {
		return gender.getValue() != null &&
		        gender.getValue().size() > 0 &&
		        gender.getValue().get(0) != null;
	}

	private static boolean hasLivingSubjectBirthTime(PRPAMT201306UV02ParameterList params) {
		return params.getLivingSubjectBirthTime() != null &&
	        params.getLivingSubjectBirthTime().size() > 0 &&
	        params.getLivingSubjectBirthTime().get(0) != null;
	}

	private boolean hasPatientAddress(PRPAMT201306UV02ParameterList params) {
		return params.getPatientAddress() != null &&
			params.getPatientAddress().size() > 0 &&
			params.getPatientAddress().get(0) != null;
	}

	private boolean hasPatientAddressValue(PRPAMT201306UV02PatientAddress address) {
		return address.getValue() != null &&
			address.getValue().size() > 0 &&
			address.getValue().get(0) != null;
	}

	private boolean hasPatientPhoneNumber(PRPAMT201306UV02ParameterList params) {
		return params.getPatientTelecom() != null &&
			params.getPatientTelecom().size() > 0 &&
			params.getPatientTelecom().get(0) != null;
	}

	private boolean hasPatientPhoneNumberValue(PRPAMT201306UV02PatientTelecom phoneNumber) {
		return phoneNumber.getValue() != null &&
			phoneNumber.getValue().size() > 0 &&
			phoneNumber.getValue().get(0) != null;
	}

	private static boolean hasLivingSubjectAdministrativeGender(PRPAMT201306UV02ParameterList params) {
		return params.getLivingSubjectAdministrativeGender() != null &&
			params.getLivingSubjectAdministrativeGender().size() > 0 &&
	        params.getLivingSubjectAdministrativeGender().get(0) != null;
	}

	private static boolean hasLivingSubjectNameValue(PRPAMT201306UV02LivingSubjectName name) {
		return name.getValue() != null && name.getValue().size() > 0 && name.getValue().get(0) != null;
	}

	private static boolean hasLivingSubjectName(PRPAMT201306UV02ParameterList params) {
		return params.getLivingSubjectName() != null
				&& params.getLivingSubjectName().size() > 0
				&& params.getLivingSubjectName().get(0) != null;
	}

	private PRPAMT201306UV02ParameterList extractQueryParameterList(
			org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
		PRPAMT201306UV02ParameterList queryParamList = null;
		PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = findCandidatesRequest.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no query parameters present in message");
            return null;
        }

        if (controlActProcess.getQueryByParameter() != null &&
                controlActProcess.getQueryByParameter().getValue() != null) {
            PRPAMT201306UV02QueryByParameter queryParams = (PRPAMT201306UV02QueryByParameter) controlActProcess.getQueryByParameter().getValue();

            if (queryParams.getParameterList() != null) {
                queryParamList = queryParams.getParameterList();
            }

        }
        return queryParamList;
	}

    public static II buildII(String root, String extension, String assigningAuthorityName) {
        II ii = new II();
        if (Utilities.isNotNullish(root)) {
            log.debug("Setting root attribute of II to " + root);
            ii.setRoot(root);
        }
        if (Utilities.isNotNullish(extension)) {
            log.debug("Setting extension attribute of II to " + extension);
            ii.setExtension(extension);
        }
        if (Utilities.isNotNullish(assigningAuthorityName)) {
            log.debug("Setting assigning authority attribute of II to " + assigningAuthorityName);
            ii.setAssigningAuthorityName(assigningAuthorityName);
        }
        return ii;
    }

	public String getAssigningAuthorityId() {
		return assigningAuthorityId;
	}

	public void setAssigningAuthorityId(String assigningAuthorityId) {
		this.assigningAuthorityId = assigningAuthorityId;
	}

	public String getLocalHomeCommunityId() {
		return localHomeCommunityId;
	}

	public void setLocalHomeCommunityId(String localHomeCommunityId) {
		this.localHomeCommunityId = localHomeCommunityId;
	}

	public boolean isAllowAddressQueryAttribute() {
		return allowAddressQueryAttribute;
	}

	public void setAllowAddressQueryAttribute(boolean allowAddressQueryAttribute) {
		this.allowAddressQueryAttribute = allowAddressQueryAttribute;
	}

	public boolean isAllowTelecomQueryAttribute() {
		return allowTelecomQueryAttribute;
	}

	public void setAllowTelecomQueryAttribute(boolean allowTelecomQueryAttribute) {
		this.allowTelecomQueryAttribute = allowTelecomQueryAttribute;
	}

	public boolean isAllowSocialSecurityNumberQueryAttribute() {
		return allowSocialSecurityNumberQueryAttribute;
	}

	public void setAllowSocialSecurityNumberQueryAttribute(
			boolean allowSocialSecurityNumberQueryAttribute) {
		this.allowSocialSecurityNumberQueryAttribute = allowSocialSecurityNumberQueryAttribute;
	}

	public boolean isAllowAddressResponseAttribute() {
		return allowAddressResponseAttribute;
	}

	public void setAllowAddressResponseAttribute(
			boolean allowAddressResponseAttribute) {
		this.allowAddressResponseAttribute = allowAddressResponseAttribute;
	}

	public boolean isAllowTelecomResponseAttribute() {
		return allowTelecomResponseAttribute;
	}

	public void setAllowTelecomResponseAttribute(
			boolean allowTelecomResponseAttribute) {
		this.allowTelecomResponseAttribute = allowTelecomResponseAttribute;
	}

	public boolean isAllowSocialSecurityNumberResponseAttribute() {
		return allowSocialSecurityNumberResponseAttribute;
	}

	public void setAllowSocialSecurityNumberResponseAttribute(
			boolean allowSocialSecurityNumberResponseAttribute) {
		this.allowSocialSecurityNumberResponseAttribute = allowSocialSecurityNumberResponseAttribute;
	}
	
	public boolean isAllowPartialMatches() {
		return allowPartialMatches;
	}

	public void setAllowPartialMatches(boolean allowPartialMatches) {
		this.allowPartialMatches = allowPartialMatches;
	}
}
