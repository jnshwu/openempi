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
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.utils.StringUtil;


/**
 * A standard non encrypted tcp connection.
 * <p/>
 * This should not be created directly but rather, requested from the ConnectionFactory.
 *
 * @author Josh Flachsbart
 * @see ConnectionFactory
 */
public class StandardConnection extends GenericConnection {
    /**Commons Log*/
	private static final Log log = LogFactory.getLog(StandardConnection.class);
    /**
     * Used by the factory to create a connection.
     */
    public StandardConnection(IConnectionDescription connectionDescription) {
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
		
		String url = "http://" + host + ":" + port;

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
     * Used by factory to start the connection.
     */
    public void connect() {
        try {
            socket = new Socket(description.getHostname(), description.getPort());
            // TODO add ATNA logging message, perhaps in finally?
        } catch (IOException e) {
            log.error("Failed to create a socket on hostname:" + description.getHostname()
                    + " port:" + description.getPort(), e);
            socket = null;
        }
    }
}
