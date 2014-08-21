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

import javax.persistence.CascadeType;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * PersonLink entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "person_link_review", schema = "public")
@GenericGenerator(name = "person_link_review_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "person_link_review_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ReviewRecordPair extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -2998399249175445866L;

	private Integer reviewRecordPairId;
	private Person personLeft;
	private Person personRight;
	private User userCreatedBy;
	private Date dateCreated;
	private User userReviewedBy;
	private Date dateReviewed;
	private Double weight;
	private LinkSource linkSource;
	private Boolean recordsMatch;

	/** default constructor */
	public ReviewRecordPair() {
	}

	/** full constructor */
	public ReviewRecordPair(Integer reviewRecordPairId, Person personByLhPersonId, Person personByRhPersonId, User userCreatedBy,
			Date dateCreated) {
		this.reviewRecordPairId = reviewRecordPairId;
		this.personLeft = personByLhPersonId;
		this.personRight = personByRhPersonId;
		this.userCreatedBy = userCreatedBy;
		this.dateCreated = dateCreated;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator="person_link_review_gen")
	@Column(name = "person_link_review_id", unique = true, nullable = false)
	@XmlElement
	public Integer getReviewRecordPairId() {
		return this.reviewRecordPairId;
	}

	public void setReviewRecordPairId(Integer reviewRecordPairId) {
		this.reviewRecordPairId = reviewRecordPairId;
	}	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reviewer_id", nullable = true)
	@XmlElement
	public User getUserReviewedBy() {
		return userReviewedBy;
	}

	public void setUserReviewedBy(User userReviewedBy) {
		this.userReviewedBy = userReviewedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_reviewed", nullable = true, length = 8)
	@XmlElement
	public Date getDateReviewed() {
		return dateReviewed;
	}

	public void setDateReviewed(Date dateReviewed) {
		this.dateReviewed = dateReviewed;
	}

	@Column(name = "records_match", nullable = true)
	@XmlElement
	public Boolean getRecordsMatch() {
		return recordsMatch;
	}

	public void setRecordsMatch(Boolean recordsMatch) {
		this.recordsMatch = recordsMatch;
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

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReviewRecordPair other = (ReviewRecordPair) obj;
		if (personLeft == null) {
			if (other.personLeft != null)
				return false;
		} else if (!personLeft.equals(other.personLeft))
			return false;
		if (personRight == null) {
			if (other.personRight != null)
				return false;
		} else if (!personRight.equals(other.personRight))
			return false;
		if (personLeft.getPersonId() == other.getPersonRight().getPersonId() ||
				personRight.getPersonId() == other.getPersonLeft().getPersonId())
			return true;
		if (reviewRecordPairId == null) {
			if (other.reviewRecordPairId != null)
				return false;
		} else if (!reviewRecordPairId.equals(other.reviewRecordPairId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((personLeft == null) ? 0 : personLeft.hashCode());
		result = prime * result + ((personRight == null) ? 0 : personRight.hashCode());
		result = prime * result + ((reviewRecordPairId == null) ? 0 : reviewRecordPairId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ReviewRecordPair [reviewRecordPairId=" + reviewRecordPairId + ", personLeft=" + personLeft
				+ ", personRight=" + personRight + ", userCreatedBy=" + userCreatedBy + ", dateCreated=" + dateCreated
				+ ", userReviewedBy=" + userReviewedBy + ", dateReviewed=" + dateReviewed + ", weight=" + weight
				+ ", linkSource=" + linkSource + ", recordsMatch=" + recordsMatch + "]";
	}
}