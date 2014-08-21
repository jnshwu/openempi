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

import javax.net.ssl.SSLServerSocket;

/**
 * An encrypted tcp server connection.
 * <p/>
 * This should not be created directly but rather, requested from the ConnectionFactory.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class SecureServerConnection extends GenericServerConnection {
    /**
     * Used by the factory to create a server connection.
     */
    public SecureServerConnection(IConnectionDescription connectionDescription) {
        super(connectionDescription);
    }

    /**
     * Checks to make sure the description matches requirements for SecureServerConnection.
     */
    public boolean isServerConnectionValid() {
        boolean isValid = false;
        if ((description != null) &&
                description.isSecure() &&
                (description instanceof SecureConnectionDescription)) {
            isValid = super.isServerConnectionValid();
        }
        return isValid;
    }

    /**
     * Used by factory to start the server connection.
     */
    public void connect() {
        SSLServerSocket secureServerSocket = null;
        if (description != null && description instanceof SecureConnectionDescription) {
            SecureConnectionDescription scd = (SecureConnectionDescription) description;
            // Secure socket factory handles bidirectional certs.
            SecureSocketFactory sslFactory = new SecureSocketFactory(scd);
            secureServerSocket = (SSLServerSocket) sslFactory.createServerSocket(description.getPort());

            // TODO Add ATNA logging.
        }
        ssocket = secureServerSocket;
    }

}
