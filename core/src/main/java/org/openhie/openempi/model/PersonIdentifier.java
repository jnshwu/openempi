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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * PersonIdentifier entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "person_identifier")
@GenericGenerator(name = "person_identifier_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "person_identifier_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PersonIdentifier extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;

	private Integer personIdentifierId;
	private IdentifierDomain identifierDomain;
	private User userCreatedBy;
	private User userVoidedBy;
	private Person person;
	private String identifier;
	private Date dateCreated;
	private Date dateVoided;

	/** default constructor */
	public PersonIdentifier() {
	}

	/** minimal constructor */
	public PersonIdentifier(Integer personIdentifierId, IdentifierDomain identifierDomain, User userByCreatorId,
			Person person, String identifier, Date dateCreated) {
		this.personIdentifierId = personIdentifierId;
		this.identifierDomain = identifierDomain;
		this.userCreatedBy = userByCreatorId;
		this.person = person;
		this.identifier = identifier;
		this.dateCreated = dateCreated;
	}

	/** full constructor */
	public PersonIdentifier(Integer personIdentifierId, IdentifierDomain identifierDomain, User userByCreatorId,
			User userByVoidedById, Person person, String identifier, Date dateCreated, Date dateVoided) {
		this.personIdentifierId = personIdentifierId;
		this.identifierDomain = identifierDomain;
		this.userCreatedBy = userByCreatorId;
		this.userVoidedBy = userByVoidedById;
		this.person = person;
		this.identifier = identifier;
		this.dateCreated = dateCreated;
		this.dateVoided = dateVoided;
	}

	// Property accessors
	@Id
	@GeneratedValue( generator="person_identifier_gen")
	@Column(name = "person_identifier_id", unique = true, nullable = false)
	@XmlElement
	public Integer getPersonIdentifierId() {
		return this.personIdentifierId;
	}

	public void setPersonIdentifierId(Integer personIdentifierId) {
		this.personIdentifierId = personIdentifierId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "identifier_domain_id", nullable = false)
	@XmlElement
	public IdentifierDomain getIdentifierDomain() {
		return this.identifierDomain;
	}

	public void setIdentifierDomain(IdentifierDomain identifierDomain) {
		this.identifierDomain = identifierDomain;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id", nullable = false)
	@XmlElement
	public User getUserCreatedBy() {
		return this.userCreatedBy;
	}

	public void setUserCreatedBy(User userByCreatorId) {
		this.userCreatedBy = userByCreatorId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "voided_by_id")
	@XmlElement
	public User getUserVoidedBy() {
		return this.userVoidedBy;
	}

	public void setUserVoidedBy(User userByVoidedById) {
		this.userVoidedBy = userByVoidedById;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id", nullable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "identifier", nullable = false)
	@XmlElement
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	@XmlElement
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided", length = 8)
	@XmlElement
	public Date getDateVoided() {
		return this.dateVoided;
	}

	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PersonIdentifier))
			return false;
		PersonIdentifier castOther = (PersonIdentifier) other;
		if (other == this) {
			return true;
		}
		if (personIdentifierId == castOther.personIdentifierId) {
			return true;
		}
		return new EqualsBuilder().append(identifierDomain,
				castOther.identifierDomain)
				.append(identifier, castOther.identifier)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(personIdentifierId)
			.append(identifierDomain)
			.append(identifier).toHashCode();
	}
/*
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("personIdentifierId", personIdentifierId).append("identifierDomain",
				identifierDomain).append("userByCreatorId", userCreatedBy).append("userByVoidedById",
				userVoidedBy).append("identifier", identifier).append("dateCreated",
				dateCreated).append("dateVoided", dateVoided).toString();
	}
	*/
	@Override
	public String toString() {
		if (identifierDomain != null) {
			return new ToStringBuilder(this)
				.append("personIdentifierId", personIdentifierId)
				.append("identifierDomainId", identifierDomain.getIdentifierDomainId())
				.append("identifier", identifier)
				.append("dateVoided", dateVoided).toString();
		} else {
			return new ToStringBuilder(this)
			.append("personIdentifierId", personIdentifierId)
			.append("identifierDomain", identifierDomain)
			.append("identifier", identifier)
			.append("dateVoided", dateVoided).toString();			
		}
	}

	public Integer hydrate() {
		return getPersonIdentifierId();
	}

}