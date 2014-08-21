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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openhealthtools.openexchange.datamodel.Address;
import org.openhealthtools.openexchange.datamodel.DriversLicense;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openexchange.datamodel.PersonName;
import org.openhealthtools.openexchange.datamodel.PhoneNumber;
import org.openhealthtools.openexchange.datamodel.SharedEnums.SexType;

/**
 * This is query object used for Patient Demographics Query.
 * 
 * @author Wenzhi Li
 * @version 1.0, Dec 15, 2008
 */
public class PdqQuery {
	private PatientIdentifier patientIdentifier;
	private PersonName personName;
	private PersonName motherMaidenName;
	private DriversLicense driversLicense;
	private String ssn;
	private SexType sex;
	private Calendar birthDate;
    private PatientIdentifier patientAccountNumber;
	/* Fields with multiple values for patient, but only one in query */
	private PhoneNumber phone;
	private Address  address;
	
    /*for non-patient related field*/
	//confidence is not used if it is <0;
	private double confidence = -1; 
	//how many patient records to retrieve; defaults to all if<0
	private int howMany = -1;  
	//the reference pointer for subsequent query
	private String continuationPointer;  
	//the domains in which patients will be returned
	private List<Identifier> returnDomains = new ArrayList<Identifier>(); 
	
	//the query tag (the query id), used by QPD-2 in PDQ Query, 
	//and by QID-1 in Cancel Query.
	private String queryTag;
	//the query name, used by QPD-1 in PDQ Query, and by QID-2 in Cancel Query
	private String queryName;
	
    /*Wild Card search*/
    //Wild card prefix characters. Applies only to the string fields
    private String prefix;
    //Wild card suffix characters. Applies only to the string fields
    private String suffix;
    	
	/**
	 * Gets the patient name.
	 * 
	 * @return the personName
	 */
	public PersonName getPersonName() {
		return personName;
	}
	
	/**
	 * Sets the patient name.
	 * 
	 * @param personName the personName to set
	 */
	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}
	
	/**
	 * Gets the mother's maiden name.
	 * 
	 * @return the mother's maiden name
	 */
	public PersonName getMotherMaidenName() {
		return motherMaidenName;
	}
	
	/**
	 * Sets the mother's maiden name.
	 * 
	 * @param motherMaidenName the mother's maiden name to set
	 */
	public void setMotherMaidenName(PersonName motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}
	
	/**
	 * Gets the driver's license.
	 * 
	 * @return the driver's license
	 */
	public DriversLicense getDriversLicense() {
		return driversLicense;
	}
	
	/**
	 * Sets the driver's license.
	 *  
	 * @param driversLicense the driver's license to set
	 */
	public void setDriversLicense(DriversLicense driversLicense) {
		this.driversLicense = driversLicense;
	}
	
	/**
	 * Gets the social security number.
	 * 
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}
	
	/**
	 * Sets the social security number.
	 * 
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	/**
	 * Gets the sex.
	 * 
	 * @return the sex
	 */
	public SexType getSex() {
		return sex;
	}
	
	/**
	 * Sets the sex.
	 * 
	 * @param sex the sex to set
	 */
	public void setSex(SexType sex) {
		this.sex = sex;
	}
	
	/**
	 * Gets the birth date.
	 * 
	 * @return the birthDate
	 */
	public Calendar getBirthDate() {
		return birthDate;
	}
	
	/**
	 * Sets the birth date.
	 * 
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Calendar birthDate) {
		this.birthDate = birthDate;
	}
	
	/**
	 * Gets the patient account number.
	 * 
	 * @return the patient account number
	 */
	public PatientIdentifier getPatientAccountNumber() {
		return patientAccountNumber;
	}
	
	/**
	 * Sets the patient account number.
	 * 
	 * @param patientAccountNumber the patient account number to set
	 */
	public void setPatientAccountNumber(PatientIdentifier patientAccountNumber) {
		this.patientAccountNumber = patientAccountNumber;
	}
	
	/**
	 * Gets the phone number.
	 * 
	 * @return the phone number
	 */
	public PhoneNumber getPhone() {
		return phone;
	}
	
	/**
	 * Sets the phone number.
	 * 
	 * @param phone the phone to set
	 */
	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}
	
	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 * 
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	
	/**
	 * Gets the expected confidence of this search. Ignore confidence
	 * if eMPI does not support confidence search. 
	 * 
	 * @return the confidence
	 */
	public double getConfidence() {
		return confidence;
	}
	
	/**
	 * Sets the confidence. Ignore confidence
	 * if eMPI does not support confidence search. 
	 * 
	 * @param confidence the confidence to set
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	/**
	 * Gets the number of patient records to return for this 
	 * PDQ search.
	 * 
	 * @return the howMany the number patient records
	 */
	public int getHowMany() {
		return howMany;
	}
	
	/**
	 * Sets the number of patient records to return for this
	 * PDQ search.
	 * 
	 * @param howMany the number of patient records to set
	 */
	public void setHowMany(int howMany) {
		this.howMany = howMany;
	}
	
	/**
	 * Gets the continuation pointer for this PDQ query search.
	 * 
	 * @return the continuation pointer
	 */
	public String getContinuationPointer() {
		return continuationPointer;
	}
	
	/**
	 * Sets the continuation pointer for this PDQ query search.
	 * 
	 * @param continuationPointer the continuation pointer to set
	 */
	public void setContinuationPointer(String continuationPointer) {
		this.continuationPointer = continuationPointer;
	}
	 
	/**
	 * Gets the return domains.
	 * 
	 * @return the list of return domains in the form of <code>Identifier</code>.
	 */
	public List<Identifier> getReturnDomains() {
		return returnDomains;
	}

	/**
	 * Adds a list of return domains.
	 * 
	 * @param returnDomains the return domains to add
	 */
	public void addReturnDomains(List<Identifier> returnDomains) {
		this.returnDomains.addAll(returnDomains);
	}

	/**
	 * Gets the query tag of this PDQ search.
	 * 
	 * @return the query tag
	 */
	public String getQueryTag() {
		return queryTag;
	}
	
	/**
	 * Sets the query tag of this PDQ search.
	 * 
	 * @param queryTag the query tag to set
	 */
	public void setQueryTag(String queryTag) {
		this.queryTag = queryTag;
	}

	/**
	 * Gets the query name of this PDQ search.
	 * 
	 * @return the query name
	 */
	public String getQueryName() {
		return queryName;
	}
	
	/**
	 * Sets the query name of this PDQ search.
	 * 
	 * @param queryName the query name to set
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	
	/**
	 * Gets the prefix wild card search string.
	 * 
	 * @return the prefix wild card
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Sets the prefix wild card search string.
	 * 
	 * @param prefix the prefix wild card to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * Gets the suffix wild search string.
	 * 
	 * @return the suffix wild card
	 */
	public String getSuffix() {
		return suffix;
	}
	
	/**
	 * Sets the suffix wild card search string.
	 * 
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Gets the patient identifier.
	 * 
	 * @return the patient identifier
	 */
	public PatientIdentifier getPatientIdentifier() {
		return patientIdentifier;
	}
	
	/**
	 * Sets the patient identifier.
	 * 
	 * @param patientIdentifier the patient identifier to set
	 */
	public void setPatientIdentifier(PatientIdentifier patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}

	
}
