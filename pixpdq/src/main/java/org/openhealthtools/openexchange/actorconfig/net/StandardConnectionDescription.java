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

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.ActorDescription;
import org.openhealthtools.openexchange.utils.Pair;


/**
 * A description of an insecure connection. <p />
 * <p/>
 * This should only be obtained throught the ConnectionFactory's
 * getConnectionDescription function.
 *
 * @author Josh Flachsbart
 */
public class StandardConnectionDescription extends BaseDescription implements IConnectionDescription {

	private static final Log log = LogFactory.getLog(StandardConnectionDescription.class);

    /* Standard properties of all connections */
    private String hostName = null;
    private int port = -1;
    private String urlPath = null;
    
    private boolean isServer = false;
    /**
     * Used by the factory to make a connection with a specified port. <p />
     * <p/>
     * After creation, all of the elements of the connection description
     * must be set by the factory.  This should not be instantiated outside
     * of the factory, nor should any of the setters be used after creation.
     */
    public StandardConnectionDescription() {
        log.debug("Creating standard description.");
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setHostname(String hostName) {
        if (!complete) {
            this.hostName = hostName;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setPort(int port) {
        if (!complete) {
            this.port = port;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setUrlPath(String urlPath) {
        if (!complete) {
            this.urlPath = urlPath;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription#getHostname()
     */
    public String getHostname() {
        return hostName;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription#getPort()
     */
    public int getPort() {
        return port;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription#getUrlPath()
     */
    public String getUrlPath() {
        return urlPath;
    }
    
	/* (non-Javadoc)
     * @see org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription#isSecure()
     */
	public boolean isServer() {
		return isServer;
	}

    /**
     * Only used for init.   
     */
	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	/* (non-Javadoc)
     * @see org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription#isSecure()
     */
    public boolean isSecure() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription#getDescription()
     */
    public String getDescription() {
        StringBuffer sb = new StringBuffer();
        if (name != null) {
            // "name (host:port TLS)"
            sb.append(name);
            if (hostName != null) {
                sb.append('(');
                sb.append(hostName);
                if (port >= 0) {
                    sb.append(':');
                    sb.append(port);
                }
                if (isSecure()) {
                    sb.append(" TLS");
                }
                sb.append(')');
            }
        } else if (hostName != null) {
            // "host:port (TLS)"
            sb.append(hostName);
            if (port >= 0) {
                sb.append(':');
                sb.append(port);
            }
            if (isSecure()) {
                sb.append(" (TLS)");
            }
        }
        // Done
        if (sb.length() <= 0) {
            return null;
        }
        return sb.toString();
    }

    /**
     * Loads configuration information for a single connection from an XML configuration file.
     * This method is used only for for MESA and unit tests.
     *
     * @param filename The name of the configuration file to load
     * @return True if the file was loaded without error
     */
    public boolean loadConfiguration(String filename) {
        return ConnectionDescriptionLoader.processDescriptionIncludeFile(this, new File(filename));
    }
}
