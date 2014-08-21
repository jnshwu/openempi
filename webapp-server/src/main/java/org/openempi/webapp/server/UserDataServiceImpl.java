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
package org.openempi.webapp.server;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.UserDataService;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.RoleWeb;
import org.openempi.webapp.client.model.UserWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.UserManager;

public class UserDataServiceImpl extends AbstractRemoteServiceServlet implements UserDataService
{
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
//		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}
	
	public List<UserWeb> getUsers() throws Exception {			
			authenticateCaller();	
			
			UserManager userService = Context.getUserManager();
			List<org.openhie.openempi.model.User> users = userService.getUsers(null);
			List<UserWeb> dtos = new java.util.ArrayList<UserWeb>(users.size());
			for (User us : users) {
				UserWeb uw = ModelTransformer.mapToUser( us, UserWeb.class, false);
				dtos.add(uw);
			}
			return dtos;		
	}

    public UserWeb getUserByUsername(String username) throws Exception {
    	
		authenticateCaller();	
		try {
			UserManager userService = Context.getUserManager();			
			User user = userService.getUserByUsername(username);
			
			return ModelTransformer.mapToUser( user, UserWeb.class, true);

		} catch (Throwable t) {
			log.error("Failed while getting a user entry: " + t, t);
			throw new Exception(t.getMessage());
		}
    }
    
	public UserWeb saveUser(UserWeb userWeb) throws Exception {
		log.debug("Received request to update the user entry in the repository.");
		
		authenticateCaller();	
		try {
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.User user = ModelTransformer.mapToUser(userWeb, org.openhie.openempi.model.User.class);
			
			User updatedUser = userService.saveUser(user);
			
			return ModelTransformer.mapToUser( updatedUser, UserWeb.class, true);

		} catch (Throwable t) {
			log.error("Failed while updating a user entry: " + t, t);
			throw new Exception(t.getMessage());
		}
	}
	
	public String deleteUser(UserWeb userWeb) throws Exception {
		log.debug("Received request to delete user entry to the repository.");
		
		authenticateCaller();	
		String msg = "";
		try {
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.User user = ModelTransformer.mapToUser(userWeb, org.openhie.openempi.model.User.class);
			
			userService.removeUser(user.getId().toString());
			return msg;			
		} catch (Throwable t) {
			log.error("Failed while delete a user entry: " + t, t);
			throw new Exception(t.getMessage());
		}
	}

	public Map<String,PermissionWeb> getUserPermissions(UserWeb userWeb) throws Exception {
		
		authenticateCaller();	
		try {
			// UserManager userService = Context.getUserManager();

			Map<String,PermissionWeb> userPermissionByName = new java.util.HashMap<String,PermissionWeb>();
	
			if (userWeb.getRoles() != null && userWeb.getRoles().size() > 0 ) {
				for (RoleWeb roleWeb : userWeb.getRoles()) {	
					
					 RoleWeb role = getRole(roleWeb.getId());
					 if(role.getPermissions() != null && role.getPermissions().size() > 0 ) {
						for (PermissionWeb pw : role.getPermissions()) {
							PermissionWeb  userPermission = userPermissionByName.get(pw.getName());
							if (userPermission == null) {							
								userPermissionByName.put(pw.getName(), pw);		
							}
						}
					 }			
				}
			}					
			return userPermissionByName;		
			
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public UserWeb authenticateUser(String username, String password, boolean verifyPasswordOnly) throws Exception {
		log.debug("Received request to authenticate the user entry in the repository.");
		try {
			UserManager userService = Context.getUserManager();
			try {
				User authenticatedUser = userService.authenticate(username, password);
				UserWeb userWeb = ModelTransformer.mapToUser( authenticatedUser, UserWeb.class, true);
				if (!verifyPasswordOnly) {
					String sessionKey = userService.createSession(authenticatedUser);
					storeSessionKeyInSession(sessionKey);
					userWeb.setSessionKey(sessionKey);
				}
				return userWeb;
			} catch (AuthenticationException e) {
				if (!verifyPasswordOnly) {
					log.warn("Invalid loging attempt using username " + username);
				}
				throw e;
			}
		} catch (Throwable t) {
			// log.error("Failed while authenticate a user entry: " + t, t);
			throw new Exception(t.getMessage());
		}
	}
	
	public List<RoleWeb> getRoles() throws Exception {
		
		authenticateCaller();	
		
		UserManager userService = Context.getUserManager();
		List<org.openhie.openempi.model.Role> roles = userService.getRoles();
		List<RoleWeb> dtos = new java.util.ArrayList<RoleWeb>(roles.size());
		for (Role rs : roles) {
			RoleWeb rw = ModelTransformer.mapToRole( rs, RoleWeb.class, false);
			dtos.add(rw);
		}
			
		return dtos;		
	}
	
	public RoleWeb getRole(Long roleId) throws Exception {
		
		authenticateCaller();
		try {
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.Role role = userService.getRole(roleId);

			return ModelTransformer.mapToRole( role, RoleWeb.class, true);	
			
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	public List<PermissionWeb> getPermissions() throws Exception {
		
		authenticateCaller();	

		UserManager userService = Context.getUserManager();
		List<org.openhie.openempi.model.Permission> permissions = userService.getPermissions();
		List<PermissionWeb> dtos = new java.util.ArrayList<PermissionWeb>(permissions.size());
		for (Permission ps : permissions) {
			PermissionWeb pw = ModelTransformer.map( ps, PermissionWeb.class);
			dtos.add(pw);
		}
			
		return dtos;		
	}
	
	public RoleWeb saveRole(RoleWeb roleWeb) throws Exception {
		log.debug("Received request to update the role entry in the repository.");
		
		authenticateCaller();
		try {
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.Role role = ModelTransformer.mapToRole(roleWeb, org.openhie.openempi.model.Role.class);
			
			Role updatedRole = userService.saveRole(role);
			
			return ModelTransformer.mapToRole( updatedRole, RoleWeb.class, true);

		} catch (Throwable t) {
			// log.error("Failed while updating a role entry: " + t, t);
			throw new Exception(t.getMessage());
		}
	}
	
	public String deleteRole(RoleWeb roleWeb) throws Exception {
		log.debug("Received request to delete role entry to the repository.");
		
		authenticateCaller();
		String msg = "";
		try {
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.Role role = ModelTransformer.mapToRole(roleWeb, org.openhie.openempi.model.Role.class);
			
			userService.removeRole(role.getName());
			return msg;			
		} catch (Throwable t) {
			// log.error("Failed while delete a role entry: " + t, t);
			throw new Exception(t.getMessage());
		}
	}
}
