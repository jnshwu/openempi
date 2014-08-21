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
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserDataServiceAsync
{	
	public void getUsers( AsyncCallback<List<UserWeb>> callback);
	
	public void getUserByUsername(String username, AsyncCallback<UserWeb> callback);
	
	public void saveUser(UserWeb user, AsyncCallback<UserWeb> callback);
	
	public void deleteUser(UserWeb user, AsyncCallback<String> callback);
	
	public void authenticateUser(String username, String password, boolean verifyPasswordOnly, AsyncCallback<UserWeb> callback);
	
	public void getRoles( AsyncCallback<List<RoleWeb>> callback);
	
	public void getRole(Long roleId, AsyncCallback<RoleWeb> callback);
	
	public void saveRole(RoleWeb role, AsyncCallback<RoleWeb> callback);
	
	public void deleteRole(RoleWeb role, AsyncCallback<String> callback);
	
	public void getPermissions( AsyncCallback<List<PermissionWeb>> callback);
	
	public void getUserPermissions( UserWeb user, AsyncCallback<Map<String,PermissionWeb>> callback );
}
