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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * IdentifierDomain entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "identifier_domain")
@GenericGenerator(name = "identifier_domain_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "identifier_domain_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class IdentifierDomain implements java.io.Serializable
{
	private static final long serialVersionUID = 2611151383140968220L;

	private Integer identifierDomainId;
	private User userCreatedBy;
	private String identifierDomainName;
	private String identifierDomainDescription;
	private String universalIdentifier;
	private String universalIdentifierTypeCode;
	private String namespaceIdentifier;
	private Date dateCreated;
	
	/** default constructor */
	public IdentifierDomain() {
	}

	/** minimal constructor */
	public IdentifierDomain(Integer identifierDomainId, User user, Date dateCreated) {
		this.identifierDomainId = identifierDomainId;
		this.userCreatedBy = user;
		this.dateCreated = dateCreated;
	}

	/** full constructor */
	public IdentifierDomain(Integer identifierDomainId, User user, String universalIdentifier,
			String universalIdentifierTypeCode, String namespaceIdentifier, Date dateCreated) {
		this.identifierDomainId = identifierDomainId;
		this.userCreatedBy = user;
		this.universalIdentifier = universalIdentifier;
		this.universalIdentifierTypeCode = universalIdentifierTypeCode;
		this.namespaceIdentifier = namespaceIdentifier;
		this.dateCreated = dateCreated;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator="identifier_domain_gen")
	@Column(name = "identifier_domain_id", unique = true, nullable = false)
	@XmlElement
	public Integer getIdentifierDomainId() {
		return this.identifierDomainId;
	}

	public void setIdentifierDomainId(Integer identifierDomainId) {
		this.identifierDomainId = identifierDomainId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id", nullable = false)
	public User getUserCreatedBy() {
		return this.userCreatedBy;
	}

	public void setUserCreatedBy(User user) {
		this.userCreatedBy = user;
	}

	@Column(name = "universal_identifier")
	@XmlElement
	public String getUniversalIdentifier() {
		return this.universalIdentifier;
	}

	public void setUniversalIdentifier(String universalIdentifier) {
		this.universalIdentifier = universalIdentifier;
	}

	@Column(name = "universal_identifier_type_code")
	@XmlElement
	public String getUniversalIdentifierTypeCode() {
		return this.universalIdentifierTypeCode;
	}

	public void setUniversalIdentifierTypeCode(String universalIdentifierTypeCode) {
		this.universalIdentifierTypeCode = universalIdentifierTypeCode;
	}

	@Column(name = "namespace_identifier")
	@XmlElement
	public String getNamespaceIdentifier() {
		return this.namespaceIdentifier;
	}

	public void setNamespaceIdentifier(String namespaceIdentifier) {
		this.namespaceIdentifier = namespaceIdentifier;
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

	@Column(name = "identifier_domain_name")
	@XmlElement
	public String getIdentifierDomainName() {
		return this.identifierDomainName;
	}

	public void setIdentifierDomainName(String identifierDomainName) {
		this.identifierDomainName = identifierDomainName;
	}

	@Column(name = "identifier_domain_description")
	@XmlElement
	public String getIdentifierDomainDescription() {
		return this.identifierDomainDescription;
	}

	public void setIdentifierDomainDescription(String identifierDomainDescription) {
		this.identifierDomainDescription = identifierDomainDescription;
	}

	@Override
	public boolean equals(final Object other) {
		if (this==other) {
			return true;
		}
		if (identifierDomainId == null) {
			return false;
		}
		if (!(other instanceof IdentifierDomain)) {
			return false;
		}
		IdentifierDomain castOther = (IdentifierDomain) other;
		if (this.identifierDomainId == castOther.getIdentifierDomainId()) {
			return true;
		}
		if (this.getNamespaceIdentifier() != null && castOther.getNamespaceIdentifier() != null) {
			return getNamespaceIdentifier().equals(castOther.getNamespaceIdentifier());
		}
		if (this.getUniversalIdentifier() != null && castOther.getUniversalIdentifier() != null &&
				this.getUniversalIdentifierTypeCode() != null && castOther.getUniversalIdentifierTypeCode() != null) {
			return new EqualsBuilder().append(universalIdentifier, castOther.universalIdentifier).append(
							universalIdentifierTypeCode, castOther.universalIdentifierTypeCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (identifierDomainId != null) {
			return new HashCodeBuilder().append(identifierDomainId).toHashCode(); 
		}
		if (this.getNamespaceIdentifier() != null ) {
			return new HashCodeBuilder().append(namespaceIdentifier).toHashCode();
		}
		if (this.getUniversalIdentifier() != null &&
				this.getUniversalIdentifierTypeCode() != null) {
			return new HashCodeBuilder().append(universalIdentifier)
			.append(universalIdentifierTypeCode).toHashCode();
		}
		return System.identityHashCode(this);
	}

	@Override
	public String toString() {
		return  "IdentifierDomain [identifierDomainId=" + identifierDomainId 
							  + ", identifierDomainName=" + identifierDomainName
							  + ", identifierDomainDescription=" + identifierDomainDescription
							  + ", universalIdentifier=" + universalIdentifier
							  + ", universalIdentifierTypeCode=" + universalIdentifierTypeCode
							  + ", namespaceIdentifier=" + namespaceIdentifier
							  + "]";
	}

	public Integer hydrate() {
		return getIdentifierDomainId();
	}

}