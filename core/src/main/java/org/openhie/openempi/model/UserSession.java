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
 * IdentifierDomain entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "user_session")
@GenericGenerator(name = "user_session_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "user_session_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class UserSession implements Serializable
{
	private static final long serialVersionUID = 2611151383140968220L;

	private Integer sessionId;
	private String sessionKey;
	private User user;
	private Date dateCreated;
	
	/** default constructor */	
	public UserSession() {
	}

	/** Common constructor */
	/** full constructor */
	public UserSession(String sessionKey, User user, Date dateCreated) {
		this.sessionKey = sessionKey;
		this.user = user;
		this.dateCreated = dateCreated;
	}
	
	/** full constructor */
	public UserSession(Integer sessionId, String sessionKey, User user, Date dateCreated) {
		this.sessionId = sessionId;
		this.sessionKey = sessionKey;
		this.user = user;
		this.dateCreated = dateCreated;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator="user_session_gen")
	@Column(name = "session_id", unique = true, nullable = false)
	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "session_key")
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof UserSession))
			return false;
		UserSession castOther = (UserSession) other;
		return new EqualsBuilder().append(sessionKey, castOther.sessionKey).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(sessionKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("sessionId", sessionId).append("sessionKey", sessionKey).append("user",
				user).append("dateCreated", dateCreated).toString();
	}
}
