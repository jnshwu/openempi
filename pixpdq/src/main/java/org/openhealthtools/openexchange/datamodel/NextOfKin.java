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
public class NextOfKin {
	
	private PersonName  nextOfKinName;
    private Calendar    birthDateTime;
    private SexType     administrativeSex;
    private String      race;
    private String      primaryLanguage;
    private String      maritalStatus;
    private String      religion;
    private String      ssn;
    private String      ethnicGroup;
    private String      citizenship;
    private String      nextOfKinRelationship;
    private String      contactRole;

    private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
    private List<Address>  addresses = new ArrayList<Address>();
    
	/**
	 * Default Constructor
	 */
	public NextOfKin() {
	}


	/**
	 * Gets nextOfKinName.
	 *
	 * @return the nextOfKinName
	 */
	public PersonName getNextOfKinName() {
		return nextOfKinName;
	}


	/**
	 * Sets nextOfKinName.
	 *
	 * @param nextOfKinName the nextOfKinName to set
	 */
	public void setNextOfKinName(PersonName name) {
		this.nextOfKinName = name;
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
		result = prime * result
				+ ((citizenship == null) ? 0 : citizenship.hashCode());
		result = prime * result
				+ ((ethnicGroup == null) ? 0 : ethnicGroup.hashCode());
		result = prime * result
				+ ((maritalStatus == null) ? 0 : maritalStatus.hashCode());
		result = prime * result + ((nextOfKinName == null) ? 0 : nextOfKinName.hashCode());
		result = prime * result + ((nextOfKinRelationship == null) ? 0 : nextOfKinRelationship.hashCode());
        result = prime * result + ((contactRole == null) ? 0 : contactRole.hashCode());

		result = prime * result
				+ ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
		result = prime * result
				+ ((primaryLanguage == null) ? 0 : primaryLanguage.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
		result = prime * result
				+ ((religion == null) ? 0 : religion.hashCode());
		result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
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
		if (!(obj instanceof NextOfKin))
			return false;
		final NextOfKin other = (NextOfKin) obj;
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
		if (citizenship == null) {
			if (other.citizenship != null)
				return false;
		} else if (!citizenship.equals(other.citizenship))
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
		if (nextOfKinName == null) {
			if (other.nextOfKinName != null)
				return false;
		} else if (!nextOfKinName.equals(other.nextOfKinName))
			return false;
		if (nextOfKinRelationship == null) {
			if (other.nextOfKinRelationship != null)
				return false;
		} else if (!nextOfKinRelationship.equals(other.nextOfKinRelationship))
			return false;
        if (contactRole == null) {
			if (other.contactRole != null)
				return false;
		} else if (!contactRole.equals(other.contactRole))
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
		return true;
	}

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}


	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

    public String getContactRole() {
        return contactRole;
    }

    public void setContactRole(String contactRole) {
        this.contactRole = contactRole;
    }

    public String getNextOfKinRelationship() {
        return nextOfKinRelationship;
    }

    public void setNextOfKinRelationship(String nextOfKinRelationship) {
        this.nextOfKinRelationship = nextOfKinRelationship;
    }


}
