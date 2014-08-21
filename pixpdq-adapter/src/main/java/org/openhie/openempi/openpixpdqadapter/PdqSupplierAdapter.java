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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openhealthtools.openexchange.datamodel.MessageHeader;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openpixpdq.api.IPdSupplierAdapter;
import org.openhealthtools.openpixpdq.api.PdSupplierException;
import org.openhealthtools.openpixpdq.api.PdqQuery;
import org.openhealthtools.openpixpdq.api.PdqResult;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PdqSupplierAdapter extends BasePixPdqAdapter implements IPdSupplierAdapter
{
	public PdqSupplierAdapter() {
		super();
	}
	
	public PdqResult findPatients(PdqQuery query, MessageHeader header) throws PdSupplierException {
		Person personTemplate = ConversionHelper.getPerson(query);
		try {
			SecurityHelper.getSessionKey();
			List<List<Patient>> allPatients = new ArrayList<List<Patient>>();
			
			List<Person> persons = null;
			// If the caller provides identifiers to use in the query, then search using the identifiers else use the attributes
			if (personTemplate.getPersonIdentifiers().size() > 0) {
				persons = Context.getPersonQueryService().findPersonsById(personTemplate.getPersonIdentifiers().iterator().next());
			} else {
				persons = Context.getPersonQueryService().findPersonsByAttributes(personTemplate);
			}
			
			if (persons == null || persons.size() == 0) {
				return new PdqResult(allPatients);
			}

			List<Person> uniquePersons = new ArrayList<Person>();
			for (int i=0; i < persons.size(); i++) {
				Person person = Context.getPersonQueryService().loadPerson(persons.get(i).getPersonId());
				Person uniquePerson = findLinkedPersonInList(person, uniquePersons);
				if (uniquePerson != null) {
					addPersonIdentifiersToUniquePerson(person.getPersonIdentifiers(), uniquePerson);
				} else {
					uniquePersons.add(person);
				}
			}

			//Converts to Patients
			for (Person person : uniquePersons) {
				List<Patient> patients = new ArrayList<Patient>();
				Patient patient = ConversionHelper.getPatient(person);
				patients.add(patient);
				allPatients.add(patients);
			}
			return new PdqResult(allPatients);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new PdSupplierException(e);
		}
	}
	
	private void addPersonIdentifiersToUniquePerson(Set<PersonIdentifier> personIdentifiers, Person uniquePerson) {
		Set<org.openhie.openempi.model.PersonIdentifier> identifiers = uniquePerson.getPersonIdentifiers();
		identifiers.addAll(personIdentifiers);
	}

	private Person findLinkedPersonInList(Person person, List<Person> uniquePersons) {
		if (uniquePersons.size() == 0) {
			return null;
		}
		Set<Person> linkedPersonSet = new HashSet<Person>();
		for (org.openhie.openempi.model.PersonIdentifier identifier : person.getPersonIdentifiers()) {
			List<Person> linkedPersons = Context.getPersonQueryService().findLinkedPersons(identifier);
			linkedPersonSet.addAll(linkedPersons);
		}
		for (Person uniquePerson : uniquePersons) {
			for (Person linkedPerson : linkedPersonSet) {
				if (linkedPerson.getPersonId().equals(uniquePerson.getPersonId())) {
					return uniquePerson;
				}
			}
		}
		return null;
	}

	public void cancelQuery(String queryTag, String messageQueryName) throws PdSupplierException {
		//TODO: Add Implementation
		//Need to keep a mapping between the continuation reference 
		//id and the given queryTag.
	}
}
