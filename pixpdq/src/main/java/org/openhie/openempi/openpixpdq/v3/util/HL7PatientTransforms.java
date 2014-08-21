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

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT150002UV01Organization;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UV02BirthPlace;
import org.hl7.v3.PRPAMT201301UV02OtherIDs;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201302UV02BirthPlace;
import org.hl7.v3.PRPAMT201302UV02OtherIDs;
import org.hl7.v3.PRPAMT201302UV02OtherIDsId;
import org.hl7.v3.PRPAMT201302UV02Patient;
import org.hl7.v3.PRPAMT201302UV02PatientId;
import org.hl7.v3.PRPAMT201302UV02PatientPatientPerson;
import org.hl7.v3.PRPAMT201302UV02PatientStatusCode;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.hl7.v3.PRPAMT201304UV02Person;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.PRPAMT201310UV02BirthPlace;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PatientTransforms
{
    private static Log log = LogFactory.getLog(HL7PatientTransforms.class);

    public static org.hl7.v3.PRPAMT201301UV02Patient create201301Patient(PRPAMT201306UV02ParameterList paramList, String aaId) {
        PRPAMT201301UV02Patient result = new PRPAMT201301UV02Patient();

        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();

        if (paramList == null) {
            return null;
        }
        // Set the Subject Gender Code
        if (paramList.getLivingSubjectAdministrativeGender() != null &&
                paramList.getLivingSubjectAdministrativeGender().size() > 0) {
            CE genderCode = paramList.getLivingSubjectAdministrativeGender().get(0).getValue().get(0);

            person.setAdministrativeGenderCode(genderCode);
        }

        // Set the Subject Birth Time
        if (paramList.getLivingSubjectBirthTime() != null && paramList.getLivingSubjectBirthTime().size() > 0) {

            person.setBirthTime(createBirthTime(paramList.getLivingSubjectBirthTime().get(0)));
        }
        // Set the address
        if (paramList.getPatientAddress() != null &&
                paramList.getPatientAddress().size() > 0) {
            for (PRPAMT201306UV02PatientAddress patAdd : paramList.getPatientAddress()) {
                for (ADExplicit newAdd : patAdd.getValue()) {
                    person.getAddr().add(newAdd);
                }
            }
        }
        //Set the telcom
        if (paramList.getPatientTelecom() != null && paramList.getPatientTelecom().size() > 0) {
            for (PRPAMT201306UV02PatientTelecom telcom : paramList.getPatientTelecom()) {
                if (telcom != null) {
                    for (TELExplicit newTelcom : telcom.getValue()) {
                        person.getTelecom().add(newTelcom);
                    }
                }

            }
        }

        // Set the Subject Name
        if (paramList.getLivingSubjectName() != null &&
                paramList.getLivingSubjectName().size() > 0) {
            for (PRPAMT201306UV02LivingSubjectName subjName : paramList.getLivingSubjectName()) {

                for (ENExplicit name : subjName.getValue()) {
                    PNExplicit newName = HL7DataTransformHelper.convertENtoPN(name);
                    newName = HL7ArrayTransforms.copyNullFlavors(name, newName);
                    person.getName().add(newName);
                }
            }
            //paramList.getLivingSubjectName().get(index)
        }

        // Set the subject Id
        if (paramList.getLivingSubjectId() != null &&
                paramList.getLivingSubjectId().size() > 0) {
            for (PRPAMT201306UV02LivingSubjectId subjId : paramList.getLivingSubjectId()) {
                for (II id : subjId.getValue()) {
                    if (id.getRoot().equalsIgnoreCase(aaId)) {
                        result.getId().add(id);
                    } else {
                        PRPAMT201301UV02OtherIDs otherId = new PRPAMT201301UV02OtherIDs();
                        otherId.getId().add(id);
                        person.getAsOtherIDs().add(otherId);
                    }
                }
            }
        }

        person = HL7ArrayTransforms.copyNullFlavors(paramList, person);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> jaxbPerson = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, person);

        jaxbPerson.setValue(person);

        result.setPatientPerson(jaxbPerson);

        return result;
    }

    public static PRPAMT201301UV02Patient create201301Patient(JAXBElement<PRPAMT201301UV02Person> person, String patId) {
        return create201301Patient(person, HL7DataTransformHelper.IIFactory(patId));
    }

    public static PRPAMT201301UV02Patient createPRPAMT201301UVPatient(PRPAMT201310UV02Patient patient) {
        PRPAMT201301UV02Patient result = new PRPAMT201301UV02Patient();

        if (patient == null) {
            return null;
        }


        result.setEffectiveTime(patient.getEffectiveTime());
        if (patient.getProviderOrganization() != null) {
            result.setProviderOrganization(patient.getProviderOrganization().getValue());
        }

        result.setStatusCode(patient.getStatusCode());
        result.setTypeId(patient.getTypeId());
        result.setVeryImportantPersonCode(patient.getVeryImportantPersonCode());

        for (ADExplicit address : patient.getAddr()) {
            result.getAddr().add(address);
        }
        for (TELExplicit telephone : patient.getTelecom()) {
            result.getTelecom().add(telephone);
        }

        for (CE code : patient.getConfidentialityCode()) {
            result.getConfidentialityCode().add(code);
        }

        result.setPatientPerson(create201301PatientPerson(patient.getPatientPerson().getValue()));
        result = HL7ArrayTransforms.copyIIs(patient, result);

        return result;
    }

    public static PRPAMT201301UV02Patient createPRPAMT201301UVPatient(PRPAMT201302UV02Patient patient) {
        PRPAMT201301UV02Patient result = new PRPAMT201301UV02Patient();

        if (patient == null) {
            return null;
        }

        result.setEffectiveTime(patient.getEffectiveTime());
        if (patient.getProviderOrganization() != null) {
            result.setProviderOrganization(patient.getProviderOrganization().getValue());
        }

        result.setStatusCode(patient.getStatusCode());
        result.setTypeId(patient.getTypeId());
        result.setVeryImportantPersonCode(patient.getVeryImportantPersonCode());

        for (ADExplicit address : patient.getAddr()) {
            result.getAddr().add(address);
        }
        for (TELExplicit telephone : patient.getTelecom()) {
            result.getTelecom().add(telephone);
        }

        for (CE code : patient.getConfidentialityCode()) {
            result.getConfidentialityCode().add(code);
        }


        result.setPatientPerson(create201301PatientPerson(patient.getPatientPerson()));
        return result;
    }

    public static PRPAMT201301UV02Patient create201301Patient(JAXBElement<PRPAMT201301UV02Person> person, String patId, String assigningAuthority) {
        return create201301Patient(person, HL7DataTransformHelper.IIFactory(assigningAuthority, patId));
    }

    public static PRPAMT201301UV02Patient create201301Patient(JAXBElement<PRPAMT201301UV02Person> person, II id) {
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();

        patient.getClassCode().add("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(id);

        patient.setPatientPerson(person);

        patient.setProviderOrganization(null);

        return patient;
    }    

	public static PRPAMT201304UV02Patient create201304Patient(List<PatientIdentifier> ids) {
		PRPAMT201304UV02Patient patient = new PRPAMT201304UV02Patient();
        patient.getClassCode().add(HL7Constants.CLASS_CODE_PATIENT);
        patient.setStatusCode(HL7DataTransformHelper.CSFactory(HL7Constants.STATUS_CODE_ACTIVE));
        for (PatientIdentifier id : ids) {
        	II ii = HL7DataTransformHelper.IIFactory(id.getAssigningAuthority().getUniversalId(), id.getId());
        	patient.getId().add(ii);
        }
        patient.setPatientPerson(create201304PatientPerson(ids));
		return patient;
	}

    private static JAXBElement<PRPAMT201304UV02Person> create201304PatientPerson(List<PatientIdentifier> ids) {
    	PRPAMT201304UV02Person person = new PRPAMT201304UV02Person();
    	person.setDeterminerCode(HL7Constants.DETERMINER_CODE_INSTANCE);
    	person.getClassCode().add(HL7Constants.CLASS_CODE_PSN);
    	PNExplicit patName = new PNExplicit();
    	patName.getNullFlavor().add("NA");
    	person.getName().add(patName);
//        if (ids.size() > 1) {
//        	for (int i=1; i < ids.size(); i++) {
//        		PatientIdentifier id = ids.get(i);
//            	II ii = HL7DataTransformHelper.IIFactory(id.getAssigningAuthority().getUniversalId(), id.getId());
//            	PRPAMT201304UV02OtherIDs otherId = new PRPAMT201304UV02OtherIDs();
//            	otherId.getId().add(ii);
//            	COCTMT150002UV01Organization scopingOrg = new COCTMT150002UV01Organization();
//            	scopingOrg.setClassCode(HL7Constants.CLASS_CODE_ORG);
//            	scopingOrg.setDeterminerCode(HL7Constants.DETERMINER_CODE_INSTANCE);
//            	II orgId = HL7DataTransformHelper.IIFactory(id.getAssigningAuthority().getUniversalId());
//            	scopingOrg.getId().add(orgId);
//            	otherId.setScopingOrganization(scopingOrg);
//            	person.getAsOtherIDs().add(otherId);
//        	}
//        }
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201304UV02Person> result = new JAXBElement<PRPAMT201304UV02Person>(xmlqname, PRPAMT201304UV02Person.class, person);
		return result;
	}

	public static JAXBElement<PRPAMT201310UV02Person> create201310PatientPerson(JAXBElement<PRPAMT201301UV02Person> person201301) {
        TSExplicit birthTime = person201301.getValue().getBirthTime();
        CE gender = person201301.getValue().getAdministrativeGenderCode();
        PNExplicit patName = person201301.getValue().getName().get(0);
        String ssn = null;
        if (person201301 != null &&
                person201301.getValue() != null &&
                Utilities.isNotNullish(person201301.getValue().getAsOtherIDs()) &&
                person201301.getValue().getAsOtherIDs().get(0) != null &&
                Utilities.isNotNullish(person201301.getValue().getAsOtherIDs().get(0).getId()) &&
                person201301.getValue().getAsOtherIDs().get(0).getId().get(0) != null &&
                Utilities.isNotNullish(person201301.getValue().getAsOtherIDs().get(0).getId().get(0).getExtension())) {
            ssn = person201301.getValue().getAsOtherIDs().get(0).getId().get(0).getExtension();
        }
        return create201310PatientPerson(patName, gender, birthTime, createPRPAMT201310UVOtherIDs(ssn));


    }

    public static PRPAMT201310UV02Patient create201310Patient(PRPAMT201301UV02Patient patient, String patientId, String orgId) {
        JAXBElement<PRPAMT201310UV02Person> person = create201310PatientPerson(patient.getPatientPerson());

        return create201310Patient(person, patientId, orgId);
    }

    public static PRPAMT201310UV02Patient create201310Patient(JAXBElement<PRPAMT201310UV02Person> person, String patId) {
        log.debug("begin create201310Patient");
        return create201310Patient(person, HL7DataTransformHelper.IIFactory(patId));
    }

    public static PRPAMT201310UV02Patient create201310Patient(JAXBElement<PRPAMT201310UV02Person> person, String patId, String assigningAuthority) {
        return create201310Patient(person, HL7DataTransformHelper.IIFactory(assigningAuthority, patId));
    }

    public static PRPAMT201310UV02Patient create201310Patient(JAXBElement<PRPAMT201310UV02Person> person, II id) {
        PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();

        patient.getClassCode().add("PAT");
        patient.setStatusCode(HL7DataTransformHelper.CSFactory("active"));

        patient.getId().add(id);

        patient.setPatientPerson(person);

        patient.setProviderOrganization(null);

        return patient;
    }

    public static PRPAMT201302UV02Patient create201302Patient(String remotePatId, JAXBElement<PRPAMT201301UV02Person> person, II localPatId) {
        return create201302Patient(HL7DataTransformHelper.IIFactory(remotePatId), person, localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(String remotePatId, String remoteAssigningAuthority, JAXBElement<PRPAMT201301UV02Person> person, II localPatId) {
        return create201302Patient(HL7DataTransformHelper.IIFactory(remoteAssigningAuthority, remotePatId), person, localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(II remotePatId, JAXBElement<PRPAMT201301UV02Person> person, II localPatId) {
        PRPAMT201302UV02Patient patient = new PRPAMT201302UV02Patient();

        patient.getClassCode().add("PAT");

        PRPAMT201302UV02PatientStatusCode statusCode = new PRPAMT201302UV02PatientStatusCode();
        statusCode.setCode("active");
        patient.setStatusCode(statusCode);
        PRPAMT201302UV02PatientId patId = new PRPAMT201302UV02PatientId();
        patId.setExtension(localPatId.getExtension());
        patId.setRoot(localPatId.getRoot());
        patient.getId().add(patId);

        if (person.getValue() != null) {
            PRPAMT201301UV02Person inputPerson = person.getValue();
            JAXBElement<PRPAMT201302UV02PatientPatientPerson> patientPerson = create201302PatientPerson(inputPerson.getName().get(0), inputPerson.getAdministrativeGenderCode(), inputPerson.getBirthTime(), inputPerson.getAsOtherIDs(), remotePatId);
            patient.setPatientPerson(patientPerson);
        }

        patient.setProviderOrganization(null);

        return patient;
    }

    public static PRPAMT201302UV02Patient create201302Patient(JAXBElement<PRPAMT201310UV02Person> person, String remotePatId, II localPatId) {
        return create201302Patient(person, HL7DataTransformHelper.IIFactory(remotePatId), localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(JAXBElement<PRPAMT201310UV02Person> person, String remotePatId, String remoteAssigningAuthority, II localPatId) {
        return create201302Patient(person, HL7DataTransformHelper.IIFactory(remoteAssigningAuthority, remotePatId), localPatId);
    }

    public static PRPAMT201302UV02Patient create201302Patient(JAXBElement<PRPAMT201310UV02Person> person, II remotePatId, II localPatId) {
        PRPAMT201302UV02Patient patient = new PRPAMT201302UV02Patient();

        patient.getClassCode().add("PAT");
        PRPAMT201302UV02PatientStatusCode statusCode = new PRPAMT201302UV02PatientStatusCode();
        statusCode.setCode("active");
        patient.setStatusCode(statusCode);

        PRPAMT201302UV02PatientId patId = new PRPAMT201302UV02PatientId();
        patId.setExtension(localPatId.getExtension());
        patId.setRoot(localPatId.getRoot());
        patient.getId().add(patId);

        if (person.getValue() != null) {
            PRPAMT201310UV02Person inputPerson = person.getValue();
            JAXBElement<PRPAMT201302UV02PatientPatientPerson> patientPerson = create201302PatientPerson(inputPerson.getAsOtherIDs(), inputPerson.getName().get(0), inputPerson.getAdministrativeGenderCode(), inputPerson.getBirthTime(), remotePatId);
            patient.setPatientPerson(patientPerson);
        }

        patient.setProviderOrganization(null);

        return patient;
    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
        log.debug("begin create201301PatientPerson");
        PNExplicit name = null;

        log.debug("begin create Name");
        if (Utilities.isNotNullish(patFirstName) ||
                Utilities.isNotNullish(patLastName)) {
            log.debug("not nullish");
            name = HL7DataTransformHelper.CreatePNExplicit(patFirstName, patLastName);
        }

        log.debug("begin create gender");
        CE genderCode = null;
        if (Utilities.isNotNullish(gender)) {
            genderCode = HL7DataTransformHelper.CEFactory(gender);
        }

        log.debug("begin create birthTime");
        TSExplicit bday = null;
        if (Utilities.isNotNullish(birthTime)) {
            bday = HL7DataTransformHelper.TSExplicitFactory(birthTime);
        }

        log.debug("begin create otherIds");
        PRPAMT201301UV02OtherIDs otherIds = null;
        if (Utilities.isNotNullish(ssn)) {
            otherIds = createPRPAMT201301UVOtherIDs(ssn);
        }

        log.debug("end create201301PatientPerson");
        return create201301PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201301UV02OtherIDs otherIds) {
        PRPAMT201301UV02Person person = new PRPAMT201301UV02Person();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        // Set the Birth Time
        if (birthTime != null) {
            person.setBirthTime(birthTime);
        }

        // Set the SSN
        if (otherIds != null) {
            person.getAsOtherIDs().add(otherIds);
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> result = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, person);

        return result;
    }

    public static JAXBElement<PRPAMT201310UV02Person> create201310PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn) {
        PNExplicit name = null;
        if (Utilities.isNotNullish(patFirstName) &&
                Utilities.isNotNullish(patLastName)) {
            name = HL7DataTransformHelper.CreatePNExplicit(patFirstName, patLastName);
        }

        CE genderCode = null;
        if (Utilities.isNotNullish(gender)) {
            genderCode = HL7DataTransformHelper.CEFactory(gender);
        }

        TSExplicit bday = null;
        if (Utilities.isNotNullish(birthTime)) {
            bday = HL7DataTransformHelper.TSExplicitFactory(birthTime);
        }

        PRPAMT201310UV02OtherIDs otherIds = null;
        if (Utilities.isNotNullish(ssn)) {
            otherIds = createPRPAMT201310UVOtherIDs(ssn);
        }

        return create201310PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201310UV02Person> create201310PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201310UV02OtherIDs otherIds) {
        PRPAMT201310UV02Person person = new PRPAMT201310UV02Person();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        // Set the Birth Time
        if (birthTime != null) {
            person.setBirthTime(birthTime);
        }

        // Set the SSN
        if (otherIds != null) {
            person.getAsOtherIDs().add(otherIds);
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201310UV02Person> result = new JAXBElement<PRPAMT201310UV02Person>(xmlqname, PRPAMT201310UV02Person.class, person);

        return result;
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(String patFirstName, String patLastName, String gender, String birthTime, String ssn, II remotePatId) {
        PNExplicit name = null;
        if (Utilities.isNotNullish(patFirstName) &&
                Utilities.isNotNullish(patLastName)) {
            name = HL7DataTransformHelper.CreatePNExplicit(patFirstName, patLastName);
        }

        CE genderCode = null;
        if (Utilities.isNotNullish(gender)) {
            genderCode = HL7DataTransformHelper.CEFactory(gender);
        }

        TSExplicit bday = null;
        if (Utilities.isNotNullish(birthTime)) {
            bday = HL7DataTransformHelper.TSExplicitFactory(birthTime);
        }

        PRPAMT201302UV02OtherIDs otherIds = null;
        if (Utilities.isNotNullish(ssn) ||
                (remotePatId != null &&
                Utilities.isNotNullish(remotePatId.getRoot()) &&
                Utilities.isNotNullish(remotePatId.getExtension()))) {
            otherIds = createPRPAMT201302UVOtherIDs(ssn, remotePatId);
        }

        return create201302PatientPerson(name, genderCode, bday, otherIds);
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, List<PRPAMT201301UV02OtherIDs> otherIds, II remotePatId) {
        PRPAMT201302UV02OtherIDs convertedOtherIds = null;
        if (otherIds != null &&
                otherIds.size() > 0 &&
                otherIds.get(0) != null &&
                otherIds.get(0).getId() != null &&
                otherIds.get(0).getId().size() > 0 &&
                otherIds.get(0).getId().get(0) != null &&
                Utilities.isNotNullish(otherIds.get(0).getId().get(0).getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(otherIds.get(0).getId().get(0).getExtension(), remotePatId);
        } else if (remotePatId != null &&
                Utilities.isNotNullish(remotePatId.getRoot()) &&
                Utilities.isNotNullish(remotePatId.getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(null, remotePatId);
        }

        return create201302PatientPerson(patName, gender, birthTime, convertedOtherIds);
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(List<PRPAMT201310UV02OtherIDs> otherIds, PNExplicit patName, CE gender, TSExplicit birthTime, II remotePatId) {
        PRPAMT201302UV02OtherIDs convertedOtherIds = null;
        if (otherIds != null &&
                otherIds.size() > 0 &&
                otherIds.get(0) != null &&
                otherIds.get(0).getId() != null &&
                otherIds.get(0).getId().size() > 0 &&
                otherIds.get(0).getId().get(0) != null &&
                Utilities.isNotNullish(otherIds.get(0).getId().get(0).getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(otherIds.get(0).getId().get(0).getExtension(), remotePatId);
        } else if (remotePatId != null &&
                Utilities.isNotNullish(remotePatId.getRoot()) &&
                Utilities.isNotNullish(remotePatId.getExtension())) {
            convertedOtherIds = createPRPAMT201302UVOtherIDs(null, remotePatId);
        }

        return create201302PatientPerson(patName, gender, birthTime, convertedOtherIds);
    }

    public static JAXBElement<PRPAMT201302UV02PatientPatientPerson> create201302PatientPerson(PNExplicit patName, CE gender, TSExplicit birthTime, PRPAMT201302UV02OtherIDs otherIds) {
        PRPAMT201302UV02PatientPatientPerson person = new PRPAMT201302UV02PatientPatientPerson();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        // Set the Birth Time
        if (birthTime != null) {
            person.setBirthTime(birthTime);
        }

        // Set the SSN
        if (otherIds != null) {
            person.getAsOtherIDs().add(otherIds);
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201302UV02PatientPatientPerson> result = new JAXBElement<PRPAMT201302UV02PatientPatientPerson>(xmlqname, PRPAMT201302UV02PatientPatientPerson.class, person);

        return result;
    }

    public static PRPAMT201301UV02OtherIDs createPRPAMT201301UVOtherIDs(String ssn) {
        PRPAMT201301UV02OtherIDs otherIds = new PRPAMT201301UV02OtherIDs();

        otherIds.getClassCode().add("PAT");

        // Set the SSN
        if (Utilities.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            otherIds.getId().add(HL7DataTransformHelper.IIFactory(HL7Constants.SSN_ID_ROOT, ssn));
        }

        return otherIds;
    }

    public static PRPAMT201310UV02OtherIDs createPRPAMT201310UVOtherIDs(String ssn) {
        PRPAMT201310UV02OtherIDs otherIds = new PRPAMT201310UV02OtherIDs();

        otherIds.getClassCode().add("PAT");

        // Set the SSN
        if (Utilities.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            otherIds.getId().add(HL7DataTransformHelper.IIFactory(HL7Constants.SSN_ID_ROOT, ssn));
        }

        return otherIds;
    }

    public static PRPAMT201302UV02OtherIDs createPRPAMT201302UVOtherIDs(String ssn, II remotePatId) {
        PRPAMT201302UV02OtherIDs otherIds = new PRPAMT201302UV02OtherIDs();

        otherIds.getClassCode().add("PAT");

        // Set the SSN
        if (Utilities.isNotNullish(ssn)) {
            log.info("Setting Patient SSN: " + ssn);
            PRPAMT201302UV02OtherIDsId ssnId = new PRPAMT201302UV02OtherIDsId();
            ssnId.setExtension(ssn);
            ssnId.setRoot(HL7Constants.SSN_ID_ROOT);
            otherIds.getId().add(ssnId);
        }

        if (remotePatId != null &&
                Utilities.isNotNullish(remotePatId.getRoot()) &&
                Utilities.isNotNullish(remotePatId.getExtension())) {
            log.info("Setting Remote Patient Id: " + remotePatId.getExtension());
            log.info("Setting Remote Assigning Authority: " + remotePatId.getRoot());
            PRPAMT201302UV02OtherIDsId respondingId = new PRPAMT201302UV02OtherIDsId();
            respondingId.setExtension(remotePatId.getExtension());
            respondingId.setRoot(remotePatId.getRoot());
            otherIds.getId().add(respondingId);
        }

        return otherIds;
    }

    public static JAXBElement<PRPAMT201301UV02BirthPlace> createPRPAMT201301UVBirthPlace(PRPAMT201310UV02BirthPlace birthPlace) {
        PRPAMT201301UV02BirthPlace result = new PRPAMT201301UV02BirthPlace();

        if (birthPlace == null) {
            return null;
        }

        if (birthPlace.getBirthplace() != null) {
            result.setBirthplace(birthPlace.getBirthplace());
        }
        if (Utilities.isNotNullish(birthPlace.getClassCode())) {
            for (String code : birthPlace.getClassCode()) {
                result.getClassCode().add(code);
            }
        }
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "birthPlace");
        JAXBElement<PRPAMT201301UV02BirthPlace> element = new JAXBElement<PRPAMT201301UV02BirthPlace>(xmlqname, PRPAMT201301UV02BirthPlace.class, result);


        return element;
    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(PRPAMT201310UV02Person person) {
        PRPAMT201301UV02Person result = new PRPAMT201301UV02Person();

        if (person == null) {
            return null;
        }

        for (PNExplicit name : person.getName()) {
            result.getName().add(name);
        }

        result.setAdministrativeGenderCode(person.getAdministrativeGenderCode());

        result.setBirthTime(person.getBirthTime());


        for (ADExplicit add : person.getAddr()) {
            result.getAddr().add(add);
        }

        if (person.getBirthPlace() != null) {
            result.setBirthPlace(createPRPAMT201301UVBirthPlace(person.getBirthPlace().getValue()));
        }

        for (II id : person.getId()) {
            result.getId().add(id);
        }

        for (TELExplicit telephone : person.getTelecom()) {
            result.getTelecom().add(telephone);
        }

        if (person.getAsOtherIDs() != null) {
            for (PRPAMT201310UV02OtherIDs otherId : person.getAsOtherIDs()) {
                PRPAMT201301UV02OtherIDs newId = new PRPAMT201301UV02OtherIDs();
                if (otherId != null &&
                        Utilities.isNotNullish(otherId.getId()) &&
                        otherId.getId().get(0) != null) {
                    newId.getId().add(otherId.getId().get(0));
                    result.getAsOtherIDs().add(newId);
                }
            }
        }
        result.setDeceasedInd(person.getDeceasedInd());
        result.setDeceasedTime(person.getDeceasedTime());
        result.setDeterminerCode(person.getDeterminerCode());
        result.setDesc(person.getDesc());
        result.setEducationLevelCode(person.getEducationLevelCode());
        result.setLivingArrangementCode(person.getLivingArrangementCode());
        result.setMaritalStatusCode(person.getMaritalStatusCode());
        result.setMultipleBirthInd(person.getMultipleBirthInd());
        result.setMultipleBirthOrderNumber(person.getMultipleBirthOrderNumber());
        result.setOrganDonorInd(person.getOrganDonorInd());
        result.setReligiousAffiliationCode(person.getReligiousAffiliationCode());
        result.setTypeId(person.getTypeId());


        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> element = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, result);

        return element;

    }

    public static JAXBElement<PRPAMT201301UV02Person> create201301PatientPerson(JAXBElement<PRPAMT201302UV02PatientPatientPerson> person) {
        PRPAMT201301UV02Person result = new PRPAMT201301UV02Person();

        if (person == null) {
            return null;
        }

        for (PNExplicit name : person.getValue().getName()) {
            result.getName().add(name);
        }

        result.setAdministrativeGenderCode(person.getValue().getAdministrativeGenderCode());

        result.setBirthTime(person.getValue().getBirthTime());


        for (ADExplicit add : person.getValue().getAddr()) {
            result.getAddr().add(add);
        }

        if (person.getValue().getBirthPlace() != null) {
            result.setBirthPlace(createPRPAMT201301UVBirthPlace(person.getValue().getBirthPlace()));
        }

        for (II id : person.getValue().getId()) {
            result.getId().add(id);
        }

        for (TELExplicit telephone : person.getValue().getTelecom()) {
            result.getTelecom().add(telephone);
        }

        result.setDeceasedInd(person.getValue().getDeceasedInd());
        result.setDeceasedTime(person.getValue().getDeceasedTime());
        result.setDeterminerCode(person.getValue().getDeterminerCode());
        result.setDesc(person.getValue().getDesc());
        result.setEducationLevelCode(person.getValue().getEducationLevelCode());
        result.setLivingArrangementCode(person.getValue().getLivingArrangementCode());
        result.setMaritalStatusCode(person.getValue().getMaritalStatusCode());
        result.setMultipleBirthInd(person.getValue().getMultipleBirthInd());
        result.setMultipleBirthOrderNumber(person.getValue().getMultipleBirthOrderNumber());
        result.setOrganDonorInd(person.getValue().getOrganDonorInd());
        result.setReligiousAffiliationCode(person.getValue().getReligiousAffiliationCode());
        result.setTypeId(person.getValue().getTypeId());


        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> element = new JAXBElement<PRPAMT201301UV02Person>(xmlqname, PRPAMT201301UV02Person.class, result);

        return element;

    }

    public static JAXBElement<PRPAMT201301UV02BirthPlace> createPRPAMT201301UVBirthPlace(JAXBElement<PRPAMT201302UV02BirthPlace> value) {
        PRPAMT201301UV02BirthPlace result = new PRPAMT201301UV02BirthPlace();
        PRPAMT201302UV02BirthPlace birthPlace;

        if (value == null) {
            return null;
        }

        birthPlace = value.getValue();

        if (birthPlace.getBirthplace() != null) {
            result.setBirthplace(birthPlace.getBirthplace());
        }
        if (Utilities.isNotNullish(birthPlace.getClassCode())) {
            for (String code : birthPlace.getClassCode()) {
                result.getClassCode().add(code);
            }
        }
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "birthPlace");
        JAXBElement<PRPAMT201301UV02BirthPlace> element = new JAXBElement<PRPAMT201301UV02BirthPlace>(xmlqname, PRPAMT201301UV02BirthPlace.class, result);


        return element;
    }

    public static TSExplicit createBirthTime(PRPAMT201306UV02LivingSubjectBirthTime birthTime) {
        TSExplicit birthDay = null;
        IVLTSExplicit bday;

        if (birthTime != null) {
            bday = birthTime.getValue().get(0);
            birthDay = createBirthTime(bday);
        }

        return birthDay;

    }

    private static TSExplicit createBirthTime(IVLTSExplicit bday) {
        return HL7DataTransformHelper.TSExplicitFactory(bday.getValue());
    }

	public static PRPAMT201302UV02Patient create201302Patient(Patient pat, List<PatientIdentifier> ids) {
		PRPAMT201302UV02Patient patient = new PRPAMT201302UV02Patient();
		patient.getClassCode().add(HL7Constants.CLASS_CODE_PATIENT);
		PRPAMT201302UV02PatientStatusCode statusCode = new PRPAMT201302UV02PatientStatusCode();
		statusCode.setCode(HL7Constants.STATUS_CODE_ACTIVE);
		patient.setStatusCode(statusCode);
		for (PatientIdentifier id : ids) {
			PRPAMT201302UV02PatientId pid = new PRPAMT201302UV02PatientId();
			pid.setRoot(id.getAssigningAuthority().getUniversalId());
			pid.setExtension(id.getId());
			patient.getId().add(pid);
		}
		patient.setPatientPerson(createPRPAMT201302UV02PatientPerson(pat, ids));
		return patient;
	}

	private static JAXBElement<PRPAMT201302UV02PatientPatientPerson> createPRPAMT201302UV02PatientPerson(Patient pat, List<PatientIdentifier> ids) {
		PRPAMT201302UV02PatientPatientPerson person = new PRPAMT201302UV02PatientPatientPerson();
		person.getClassCode().add(HL7Constants.CLASS_CODE_PSN);
		person.setDeterminerCode(HL7Constants.DETERMINER_CODE_INSTANCE);
        PNExplicit name = null;
        if (Utilities.isNotNullish(pat.getPatientName().getFirstName()) &&
                Utilities.isNotNullish(pat.getPatientName().getLastName())) {
            name = HL7DataTransformHelper.CreatePNExplicit(pat.getPatientName().getFirstName(), pat.getPatientName().getLastName());
        }
        person.getName().add(name);
        
//        if (ids.size() > 1) {
//        	for (int i=1; i < ids.size(); i++) {
//        		PatientIdentifier id = ids.get(i);
//            	PRPAMT201302UV02OtherIDs otherId = new PRPAMT201302UV02OtherIDs();
//            	PRPAMT201302UV02OtherIDsId pid = new PRPAMT201302UV02OtherIDsId();
//            	pid.setRoot(id.getAssigningAuthority().getUniversalId());
//            	pid.setExtension(id.getId());
//            	otherId.getId().add(pid);
//            	COCTMT150002UV01Organization scopingOrg = new COCTMT150002UV01Organization();
//            	scopingOrg.setClassCode(HL7Constants.CLASS_CODE_ORG);
//            	scopingOrg.setDeterminerCode(HL7Constants.DETERMINER_CODE_INSTANCE);
//            	II orgId = HL7DataTransformHelper.IIFactory(id.getAssigningAuthority().getUniversalId());
//            	scopingOrg.getId().add(orgId);
//            	otherId.setScopingOrganization(scopingOrg);
//            	person.getAsOtherIDs().add(otherId);
//        	}
//        }
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201302UV02PatientPatientPerson> result = new JAXBElement<PRPAMT201302UV02PatientPatientPerson>(xmlqname, PRPAMT201302UV02PatientPatientPerson.class, person);
		return result;
	}
}
