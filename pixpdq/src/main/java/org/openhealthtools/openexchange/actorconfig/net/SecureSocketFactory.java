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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Used by the connection factory to create customized secure sockets.
 * <p>
 * SecureSocketFactory can be used to validate the identity of the HTTPS 
 * server against a list of trusted certificates and to authenticate to the HTTPS 
 * server using a private key. 
 * </p>
 * 
 * <p>
 * SecureSocketFactory will enable server authentication when supplied with
 * a {@link KeyStore truststore} file containg one or several trusted certificates. 
 * The client secure socket will reject the connection during the SSL session handshake 
 * if the target HTTPS server attempts to authenticate itself with a non-trusted 
 * certificate.
 * </p>
 * 
 * <p>
 * Use JDK keytool utility to import a trusted certificate and generate a truststore file:    
 *    <pre>
 *     keytool -import -alias "my server cert" -file server.crt -keystore misys.jks
 *    </pre>
 * Alternately, generate a pkcs12 keychain and use that as a truststore:
 * 	  <pre>
 *     openssl pkcs12 -in provided.cert -export -out misys.p12 -name provided
 * 	  </pre>
 * </p>
 * 
 * <p>
 * SecureSocketFactory will enable client authentication when supplied with
 * a {@link KeyStore keystore} file containg a private key/public certificate pair. 
 * The client secure socket will use the private key to authenticate itself to the target 
 * HTTPS server during the SSL session handshake if requested to do so by the server. 
 * The target HTTPS server will in its turn verify the certificate presented by the client
 * in order to establish client's authenticity
 * </p>
 * 
 * <p>
 * Use the following sequence of actions to generate a keystore file
 * </p>
 * <p>
 * Use JDK keytool utility to generate a new key, make sure the store has the same 
 * password as the key.  Then check to make sure that the key generation took.  You 
 * can use a keystore and a truststore, or one that does both.
 * </p>
 * <pre>
 *  keytool -genkey -v -alias "misys" -validity 365 -keystore misys.jks
 *  keytool -list -v -keystore my.keystore
 * </pre> 
 * <p>
 * The lame keytool tool can't import private keys, so if you need to use another
 * privte key (e.g. one given to you for IHE connectathon) then you need to use 
 * a different system.  Change it to pkcs12 format and make sure that you have the
 * filname end in .p12 then the factory wil deal with it correctly.  How to use OpenSSL
 * to generate the appropriate files:
 * </p>
 * <pre>
 *  openssl pkcs12 -in provided.cert -inkey provided.key -export -out misys.p12 -name provided
 * </pre>
 * <p>
 * That makes a single keystore for both the private key and the public certs.
 * </p>
 * <p>
 * Example of using custom protocol socket factory for a specific host:
 *     <pre>
 *     Protocol authhttps = new Protocol("https",  
 *          new SecureSocketFactory(
 *              new URL("file:my.keystore"), "mypassword",
 *              new URL("file:my.truststore"), "mypassword"), 443); 
 *
 *     HttpClient client = new HttpClient();
 *     client.getHostConfiguration().setHost("localhost", 443, authhttps);
 *     // use relative url only
 *     GetMethod httpget = new GetMethod("/");
 *     client.executeMethod(httpget);
 *     </pre>
 * </p>
 * <p>
 * Example of using custom protocol socket factory per default instead of the standard one:
 *     <pre>
 *     Protocol authhttps = new Protocol("https",  
 *          new SecureSocketFactory(
 *              new URL("file:my.keystore"), "mypassword",
 *              new URL("file:my.truststore"), "mypassword"), 443); 
 *     Protocol.registerProtocol("https", authhttps);
 *
 *     HttpClient client = new HttpClient();
 *     GetMethod httpget = new GetMethod("https://localhost/");
 *     client.executeMethod(httpget);
 *     </pre>
 * </p>
 * 
 * @author Josh Flachsbart
 * @version 2.0 - Oct 25, 2005
 */

public class SecureSocketFactory implements SecureProtocolSocketFactory {

	/** Log object for this class. */
	private static final Log LOG = LogFactory.getLog(SecureSocketFactory.class);

	private URL keystoreUrl = null;
	private String keystorePassword = null;
	private URL truststoreUrl = null;
	private String truststorePassword = null;
	private SSLContext sslcontext = null;
	private SecureConnectionDescription scd;

	/**
	 * Constructor for HttpStreamHandler. Either a keystore or truststore file
	 * must be given. Otherwise SSL context initialization error will result.
	 *
     * @param scd The secure connection description
	 */
	public SecureSocketFactory(SecureConnectionDescription scd) {
		super();

		this.keystoreUrl = scd.getKeyStore();
		this.keystorePassword = scd.getKeyStorePassword();
		this.truststoreUrl = scd.getTrustStore();
		this.truststorePassword = scd.getTrustStorePassword();
		this.scd = scd;
	}

