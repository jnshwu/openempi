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
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader;

/**
 * A simple class for dealing with certificate management. <p />
 * <p/>
 * This is a single point of entry for the entire suite of ssl
 * key and certificate handling functions.  Basically you should
 * use it as a static class for obtaining KeyManager s and
 * TrustManager s.  KeyManagers hold keys and as such require
 * passwords.  TrustManagers on the other hand only hold certificates
 * and as such do not require passwords.  You should also use it
 * to obtain the KeyStore needed to generate the KeyManager s and
 * TrustManager s.  <p />
 * <p/>
 * Once everything is complete, this should only be used by the
 * ConnectionFactory and should never be called directly. <p />
 * <p/>
 * (Actually that means that these should all be protected and
 * not public, but I think that would break the unit tests.) <p />
 *
 * @author Josh Flachsbart
 */
public class ConnectionCertificateHandler {

	private static final Log log = LogFactory.getLog(ConnectionCertificateHandler.class);

    /**
     * Creates a key/trust store and loads in the corresponding file.
     */
    public static KeyStore createKeyStore(final URL url, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }
        log.debug("Initializing key store");
        KeyStore keystore = null;
        if (url.getFile().endsWith(".p12")) {
            keystore = KeyStore.getInstance("pkcs12");
        } else {
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        }
        keystore.load(url.openStream(), password != null ? password.toCharArray() : null);
        return keystore;
    }

    /**
     * Creates keymanagers from a keystore.
     */
    public static KeyManager[] createKeyManagers(final KeyStore keystore, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        log.debug("Initializing key manager");
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, password != null ? password.toCharArray() : null);
        return kmfactory.getKeyManagers();
    }

    /**
     * Creates trustmanagers from a truststore.
     */
    public static TrustManager[] createTrustManagers(final KeyStore keystore, SecureConnectionDescription scd)
            throws KeyStoreException, NoSuchAlgorithmException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        log.debug("Initializing trust manager");
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(keystore);
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        for (int i = 0; i < trustmanagers.length; i++) {
            if (trustmanagers[i] instanceof X509TrustManager) {
                trustmanagers[i] = new LoggedX509TrustManager((X509TrustManager) trustmanagers[i], scd);
            }
        }
        return trustmanagers;
    }

    /**
     * For debuging only.  Prints out keystore certificate chain.
     *
     * @param keystore Keystore to print out.
     * @throws KeyStoreException If the keystore is broken.
     */
    public static void printTrustCerts(KeyStore keystore) throws KeyStoreException {
        Enumeration<String> aliases = keystore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            String message = "Trusted certificate '" + alias + "':";
            Certificate trustedcert = keystore.getCertificate(alias);
            if (trustedcert != null && trustedcert instanceof X509Certificate) {
                X509Certificate cert = (X509Certificate) trustedcert;
                message += "\n  Subject DN: " + cert.getSubjectDN();
                message += "\n  Signature Algorithm: " + cert.getSigAlgName();
                message += "\n  Valid from: " + cert.getNotBefore();
                message += "\n  Valid until: " + cert.getNotAfter();
                message += "\n  Issuer: " + cert.getIssuerDN();
            }
            log.info(message);
        }
    }

    /**
     * For debuging only.  Prints out keystore certificate chain.
     *
     * @param keystore Keystore to print out.
     * @throws KeyStoreException If the keystore is broken.
     */
    public static void printKeyCertificates(KeyStore keystore) throws KeyStoreException {
        Enumeration<String> aliases = keystore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate[] certs = keystore.getCertificateChain(alias);
            if (certs != null) {
                String message = "Certificate chain '" + alias + "':";
                int i = 1;
                for (Certificate cert : certs) {
                    if (cert instanceof X509Certificate) {
                        X509Certificate Xcert = (X509Certificate) cert;
                        message += "\n Certificate " + i++ + ":";
                        message += "\n  Subject DN: " + Xcert.getSubjectDN();
                        message += "\n  Signature Algorithm: " + Xcert.getSigAlgName();
                        message += "\n  Valid from: " + Xcert.getNotBefore();
                        message += "\n  Valid until: " + Xcert.getNotAfter();
                        message += "\n  Issuer: " + Xcert.getIssuerDN();
                    }
                }
                log.info(message);
            }
        }
    }

    public static void main(String[] args) {
        try {
            //ConnectionCertificateHandler cch = new ConnectionCertificateHandler();
            KeyStore ks = ConnectionCertificateHandler.createKeyStore(new URL("file:certs/keystore"), "password");
            ConnectionCertificateHandler.printKeyCertificates(ks);
            KeyManager[] kms = ConnectionCertificateHandler.createKeyManagers(ks, "password");
            System.out.println("Printing all key managers:");
            for (KeyManager km : kms) {
                System.out.println(km.toString());
            }
            KeyStore ts = ConnectionCertificateHandler.createKeyStore(new URL("file:certs/truststore"), "password");
            ConnectionCertificateHandler.printTrustCerts(ts);
            TrustManager[] tms = ConnectionCertificateHandler.createTrustManagers(ts, null);
            System.out.println("Printing all trust managers:");
            for (TrustManager tm : tms) {
                System.out.println(tm.toString());
            }

        } catch (Exception e) {
            System.out.println("Top Level Error: " + e);
        }
    }

}
