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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserWeb extends BaseModelData
{ 	
	
	@SuppressWarnings("unused")
    private RoleWeb unusedRoleWeb;
	
	public UserWeb() {
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.String getUsername() {
		return get("username");
	}

	public void setUsername(java.lang.String name) {
		set("username", name);
	}

	public java.lang.String getPassword() {
		return get("password");
	}

	public void setPassword(java.lang.String setPassword) {
		set("password", setPassword);
	}
	
	public java.lang.String getConfirmPassword() {
		return get("confirmPassword");
	}

	public void setConfirmPassword(java.lang.String confirmPassword) {
		set("confirmPassword", confirmPassword);
	}
	
	public java.lang.String getPasswordHint() {
		return get("passwordHint");
	}

	public void setPasswordHint(java.lang.String passwordHint) {
		set("passwordHint", passwordHint);
	}

	public java.lang.String getFirstName() {
		return get("firstName");
	}

	public void setFirstName(java.lang.String firstName) {
		set("firstName", firstName);
	}
	
	public java.lang.String getLastName() {
		return get("lastName");
	}

	public void setLastName(java.lang.String lastName) {
		set("lastName", lastName);
	}
	
	public java.lang.String getEmail() {
		return get("email");
	}

	public void setEmail(java.lang.String email) {
		set("email", email);
	}
	
	public java.lang.String getPhoneNumber() {
		return get("phoneNumber");
	}

	public void setPhoneNumber(java.lang.String phoneNumber) {
		set("phoneNumber", phoneNumber);
	}	
	
	public java.lang.String getWebsite() {
		return get("website");
	}

	public void setWebsite(java.lang.String website) {
		set("website", website);
	}	
	
	public java.lang.String getAddress() {
		return get("address");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}	

	public java.lang.String getCity() {
		return get("city");
	}

	public void setCity(java.lang.String city) {
		set("city", city);
	}

	public java.lang.String getState() {
		return get("state");
	}

	public void setState(java.lang.String state) {
		set("state", state);
	}
	
	public java.lang.String getPostalCode() {
		return get("postalCode");
	}

	public void setPostalCode(java.lang.String postalCode) {
		set("postalCode", postalCode);
	}
	
	public java.lang.String getCountry() {
		return get("country");
	}

	public void setCountry(java.lang.String country) {
		set("country", country);
	}
	
	public java.lang.String getVersion() {
		return get("version");
	}

	public void setVersion(java.lang.String version) {
		set("version", version);
	}	
	
	public java.lang.Boolean getEnabled() {
		return get("enabled");
	}

	public void setEnabled(java.lang.Boolean enabled) {
		set("enabled", enabled);
	}	
	
	public java.lang.Boolean getAccountExpired() {
		return get("accountExpired");
	}

	public void setAccountExpired(java.lang.Boolean accountExpired) {
		set("accountExpired", accountExpired);
	}	
	
	public java.lang.Boolean getAccountLocked() {
		return get("accountLocked");
	}

	public void setAccountLocked(java.lang.Boolean accountLocked) {
		set("accountLocked", accountLocked);
	}	
	
	public java.lang.Boolean getCredentialsExpired() {
		return get("credentialsExpired");
	}

	public void setCredentialsExpired(java.lang.Boolean credentialsExpired) {
		set("credentialsExpired", credentialsExpired);
	}	
	
	public java.util.Set<RoleWeb> getRoles() {
		return get("roles");
	}

	public void setRoles(java.util.Set<RoleWeb> roles) {
		set("roles", roles);
	}

	public String getSessionKey() {
		return get("sessionKey");
	}
	
	public void setSessionKey(String sessionKey) {
		set("sessionKey", sessionKey);
	}
}
