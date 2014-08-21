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
package org.openhie.openempi.openpixpdqadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.datamodel.MessageHeader;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openpixpdq.api.IPixManagerAdapter;
import org.openhealthtools.openpixpdq.api.PixManagerException;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PixManagerAdapter extends BasePixPdqAdapter implements IPixManagerAdapter
{
	protected final Log log = LogFactory.getLog(getClass());
	
	public PixManagerAdapter() {
		super();
	}
	
	public List<PatientIdentifier> createPatient(Patient patient, MessageHeader armessageHeader1) throws PixManagerException {
		Person person = ConversionHelper.getPerson(patient);
		
		Person personAdded;
		try {
			SecurityHelper.getSessionKey();
			personAdded = Context.getPersonManagerService().addPerson(person);
		} catch (Exception e) {
			// If the person already exists then ignore the error
			if (e instanceof ApplicationException) {
				return patient.getPatientIds();
			}
			throw new PixManagerException(e);
		}
		
		//Find the updated matching list for PIX Update Notification
		List<PatientIdentifier> matching = getPatientIds(personAdded);
		return matching;
	}

	public List<List<PatientIdentifier>> mergePatients(Patient patientMain, Patient otherPatient, MessageHeader header)
			throws PixManagerException {
		List<List<PatientIdentifier>> ret = new ArrayList<List<PatientIdentifier>>();
    	
		//1. Find the old matching of mergePatient
		PatientIdentifier mainPatientId = patientMain.getPatientIds().get(0);
		PatientIdentifier otherPatientId = otherPatient.getPatientIds().get(0);
		List<PatientIdentifier> oldMrgMatching = findPatientIds(mainPatientId, header);

		//2. Merge Patients
		try {
			Context.getPersonManagerService().mergePersons(ConversionHelper.getPersonIdentifier(otherPatientId),
					ConversionHelper.getPersonIdentifier(mainPatientId));
		} catch (Exception e) {
			throw new PixManagerException(e);
		}

		//3. Find lists of patients to be updated
		List<PatientIdentifier> newMatching = findPatientIds(mainPatientId, header);
		List<PatientIdentifier> unmatching = new ArrayList<PatientIdentifier>();
		for (PatientIdentifier oldMrg : oldMrgMatching) {
			if (!newMatching.contains(oldMrg)) {
				unmatching.add(oldMrg);
			}
		}

		//4.PIX Update Notification to PIX consumers					
		//If there is any update on the matching
		if (!newMatching.equals(oldMrgMatching)) {
			//Add the original patient id since findPatientIds 
			//does not include the original patient id.
			newMatching.add(mainPatientId);
			ret.add(newMatching);
		}
		if (unmatching.size() > 0) {
			ret.add(unmatching);			
		}
		return ret;
	}

	public List<List<PatientIdentifier>> updatePatient(Patient patient, MessageHeader messageHeader) throws PixManagerException {
		List<List<PatientIdentifier>> ret = new ArrayList<List<PatientIdentifier>>();
		
		Person person = ConversionHelper.getPerson(patient);
		
		PatientIdentifier patientId = patient.getPatientIds().get(0);
		List<PatientIdentifier> oldMatching = findPatientIds(patientId, messageHeader);
		
		//1. find the original matching patient
		// TODO: Need to check that there is at least one identifier provided here but
		// for now the assumption is that the generic PIX code will do the validation before it forwards the
		// request to the adaptor.
		//
		PersonIdentifier id = person.getPersonIdentifiers().iterator().next();
		
		Person originalPerson;
		try {
			SecurityHelper.getSessionKey();
			originalPerson = Context.getPersonQueryService().findPersonById(id);
		} catch (Exception e) {
			throw new PixManagerException(e);
		}
		
		if (originalPerson == null) {
			log.warn("Unable to find the patient to be updated using the identifier: "  + id);
			throw new PixManagerException("Unable to find the patient to be updated in the registry.");
		}
		
		updatePersonAttributes(originalPerson, person);
		
		//2. Update Patient
		try {
			Context.getPersonManagerService().updatePerson(originalPerson);
		} catch (Exception e) {
			throw new PixManagerException(e);
		}
		
		List<PatientIdentifier> newMatching = findPatientIds(patientId, messageHeader);
		List<PatientIdentifier> unmatching = new ArrayList<PatientIdentifier>();
		for (PatientIdentifier oldPid : oldMatching) {
			if (!newMatching.contains(oldPid)) {
				unmatching.add(oldPid);
			}					
		}
		
		//If there is any update on the matching
		if (!newMatching.equals(oldMatching)) {
			//Add the original patient id since findPatientIds 
			//does not include the original patient id.
			newMatching.add(patientId);
			ret.add(newMatching);
		}
		if (unmatching.size() > 0) {
			ret.add(unmatching);			
		}
		return ret;
	}

	private void updatePersonAttributes(Person originalPerson, Person person) {
		if (isPopulated(person.getGivenName())) {
			originalPerson.setGivenName(person.getGivenName());
		}
		if (isPopulated(person.getFamilyName())) {
			originalPerson.setFamilyName(person.getFamilyName());
		}
		if (isPopulated(person.getDateOfBirth())) {
			originalPerson.setDateOfBirth(person.getDateOfBirth());
		}
		if (person.getGender() != null) {
			
			originalPerson.setGender(person.getGender());
		}
		if (isPopulated(person.getAddress1())) {
			originalPerson.setAddress1(person.getAddress1());
		}
		if (isPopulated(person.getCity())) {
			originalPerson.setCity(person.getCity());
		}
		if (isPopulated(person.getState())) {
			originalPerson.setState(person.getState());
		}
		if (isPopulated(person.getPostalCode())) {
			originalPerson.setPostalCode(person.getPostalCode());
		}
		if (isPopulated(person.getPhoneAreaCode())) {
			originalPerson.setPhoneAreaCode(person.getPhoneAreaCode());
		}
		if (isPopulated(person.getPhoneNumber())) {
			originalPerson.setPhoneNumber(person.getPhoneNumber());
		}
	}

	private boolean isPopulated(String field) {
		if (field != null && field.length() > 0) {
			return true;
		}
		return false;
	}
	
	private boolean isPopulated(java.util.Date field) {
		if (field != null && field.toString().length() > 0) {
			return true;
		}
		return false;
	}
	
	public List<PatientIdentifier> findPatientIds(PatientIdentifier identifier, MessageHeader messageHeader)
			throws PixManagerException {

		List<PatientIdentifier> ret = new ArrayList<PatientIdentifier>();
		PersonIdentifier searchIdentifier = ConversionHelper.getPersonIdentifier(identifier);

		try {
			SecurityHelper.getSessionKey();
			Person personFound = Context.getPersonQueryService().findPersonById(searchIdentifier);
			if (personFound == null) {
				return ret;
			}
			
			for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
				if (!id.equals(searchIdentifier)) {
					ret.add(ConversionHelper.getPatientIdentifier(id));
				}
			}
			List<Person> persons = Context.getPersonQueryService().findLinkedPersons(searchIdentifier);
			for (Person person : persons) {
				for (PersonIdentifier id : person.getPersonIdentifiers()) {
					if (!id.equals(searchIdentifier)) {
						ret.add(ConversionHelper.getPatientIdentifier(id));
					}
				}
			}
		} catch (Exception e) {
			throw new PixManagerException(e);
		}
		return ret;
	}

	public boolean isValidPatient(PatientIdentifier patientIdentifier, MessageHeader messageHeader) throws PixManagerException {
		try {
			SecurityHelper.getSessionKey();
			Person person = Context.getPersonQueryService().findPersonById(ConversionHelper.getPersonIdentifier(patientIdentifier));
			if (person != null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new PixManagerException(e);
		}
	}

	private List<PatientIdentifier> getPatientIds(Person person) {
		List<PatientIdentifier> ret = new ArrayList<PatientIdentifier>();
		List<org.openhie.openempi.model.PersonIdentifier> localIds = new ArrayList<org.openhie.openempi.model.PersonIdentifier>();
		if (person != null) {
			Set<org.openhie.openempi.model.PersonIdentifier> pids = person.getPersonIdentifiers();
			for (org.openhie.openempi.model.PersonIdentifier from : pids) {
				PatientIdentifier to = ConversionHelper.convertPatientIdentifier(from);
				localIds.add(from);
				ret.add( to );
			}
			
			Set<org.openhie.openempi.model.PersonIdentifier> foundIds = new java.util.HashSet<org.openhie.openempi.model.PersonIdentifier>();
			foundIds.addAll(localIds);
			for (org.openhie.openempi.model.PersonIdentifier searchIdentifier : localIds) {
				List<Person> persons = Context.getPersonQueryService().findLinkedPersons(searchIdentifier);
				for (Person foundPerson: persons) {
					for (PersonIdentifier id : foundPerson.getPersonIdentifiers()) {
						if (!idInSet(id, foundIds)) {
							ret.add(ConversionHelper.getPatientIdentifier(id));
							foundIds.add(id);
						}
					}
				}
			}
		}
		return ret;
	}

	private boolean idInSet(PersonIdentifier id, Set<PersonIdentifier> foundIds) {
		if (foundIds.contains(id)) {
			return true;
		}
		return false;
	}
}
