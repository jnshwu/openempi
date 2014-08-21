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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * AuditEvent entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "audit_event")
@GenericGenerator(name = "audit_event_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "audit_event_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class AuditEvent extends BaseObject implements Serializable
{
	private static final long serialVersionUID = -6061320465621019356L;

	private Integer auditEventId;
	private Date dateCreated;
	private AuditEventType auditEventType;
	private String auditEventDescription;
	private Person refPerson;
	private Person altRefPerson;
	private User userCreatedBy;

	/** default constructor */
	public AuditEvent() {
	}

	public AuditEvent(Date dateCreated, AuditEventType auditEventType, String auditEventDescription, User userCreatedBy) {
		super();
		this.dateCreated = dateCreated;
		this.auditEventType = auditEventType;
		this.auditEventDescription = auditEventDescription;
		this.userCreatedBy = userCreatedBy;
	}

	@Id
	@GeneratedValue(generator="audit_event_gen")
	@Column(name = "audit_event_id", unique = true, nullable = false)
	public Integer getAuditEventId() {
		return auditEventId;
	}

	public void setAuditEventId(Integer auditEventId) {
		this.auditEventId = auditEventId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "audit_event_type_cd")
	public AuditEventType getAuditEventType() {
		return auditEventType;
	}

	public void setAuditEventType(AuditEventType auditEventType) {
		this.auditEventType = auditEventType;
	}

	@Column(name = "audit_event_description", length = 255)
	public String getAuditEventDescription() {
		return auditEventDescription;
	}

	public void setAuditEventDescription(String auditEventDescription) {
		this.auditEventDescription = auditEventDescription;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ref_person_id", nullable = true)
	public Person getRefPerson() {
		return refPerson;
	}

	public void setRefPerson(Person refPerson) {
		this.refPerson = refPerson;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "alt_ref_person_id", nullable = true)
	public Person getAltRefPerson() {
		return altRefPerson;
	}

	public void setAltRefPerson(Person altRefPerson) {
		this.altRefPerson = altRefPerson;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id", nullable = false)
	public User getUserCreatedBy() {
		return userCreatedBy;
	}

	public void setUserCreatedBy(User userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof AuditEvent))
			return false;
		AuditEvent castOther = (AuditEvent) other;
		return new EqualsBuilder().append(auditEventId, castOther.auditEventId)
				.append(dateCreated, castOther.dateCreated).append(
						auditEventType, castOther.auditEventType).append(
						auditEventDescription, castOther.auditEventDescription)
				.append(refPerson, castOther.refPerson).append(altRefPerson,
						castOther.altRefPerson).append(userCreatedBy,
						castOther.userCreatedBy).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(auditEventId).append(dateCreated)
				.append(auditEventType).append(auditEventDescription).append(
						refPerson).append(altRefPerson).append(userCreatedBy)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("auditEventId", auditEventId)
				.append("dateCreated", dateCreated).append("auditEventType",
						auditEventType).append("auditEventDescription",
						auditEventDescription).append("refPerson", refPerson)
				.append("altRefPerson", altRefPerson).append("userCreatedBy",
						userCreatedBy).toString();
	}

}
