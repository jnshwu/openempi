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
package org.openhie.openempi.service;

import java.util.List;
import java.util.Set;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;

public class RoleTest extends BaseServiceTestCase {
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
	}

	public void testAddRole() {
		UserManager userService = Context.getUserManager();
		
		// First add a new role
		Role role = new Role();
		role.setName("testRole");
		role.setDescription("test role description.");
		try {
			role = userService.saveRole(role);
			log.debug("New role was saved: " + role);
		} catch (Exception e) {
			log.error("Adding a role test failed: " + e, e);
			assertTrue(true);
		}
		
		// Associate the role with the user and then attempt to remove the
		// role; this should fail because the role is in use.
		User user = userService.getUserByUsername("admin");
		if (user != null) {
			user.addRole(role);
			try {
				userService.saveUser(user);
				Set<Permission> permissions = userService.getUserPermissions(user);
				for (Permission perm : permissions) {
					log.debug("User " + user + " has permission: " + perm);
				}
				userService.removeRole(role.getName());
				assertTrue(false);
			} catch (Exception e) {
				log.error("Removing a role test failed as it should: " + e, e);
				assertTrue(true);
			}
			user.removeRole(role.getName());
			try {
				userService.saveUser(user);
				assertTrue(true);
			} catch (Exception e) {
				log.error("Removing a role test failed: " + e, e);
				assertTrue(false);
			}			
		}			
	}
	
	public void testRemoveRole() {
		UserManager userService = Context.getUserManager();
		Role role = userService.getRoleByName("testRole");
		if (role == null) {
			return;
		}
		try {
			Permission permission = getViewRecordPermission(userService);
			role.addPermission(permission);
			userService.saveRole(role);
			userService.removeRole(role.getName());
		} catch (Exception e) {
			log.error("Removing a role test failed: " + e, e);
			assertTrue(true);
		}		
	}
	public void testRoleManagement() {
		UserManager userService = Context.getUserManager();
		
		User user = userService.getUserByUsername("admin");

		try {
			List<Role> roles = userService.getRoles();
			Role adminRole=null;
			for (Role role : roles) {
				role = userService.getRole(role.getId());
				log.debug("Found role: " + role);
				if (role.getName().equalsIgnoreCase("ROLE_ADMIN")) {
					adminRole = userService.getRole(role.getId());
				}
			}
			
			if (adminRole == null) {
				assertTrue("Unable to find the default role: ROLE_ADMIN", false);
			}
			
			Permission viewRecordPermission = getViewRecordPermission(userService);
			if (viewRecordPermission == null) {
				assertTrue("Unable to find the permission: RECORD_VIEW", false);
			}
			
			Set<Permission> adminPerms = adminRole.getPermissions();
			Permission permToRemove=null;
			for (Permission perm : adminPerms) {
				if (perm.getPermissionId().intValue() == viewRecordPermission.getPermissionId().intValue()) {
					permToRemove = perm;
				}
			}
			if (permToRemove != null) {
				log.debug("Removing the permission " + permToRemove.getName() + " from the permissions associated with role " + adminRole.getName());
				adminPerms.remove(permToRemove);
			}
			userService.saveRole(adminRole);
			log.debug("List of permissions after removing the permission: " + viewRecordPermission.getName());
			for (Permission permission : adminRole.getPermissions()) {
				log.debug("In role " + adminRole.getName() + " found permission: " + permission);
			}
			
			adminPerms.add(viewRecordPermission);
			userService.saveRole(adminRole);
			log.debug("List of permissions after adding back the permission: " + viewRecordPermission.getName());
			for (Permission permission : adminRole.getPermissions()) {
				log.debug("In role " + adminRole.getName() + " found permission: " + permission);
			}
			
		} catch (Exception e) {
			log.error("The role test failed: "
					+ e, e);
			assertTrue(true);
		}
	}

	private Permission getViewRecordPermission(UserManager userService) {
		Permission viewRecordPermission=null;
		List<Permission> permissions = userService.getPermissions();
		for (Permission permission : permissions) {
			log.debug("Found permission: " + permission);
			if (permission.getName().equals("RECORD_VIEW")) {
				viewRecordPermission = permission;
			}
		}
		return viewRecordPermission;
	}
}
