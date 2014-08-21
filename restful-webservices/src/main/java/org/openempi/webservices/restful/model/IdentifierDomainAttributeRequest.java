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
package org.openempi.webservices.restful.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.openhie.openempi.model.IdentifierDomain;

@XmlRootElement
public class IdentifierDomainAttributeRequest
{
	private IdentifierDomain identifierDomain;
	private String attributeName;
	private String attributeValue;
	
	public IdentifierDomainAttributeRequest() {
	}

	public IdentifierDomainAttributeRequest(IdentifierDomain identifierDomain, String attributeName) {
		this.identifierDomain = identifierDomain;
		this.attributeName = attributeName;
	}

	public IdentifierDomainAttributeRequest(IdentifierDomain identifierDomain, String attributeName,  String attributeValue) {
		this.identifierDomain = identifierDomain;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	} 
	
	public IdentifierDomain getIdentifierDomain() {
		return identifierDomain;
	}
	public void setIdentifierDomain(IdentifierDomain identifierDomain) {
		this.identifierDomain = identifierDomain;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@Override
	public String toString() {
		return "IdentifierDomainAttributeRequest [identifierDomain="
				+ identifierDomain + ", attributeName=" + attributeName + "]";
	}
}
