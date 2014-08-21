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

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A description of a secure (SSL/TLS) connection. <p />
 * <p/>
 * This description contains all of the additional information required by secure connections.
 * In general this should be obtained by a call to the getConnectionDescription function
 * in the ConnectionFactory, and not created directly.
 *
 * @author Josh Flachsbart
 */
public class SecureConnectionDescription extends StandardConnectionDescription {
	private static final Log log = LogFactory.getLog(SecureConnectionDescription.class);

    private String trustStoreLocation = null;
    private String trustStorePassword = null;
    private String keyStoreLocation = null;
    private String keyStorePassword = null;

    /**
     * Constuctor for bidirectional authentication connections.
     * <p/>
     * Should not be used except for by the ConnectionFactory.getConnectionDescription function.
     */
    public SecureConnectionDescription() {
        log.debug("Generating bidirectional secure connection description.");
    }

    /* (non-Javadoc)
      * @see org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription#isSecure()
      */
    public boolean isSecure() {
        return true;
    }

    /**
     * Should only be used by the factory.
     * <p/>
     * Currently bidirectional authentication is true.
     */
    public boolean complete() {
        boolean complete = super.complete();
        //If there is an invoked level, the description is not finished processing.
        if (invokedLevel >= 1) {
            return false;
        }
//     one directional or no authentication may work.
//		if (( (keyStorePassword != null && keyStoreLocation != null) ||
//			  (keyStorePassword == null && keyStoreLocation == null) ) &&
//			( (trustStorePassword != null && trustStoreLocation != null) ||
//			  (trustStorePassword == null && trustStoreLocation == null) ))
        if (((keyStorePassword != null && keyStoreLocation != null)) &&
                ((trustStorePassword != null && trustStoreLocation != null))) {
            this.complete = complete;
        } else {
            log.warn("Attempt to complete invalid secure connection description.");
            this.complete = false;
        }

        return this.complete;
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setKeyStore(String keyStoreLocation) {
        if (!complete) {
            this.keyStoreLocation = keyStoreLocation;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setKeyStorePassword(String keyStorePassword) {
        if (!complete) {
            this.keyStorePassword = keyStorePassword;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setTrustStore(String trustStoreLocation) {
        if (!complete) {
            this.trustStoreLocation = trustStoreLocation;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }

    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setTrustStorePassword(String trustStorePassword) {
        if (!complete) {
            this.trustStorePassword = trustStorePassword;
        } else {
            log.warn("Connection Descriptor setter used outside of factory.");
        }
    }


    /**
     * Use to get the URL of the keystore described in this connection description. <p />
     * <p/>
     * Most likely folder/keystore or something like that.  Should be local.
     *
     * @return The location of this keystore as a URL class.
     */
    public URL getKeyStore() {
        URL keyStore = null;
        if (keyStoreLocation != null) {
            try {
                keyStore = new URL("file:" + keyStoreLocation);
            }
            catch (Exception e) {
                log.error("Keystore has a malformed name: " + keyStoreLocation, e);
            }
        }
        return keyStore;
    }

    public String getKeyStoreString() {
        return keyStoreLocation;
    }

    /**
     * Use to get the URL of the truststore described in this connection description. <p />
     * <p/>
     * Most likely folder/truststore or something like that.  Should be local.
     *
     * @return The location of this truststore as a URL class.
     */
    public URL getTrustStore() {
        URL trustStore = null;
        if (trustStoreLocation != null) {
            try {
                trustStore = new URL("file:" + trustStoreLocation);
            }
            catch (Exception e) {
                log.error("Truststore has a malformed name: " + trustStoreLocation, e);
            }
        }
        return trustStore;
    }

    public String getTrustStoreString() {
        return trustStoreLocation;
    }

    /**
     * Returns the password to use for this keystore. <p />
     * <p/>
     * Note that for now it is assumed that the key has the
     * same password.
     *
     * @return The password for the key and the keystore.
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * Returns the password to use for this truststore. <p />
     *
     * @return The password for the truststore.
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }
}
