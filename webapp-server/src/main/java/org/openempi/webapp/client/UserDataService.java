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
package org.openempi.webapp.client;

import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.model.RoleWeb;
import org.openempi.webapp.client.model.UserWeb;
import org.openempi.webapp.client.model.PermissionWeb;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UserDataService extends RemoteService
{
	public List<UserWeb> getUsers() throws Exception;

	public UserWeb saveUser(UserWeb user) throws Exception;
	
	public UserWeb getUserByUsername(String username) throws Exception;
	
	public String deleteUser(UserWeb user) throws Exception;
	
	public UserWeb authenticateUser(String username, String password, boolean verifyPasswordOnly) throws Exception;
	
	public List<RoleWeb> getRoles() throws Exception;
	
	public RoleWeb saveRole(RoleWeb role) throws Exception;
	
	public RoleWeb getRole(Long roleId) throws Exception;
	
	public String deleteRole(RoleWeb role) throws Exception;
	
	public List<PermissionWeb> getPermissions() throws Exception;
	
	public Map<String,PermissionWeb> getUserPermissions(UserWeb user) throws Exception;
	
}
