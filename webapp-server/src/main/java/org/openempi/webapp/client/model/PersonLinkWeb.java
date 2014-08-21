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

import com.extjs.gxt.ui.client.data.BaseModel;

public class PersonLinkWeb extends BaseModel implements Serializable
{
	@SuppressWarnings("unused")
    private PersonWeb unusedPersonWeb;
	@SuppressWarnings("unused")
    private PersonIdentifierWeb unusedPersonIdentifierWeb;
	@SuppressWarnings("unused")
    private IdentifierDomainWeb unusedIdentifierDomainWeb;
	@SuppressWarnings("unused")
    private LinkSourceWeb unusedLinkSourceWeb;
	
	public PersonLinkWeb() {
	}

	public java.lang.Integer getPersonLinkId() {
		return get("personLinkId");
	}
	
	public void setPersonLinkId(java.lang.Integer personLinkId) {
		set("personLinkId", personLinkId);
	}
	
	public PersonWeb getLeftPerson() {
		return get("leftPerson");
	}

	public void setLeftPerson(PersonWeb leftPerson) {
		set("leftPerson", leftPerson);
	}

	public PersonWeb getRightPerson() {
		return get("rightPerson");
	}

	public void setRightPerson(PersonWeb rightPerson) {
		set("rightPerson", rightPerson);
	}
	
	public LinkSourceWeb getLinkSource() {
		return get("linkSource");
	}

	public void setLinkSource(LinkSourceWeb linkSource) {
		set("linkSource", linkSource);
	}

	public java.util.Date getDateCreated() {
		return get("dateCreated");
	}
	
	public void setDateCreated(java.util.Date dateCreated) {
		set("dateCreated", dateCreated);
	}
	
	public java.lang.Double getWeight() {
		return get("weight");
	}
	
	public void setWeight(java.lang.Double weight) {
		set("weight", weight);
	}

	public java.lang.Integer getClusterId() {
		return get("clusterId");
	}
	
	public void setClusterId(java.lang.Integer clusterId) {
		set("clusterId", clusterId);
	}
	
	public java.lang.Boolean getSelected() {
		return get("selected");
	}
	
	public void setSelected(java.lang.Boolean selected) {
		set("selected", selected);
	}
}
