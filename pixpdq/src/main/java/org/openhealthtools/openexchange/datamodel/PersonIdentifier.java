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

import java.util.Calendar;

/**
 * Data class for a patient identifier, corresponding HL7 CX Type.
 * 
 * @author Wenzhi Li
 * @version 1.0,  Nov 13, 2008
 */
public class PersonIdentifier {
	
	private String id;
	private Identifier assigningAuthority;
	private String identifierTypeCode;
	private Identifier assigningFacility;	
	private Calendar effectiveDate;
	private Calendar expirationDate;
	
	public PersonIdentifier() {		
	}
	public PersonIdentifier(String id, Identifier assigningAuthority) {
		this.id = id;
		this.assigningAuthority = assigningAuthority;
	}
	/**
	 * Gets id
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets id
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Gets assigningAuthority
	 *
	 * @return the assigningAuthority
	 */
	public Identifier getAssigningAuthority() {
		return assigningAuthority;
	}
	/**
	 * Sets assigningAuthority
	 * 
	 * @param assigningAuthority the assigningAuthority to set
	 */
	public void setAssigningAuthority(Identifier assigningAuthority) {
		this.assigningAuthority = assigningAuthority;
	}
	/**
	 * Gets identifierTypeCode
	 *
	 * @return the identifierTypeCode
	 */
	public String getIdentifierTypeCode() {
		return identifierTypeCode;
	}
	/**
	 * Sets identifierTypeCode
	 * 
	 * @param identifierTypeCode the identifierTypeCode to set
	 */
	public void setIdentifierTypeCode(String identifierTypeCode) {
		this.identifierTypeCode = identifierTypeCode;
	}
	/**
	 * Gets assigningFacility
	 *
	 * @return the assigningFacility
	 */
	public Identifier getAssigningFacility() {
		return assigningFacility;
	}
	/**
	 * Sets assigningFacility
	 * 
	 * @param assigningFacility the assigningFacility to set
	 */
	public void setAssigningFacility(Identifier assigningFacility) {
		this.assigningFacility = assigningFacility;
	}
	/**
	 * Gets effectiveDate
	 *
	 * @return the effectiveDate
	 */
	public Calendar getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * Sets effectiveDate
	 * 
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Calendar effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	/**
	 * Gets expirationDate
	 *
	 * @return the expirationDate
	 */
	public Calendar getExpirationDate() {
		return expirationDate;
	}
	/**
	 * Sets expirationDate
	 * 
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((assigningAuthority == null) ? 0 : assigningAuthority
						.hashCode());
		result = prime
				* result
				+ ((assigningFacility == null) ? 0 : assigningFacility
						.hashCode());
		result = prime * result
				+ ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((identifierTypeCode == null) ? 0 : identifierTypeCode
						.hashCode());
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
		if (!(obj instanceof PersonIdentifier))
			return false;
		final PersonIdentifier other = (PersonIdentifier) obj;
		//PIX/PDQ profiles currently only use ID and AssigningAuthority
		if (assigningAuthority == null) {
			if (other.assigningAuthority != null)
				return false;
		} else if (!assigningAuthority.equals(other.assigningAuthority))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
