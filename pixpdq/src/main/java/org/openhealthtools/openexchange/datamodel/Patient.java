/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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

package org.openhealthtools.openexchange.datamodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openhealthtools.openexchange.datamodel.SharedEnums.PhoneType;
import org.openhealthtools.openexchange.datamodel.SharedEnums.SexType;


/**
 * This class represents a patient throughout the PIX/PDQ 
 * sever.  It is used for passing patient information between
 * PIX/PDQ clients actors (such as PIX Source, PIX Consumer and 
 * PDQ Consumer) and underneath eMPI system.
 * 
 * @author Wenzhi Li
 * @version 1.0  Nov 11, 2008
 *
 */
public class Patient {
	
	/* a list of patient identifiers of this patient*/
	private List<PatientIdentifier> patientIds = new ArrayList<PatientIdentifier>();

	private PersonName  patientName; 
	private PersonName  mothersMaidenName;
    private Calendar    birthDateTime;
    private SexType     administrativeSex;
    private String      race;
    private String      primaryLanguage;
    private String      maritalStatus;
    private String      religion;
    private PatientIdentifier patientAccountNumber;
    private String      ssn;
    private DriversLicense driversLicense;
    private PersonIdentifier mothersId;
    private String      ethnicGroup;
    private String      birthPlace;
    private int         birthOrder;
    private String      citizenship;
    private Calendar    deathDate;
    private String     deathIndicator;

    private String		vipIndicator;

    private List<PersonName>  patientAliases = new ArrayList<PersonName>();
    private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
    private List<Address>  addresses = new ArrayList<Address>();
    private List<Visit>    visits = new ArrayList<Visit>();
    private List<NextOfKin>    nextOfKin = new ArrayList<NextOfKin>();

	/**
	 * Default Constructor
	 */
	public Patient() {
	}


	/**
	 * Gets a list of <code>PatientIdentifier</code>.
	 * 
	 * @return the <code>PatientIdentifier</code> list
	 */
	public List<PatientIdentifier> getPatientIds() {
		return patientIds;
	}


	/**
	 * Adds a <code>PatientIdentifier</code> to this patient.
	 *  
	 * @param id the <code>PatientIdentifier</code> to add
	 */
	public void addPatientId(PatientIdentifier id) {
		this.patientIds.add(id);
	}

	/**
	 * Removes a <code>PatientIdentifier</code> from this patient.
	 * 
	 * @param id the <code>PatientIdentifier</code> to be removed
	 */
	public void removePatientId(PatientIdentifier id) {
		this.patientIds.remove(id);
	}

	/**
	 * Removes all the <code>PatientIdentifier</code> from this patient.
	 * 
	 */
	public void clearPatientId() {
		this.patientIds.clear();
	}
    
 

	/**
	 * Gets patientName.
	 *
	 * @return the patientName
	 */
	public PersonName getPatientName() {
		return patientName;
	}


	/**
	 * Sets patientName.
	 *
	 * @param patientName the patientName to set
	 */
	public void setPatientName(PersonName name) {
		this.patientName = name;
	}


	/**
	 * Gets mothersMaidenName.
	 *
	 * @return the mothersMaidenName
	 */
	public PersonName getMothersMaidenName() {
		return mothersMaidenName;
	}


	/**
	 * Sets mothersMaidenName.
	 *
	 * @param mothersMaidenName the mothersMaidenName to set
	 */
	public void setMothersMaidenName(PersonName mothersMaidenName) {
		this.mothersMaidenName = mothersMaidenName;
	}


	/**
	 * Gets birthDateTime.
	 *
	 * @return the birthDateTime
	 */
	public Calendar getBirthDateTime() {
		return birthDateTime;
	}


	/**
	 * Sets birthDateTime.
	 *
	 * @param birthDateTime the birthDateTime to set
	 */
	public void setBirthDateTime(Calendar birthDateTime) {
		this.birthDateTime = birthDateTime;
	}


	/**
	 * Gets administrativeSex.
	 *
	 * @return the administrativeSex
	 */
	public SexType getAdministrativeSex() {
		return administrativeSex;
	}


	/**
	 * Sets administrativeSex.
	 *
	 * @param administrativeSex the administrativeSex to set
	 */
	public void setAdministrativeSex(SexType administrativeSex) {
		this.administrativeSex = administrativeSex;
	}

	/**
	 * Gets a <code>PersonName</code> list.
	 *
	 * @return the list of aliases
	 */
	public List<PersonName> getPatientAliases() {
		return patientAliases;
	}

	public void setPatientAliases(List<PersonName> patientAliases) {
		this.patientAliases = patientAliases;
	}

