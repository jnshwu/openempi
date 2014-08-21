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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * This class is used to represent available roles in the database.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Version by Dan Kibler dan@getrolling.com
 *         Extended to implement Acegi GrantedAuthority interface
 *         by David Carter david@carter.net
 */
@Entity
@Table(name="role")
@NamedQueries ({
    @NamedQuery(
        name = "findRoleByName",
        query = "select r from Role r where r.name = :name "
        )
})
public class Role extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 3690197650654049848L;
    private Long id;
    private String name;
    private String description;
    private Set<Permission> permissions;
    
    /**
     * Default constructor - creates a new instance with no values set.
     */
    public Role() {
    	permissions = new java.util.HashSet<Permission>();
    }

    /**
     * Create a new instance and set the name.
     * @param name name of the role.
     */
    public Role(final String name) {
        this.name = name;
    }

    @Id
    @Column(name="role_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(length=20)
    public String getName() {
        return this.name;
    }

    @Column(length=64)
    public String getDescription() {
        return this.description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission",
    	joinColumns = {@JoinColumn(name="role_id")},
    	inverseJoinColumns={@JoinColumn(name="permission_id")})
    public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(Permission permission) {
		permissions.add(permission);
	}
    
    public void removePermission(String permissionName) {
    	Permission permToRemove = null;
    	for (Permission permission : getPermissions()) {
    		if (permission.getName().equalsIgnoreCase(permissionName)) {
    			permToRemove=permission;
    			break;
    		}
    	}
    	if (permToRemove != null) {
    		getPermissions().remove(permToRemove);
    	}
    }
	
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }

        final Role role = (Role) o;

        return !(name != null ? !name.equals(role.name) : role.name != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

	public int compareTo(Object o) {
        return (equals(o) ? 0 : -1);
    }

    @Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", description=" + description + ", permissions=" + permissions
				+ "]";
	}
}
