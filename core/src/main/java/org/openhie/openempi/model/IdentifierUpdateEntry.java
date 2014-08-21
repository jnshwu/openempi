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
package org.openhie.openempi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * IdentifierUpdateEnty entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "identifier_update_entry")
@GenericGenerator(name = "identifier_update_entry_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "identifier_event_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class IdentifierUpdateEntry extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	
	private Integer identifierUpdateEntryId;
	private IdentifierDomain identifierDomain;
	private String identifier;

	public IdentifierUpdateEntry() {
		
	}
	
	public IdentifierUpdateEntry(IdentifierDomain identifierDomain, String identifier) {
		this.identifierDomain = identifierDomain;
		this.identifier = identifier;
	}

	@Id
	@GeneratedValue(generator="identifier_update_entry_gen")
	@Column(name = "identifier_update_entry_id", unique = true, nullable = false)
	public Integer getIdentifierUpdateEntryId() {
		return identifierUpdateEntryId;
	}

	public void setIdentifierUpdateEntryId(Integer identifierUpdateEntryId) {
		this.identifierUpdateEntryId = identifierUpdateEntryId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "identifier_domain_id", nullable = false)
	@XmlElement
	public IdentifierDomain getIdentifierDomain() {
		return identifierDomain;
	}

	public void setIdentifierDomain(IdentifierDomain identifierDomain) {
		this.identifierDomain = identifierDomain;
	}

	@Column(name = "identifier", nullable = false)
	@XmlElement
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifierUpdateEntry other = (IdentifierUpdateEntry) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (identifierDomain == null) {
			if (other.identifierDomain != null)
				return false;
		} else if (!identifierDomain.equals(other.identifierDomain))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((identifierDomain == null) ? 0 : identifierDomain.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "IdentifierUpdateEnty [identifierUpdateEntryId=" + identifierUpdateEntryId + ", identifierDomain="
				+ identifierDomain + ", identifier=" + identifier + "]";
	}
}
