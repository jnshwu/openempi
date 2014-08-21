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

package org.openhealthtools.openpixpdq.api;

import java.util.List;

import org.openhealthtools.openexchange.datamodel.MessageHeader;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;

/** 
 * This PIXManager Adapter is the bridge between IHE PIX Manager 
 * actor and underneath eMPI. OpenPIXPDQ, according to the IHE 
 * PIX specifications, invokes the methods in this Adapter, whose 
 * implementation is provided by the underneath eMPI.  
 *
 * @author Wenzhi Li
 * @version 1.0, Oct 21, 2008
 */
public interface IPixManagerAdapter extends IPixPdqAdapter {

    /**
     * Whether the given patient is a valid patient in the eMPI database.
     *
     * @param pid the {@link PatientIdentifier} to be checked
	 * @param header the <code>MessageHeader</code> from the incoming PIX client message
     * @return <code>true</code> if the patient id is valid; <code>false</code> otherwise.
     */
    public boolean isValidPatient(PatientIdentifier pid, MessageHeader header) throws PixManagerException;

    /**
     * Finds from the underneath eMPI all patient ids cross all patient 
     * domains (assigning authorities) given a patient id in a particular domain.
     * All retrieved patient ids must represent the same logic patient, 
     * though they may exist in different patient id domains.  
     *
     * @param header the <code>MessageHeader</code> of the incoming PIX Query message
     * @return A list of PatientIdentifier which does not include the request patient id.
     *         Return an empty list instead of null if no matching is found.
     * @throws PixManagerException when there is trouble cross finding all patients
     */
    public List<PatientIdentifier> findPatientIds(PatientIdentifier pid, MessageHeader header) throws PixManagerException;
    
	/**
	 * Creates a new patient in the eMPI database.  This method 
	 * sends the patient demographics contained
	 * in the <code>Patient</code> to the underneath eMPI.
	 * <p>
	 * 
	 * @param patient the demographics of the patient to be created
     * @param header the <code>MessageHeader</code> of the incoming PIX Feed message
     * @return a list of new matching {@link PatientIdentifier}s of this patient 
     *           as a result of creating this patient. OpenPIXPDQ will send a PIX 
     *           Update Notification message for this list to those PIX Consumers
     *           that have subscribed to PIX Update Notification.
     *           <p>
     *           If PIX Update Notification is not supported, or if there 
     *           is no matching (i.e, the patient is registered for the first time,
     *           no need to send PIX Update Notification Message), an empty list or null
     *           can be returned.
	 * @throws PixManagerException When there is trouble creating the patient
	 */
	public List<PatientIdentifier> createPatient(Patient patient, MessageHeader header) 
	throws PixManagerException;

	/**
	 * Updates the patient's demographics in the eMPI's database. This 
	 * method sends the updated patient demographics contained
	 * in the <code>Patient</code> to the underneath eMPI.
	 * 
	 * @param patient the new demographics of the patient to be updated
     * @param header the <code>MessageHeader</code> of the incoming PIX Update message
     * @return a list of list of updated matching {@link PatientIdentifier}s
     *           as a result of updating this patient. The outer list is used to 
     *           store different logic patients, while each inner list represents 
     *           the same logic patient with matching patient ids across patient id domains.
     *           For each inner list (matching list), OpenPIXPDQ will send a PIX Update 
     *           Notification message to those PIX Consumers that have subscribed 
     *           to PIX Update Notification. 
     *           <p>
     *           For example, if patient(A)'s address is 
     *           updated, and this results in an un-matching of originally matched 
     *           patients (A, B, C & D), two lists are created, one list representing 
     *           updated matching patients (A, E & F); the other one representing updated 
     *           un-matching patients (B, C & D). 
     *           <p>
     *           If PIX Update Notification is not supported, or if there is no update 
     *           on the patient matching list, just return an empty list or null.  
     *            
	 * @throws PixManagerException when there is trouble updating the patient
	 */
	public List<List<PatientIdentifier>> updatePatient(Patient patient, MessageHeader header) 
	throws PixManagerException;

	/**
	 * Merges two patients together because they have been found to be
	 * the same patient.  The first argument describes the surviving patient 
	 * demographics; the second argument represents the patient to be merged
	 * with the surviving patient. This method sends the surviving and merged
	 * patients to the underneath eMPI.
	 * 
	 * @param patientMain the surviving patient
	 * @param patientOld the patient to be replaced, and merged with the surviving patient
     * @param header the <code>MessageHeader</code> of the incoming PIX Merge message
     * @return a list of list of updated matching {@link PatientIdentifier}s
     *           as a result of merging patients. The outer list is used to 
     *           store different logic patients, while each inner list represents 
     *           the same logic patient with matching patient ids across patient id domains.
     *           For each inner list (matching list), OpenPIXPDQ will send a PIX Update 
     *           Notification message to those PIX Consumers that have subscribed 
     *           to PIX Update Notification. 
     *           <p>
     *           If PIX Update Notification is not supported, or if there is no update 
     *           on the patient matching list, just return an empty list or null.  
     * 
	 * @throws PixManagerException when there is trouble merging the patients
	 */
	public List<List<PatientIdentifier>> mergePatients(Patient patientMain, 
			Patient patientOld, MessageHeader header) throws PixManagerException;

}
