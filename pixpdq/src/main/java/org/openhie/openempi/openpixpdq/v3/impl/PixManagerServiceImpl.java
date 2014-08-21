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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.bind.JAXBElement;

import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MFMIMT700701UV01ReplacementOf;
import org.hl7.v3.MFMIMT700701UV01Subject3;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201302UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201304UV02;
import org.hl7.v3.PRPAIN201304UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201302UV02PatientPatientPerson;
import org.hl7.v3.PRPAMT201303UV02Person;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.IheActor;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openpixpdq.api.Hl7MessageValidationException;
import org.openhealthtools.openpixpdq.api.IPixManagerAdapter;
import org.openhealthtools.openpixpdq.api.IPixUpdateNotificationRequest;
import org.openhealthtools.openpixpdq.api.PixManagerException;
import org.openhealthtools.openpixpdq.common.AssigningAuthorityUtil;
import org.openhealthtools.openpixpdq.common.PatientBroker;
import org.openhealthtools.openpixpdq.common.PixPdqFactory;
import org.openhealthtools.openpixpdq.common.PixUpdateNotifier;
import org.openhie.openempi.openpixpdq.v3.PIXManagerPortType;
import org.openhie.openempi.openpixpdq.v3.util.HL7AckTransforms;
import org.openhie.openempi.openpixpdq.v3.util.HL7Constants;

@WebService(endpointInterface = "org.openhie.openempi.openpixpdq.v3.PIXManagerPortType")
public class PixManagerServiceImpl extends AbstractIheService implements PIXManagerPortType
{
	private final static String PIX_QUERY_IDENTIFIER = "pixQueryIdentifier";
	private final static String PIX_DATA_SOURCES = "listOfDataSources";
	private final static String PIX_QUERY_ID = "queryId";
	private final static String PIX_QUERY_QUERY_BY_PARAMETER = "queryByParameter";
	
	private IPixManagerAdapter pixAdapter;
	private PixManagerV3 pixManager;
	
	public void init() {
		super.init();
		pixAdapter = PixPdqFactory.getPixManagerAdapter();
		log.info("Initializing the service." + pixAdapter);
		String actorName = getActorName();
		log.info("Looking up the PIX Manager using key " + actorName);
		IheActor iheActor = PatientBroker.getInstance().getActorByName(actorName);
		log.info("Obtained a actor: " + iheActor);
		if  (iheActor instanceof PixManagerV3) {
			pixManager = (PixManagerV3) iheActor;
		} else {
			log.warn("Service has not been configured properly since the ihe actor is either undefined or of incorrect type.");
		}
	}
	
	// PIX Manager Create Patient Request
	//
	public MCCIIN000002UV01 pixManagerPRPAIN201301UV02(PRPAIN201301UV02 message) {
		log.debug("Received request: " + message);
		Hl7HeaderAddress addr = extractHeaderAddress(message.getReceiver(), message.getSender());
		try {
			validateSenderReceivingApplicationAndFacility(addr, pixManager.getActorDescription());
		} catch (Hl7MessageValidationException e) {
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;
		}
		
		List<PRPAIN201301UV02MFMIMT700701UV01Subject1> patients = message.getControlActProcess().getSubject();
		if (patients == null || patients.size() == 0) {
			log.debug("Received request with no patient records attached.");
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		}
		
		PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1 = patients.get(0);
		if (subject1 == null ||
				subject1.getRegistrationEvent() == null ||
				subject1.getRegistrationEvent().getSubject1() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue() == null) {
			log.debug("Received request with no subject attached.");
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		}
		List<PatientIdentifier> ids = extractIdentifiers(subject1.getRegistrationEvent().getSubject1().getPatient().getId());
		JAXBElement<PRPAMT201301UV02Person> subject = subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson();
		
		Patient patient=null;
		try {
			patient = extractPatient(subject.getValue());
			// patient.setPatientIds(ids);
			patient.getPatientIds().addAll(ids);
			validatePatientIdNotEmpty(patient);
			validateDomain(patient, false);
		} catch (PixManagerException e) {
			log.warn("Failed while pre-processing a patient create operation: " + e, e);
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;			
		}
		
		try {
			List<PatientIdentifier> identifiers = pixAdapter.createPatient(patient, null);
			if (identifiers != null && identifiers.size() > 0 && pixManager.getPixConsumerConnections().size() > 0) {
				//Send PIX Update Notification	
				IPixUpdateNotificationRequest request = new PixUpdateNotificationRequestV3(pixManager, pixAdapter, patient, identifiers);
				PixUpdateNotifier.getInstance().accept(request);				
			}
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		} catch (PixManagerException e) {
			log.warn("Failed while processing a patient create operation: " + e, e);
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;			
		}
	}
	
