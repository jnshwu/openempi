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
 * Thrown when an authentication fails in the SecurityManager.
 * Borrowed from Beginning Google Web Toolkit Book
 * 
 * @author Uri Boness
 */
public class AuthenticationException extends RuntimeException implements Serializable {

    /**
     * Construct a new AuthenticationException
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException with a given message.
     *
     * @param message The given message.
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthenticationException with a given message and the root cause.
     *
     * @param message The given message.
     * @param cause The root cause of the failure.
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
