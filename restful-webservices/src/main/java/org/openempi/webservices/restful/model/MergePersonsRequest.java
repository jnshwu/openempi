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

import org.openhie.openempi.model.PersonIdentifier;

@XmlRootElement
public class MergePersonsRequest
{
	private PersonIdentifier retiredIdentifier;
	private PersonIdentifier survivingIdentifer;
	
	public MergePersonsRequest() {
	}

	public MergePersonsRequest(PersonIdentifier retiredIdentifier, PersonIdentifier survivingIdentifer) {
		this.retiredIdentifier = retiredIdentifier;
		this.survivingIdentifer = survivingIdentifer;
	}
	
	public PersonIdentifier getRetiredIdentifier() {
		return retiredIdentifier;
	}
	public void setRetiredIdentifier(PersonIdentifier retiredIdentifier) {
		this.retiredIdentifier = retiredIdentifier;
	}

	public PersonIdentifier getSurvivingIdentifer() {
		return survivingIdentifer;
	}
	public void setSurvivingIdentifer(PersonIdentifier survivingIdentifer) {
		this.survivingIdentifer = survivingIdentifer;
	}
	
	@Override
	public String toString() {
		return "MergePersonsRequest [retiredIdentifier="
				+ retiredIdentifier + ", survivingIdentifer=" + survivingIdentifer + "]";
	}
}