	// PIX Manager Update Patient Request
	//
	public MCCIIN000002UV01 pixManagerPRPAIN201302UV02(PRPAIN201302UV02 message) {
		log.debug("Received request: " + message);
		
		Hl7HeaderAddress addr = extractHeaderAddress(message.getReceiver(), message.getSender());
		try {
			validateSenderReceivingApplicationAndFacility(addr, pixManager.getActorDescription());
		} catch (Hl7MessageValidationException e) {
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;
		}
		
		List<PRPAIN201302UV02MFMIMT700701UV01Subject1> patients = message.getControlActProcess().getSubject();
		if (patients == null || patients.size() == 0) {
			log.debug("Received request with no patient records attached.");
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		}
		
		PRPAIN201302UV02MFMIMT700701UV01Subject1 subject1 = patients.get(0);
		if (subject1 == null ||
				subject1.getRegistrationEvent() == null ||
				subject1.getRegistrationEvent().getSubject1() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue() == null) {
			log.debug("Received request with no subject attached.");
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		}
		List<PatientIdentifier> ids = extractIdentifiers02Patient(subject1.getRegistrationEvent().getSubject1().getPatient().getId());
		JAXBElement<PRPAMT201302UV02PatientPatientPerson> subject = subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson();
		
		Patient patient=null;
		try {
			patient = extractPatient(subject.getValue());
			// patient.setPatientIds(ids);
			patient.getPatientIds().addAll(ids);
			validatePatientIdNotEmpty(patient);
			validateDomain(patient, false);
		} catch (PixManagerException e) {
			log.warn("Failed while pre-processing a patient create operation: " + e, e);
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;			
		}
		
		try {
			List<List<PatientIdentifier>> identifiers = pixAdapter.updatePatient(patient, null);
			if (pixManager.getPixConsumerConnections().size() > 0 && identifiers != null && identifiers.size() > 0) {
				for (List<PatientIdentifier> theIds : identifiers) {
					//Send PIX Update Notification	
					IPixUpdateNotificationRequest request = new PixUpdateNotificationRequestV3(pixManager, pixAdapter, patient, theIds);
					PixUpdateNotifier.getInstance().accept(request);
				}
			}
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		} catch (PixManagerException e) {
			log.warn("Failed while processing a patient update operation: " + e, e);
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;			
		}
		
	}

