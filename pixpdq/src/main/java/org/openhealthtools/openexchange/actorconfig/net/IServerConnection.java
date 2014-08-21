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

import java.net.ServerSocket;

/**
 * This is the interface for all server socket connections. <p />
 * <p/>
 * Use this interface instead of using the server sockets directly.  By doing so,
 * you will be able to side step all of the details of making the connection.
 * In oder to get a connection, call the factory with the appropriate
 * connection name.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface IServerConnection {
    /**
     * Checks if the server connection is valid. <p />
     * <p/>
     * A server connection might not be valid for any number of reasons,
     * including an invalid certificate,
     * an unknown connection type.  This might be expanded in the
     * future, or the factory might take care of informing the user
     * of these problems via exceptions.
     *
     * @return True if you can use this connection to get input and output streams.
     * @see ConnectionFactory
     */
    public boolean isServerConnectionValid();

    /**
     * Get a direct handle on the server socket, beware!
     *
     * @return The sever socket that is being used for communication.
     */
    public ServerSocket getServerSocket();

    /**
     * Closes the server connection.  Please close your own streams.
     */
    public void closeServerConnection();

    /**
     * Gets the description object for this connection.
     *
     * @return The description object for this conenction.
     */
    public IConnectionDescription getConnectionDescription();

    /**
     * Connects the server connection.  Only called by the factory.
     */
    public void connect();

}
