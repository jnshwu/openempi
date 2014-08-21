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
 * A standard non encrypted tcp server connection.
 * <p/>
 * This should not be created directly but rather, requested from the ConnectionFactory.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class StandardServerConnection extends GenericServerConnection {
    /**Commons Log*/
	private static final Log log = LogFactory.getLog(StandardServerConnection.class);
    /**
     * Used by the factory to create a connection.
     */
    public StandardServerConnection(IConnectionDescription connectionDescription) {
        super(connectionDescription);
    }

    /**
     * Used by factory to start the connection.
     */
    public void connect() {
        try {
            ssocket = new ServerSocket(description.getPort());
            // TODO add ATNA logging message, perhaps in finally?
        } catch (IOException e) {
            log.error("Failed to create a server socket on port:" + description.getPort(), e);
            ssocket = null;
        }
    }
}
