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

import javax.net.ssl.SSLSocket;

import org.openhealthtools.openexchange.utils.StringUtil;

/**
 * An encrypted tcp connection.
 * <p/>
 * This should not be created directly but rather, requested from the ConnectionFactory.
 *
 * @author Josh Flachsbart
 * @see ConnectionFactory
 */
public class SecureConnection extends GenericConnection {

    /**
     * Used by the factory to create a connection.
     */
    public SecureConnection(IConnectionDescription connectionDescription) {
        super(connectionDescription);
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IConnection#getConnectionEndpoint()
     */
    public String getConnectionEndpoint() {
		String host = description.getHostname();
		if (host == null)
			host = "localhost";
		
		int port = description.getPort();
		
		String url = "https://" + host + ":" + port;

		//path is optional
		String path = description.getUrlPath();
		if (StringUtil.goodString(path)) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			url += path;
		}
		
		return url;
    }
    
    /**
     * Checks to make sure the description matches requirements for SecureConnection.
     */
    public boolean isConnectionValid() {
        boolean isValid = false;
        if ((description != null) &&
                description.isSecure() &&
                (description instanceof SecureConnectionDescription)) {
            isValid = super.isConnectionValid();
        }
        return isValid;
    }

    /**
     * Used by factory to start the connection.
     */
    public void connect() {
        SSLSocket secureSocket = null;
        if (description != null && description instanceof SecureConnectionDescription) {
            SecureConnectionDescription scd = (SecureConnectionDescription) description;
            // Secure socket factory handles bidirectional certs.
            SecureSocketFactory sslFactory = new SecureSocketFactory(scd);
            secureSocket = (SSLSocket) sslFactory.createSocket(description.getHostname(), description.getPort());
            // TODO Add ATNA logging.
        }
        socket = secureSocket;
    }
}
