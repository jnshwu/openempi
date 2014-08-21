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

/**
 * The data class representing a person's name.
 * 
 * @author Wenzhi Li
 * @version 1.0  Nov 13, 2008
 *
 */
public class PersonName {
	private String lastName;
	private String firstName;
	private String secondName;
	private String suffix;
	private String prefix;
	private String degree;
	private String nameTypeCode;
	private String nameRepresentationCode;

	
	/**
	 * Gets lastName.
	 *
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Sets lastName.
	 *
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Gets firstName.
	 *
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Sets firstName.
	 *
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * Gets secondName.
	 *
	 * @return the secondName
	 */
	public String getSecondName() {
		return secondName;
	}
	/**
	 * Sets secondName.
	 *
	 * @param secondName the secondName to set
	 */
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	/**
	 * Gets suffix.
	 *
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}
	/**
	 * Sets suffix.
	 *
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	/**
	 * Gets prefix.
	 *
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * Sets prefix.
	 *
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * Gets degree.
	 *
	 * @return the degree
	 */
	public String getDegree() {
		return degree;
	}
	/**
	 * Sets degree.
	 *
	 * @param degree the degree to set
	 */
	public void setDegree(String degree) {
		this.degree = degree;
	}
	/**
	 * Gets nameTypeCode.
	 *
	 * @return the nameTypeCode
	 */
	public String getNameTypeCode() {
		return nameTypeCode;
	}
	/**
	 * Sets nameTypeCode.
	 *
	 * @param nameTypeCode the nameTypeCode to set
	 */
	public void setNameTypeCode(String nameTypeCode) {
		this.nameTypeCode = nameTypeCode;
	}
	/**
	 * Gets nameRepresentationCode.
	 *
	 * @return the nameRepresentationCode
	 */
	public String getNameRepresentationCode() {
		return nameRepresentationCode;
	}
	/**
	 * Sets nameRepresentationCode.
	 *
	 * @param nameRepresentationCode the nameRepresentationCode to set
	 */
	public void setNameRepresentationCode(String nameRepresentationCode) {
		this.nameRepresentationCode = nameRepresentationCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((degree == null) ? 0 : degree.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime
				* result
				+ ((nameRepresentationCode == null) ? 0
						: nameRepresentationCode.hashCode());
		result = prime * result
				+ ((nameTypeCode == null) ? 0 : nameTypeCode.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result
				+ ((secondName == null) ? 0 : secondName.hashCode());
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
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
		if (!(obj instanceof PersonName))
			return false;
		final PersonName other = (PersonName) obj;
		if (degree == null) {
			if (other.degree != null)
				return false;
		} else if (!degree.equals(other.degree))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (nameRepresentationCode == null) {
			if (other.nameRepresentationCode != null)
				return false;
		} else if (!nameRepresentationCode.equals(other.nameRepresentationCode))
			return false;
		if (nameTypeCode == null) {
			if (other.nameTypeCode != null)
				return false;
		} else if (!nameTypeCode.equals(other.nameTypeCode))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (secondName == null) {
			if (other.secondName != null)
				return false;
		} else if (!secondName.equals(other.secondName))
			return false;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		String ret = null;
		if (firstName != null) {
			ret = firstName;
			if (lastName != null)
				ret = firstName +" " +lastName;
		}
		else
			ret = lastName;
		
		return ret;
	}

	
}