	// PIX Manager Merge Patients Request
	//
	public MCCIIN000002UV01 pixManagerPRPAIN201304UV02(PRPAIN201304UV02 message) {
		log.debug("Received request: " + message);
		
		Hl7HeaderAddress addr = extractHeaderAddress(message.getReceiver(), message.getSender());
		try {
			validateSenderReceivingApplicationAndFacility(addr, pixManager.getActorDescription());
		} catch (Hl7MessageValidationException e) {
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;
		}
		
		List<PRPAIN201304UV02MFMIMT700701UV01Subject1> patients = message.getControlActProcess().getSubject();
		if (patients == null || patients.size() == 0) {
			log.debug("Received request with no patient records attached.");
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		}
		
		PRPAIN201304UV02MFMIMT700701UV01Subject1 subject1 = patients.get(0);
		if (subject1 == null ||
				subject1.getRegistrationEvent() == null ||
				subject1.getRegistrationEvent().getSubject1() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson() == null ||
				subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue() == null) {
			log.debug("Received request with no subject attached.");
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		}

		// Subject1
		List<PatientIdentifier> ids1 = extractIdentifiers(subject1.getRegistrationEvent().getSubject1().getPatient().getId());
		JAXBElement<PRPAMT201303UV02Person> patientPerson = subject1.getRegistrationEvent().getSubject1().getPatient().getPatientPerson();
		
		Patient patientRemain=null;
		try {
			patientRemain = extractPatient(patientPerson.getValue());
			// patientRemain.setPatientIds(ids1);
			patientRemain.getPatientIds().addAll(ids1);
			validatePatientIdNotEmpty(patientRemain);
			validateDomain(patientRemain, false);
		} catch (PixManagerException e) {
			log.warn("Failed while pre-processing merge patient operation: " + e, e);
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;			
		}
		
		// Replacement	
		List<MFMIMT700701UV01ReplacementOf> replacements = subject1.getRegistrationEvent().getReplacementOf();
		MFMIMT700701UV01ReplacementOf replacement = replacements.get(0);
		MFMIMT700701UV01Subject3 subject3 = replacement.getPriorRegistration().getSubject1().getValue();
		
		List<PatientIdentifier> ids2 = extractIdentifiers(subject3.getPriorRegisteredRole().getId());
		
		Patient patientReplacementOf = new Patient();
		try {
			patientReplacementOf.setPatientIds(ids2);

			validatePatientIdNotEmpty(patientReplacementOf);
			validateDomain(patientReplacementOf, false);
		} catch (PixManagerException e) {
			log.warn("Failed while pre-processing merge patient operation: " + e, e);
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;			
		}

		try {
			List<List<PatientIdentifier>> identifiers = pixAdapter.mergePatients(patientRemain, patientReplacementOf, null);
			if (pixManager.getPixConsumerConnections().size() > 0 && identifiers != null && identifiers.size() > 0) {
				for (List<PatientIdentifier> ids : identifiers) {
					IPixUpdateNotificationRequest request = new PixUpdateNotificationRequestV3(pixManager, pixAdapter, patientRemain, ids);
					PixUpdateNotifier.getInstance().accept(request);
				}
			}
			MCCIIN000002UV01 acceptAck = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), null);
			return acceptAck;
		} catch (PixManagerException e) {
			log.warn("Failed while processing a patient merge operation: " + e, e);
			MCCIIN000002UV01 errorResp = generateAckResponse(message.getId(), message.getReceiver(), message.getSender(), e);
			return errorResp;			
		}
	}

	// PIX Manager Query Patient Identifiers Request
	//
	@SuppressWarnings("unchecked")
	public PRPAIN201310UV02 pixManagerPRPAIN201309UV02(PRPAIN201309UV02 message) {
		log.debug("Received request: " + message);
		Hl7HeaderAddress addr = extractHeaderAddress(message.getReceiver(), message.getSender());
		try {
			validateSenderReceivingApplicationAndFacility(addr, pixManager.getActorDescription());
		} catch (Hl7MessageValidationException e) {
			PRPAIN201310UV02 response = HL7AckTransforms.createQueryResponse(message.getId(), addr.getSenderApplication(), addr.getSenderFacility(),
					addr.getReceiverApplication(), addr.getReceiverFacility(), e);
			return response;
		}
		
		Map<String,Object> params = null;
		try {
			params = extractQueryIdentifierAndDomains(message.getControlActProcess());
			log.debug(params);
			II id = (II) params.get(PIX_QUERY_IDENTIFIER);
			PatientIdentifier queryId = extractIdentifier(id);
			validateQueryIdentifierDomain(queryId);
			validatePatient(queryId);
			
			List<Identifier> dataSources = (List<Identifier>) params.get(PIX_DATA_SOURCES);
			if (dataSources != null && dataSources.size() > 0) {
				dataSources = validateDataSources(dataSources);
			}
			List<PatientIdentifier> ids = pixAdapter.findPatientIds(queryId, null);

			// If we found any IDs then we may need to filter the domains
			if (ids.size() > 0 && dataSources != null && dataSources.size() > 0) {
				ids = filterIdsByDataSources(ids, dataSources);
			}
			
			PRPAIN201310UV02 response = null;
			if (ids.size() > 0) {
				response = createQueryResponse(message, addr, pixManager.getActorDescription(), ids, 
					(PRPAMT201307UV02QueryByParameter) params.get(PIX_QUERY_QUERY_BY_PARAMETER),
					(II) params.get(PIX_QUERY_ID),
					HL7Constants.QUERY_ACK_OK);
			} else {
				response = createQueryResponse(message, addr, pixManager.getActorDescription(), ids, 
						(PRPAMT201307UV02QueryByParameter) params.get(PIX_QUERY_QUERY_BY_PARAMETER),
						(II) params.get(PIX_QUERY_ID),
						HL7Constants.QUERY_ACK_NF);
			}
			return response;
		} catch (PixManagerException e) {
			log.warn("Error while processing PIX Query: " + e, e);
			PRPAIN201310UV02 response = HL7AckTransforms.createQueryResponse(message.getId(), addr.getReceiverApplication(), addr.getReceiverFacility(),
					addr.getSenderApplication(), addr.getSenderFacility(), e);
			response.setControlActProcess(HL7AckTransforms.createQueryResponseControlActProcess(pixManager.getActorDescription(),
					null, (PRPAMT201307UV02QueryByParameter) params.get(PIX_QUERY_QUERY_BY_PARAMETER),
					(II) params.get(PIX_QUERY_ID),
					HL7Constants.QUERY_ACK_AE));
			return response;
		} catch (Hl7MessageValidationException e) {
			log.warn("Error while processing PIX Query: " + e, e);
			PRPAIN201310UV02 response = HL7AckTransforms.createQueryResponse(message.getId(), addr.getReceiverApplication(), addr.getReceiverFacility(),
					addr.getSenderApplication(), addr.getSenderFacility(), e);
			return response;
		}
	}

	private PRPAIN201310UV02 createQueryResponse(PRPAIN201309UV02 message, Hl7HeaderAddress addr, IActorDescription actor, List<PatientIdentifier> ids,
			PRPAMT201307UV02QueryByParameter queryByParameter, II queryId, String responseCode) {		
		PRPAIN201310UV02 response = HL7AckTransforms.createQueryResponse(message.getId(), addr.getReceiverApplication(), addr.getReceiverFacility(), 
				addr.getSenderApplication(), addr.getSenderFacility(), null);
		PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = HL7AckTransforms.
				createQueryResponseControlActProcess(actor, ids, queryByParameter, queryId, responseCode);
		response.setControlActProcess(controlActProcess);
		return response;
	}

	private List<PatientIdentifier> filterIdsByDataSources(List<PatientIdentifier> ids, List<Identifier> dataSources) {
		List<PatientIdentifier> filteredIds = new ArrayList<PatientIdentifier>();
		for (PatientIdentifier id : ids) {
			if (dataSources.contains(id.getAssigningAuthority())) {
				filteredIds.add(id);
			}
		}
		return filteredIds;
	}

	// Validate return data source domains (Case #5 in the IHE PIX Specification indicates the following:
	// .Case 5: The PIX Manager does not recognize one or more of the Patient Identification Domains for which an
	// identifier has been requested.
	// AE (application error) is returned in Acknowledgement.typeCode and in QueryAck.queryResponseCode.
	// No RegistrationEvent is returned. The queried for patient identifier is returned in the QueryByParameter parameter list. 
	// For each domain that was not recognized, an AcknowledgementDetail class is returned in which the attributes typeCode, code, 
	// and location are valued as follows.
	// typeCode: E
	// code: 204 (Unknown Key Identifier)
	// location:	XPath expression for the value element of the DataSource parameter 
	// (which includes the repetition number of the parameter)
	// 
	private List<Identifier> validateDataSources(List<Identifier> dataSources) throws PixManagerException {
	
		List<Identifier> returnDomains = new ArrayList<Identifier>();
		List<String> invalidDomains = new ArrayList<String>();
		for (Identifier domain : dataSources) {
			boolean validDomain = AssigningAuthorityUtil.validateDomain(domain, pixManager.getActorDescription(), pixAdapter);	

			if (validDomain) {
				// reconcile assigning authority
				Identifier reconciledDomain = AssigningAuthorityUtil.reconcileIdentifier(domain, pixManager.getActorDescription(),
						pixAdapter);
				returnDomains.add(reconciledDomain);
			} else {
				invalidDomains.add(domain.getUniversalId());
			}
		}
		
		if (invalidDomains.size() > 0) {
			log.debug("Found invalid data source domains in PIX Query: " + invalidDomains);
			PixManagerException e = new PixManagerException("Unknown data source domain");
			e.setCode("204");
			e.setLocation("//controlActProcess/queryByParameter/parameterList/dataSource/value/@root='");
			e.setData(invalidDomains);
			throw e;
		}
		return returnDomains;
	}

	private void validatePatient(PatientIdentifier queryId) throws PixManagerException {
		boolean isValidPatient = true;
		try {
			isValidPatient = pixAdapter.isValidPatient(queryId, null);
		} catch (PixManagerException e) {
			log.error("Exception while validating query patient identifier: " + e, e);
			throw e;
		}

		if  (!isValidPatient) {
			PixManagerException e = new PixManagerException("Unknown Key Identifier - patient ID " + 
					queryId.getId() + " in domain " + 
					queryId.getAssigningAuthority().getAuthorityNameString());
			e.setCode("204");
			e.setLocation("//controlActProcess/queryByParameter/parameterList/patientIdentifier/value/@extension='" + 
					queryId.getId() + "'");
			log.debug("Received request with an unknown query identifier.");
			throw e;
		}
	}

	private void validateQueryIdentifierDomain(PatientIdentifier queryId) throws PixManagerException {
		boolean validDomain = AssigningAuthorityUtil.validateDomain(queryId.getAssigningAuthority(),
				pixManager.getActorDescription(), pixAdapter);
		if (!validDomain) {
			PixManagerException e = new PixManagerException("Unknown Key Identifier - patient ID request domain: " +
					queryId.getAssigningAuthority().getAuthorityNameString());
			e.setCode("204");
			e.setLocation("//controlActProcess/queryByParameter/parameterList/patientIdentifier/value/@root='" + 
					queryId.getAssigningAuthority().getUniversalId() + "'");
			log.debug("Received request with an unknown assigning authority in the query identifier.");
			throw e;
		}
	}

	private Map<String, Object> extractQueryIdentifierAndDomains(PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess) throws Hl7MessageValidationException {
		if (controlActProcess == null ||
				controlActProcess.getQueryByParameter() == null ||
				controlActProcess.getQueryByParameter().getValue() == null) {
			log.warn("Received PIX query request with missing query parameters.");
			Hl7MessageValidationException e = new Hl7MessageValidationException("Invalid query request; missing query parameters.");
			throw e;
		}
		PRPAMT201307UV02QueryByParameter query = controlActProcess.getQueryByParameter().getValue();
		if (query.getQueryId() == null) {
			log.warn("Received PIX query request with missing query identifier.");
			Hl7MessageValidationException e = new Hl7MessageValidationException("Invalid query request; missing query identifier.");
			throw e;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(PIX_QUERY_ID, query.getQueryId());
		params.put(PIX_QUERY_QUERY_BY_PARAMETER, query);
		
		if (query.getParameterList() == null || 
				query.getParameterList().getPatientIdentifier() == null ||
				query.getParameterList().getPatientIdentifier().size() == 0 ||
				query.getParameterList().getPatientIdentifier().get(0).getValue() == null ||
				query.getParameterList().getPatientIdentifier().get(0).getValue().size() == 0) {
			log.warn("Received PIX query request with missing query patient identifier.");
			Hl7MessageValidationException e = new Hl7MessageValidationException("Invalid query request; missing query patient identifier.");
			throw e;
		}
		II id = query.getParameterList().getPatientIdentifier().get(0).getValue().get(0);
		params.put(PIX_QUERY_IDENTIFIER, id);
		
		if (query.getParameterList().getDataSource() != null &&
				query.getParameterList().getDataSource().size() > 0) {
			List<PRPAMT201307UV02DataSource> sources = query.getParameterList().getDataSource();
			List<Identifier> dataSourceIds = new ArrayList<Identifier>();
			for (PRPAMT201307UV02DataSource source : sources) {
				if (source != null && 
						source.getValue() != null &&
						source.getValue().size() > 0 &&
						source.getValue().get(0).getRoot() != null) {
						log.debug("Found a data source domain ID in the PIX query of " + source.getValue().get(0));
						Identifier identifier = new Identifier(null, source.getValue().get(0).getRoot(), 
								HL7Constants.UNIVERSAL_IDENTIFIER_TYPE_CODE_ISO);
						dataSourceIds.add(identifier);
				}
			}
			params.put(PIX_DATA_SOURCES, dataSourceIds);
		}
		return params;
	}
	
	private boolean validateDomain(Patient patient, boolean isSubsumedPatient) throws PixManagerException {
		List<PatientIdentifier> patientIds = patient.getPatientIds();
		
		for (int i = 0; i < patientIds.size(); i++) {
			Identifier domain = patientIds.get(i).getAssigningAuthority();
			boolean domainOk = AssigningAuthorityUtil.validateDomain(domain, pixManager.getActorDescription(), pixAdapter);

			if (!domainOk) {
				PixManagerException e = new PixManagerException("Unknown Key Identifier - domain: " + domain.getAuthorityNameString());
				e.setCode("204");
				if (isSubsumedPatient) {
					e.setLocation("//controlActProcess/subject/registrationEvent/replacementOf/priorRegistration/subject1/priorRegisteredRole/id/@root='" +
							domain.getUniversalId()+"'");					
				} else {
					e.setLocation("//controlActProcess/subject/registrationEvent/subject1/patient/id/@root='" + 
							domain.getUniversalId()+"'");
				}
				throw e;
			}	
		}
		return true;
	}
	
	private boolean validatePatientIdNotEmpty(Patient patient) throws PixManagerException {
		List<PatientIdentifier> pids = patient.getPatientIds();
		if (0 == pids.size()) {
			PixManagerException e = new PixManagerException("Unknown Key Identifier - patient id is null");
			e.setCode("204");
			e.setLocation("//controlActProcess/subject/registrationEvent/subject1/patient/id");
			throw e;
		}
		return true;
	}
}
