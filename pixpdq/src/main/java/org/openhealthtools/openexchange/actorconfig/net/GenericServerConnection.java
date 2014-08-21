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
import java.net.ServerSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * An abstract implementation of IServerConnection which does a number of items required by all connections. <p />
 * <p/>
 * To make a new type of connection which requires these features simply extend this class
 * and implement the additionally required features.  Remember that the connect call is
 * where the socket (or other connection type) should be made.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public abstract class GenericServerConnection implements IServerConnection {
	private static final Log log = LogFactory.getLog(GenericServerConnection.class);
    /**
     * The actual connection.
     */
    protected ServerSocket ssocket = null;
    /**
     * The description of the connection. This includes everything needed to connect.
     */
    protected IConnectionDescription description = null;

    public GenericServerConnection(IConnectionDescription connectionDescription) {
        description = connectionDescription;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IConnection#getConnectionDescription()
     */
    public IConnectionDescription getConnectionDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IServerConnection#isServerConnectionValid()
     */
    public boolean isServerConnectionValid() {
        boolean isValid = false;
        if (ssocket != null) {
            isValid = ssocket.isBound();
        }
        return isValid;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IServerConnection#getServerSocket()
     */
    public ServerSocket getServerSocket() {
        ServerSocket returnVal = null;
        if (isServerConnectionValid()) {
            returnVal = ssocket;
        }
        // TODO add logging message.
        return returnVal;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IServerConnection#closeServerConnection()
     */
    public void closeServerConnection() {
        if (isServerConnectionValid()) {
            try {
                ssocket.close();
            }
            catch (IOException e) {
                ;
            } // TODO add logging message.
            // TODO add ATNA message?
        }
        ssocket = null;
    }

    /**
     * This function must be instantiated by the subclasses
     * because it generates all the actual server sockets when the
     * connection is made.
     */
    public abstract void connect();

}
