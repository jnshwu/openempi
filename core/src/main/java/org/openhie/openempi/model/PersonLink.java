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
 * PersonLink entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "person_link", schema = "public")
@GenericGenerator(name = "person_link_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "person_link_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PersonLink extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -2998399249175445866L;

	private Integer personLinkId;
	private Person personLeft;
	private Person personRight;
	private User userCreatedBy;
	private Date dateCreated;
	private Double weight;
	private LinkSource linkSource;
	private Integer clusterId;

	/** default constructor */
	public PersonLink() {
	}

	/** full constructor */
	public PersonLink(Integer personLinkId, Person personByLhPersonId, Person personByRhPersonId, User userCreatedBy,
			Date dateCreated) {
		this.personLinkId = personLinkId;
		this.personLeft = personByLhPersonId;
		this.personRight = personByRhPersonId;
		this.userCreatedBy = userCreatedBy;
		this.dateCreated = dateCreated;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator="person_link_gen")
	@Column(name = "person_link_id", unique = true, nullable = false)
	@XmlElement
	public Integer getPersonLinkId() {
		return this.personLinkId;
	}

	public void setPersonLinkId(Integer personLinkId) {
		this.personLinkId = personLinkId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lh_person_id", nullable = false)
	@XmlElement
	public Person getPersonLeft() {
		return this.personLeft;
	}

	public void setPersonLeft(Person person) {
		this.personLeft = person;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rh_person_id", nullable = false)
	@XmlElement
	public Person getPersonRight() {
		return this.personRight;
	}

	public void setPersonRight(Person person) {
		this.personRight = person;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	@XmlElement
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "weight", nullable = false)
	@XmlElement
	public Double getWeight() {
		return this.weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "link_source_id", nullable = false)
	@XmlElement
	public LinkSource getLinkSource() {
		return linkSource;
	}

	public void setLinkSource(LinkSource linkSource) {
		this.linkSource = linkSource;
	}

	@Column(name = "cluster_id")
	@XmlElement
	public Integer getClusterId() {
		return clusterId;
	}

	public void setClusterId(Integer clusterId) {
		this.clusterId = clusterId;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PersonLink))
			return false;
		PersonLink castOther = (PersonLink) other;
		return new EqualsBuilder().append(personLinkId, castOther.personLinkId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(personLinkId).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("personLinkId", personLinkId)
			.append("personLeft",personLeft.getPersonId()).append("personRight", personRight.getPersonId())
			.append("creatorId", userCreatedBy).append("dateCreated", dateCreated).append("weight", weight)
			.append("linkSource", linkSource).append("clusterId", clusterId).toString();
	}
}