	private SSLContext createSSLContext() throws IOException {
		try {
            LOG.info("Attempting to create ssl context.");
            KeyManager[] keymanagers = null;
			TrustManager[] trustmanagers = null;
			if (this.keystoreUrl != null) {
				KeyStore keystore = ConnectionCertificateHandler.createKeyStore(this.keystoreUrl, this.keystorePassword);
				if (LOG.isDebugEnabled()) ConnectionCertificateHandler.printKeyCertificates(keystore);
				keymanagers = ConnectionCertificateHandler.createKeyManagers(keystore, this.keystorePassword);
			}
			if (this.truststoreUrl != null) {
				KeyStore keystore = ConnectionCertificateHandler.createKeyStore(this.truststoreUrl, this.truststorePassword);
				if (LOG.isDebugEnabled()) ConnectionCertificateHandler.printTrustCerts(keystore);
				trustmanagers = ConnectionCertificateHandler.createTrustManagers(keystore, this.scd);
			}
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(keymanagers, trustmanagers, null);
			return sslcontext;
		} catch (NoSuchAlgorithmException e) {
			LOG.error("NSA: " + e.getMessage(), e);
			throw new IOException("Unsupported algorithm exception: " + e.getMessage());
		} catch (KeyStoreException e) {
			LOG.error("Key Store: " + e.getMessage(), e);
			throw new IOException("Keystore exception: " + e.getMessage());
		} catch (GeneralSecurityException e) {
			LOG.error("General: " + e.getMessage(), e);
			throw new IOException("Key management exception: " + e.getMessage());
		} catch (IOException e) {
			LOG.error("I/O exception: " + e.getMessage(), e);
			throw new IOException("I/O error reading keystore/truststore file: " + e.getMessage());
		}
	}

	private SSLContext getSSLContext() throws IOException{
		if (this.sslcontext == null) {
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}

	private void setAtnaProtocols(SSLSocket secureSocket)
	{
		secureSocket.setEnabledProtocols(new String[] { "TLSv1" });
		String[] strings = { //retired per CP 478 "SSL_RSA_WITH_NULL_SHA", 
				     "TLS_RSA_WITH_AES_128_CBC_SHA",
				     "SSL_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_RSA_WITH_DES_CBC_SHA"};
		secureSocket.setEnabledCipherSuites(strings);
		// Useful debugging information:
		//secureSocket.setSoTimeout(1000);
		//String[] strings = secureSocket.getSupportedCipherSuites();
		//for (String s: strings) System.out.println(s);
		//strings = secureSocket.getEnabledCipherSuites();
		//for (String s: strings) System.out.println(s);
	}
	
	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int,java.net.InetAddress,int)
	 */
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) 		
	{
		Socket socket = null;
		try {
			socket = getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
			setAtnaProtocols((SSLSocket) socket);
		} catch (java.net.ConnectException e) {
			LOG.error("Connection was refused when connecting to socket.", e);
		} catch (IOException e) {
			LOG.error("I/O problem creating socket.", e);
		} catch (Exception e) {
			LOG.error("Problem creating socket.", e);			
		}
		return socket;
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)
	 */
	public Socket createSocket(String host, int port) 
	{
		Socket socket = null;
		try {
			socket = getSSLContext().getSocketFactory().createSocket(host, port);
			setAtnaProtocols((SSLSocket) socket);
		} catch (java.net.ConnectException e) {
			LOG.error("Connection was refused when connecting to socket.", e);
		} catch (IOException e) {
			LOG.error("I/O problem creating socket.", e);
		} catch (Exception e) {
			LOG.error("Problem creating socket.", e);			
		}
		return socket;
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)
	 */
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) 
	{
		Socket lsocket = null;
		try {
			lsocket = getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
			setAtnaProtocols((SSLSocket) lsocket);
		} catch (java.net.ConnectException e) {
			LOG.error("Connection was refused when connecting to socket.", e);
		} catch (IOException e) {
			LOG.error("I/O problem creating socket.", e);
		} catch (Exception e) {
			LOG.error("Problem creating socket.", e);			
		}
		return lsocket;
	}
	
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort, HttpConnectionParams params) {
		Socket lsocket = null;
		try {
			lsocket = getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
			setAtnaProtocols((SSLSocket) lsocket);
		} catch (java.net.ConnectException e) {
			LOG.error("Connection was refused when connecting to socket.", e);
		} catch (IOException e) {
			LOG.error("I/O problem creating socket.", e);
		} catch (Exception e) {
			LOG.error("Problem creating socket.", e);			
		}
		return lsocket;
	}

    /**
	 * Extra socket creation for servers only.
	 */
	public ServerSocket createServerSocket(int port) {
        javax.net.ssl.SSLServerSocket ss = null;
        try {
            ss = (javax.net.ssl.SSLServerSocket) getSSLContext().getServerSocketFactory().createServerSocket(port);
            ss.setNeedClientAuth( true );
    		String[] strings = { //retired per CP 478 "SSL_RSA_WITH_NULL_SHA", 
				     "TLS_RSA_WITH_AES_128_CBC_SHA",
				     "SSL_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_RSA_WITH_DES_CBC_SHA"};
            ss.setEnabledCipherSuites(strings);
        } catch (IOException e) {
            LOG.error("I/O problem creating server socket.", e);
        }
        return ss;
	}
}