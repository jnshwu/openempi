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
package org.openempi.webapp.client.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LinkedPersonWeb extends BaseModelData implements Serializable
{
	public LinkedPersonWeb() {
		
	}
	
	public java.lang.Integer getPersonId() {
		return get("personId");
	}

	public void setPersonId(java.lang.Integer personId) {
		set("personId", personId);
	}
	
	public java.lang.String getAddress1() {
		return get("address1");
	}

	public void setAddress1(java.lang.String address1) {
		set("address1", address1);
	}
	
	public java.lang.String getCity() {
		return get("city");
	}

	public void setCity(java.lang.String city) {
		set("city", city);
	}

	public java.util.Date getDateOfBirth() {
		return get("dateOfBirth");
	}

	public void setDateOfBirth(java.util.Date dateOfBirth) {
		set("dateOfBirth", dateOfBirth);
	}

	public java.lang.String getFamilyName() {
		return get("familyName");
	}

	public void setFamilyName(java.lang.String familyName) {
		set("familyName", familyName);
	}
	
	public java.lang.String getGivenName() {
		return get("givenName");
	}

	public void setGivenName(java.lang.String givenName) {
		set("givenName", givenName);
	}
	
	public java.lang.String getPersonIdentifier() {
		return get("personIdentifier");
	}
	
	public void setPersonIdentifier(String personIdentifier) {
		set("personIdentifier", personIdentifier);
	}
	
	public java.lang.String getPostalCode() {
		return get("postalCode");
	}

	public void setPostalCode(java.lang.String postalCode) {
		set("postalCode", postalCode);
	}
	
	public java.lang.String getState() {
		return get("state");
	}

	public void setState(java.lang.String state) {
		set("state", state);
	}
}
