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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This is the interface for all socket connections. <p />
 * <p/>
 * Use this interface insead of using the sockets directly.  By doing so,
 * you will be able to sidestep all of the details of making the connection.
 * In oder to get a connection, call the factory with the appropriate
 * connection name.
 *
 * @author Josh Flachsbart
 * @see ConnectionFactory
 */
public interface IConnection {
    /**
     * Checks if the connection is valid. <p />
     * <p/>
     * A connection might not be valid for any number of reasons,
     * including an invalid certificate, an unavailable host,
     * an unknown connection type.  This might be expanded in the
     * future, or the factory might take care of informing the user
     * of these problems via exceptions.
     *
     * @return True if you can use this connection to get input and output streams.
     * @see ConnectionFactory
     */
    public boolean isConnectionValid();

    /**
     * Get a way to write to this connection.
     *
     * @return The output stream that writes to this connection.
     */
    public OutputStream getOutputStream();

    /**
     * Get a way to read from the connection.
     *
     * @return The input stream that reads from this connection.
     */
    public InputStream getInputStream();

    /**
     * Get a direct handle on the socket, beware!
     *
     * @return The socket that is being used for communication.
     */
    public Socket getSocket();

    /**
     * Closes the connection.  Please close your own streams.
     */
    public void closeConnection();

    /**
     * Gets the description object for this connection.
     *
     * @return The description object for this conenction.
     */
    public IConnectionDescription getConnectionDescription();

    /**
     * Gets the protocol specific Endpoint with full URL for this connection.
     *
     * @return The Endpoint for this conenction.
     */
    public String getConnectionEndpoint();
    
    /**
     * Connects the connection.  Only called by the factory.
     */
    public void connect();
}
