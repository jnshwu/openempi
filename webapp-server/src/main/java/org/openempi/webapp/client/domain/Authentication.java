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
package org.openempi.webapp.client.domain;

import java.io.Serializable;

/**
 * Holds user login information
 * Borrowed from Beginning Google Web Toolkit Book
 *
 * @author Uri Boness
 */
public class Authentication implements Serializable {

    /**
     * An authentication object representing an anonymous user.
     */
    public final static Authentication ANONYMOUS = new Authentication();

    private String username;
    private String password;

    /**
     * Constructs a new Anonymous Authentication
     */
    public Authentication() {
        username = "anonymous";
        password = "anonymous";
    }

    /**
     * Constructs a new Authentication with given username and password.
     *
     * @param username The username.
     * @param password The password.
     */
    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Authentication(String username) {
    	this(username,"");
    }

    /**
     * Returns the username of this authentication.
     *
     * @return The username of this authentication.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this authentication.
     *
     * @return The password of this authentication.
     */
    public String getPassword() {
        return password;
    }

}
