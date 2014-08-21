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
 * UserFile entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "user_file")
@GenericGenerator(name = "user_file_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "user_file_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class UserFile extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 6170253797278224912L;

	private Integer userFileId;
	private User owner;
	private String name;
	private String filename;
	private String imported;
	private String profiled;
	private Integer rowsImported;
	private Integer rowsProcessed;
	private String profileProcessed;
	private Date dateCreated;
	
	public UserFile() {
	}

	public UserFile(String name, String filename) {
		this.name = name;
		this.filename = filename;
		this.imported = "N";
		this.profiled = "N";
	}
	
	@Id
	@GeneratedValue(generator="user_file_gen")
	@Column(name = "user_file_id", unique = true, nullable = false)
	public Integer getUserFileId() {
		return userFileId;
	}

	public void setUserFileId(Integer userFileId) {
		this.userFileId = userFileId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "filename", nullable = false)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name = "imported_ind", nullable = false)
	public String getImported() {
		return imported;
	}

	public void setImported(String imported) {
		this.imported = imported;
	}

	@Column(name = "profiled_ind")
	public String getProfiled() {
		return profiled;
	}

	public void setProfiled(String profiled) {
		this.profiled = profiled;
	}
	
	@Column(name = "rows_imported")
	public Integer getRowsImported() {
		return rowsImported;
	}

	public void setRowsImported(Integer rowsImported) {
		this.rowsImported = rowsImported;
	}
	
	@Column(name = "rows_processed")
	public Integer getRowsProcessed() {
		return rowsProcessed;
	}

	public void setRowsProcessed(Integer rowsProcessed) {
		this.rowsProcessed = rowsProcessed;
	}

	@Column(name = "profile_processed")
	public String getProfileProcessed() {
		return profileProcessed;
	}

	public void setProfileProcessed(String profileProcessed) {
		this.profileProcessed = profileProcessed;
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
		if (!(other instanceof UserFile))
			return false;
		UserFile castOther = (UserFile) other;
		return new EqualsBuilder().append(userFileId, castOther.userFileId)
				.append(owner, castOther.owner).append(name, castOther.name)
				.append(filename, castOther.filename).append(imported,
						castOther.imported).append(dateCreated,
						castOther.dateCreated).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(userFileId).append(owner).append(
				name).append(filename).append(imported).append(dateCreated)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("userFileId", userFileId)
				.append("owner", owner).append("name", name).append("filename",
						filename).append("imported", imported).append(
						"dateCreated", dateCreated).toString();
	}
}
