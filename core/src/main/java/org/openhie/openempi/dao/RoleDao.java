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
package org.openhie.openempi.dao;

import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Role;
import org.springframework.transaction.annotation.Transactional;

/**
 * Role Data Access Object (DAO) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface RoleDao extends GenericDao<Role, Long>
{
	/**
	 * Returns the list of all defined roles in the system.
	 * 
	 * @return list of roles.
	 */
    List<Role> getRoles();
    
    /**
     * Returns the role identified by the ID specified by the caller.
     * 
     * @param roleId
     * @return The role identified by the caller or null if the role is not found.
     */
    Role getRole(Long roleId);
    
    /**
     * Returns the list of all permissions defined in the system.
     * 
     * @return The list of permissions defined in the system.
     */
    List<Permission> getPermissions();
    
    /**
     * Creates or updates the role passed in by the caller.
     * 
     * @param role
     * @return The role after the write or update operation.
     */
    @Transactional
    Role saveRole(Role role);
 
    /**
     * Gets role information based on rolename
     * @param rolename the rolename
     * @return populated role object
     */
    Role getRoleByName(String roleName);

    /**
     * Removes a role from the database by name
     * @param rolename the role's rolename
     */
    void removeRole(String rolename) throws ApplicationException;
}
