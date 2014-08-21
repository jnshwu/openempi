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

import org.openhie.openempi.model.Person;

@XmlRootElement
public class PersonPagedRequest
{
	private Person person;
	private int firstResult;
	private int maxResults;

	public PersonPagedRequest() {
	}

	public PersonPagedRequest(Person person, int firstResult, int maxResults) {
		this.person = person;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	@Override
	public String toString() {
		return "PersonPagedRequest [person=" + person + ", firstResult="
				+ firstResult + ", maxResults=" + maxResults + "]";
	}
}
