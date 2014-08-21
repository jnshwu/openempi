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
 * This class represents a Driver's License.
 * 
 * @author Wenzhi Li
 * @version 1.0, Nov 13, 2008
 */
public class DriversLicense {
	
	private String licenseNumber;
	private String issuingState;
	private Calendar expirationDate;
	/**
	 * Gets licenseNumber.
	 *
	 * @return the licenseNumber
	 */
	public String getLicenseNumber() {
		return licenseNumber;
	}
	/**
	 * Sets licenseNumber.
	 *
	 * @param licenseNumber the licenseNumber to set
	 */
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	/**
	 * Gets issuingState.
	 *
	 * @return the issuingState
	 */
	public String getIssuingState() {
		return issuingState;
	}
	/**
	 * Sets issuingState.
	 *
	 * @param issuingState the issuingState to set
	 */
	public void setIssuingState(String issuingState) {
		this.issuingState = issuingState;
	}
	/**
	 * Gets expirationDate.
	 *
	 * @return the expirationDate
	 */
	public Calendar getExpirationDate() {
		return expirationDate;
	}
	/**
	 * Sets expirationDate.
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
		result = prime * result
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result
				+ ((issuingState == null) ? 0 : issuingState.hashCode());
		result = prime * result
				+ ((licenseNumber == null) ? 0 : licenseNumber.hashCode());
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
		if (!(obj instanceof DriversLicense))
			return false;
		final DriversLicense other = (DriversLicense) obj;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (issuingState == null) {
			if (other.issuingState != null)
				return false;
		} else if (!issuingState.equals(other.issuingState))
			return false;
		if (licenseNumber == null) {
			if (other.licenseNumber != null)
				return false;
		} else if (!licenseNumber.equals(other.licenseNumber))
			return false;
		return true;
	}
	
	
	
}
