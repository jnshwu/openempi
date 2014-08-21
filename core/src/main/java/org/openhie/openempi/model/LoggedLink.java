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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * LoggedLink entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "link_log")
@GenericGenerator(name = "link_log_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "person_link_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class LoggedLink extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -2998399249175445866L;

	private Integer linkId;
	private Long leftRecordId;
	private Long rightRecordId;
	private User userCreatedBy;
	private Date dateCreated;
	private Double weight;
	private Integer vectorValue;

	/** default constructor */
	public LoggedLink() {
	}

	// Property accessors
	@Id
	@GeneratedValue(generator="link_log_gen")
	@Column(name = "link_id", unique = true, nullable = false)
	public Integer getLinkId() {
		return this.linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	@Column(name = "lh_record_id", nullable = false)
	public Long getLeftRecordId() {
		return this.leftRecordId;
	}

	public void setLeftRecordId(Long leftRecordId) {
		this.leftRecordId = leftRecordId;
	}

	@Column(name = "rh_record_id", nullable = false)
	public Long getRightRecordId() {
		return this.rightRecordId;
	}

	public void setRightRecordId(Long rightRecordId) {
		this.rightRecordId = rightRecordId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id", nullable = false)
	public User getUserCreatedBy() {
		return this.userCreatedBy;
	}

	public void setUserCreatedBy(User userByCreatorId) {
		this.userCreatedBy = userByCreatorId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "weight", nullable = false)
	public Double getWeight() {
		return this.weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@Column(name = "vector_value")
	public Integer getVectorValue() {
		return vectorValue;
	}

	public void setVectorValue(Integer vectorValue) {
		this.vectorValue = vectorValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkId == null) ? 0 : linkId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoggedLink other = (LoggedLink) obj;
		if (linkId == null) {
			if (other.linkId != null)
				return false;
		} else if (!linkId.equals(other.linkId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoggedLink [linkId=" + linkId + ", leftRecordId =" + leftRecordId + ", rightRecordId=" + rightRecordId
				+ ", userCreatedBy=" + userCreatedBy + ", dateCreated=" + dateCreated + ", weight=" + weight
				+ ", vectorValue=" + vectorValue + "]";
	}
}