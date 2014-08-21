/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openexchange.actorconfig.net;

import java.util.List;
import java.util.Set;


/**
 * The basic connection description interface.
 * <p/>
 * It should be used to get information about a specific
 * connection.  You can get descriptions from the
 * connection factory, or from a specific connection.
 *
 * @author Josh Flachsbart
 * @see ConnectionFactory, IConnection
 */
public interface IConnectionDescription extends IBaseDescription {

    /**
     * Used to get the other host in this connection.
     *
     * @return The IP or name of the other host.
     */
    public String getHostname();

    /**
     * Used to get the port on the remote host we connect to.
     *
     * @return The port as an integer, -1 if specific port is not set.
     */
    public int getPort();

    /**
     * Used to get the URL of the service we connect to, not
     * including the hostName or port.
     *
     * @return The url of the service, or null if not specified
     */
    public String getUrlPath();

    /**
     * Used to determine if this is an SSL/TLS connection.
     *
     * @return True if it is.
     */
    public boolean isSecure();

    /**
     * Used to determine if this is a server connection.
     *
     * @return True if it is.
     */
    public boolean isServer();
}
