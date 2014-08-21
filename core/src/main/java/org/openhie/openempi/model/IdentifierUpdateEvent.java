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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * IdentifierUpdateEvent entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "identifier_update_event")
@GenericGenerator(name = "identifier_update_event_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "identifier_event_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class IdentifierUpdateEvent extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	
	public static final String ADD_SOURCE = "ADD";
	public static final String DELETE_SOURCE = "DELETE";
	public static final String UPDATE_SOURCE = "UPDATE";
	public static final String LINK_SOURCE = "LINK";
	public static final String UNLINK_SOURCE = "UNLINK";
	public static final String MERGE_SOURCE = "MERGE";

	public static final String JOIN_TRANSITION = "JOIN";
	public static final String LEAVE_TRANSITION = "LEAVE";
	
	private Long identifierUpdateEventId;
	private Date dateCreated;
	private User updateRecipient;
	private String source;
	private String transition;
	private Set<IdentifierUpdateEntry> preUpdateIdentifiers = new HashSet<IdentifierUpdateEntry>();
	private Set<IdentifierUpdateEntry> postUpdateIdentifiers = new HashSet<IdentifierUpdateEntry>();
	
	public IdentifierUpdateEvent() {
		this.dateCreated = new Date();
	}

	public IdentifierUpdateEvent(Set<IdentifierUpdateEntry> preUpdateIdentifiers, Set<IdentifierUpdateEntry> postUpdateIdentifiers, User updateRecipient) {
		this.dateCreated = new Date();
		this.preUpdateIdentifiers = preUpdateIdentifiers;
		this.postUpdateIdentifiers = postUpdateIdentifiers;
		this.updateRecipient = updateRecipient;
	}

	@Id	
	@GeneratedValue(generator="identifier_update_event_gen")
	@Column(name = "identifier_update_event_id", unique = true, nullable = false)
	@XmlElement
	public Long getIdentifierUpdateEventId() {
		return identifierUpdateEventId;
	}

	public void setIdentifierUpdateEventId(Long identifierUpdateEventId) {
		this.identifierUpdateEventId = identifierUpdateEventId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "update_recipient_id")
	public User getUpdateRecipient() {
		return updateRecipient;
	}

	public void setUpdateRecipient(User updateRecipient) {
		this.updateRecipient = updateRecipient;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_created")
	@XmlElement
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Column(name = "source", nullable = false, length = 64)
	@XmlElement
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Column(name = "transition", nullable = false, length = 64)
	@XmlElement
	public String getTransition() {
		return transition;
	}

	public void setTransition(String transition) {
		this.transition = transition;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN, 
    		org.hibernate.annotations.CascadeType.PERSIST,
    		org.hibernate.annotations.CascadeType.PERSIST})
	@JoinTable(name="identifier_pre_update",
		joinColumns={ @JoinColumn(name="identifier_update_event_id", referencedColumnName="identifier_update_event_id")},
		inverseJoinColumns={ @JoinColumn(name="identifier_update_entry_id", referencedColumnName="identifier_update_entry_id", unique=true)}
	)
	@XmlElementWrapper(name="preUpdateIdentifiers")
    @XmlElement(name="preUpdateIdentifier")
	public Set<IdentifierUpdateEntry> getPreUpdateIdentifiers() {
		return preUpdateIdentifiers;
	}

	public void setPreUpdateIdentifiers(Set<IdentifierUpdateEntry> preUpdateIdentifiers) {
		this.preUpdateIdentifiers = preUpdateIdentifiers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN, 
		org.hibernate.annotations.CascadeType.PERSIST,
		org.hibernate.annotations.CascadeType.PERSIST})
	@JoinTable(name="identifier_post_update",
		joinColumns={ @JoinColumn(name="identifier_update_event_id", referencedColumnName="identifier_update_event_id")},
		inverseJoinColumns={ @JoinColumn(name="identifier_update_entry_id", referencedColumnName="identifier_update_entry_id", unique=true)}
	)
	@XmlElementWrapper(name="postUpdateIdentifiers")
    @XmlElement(name="postUpdateIdentifier")
	public Set<IdentifierUpdateEntry> getPostUpdateIdentifiers() {
		return postUpdateIdentifiers;
	}

	public void setPostUpdateIdentifiers(Set<IdentifierUpdateEntry> postUpdateIdentifiers) {
		this.postUpdateIdentifiers = postUpdateIdentifiers;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifierUpdateEvent other = (IdentifierUpdateEvent) obj;
		if (identifierUpdateEventId == null) {
			if (other.identifierUpdateEventId != null)
				return false;
		} else if (!identifierUpdateEventId.equals(other.identifierUpdateEventId))
			return false;
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifierUpdateEventId == null) ? 0 : identifierUpdateEventId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "IdentifierUpdateEvent [identifierUpdateEventId=" + identifierUpdateEventId + ", dateCreated="
				+ dateCreated + ", updateRecipient=" + updateRecipient + ", source=" + source +  ", transition=" + transition +
				", preUpdateIdentifiers=" + preUpdateIdentifiers + ", postUpdateIdentifiers=" + postUpdateIdentifiers + "]";
	}
}
