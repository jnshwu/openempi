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

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMEUtil;

public class MailConnection {
    /**Commons Log*/
	private static final Log log = LogFactory.getLog(MailConnection.class);

    IConnectionDescription description = null;
    PropertySet smtp = null;
    PropertySet pop3 = null;
    PropertySet senderKeystore = null;
    Session session = null;
    Transport transport = null;
    Store store = null;
    Folder inbox = null;
    String senderKeystoreFile = null;
    String senderKeystorePassword = null;
    String senderKeyAlias = null;

    MailConnection(IConnectionDescription description) {
        this.description = description;
        // Create a connection for sending the message
        Properties props = new Properties();
        // fill props with any information
        session = Session.getInstance(props, null);        // Make the call and catch the output
        smtp = description.getPropertySet("smtp");
        pop3 = description.getPropertySet("pop3");
        senderKeystore = description.getPropertySet("senderKeystore");
        try {
            transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            log.error("Transport misconfigured, no smtp provider.", e);
            transport = null;
            smtp = null;
        }
        try {
            store = session.getStore("pop3");
        } catch (NoSuchProviderException e) {
            log.error("Transport misconfigured, no smtp provider.");
            transport = null;
            smtp = null;
        }
    }

    public void sendMessage(Message message) throws MessagingException {
        if (transport != null && smtp != null) {
            transport.connect(smtp.getValue("HOSTNAME"), smtp.getValue("USERNAME"), smtp.getValue("PASSWORD"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } else {
            log.error("Transport and smtp must be set before sending messages.");
            throw new MessagingException("Attempt to send to invalid smtp connection.");
        }
    }

    public Message[] retrieveAllMessages() throws MessagingException {
        Message messages[] = null;
        if (store != null && pop3 != null) {
            if (inbox == null) {
                store.connect(pop3.getValue("HOSTNAME"), pop3.getValue("USERNAME"), pop3.getValue("PASSWORD"));
                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
            }
            messages = inbox.getMessages();
        } else {
            log.error("Store and pop3 must be set before retrieving messages.");
            throw new MessagingException("Attempt to retrieve from invalid pop3 connection.");
        }
        return messages;
    }

    // Must call when done reading data from messages.
    public void finishedWithMessages() throws MessagingException {
        inbox.close(false);
        store.close();
    }

    public MimeBodyPart decryptMessage(Message message) throws MessagingException {

        try {
            /* Add BC */
            Security.addProvider(new BouncyCastleProvider());
            // Open the key store
            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            ks.load(new FileInputStream(getSenderKeystoreFile()), getSenderKeystorePassword().toCharArray());

            // find the certificate for the private key and generate a
            // suitable recipient identifier.
            X509Certificate cert = (X509Certificate) ks.getCertificate(getSenderKeyAlias());
            RecipientId recId = new RecipientId();

            recId.setSerialNumber(cert.getSerialNumber());
            recId.setIssuer(cert.getIssuerX500Principal().getEncoded());

            SMIMEEnveloped m = new SMIMEEnveloped((MimeMessage) message);
            RecipientInformationStore recipients = m.getRecipientInfos();
            // TODO figure out why this doesn't work...
            //RecipientInformation        recipient = recipients.get(recId);
            RecipientInformation recipient = (RecipientInformation) recipients.getRecipients().iterator().next();

            Key key = ks.getKey(getSenderKeyAlias(), getSenderKeystorePassword().toCharArray());
            byte[] byteContent = recipient.getContent(key, "BC");
            MimeBodyPart res = SMIMEUtil.toMimeBodyPart(byteContent);
            return res;

        } catch (Exception e) {
            log.error("Problem decrypting message: ", e);
            throw new MessagingException(e.getMessage());
        }
    }

    public MimeBodyPart unSignMessage(Message message) throws MessagingException {

        try {
            SMIMESigned s = new SMIMESigned((MimeMultipart) message.getContent());
            MimeBodyPart content = s.getContent();
            return content;
        } catch (Exception e) {
            log.error("Problem decrypting message: ", e);
            throw new MessagingException(e.getMessage());
        }
    }


    public MimeMessage getNewMessage() {
        return new MimeMessage(session);
    }

    public Session getSession() {
        return session;
    }

    public String getSenderKeystoreFile() {
        return senderKeystore.getValue("FILE");
    }

    public String getSenderKeystorePassword() {
        return senderKeystore.getValue("PASSWORD");
    }

    public String getSenderKeyAlias() {
        return senderKeystore.getValue("ALIAS");
    }
}
