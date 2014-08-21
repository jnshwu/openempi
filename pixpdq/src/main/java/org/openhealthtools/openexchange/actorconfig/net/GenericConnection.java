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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An abstract implementation of IConnection which does a number of items required by all connections. <p />
 * <p/>
 * To make a new type of connection which requires these features simply extend this class
 * and implement the additionally required features.  Remember that the connect call is
 * where the socket (or other connection type) should be made.
 *
 * @author Josh Flachsbart
 */
public abstract class GenericConnection implements IConnection {
	private static final Log log = LogFactory.getLog(GenericConnection.class);

    /**
     * The actual connection.
     */
    protected Socket socket = null;
    /**
     * The description of the connection. This includes everything needed to connect.
     */
    protected IConnectionDescription description = null;

    public GenericConnection(IConnectionDescription connectionDescription) {
        description = connectionDescription;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnection#getConnectionDescription()
      */
    public IConnectionDescription getConnectionDescription() {
        return description;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnection#isConnectionValid()
      */
    public boolean isConnectionValid() {
        boolean isValid = false;
        if (socket != null) {
            isValid = socket.isConnected();
        }
        return isValid;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnection#getOutputStream()
      */
    public OutputStream getOutputStream() {
        OutputStream returnVal = null;
        try {
            if (isConnectionValid()) {
                returnVal = socket.getOutputStream();
            }
        } catch (IOException e) {
            returnVal = null; // TODO add logging message.
        }
        return returnVal;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnection#getInputStream()
      */
    public InputStream getInputStream() {
        InputStream returnVal = null;
        try {
            if (isConnectionValid()) {
                returnVal = socket.getInputStream();
            }
        } catch (IOException e) {
            returnVal = null; // TODO add logging message.
        }
        return returnVal;
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnection#getSocket()
      */
    public Socket getSocket() {
        Socket returnVal = null;
        if (isConnectionValid()) {
            returnVal = socket;
        }
        // TODO add logging message.
        return returnVal;
    }
    

    /* (non-Javadoc)
      * @see org.openhealthtools.openatna.net.IConnection#closeConnection()
      */
    public void closeConnection() {
        if (isConnectionValid()) {
            try {
                socket.close();
            }
            catch (IOException e) {
                ;
            } // TODO add logging message.
            // TODO add ATNA message?
        }
        socket = null;
    }

    public abstract String getConnectionEndpoint();

    /**
     * This function must be instantiated by the subclasses
     * because it generates all the actual sockets when the c
     * connection is made.
     */
    public abstract void connect();
}