	/**
	 * Adds an <code>PersonName</code> to this patient.
	 *
	 * @param alias the alias to add
	 */
	public void addPatientAliases(PersonName alias) {
		this.patientAliases.add(alias);
	}

	/**
	 * Removes an <code>PersonName</code>.
	 *
	 * @param alias the <code>PersonName</code> to be removed
  	 * @return boolean true if removed, false otherwise
	 */
	public boolean removePatientAliases(PersonName alias) {
		return patientAliases.remove(alias);
	}

 	/**
	 * Clears the <code>PersonName</code> list.
	 */
	public void clearPatientAliases() {
		patientAliases.clear();
	}

	/**
	 * Gets race.
	 *
	 * @return the race
	 */
	public String getRace() {
		return race;
	}

	/**
	 * Sets race.
	 *
	 * @param race the race to set
	 */
	public void setRace(String race) {
		this.race = race;
	}

	/**
	 * Gets primaryLanguage.
	 *
	 * @return the primaryLanguage
	 */
	public String getPrimaryLanguage() {
		return primaryLanguage;
	}


	/**
	 * Sets primaryLanguage.
	 *
	 * @param primaryLanguage the primaryLanguage to set
	 */
	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}


	/**
	 * Gets maritalStatus.
	 *
	 * @return the maritalStatus
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}


	/**
	 * Sets maritalStatus.
	 *
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}


	/**
	 * Gets religion.
	 *
	 * @return the religion
	 */
	public String getReligion() {
		return religion;
	}


	/**
	 * Sets religion.
	 *
	 * @param religion the religion to set
	 */
	public void setReligion(String religion) {
		this.religion = religion;
	}


	/**
	 * Gets patientAccountNumber.
	 *
	 * @return the patientAccountNumber
	 */
	public PatientIdentifier getPatientAccountNumber() {
		return patientAccountNumber;
	}


	/**
	 * Sets patientAccountNumber.
	 *
	 * @param patientAccountNumber the patientAccountNumber to set
	 */
	public void setPatientAccountNumber(PatientIdentifier patientAccountNumber) {
		this.patientAccountNumber = patientAccountNumber;
	}


	/**
	 * Gets ssn.
	 *
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}


	/**
	 * Sets ssn.
	 *
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}


	/**
	 * Gets driversLicense.
	 *
	 * @return the driversLicense
	 */
	public DriversLicense getDriversLicense() {
		return driversLicense;
	}


	/**
	 * Sets driversLicense.
	 *
	 * @param driversLicense the driversLicense to set
	 */
	public void setDriversLicense(DriversLicense driversLicense) {
		this.driversLicense = driversLicense;
	}


	/**
	 * Gets mothersId.
	 *
	 * @return the mothersId
	 */
	public PersonIdentifier getMothersId() {
		return mothersId;
	}


	/**
	 * Sets mothersId.
	 *
	 * @param mothersId the mothersId to set
	 */
	public void setMothersId(PersonIdentifier mothersId) {
		this.mothersId = mothersId;
	}


	/**
	 * Gets ethnicGroup.
	 *
	 * @return the ethnicGroup
	 */
	public String getEthnicGroup() {
		return ethnicGroup;
	}


	/**
	 * Sets ethnicGroup.
	 *
	 * @param ethnicGroup the ethnicGroup to set
	 */
	public void setEthnicGroup(String ethnicGroup) {
		this.ethnicGroup = ethnicGroup;
	}


	/**
	 * Gets birthPlace.
	 *
	 * @return the birthPlace
	 */
	public String getBirthPlace() {
		return birthPlace;
	}


	/**
	 * Sets birthPlace.
	 *
	 * @param birthPlace the birthPlace to set
	 */
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}


	/**
	 * Gets birthOrder.
	 *
	 * @return the birthOrder
	 */
	public int getBirthOrder() {
		return birthOrder;
	}


	/**
	 * Sets birthOrder.
	 *
	 * @param birthOrder the birthOrder to set
	 */
	public void setBirthOrder(int birthOrder) {
		this.birthOrder = birthOrder;
	}


	/**
	 * Gets citizenship.
	 *
	 * @return the citizenship
	 */
	public String getCitizenship() {
		return citizenship;
	}


	/**
	 * Sets citizenship.
	 *
	 * @param citizenship the citizenship to set
	 */
	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}


	/**
	 * Gets deathDate.
	 *
	 * @return the deathDate
	 */
	public Calendar getDeathDate() {
		return deathDate;
	}


	/**
	 * Sets deathDate.
	 *
	 * @param deathDate the deathDate to set
	 */
	public void setDeathDate(Calendar deathDate) {
		this.deathDate = deathDate;
	}


	/**
	 * Gets deathIndicator.
	 *
	 * @return the deathIndicator
	 */
	public String getDeathIndicator() {
		return deathIndicator;
	}


	/**
	 * Sets deathIndicator.
	 *
	 * @param deathIndicator the deathIndicator to set
	 */
	public void setDeathIndicator(String deathIndicator) {
		this.deathIndicator = deathIndicator;
	}

	

	/**
	 * Gets vipIndicator
	 * 
	 * @return the vipIndicator
	 */
	public String getVipIndicator() {
		return vipIndicator;
	}


	/**
	 * Sets vipIndicator
	 * 
	 * @param vipIndicator the vipIndicator to set
	 */
	public void setVipIndicator(String vipIndicator) {
		this.vipIndicator = vipIndicator;
	}

	/**
	 * Gets a list of <code>PhoneNumber</code>s of this patient.
	 * 
	 * @return a list of PhoneNumbers
	 */
	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}


	/**
	 * Adds a <code>PhoneNumber</code> to this patient.
	 * 
	 * @param phoneNumber the phoneNumber to add
	 */
	public void addPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumbers.add(phoneNumber);
	}

	/**
	 * Removes a <code>PhoneNumber</code>.
	 * 
	 * @param phoneNumber the <code>PhoneNumber</code> to be removed
  	 * @return boolean true if removed, false otherwise
	 */
	public boolean removePhoneNumber(PhoneNumber phoneNumber) {
		return phoneNumbers.remove(phoneNumber);
	}

 	/**
	 * Clears the <code>PhoneNumber</code> list.
	 */
	public void clearPhoneNumbers() {
		phoneNumbers.clear();
	}
	
	/**
	 * Get a particular phone number from the list for this
	 * patient.
	 * 
	 * @param type The type of phone number to get
	 * @return The phone number
	 */
	public PhoneNumber getPhoneNumber(PhoneType type) {
		if (phoneNumbers == null) return null;
		// Remove the old entry for this type of address
		for (int i=0; i<phoneNumbers.size(); i++) {
			PhoneNumber next = phoneNumbers.get(i);
			PhoneType nextType = next.getType();
			if (nextType == null) {
				if (type == null) return next;
			} else if ((type != null) && nextType.equals(type)) {
				return next;
			}
		}
		// Couldn't find a match
		return null;
	}

	/**
	 * Gets a <code>Address</code> list.
	 * 
	 * @return the list of addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}


	/**
	 * Adds an <code>Address</code> to this patient.
	 * 
	 * @param address the address to add
	 */
	public void addAddress(Address address) {
		this.addresses.add(address);
	}

	/**
	 * Removes an <code>Address</code>.
	 * 
	 * @param address the <code>Address</code> to be removed
  	 * @return boolean true if removed, false otherwise
	 */
	public boolean removeAddress(Address address) {
		return addresses.remove(address);
	}

 	/**
	 * Clears the <code>Address</code> list.
	 */
	public void clearAddresses() {
		addresses.clear();
	}

	/**
	 * Gets a <code>Visit</code> list.
	 * 
	 * @return the list of visits
	 */
	public List<Visit> getVisits() {
		return visits;
	}


	/**
	 * Adds a <code>Visit</code> to this patient.
	 * 
	 * @param visit the <code>Visit</code> to add
	 */
	public void addVisit(Visit visit) {
		this.visits.add(visit);
	}

	/**
	 * Removes a <code>Visit</code>.
	 * 
	 * @param visit the <code>Visit</code> to be removed
  	 * @return boolean true if removed, false otherwise
	 */
	public boolean removeVisit(Visit visit) {
		return visits.remove(visit);
	}

 	/**
	 * Clears the <code>Visit</code> list.
	 */
	public void clearVisits() {
		visits.clear();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addresses == null) ? 0 : addresses.hashCode());
		result = prime
				* result
				+ ((administrativeSex == null) ? 0 : administrativeSex
						.hashCode());
		result = prime * result
				+ ((birthDateTime == null) ? 0 : birthDateTime.hashCode());
		result = prime * result + birthOrder;
		result = prime * result
				+ ((birthPlace == null) ? 0 : birthPlace.hashCode());
		result = prime * result
				+ ((citizenship == null) ? 0 : citizenship.hashCode());
		result = prime * result
				+ ((deathDate == null) ? 0 : deathDate.hashCode());
		result = prime * result
				+ ((deathIndicator == null) ? 0 : deathIndicator.hashCode());
		result = prime * result + ((vipIndicator == null) ? 0 : vipIndicator.hashCode());				
		result = prime * result
				+ ((driversLicense == null) ? 0 : driversLicense.hashCode());
		result = prime * result
				+ ((ethnicGroup == null) ? 0 : ethnicGroup.hashCode());
		result = prime * result
				+ ((maritalStatus == null) ? 0 : maritalStatus.hashCode());
		result = prime * result
				+ ((mothersId == null) ? 0 : mothersId.hashCode());
		result = prime
				* result
				+ ((mothersMaidenName == null) ? 0 : mothersMaidenName
						.hashCode());
		result = prime * result + ((patientName == null) ? 0 : patientName.hashCode());
		result = prime
				* result
				+ ((patientAccountNumber == null) ? 0 : patientAccountNumber
						.hashCode());
		result = prime * result
				+ ((patientAliases == null) ? 0 : patientAliases.hashCode());
		result = prime * result
				+ ((patientIds == null) ? 0 : patientIds.hashCode());
		result = prime * result
				+ ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
		result = prime * result
				+ ((primaryLanguage == null) ? 0 : primaryLanguage.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
		result = prime * result
				+ ((religion == null) ? 0 : religion.hashCode());
		result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
		result = prime * result + ((visits == null) ? 0 : visits.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Patient))
			return false;
		final Patient other = (Patient) obj;
		if (addresses == null) {
			if (other.addresses != null)
				return false;
		} else if (!addresses.equals(other.addresses))
			return false;
		if (administrativeSex == null) {
			if (other.administrativeSex != null)
				return false;
		} else if (!administrativeSex.equals(other.administrativeSex))
			return false;
		if (birthDateTime == null) {
			if (other.birthDateTime != null)
				return false;
		} else if (!birthDateTime.equals(other.birthDateTime))
			return false;
		if (birthOrder != other.birthOrder)
			return false;
		if (birthPlace == null) {
			if (other.birthPlace != null)
				return false;
		} else if (!birthPlace.equals(other.birthPlace))
			return false;
		if (citizenship == null) {
			if (other.citizenship != null)
				return false;
		} else if (!citizenship.equals(other.citizenship))
			return false;
		if (deathDate == null) {
			if (other.deathDate != null)
				return false;
		} else if (!deathDate.equals(other.deathDate))
			return false;
		if (deathIndicator != other.deathIndicator)
			return false;
		if (driversLicense == null) {
			if (other.driversLicense != null)
				return false;
		} else if (!driversLicense.equals(other.driversLicense))
			return false;
		if (ethnicGroup == null) {
			if (other.ethnicGroup != null)
				return false;
		} else if (!ethnicGroup.equals(other.ethnicGroup))
			return false;
		if (maritalStatus == null) {
			if (other.maritalStatus != null)
				return false;
		} else if (!maritalStatus.equals(other.maritalStatus))
			return false;
		if (mothersId == null) {
			if (other.mothersId != null)
				return false;
		} else if (!mothersId.equals(other.mothersId))
			return false;
		if (mothersMaidenName == null) {
			if (other.mothersMaidenName != null)
				return false;
		} else if (!mothersMaidenName.equals(other.mothersMaidenName))
			return false;
		if (patientName == null) {
			if (other.patientName != null)
				return false;
		} else if (!patientName.equals(other.patientName))
			return false;
		if (patientAccountNumber == null) {
			if (other.patientAccountNumber != null)
				return false;
		} else if (!patientAccountNumber.equals(other.patientAccountNumber))
			return false;
		if (patientAliases == null) {
			if (other.patientAliases != null)
				return false;
		} else if (!patientAliases.equals(other.patientAliases))
			return false;
		if (patientIds == null) {
			if (other.patientIds != null)
				return false;
		} else if (!patientIds.equals(other.patientIds))
			return false;
		if (phoneNumbers == null) {
			if (other.phoneNumbers != null)
				return false;
		} else if (!phoneNumbers.equals(other.phoneNumbers))
			return false;
		if (primaryLanguage == null) {
			if (other.primaryLanguage != null)
				return false;
		} else if (!primaryLanguage.equals(other.primaryLanguage))
			return false;
		if (race == null) {
			if (other.race != null)
				return false;
		} else if (!race.equals(other.race))
			return false;
		if (religion == null) {
			if (other.religion != null)
				return false;
		} else if (!religion.equals(other.religion))
			return false;
		if (ssn == null) {
			if (other.ssn != null)
				return false;
		} else if (!ssn.equals(other.ssn))
			return false;
		if (vipIndicator == null) {
			if (other.vipIndicator != null)
				return false;
		} else if (!vipIndicator.equals(other.vipIndicator))
			return false;		
		if (visits == null) {
			if (other.visits != null)
				return false;
		} else if (!visits.equals(other.visits))
			return false;
		
		return true;
	}
	public void setPatientIds(List<PatientIdentifier> patientIds) {
		this.patientIds = patientIds;
	}


	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}


	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}


	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

    public List<NextOfKin> getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(List<NextOfKin> nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    
}
