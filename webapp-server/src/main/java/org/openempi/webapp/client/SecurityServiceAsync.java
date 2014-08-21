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

import org.openempi.webapp.client.domain.Authentication;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * Manages all security aspects of the application.
 * Borrowed from Beginning Google Web Toolkit Book
 *
 * @author Uri Boness
 */
public interface SecurityServiceAsync {

	/**
	 * 
	 * @param userName
	 * @throws AuthenticationException if userName is 'badguy'
	 */
    void login(String userName, AsyncCallback<Void> callback);

    /**
     * Logs out the currently authenticated user.
     */
    void logout(AsyncCallback<Void> callback);

    /**
     * Indicates whether there is a logged in user.
     *
     * @param callback the callback to return Whether there is a logged in user.
     */
    void isLoggedIn(AsyncCallback<Boolean> callback);

    /**
     * Returns the currently logged in authentication.
     *
     * @param callback the callback to return The currently logged in authentication.
     */
    void getCurrentAuthentication(AsyncCallback<Authentication> callback);

}
