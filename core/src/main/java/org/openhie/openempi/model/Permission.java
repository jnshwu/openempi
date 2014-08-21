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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.GrantedAuthority;

/**
 * Permission entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "permission")
public class Permission extends BaseObject implements Serializable, GrantedAuthority
{
	private static final long serialVersionUID = 3690197650654049848L;
	
	public final static String RECORD_ADD = "RECORD_ADD";
	public final static String RECORD_DELETE = "RECORD_DELETE";
	public final static String RECORD_EDIT = "RECORD_EDIT";
	public final static String RECORD_VIEW = "RECORD_VIEW";
	public final static String IDENTIFIER_DOMAIN_ADD = "IDENTIFIER_DOMAIN_ADD";
	public final static String IDENTIFIER_DOMAIN_DELETE = "IDENTIFIER_DOMAIN_DELETE";
	public final static String IDENTIFIER_DOMAIN_EDIT = "IDENTIFIER_DOMAIN_EDIT";
	public final static String IDENTIFIER_DOMAIN_VIEW = "IDENTIFIER_DOMAIN_VIEW";
	public final static String RECORD_LINKS_REVIEW = "RECORD_LINKS_REVIEW";
	public final static String REPORT_GENERATE = "REPORT_GENERATE";
	public final static String REPORT_VIEW = "REPORT_VIEW";
	public final static String CUSTOM_FIELDS_CONFIGURE = "CUSTOM_FIELDS_CONFIGURE";
	public final static String BLOCKING_CONFIGURE = "BLOCKING_CONFIGURE";
	public final static String MATCHING_CONFIGURE = "MATCHING_CONFIGURE";
	public final static String FILE_IMPORT = "FILE_IMPORT";
	public final static String USER_ADD = "USER_ADD";
	public final static String USER_DELETE = "USER_DELETE";
	public final static String USER_EDIT = "USER_EDIT";
	public final static String USER_VIEW = "USER_VIEW";
	public final static String EVENT_CONFIGURATION_EDIT = "EVENT_CONFIGURATION_EDIT";
	public final static String GLOBAL_IDENTIFIERS_EDIT = "GLOBAL_IDENTIFIERS_EDIT";
	public final static String PIXPDQ_MANAGE = "PIXPDQ_MANAGE";
	  
	private Integer permissionId;
	private String name;
	private String description;

    /**
     * Default constructor - creates a new instance with no values set.
     */
    public Permission() {
    }
    
    public Permission(int permissionId, String name, String description) {
    	this.permissionId = permissionId;
    	this.name = name;
    	this.description = description;
    }

    @Id
	@Column(name = "permission_id", unique = true, nullable = false)
	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	@Column(name = "permission_name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "permission_description", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public String getAuthority() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((permissionId == null) ? 0 : permissionId.hashCode());
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
		Permission other = (Permission) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (permissionId == null) {
			if (other.permissionId != null)
				return false;
		} else if (!permissionId.equals(other.permissionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Permission [permissionId=" + permissionId + ", name=" + name + ", description=" + description + "]";
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
