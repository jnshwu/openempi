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

import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CODETYPE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.ENUMMAP;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.HOSTNAME;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.IDENTIFIER;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.INCLUDEFILE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.KEYPASS;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.KEYSTORE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.NAME;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.PORT;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.PROPERTYSET;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.SECURECONNECTION;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.STANDARDCONNECTION;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.STRINGMAP;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.TRUSTPASS;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.TRUSTSTORE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.URLPATH;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.VALUE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class loads connection description configuration files and creates
 * a set of ConnectionDescription objects.
 *
 * @author Jim Firby
 */
public class ConnectionDescriptionLoader extends DescriptionLoader {

    /* Logger for debugging messages */
	private static final Log log = LogFactory.getLog(ConnectionDescriptionLoader.class);

    /**
     * Loads connection descriptions from the given filename.  The filename
     * must point to an XML configuration file.
     *
     * @param filename The XML connection configuration file to load
     * @return A list of connection descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    public static List<IConnectionDescription> loadConnectionDescriptions(String filename)
            throws SAXException, IOException, ParserConfigurationException {
        return loadConnectionDescriptions(new File(filename));
    }

    /**
     * Loads connection descriptions from the given File object.  The File
     * must point to an XML configuration file.
     *
     * @param file The Java File object pointing to the XML configuration file
     * @return A list of connection descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    public static List<IConnectionDescription> loadConnectionDescriptions(File file) throws SAXException, IOException, ParserConfigurationException {
        ArrayList<IConnectionDescription> allDescriptions = new ArrayList<IConnectionDescription>();
        addConnectionDescriptions(allDescriptions, file);
        return allDescriptions;
    }

    /**
     * Processes a configuration file containing connection descriptions and add any
     * connections found to the list of connection descriptions passed in.
     *
     * @param descriptions The list of connection descriptions to be extended
     * @param file         The configuration file holding more connection descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    private static void addConnectionDescriptions(List<IConnectionDescription> descriptions, File file)
            throws SAXException, IOException, ParserConfigurationException {
        // Create a builder factory and a builder, and get the document.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        Document doc = factory.newDocumentBuilder().parse(file);
        // Get all the connection descriptions from the root node.
        //TODO check that the root node is NETOWRKCONFIGURATION.
        NodeList allDescriptionElements = doc.getDocumentElement().getChildNodes();
        // Process each one
        for (int elementIndex = 0; elementIndex < allDescriptionElements.getLength(); elementIndex++) {
            Node currentDescriptionElement = allDescriptionElements.item(elementIndex);
            String name = currentDescriptionElement.getNodeName();
            if (name.equalsIgnoreCase(SECURECONNECTION) || name.equalsIgnoreCase(STANDARDCONNECTION)) {
                // A connection description, process it
                IConnectionDescription currentDescription = processDescriptionNode(currentDescriptionElement, file);
                if (currentDescription != null) {
                    descriptions.add(currentDescription);
                }
            } else if (name.equalsIgnoreCase(INCLUDEFILE)) {
                // A top-level include file, it will contain more connection descriptions
                String filename = getAttributeValue(currentDescriptionElement, NAME);
                if (filename == null) {
                    filename = getNodeAsText(currentDescriptionElement);
                }
                if (filename != null) {
                    try {
                        File includeFile = new File(file.getParentFile(), filename.trim());
                        addConnectionDescriptions(descriptions, includeFile);
                    } catch (Exception e) {
                        logIncludeFileError(filename, null, e);
                    }
                } else {
                    logMissingAttributeWarning(name, NAME, null);
                }
            } else if (currentDescriptionElement instanceof Element) {
                // An XML element, if we don't know what kind, it probably shouldn't be there
                logUnexpectedTagWarning(name, null);
            }

        }
    }
    
    /**
     * Processes a connection description from an XML node.
     * 
     * @param description the node that contains the connection description details
     * @param parent the parent file from where the node is obtained
     * @return the connection description to process
     */
    static IConnectionDescription processDescriptionNode(Node description, File parent) {
        StandardConnectionDescription connection = null;
        // Build the right sort of connection
        String name = description.getNodeName();
        if (name.equalsIgnoreCase(SECURECONNECTION)) {
            connection = new SecureConnectionDescription();
        } else if (name.equalsIgnoreCase(STANDARDCONNECTION)) {
            connection = new StandardConnectionDescription();
        } else {
            logUnexpectedTagWarning(name, null);
        }
        // Load all of its properties from the XML
        if (connection != null) {
            connection.setName(getAttributeValue(description, NAME));
            processDescription(connection, description.getChildNodes(), parent);
        }
        // Done
        return connection;
    }

    /**
     * Processes a single connection description DOM object and update the connection
     * description object it is describing.
     *
     * @param description      The connection description object being described
     * @param descriptionNodes The XML DOM description being processed
     * @param parent           The file that holds this description
     */
    private static void processDescription(StandardConnectionDescription description, NodeList descriptionNodes, File parent) {
        description.invoke();
        // Loop over every item in the connection description XML
        for (int valueIndex = 0; valueIndex < descriptionNodes.getLength(); valueIndex++) {
            Node item = descriptionNodes.item(valueIndex);
            String itemName = item.getNodeName();
            // If this is a standard connection value, process is
            boolean handled = handleStandardConnectionValue(itemName, item, description);
            // If this is a secure connection value, process it
            if (!handled) {
                handled = handleSecureConnectionValue(itemName, item, description, parent);
            }
            // A connection actor value
            if (!handled) {
                if (itemName.equalsIgnoreCase(CODETYPE)) {
                    // A set of ebXML Repository Classification codes
                    processCodingScheme(description, item);
                } else if (itemName.equalsIgnoreCase(ENUMMAP)) {
                    // An enum map from Connect ENUM types to IHE Affinity Domain Codes.
                    processEnumMap(description, item);
                } else if (itemName.equalsIgnoreCase(STRINGMAP)) {
                    // An enum map from Connect string types to IHE Affinity Domain Codes.
                    processStringMap(description, item);
                } else if (itemName.equalsIgnoreCase(PROPERTYSET)) {
                    // A set of related properties needed to map to the IHS Affinity Domain.
                    processPropertySet(description, item);
                } else if (itemName.equalsIgnoreCase(IDENTIFIER)) {
                    // A set of related properties needed to map to the IHS Affinity Domain.
                    processIdentifier(description, item);
                } else if (itemName.equalsIgnoreCase(INCLUDEFILE)) {
                    // A nested file inclusion, process it
                    String filename = getAttributeValue(item, NAME);
                    if (filename == null) {
                        filename = getNodeAsText(item);
                    }
                    if (filename != null) {
                        File includeFile = new File(parent.getParentFile(), filename);
                        processDescriptionIncludeFile(description, includeFile);
                    } else {
                        logMissingAttributeWarning(itemName, NAME, description);
                    }
                } else if (itemName.equalsIgnoreCase("PROPERTY")) {
                    // A property to associate with this connection
                    String propertyName = getAttributeValue(item, NAME);
                    String propertyValue = getAttributeValue(item, VALUE);
                    if (propertyValue == null) {
                        propertyValue = getNodeAsText(item);
                    }
                    if (propertyName != null) {
                        description.setProperty(propertyName, propertyValue);
                    } else {
                        logMissingAttributeWarning(itemName, NAME, description);
                    }
                } else if (item instanceof Element) {
                    // An XML element, if we don't know what kind, it probably shouldn't be there
                    logUnexpectedTagWarning(itemName, description);
                }
            }
        }
        // Done, finish processing the description
        description.complete();
    }

    /**
     * Processes a configuration file included within the description of a single connection.
     *
     * @param description The connection description object being described
     * @param file        The included configuration file describing that connection
     * @return True if the included file was processed without error
     */
    public static boolean processDescriptionIncludeFile(StandardConnectionDescription description, File file) {
        try {
            return processDescriptionIncludeFile(description, new FileInputStream(file), file);
        } catch (Exception e) {
            logIncludeFileError(file.getAbsolutePath(), description, e);
            return false;
        }
    }

    /**
     * Processes a configuration input stream that describes a single connection.
     *
     * @param description The connection description being described by the input stream
     * @param stream      The input stream holding configuration information
     * @param parent      The file holding this description
     * @return True if the input stream is processed without error
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     */
    public static boolean processDescriptionIncludeFile(StandardConnectionDescription description, InputStream stream, File parent)
            throws SAXException, IOException, ParserConfigurationException {
        // Create a builder factory and a builder, and get the document.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        Document doc = factory.newDocumentBuilder().parse(stream);
        // Process the children of this include as if they were in the connection description
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        processDescription(description, nodes, parent);
        return true;
    }

    /**
     * Processes a configuration XML tag and see if it is a standard connection
     * description property.
     *
     * @param itemName    The tag name
     * @param item        The DOM node for the XML item
     * @param description The connection description being loaded
     * @return True if this tag was processes as a standard connection property
     */
    private static boolean handleStandardConnectionValue(String itemName, Node item, StandardConnectionDescription description) {
        boolean handled = true;
        if (itemName.equalsIgnoreCase(HOSTNAME)) {
            description.setHostname(getNodeAsText(item));
        } else if (itemName.equalsIgnoreCase(PORT)) {
            description.setPort(getNodeAsInt(item));
        } else if (itemName.equalsIgnoreCase(URLPATH)) {
            description.setUrlPath(getNodeAsText(item));
        } else if (itemName.equalsIgnoreCase(NAME)) {
            description.setName(getNodeAsText(item));
        } else {
            handled = false;
        }
        return handled;
    }

    /**
     * Processes a configuration XML tag and see if it is a secure connection
     * description property.
     *
     * @param itemName    The tag name
     * @param item        The DOM node for the XML item
     * @param description The connection description being loaded
     * @return True if this tag was processes as a secure connection property
     */
    private static boolean handleSecureConnectionValue(String itemName, Node item, StandardConnectionDescription description, File parent) {
        boolean handled = true;
        // See if we are loading a secure connection
        SecureConnectionDescription secure = null;
        if (description instanceof SecureConnectionDescription) {
            secure = (SecureConnectionDescription) description;
        }
        // Now look at the tag name
        if (itemName.equalsIgnoreCase(KEYSTORE)) {
            String filename = getNodeAsText(item);
            if (filename != null) {
                File keyFile = new File(parent.getParentFile(), filename);
                if (secure != null) {
                    secure.setKeyStore(keyFile.getAbsolutePath());
                } else {
                    log.error(KEYSTORE + " element in non secure connection.");
                }
            } else {
                logMissingAttributeWarning(itemName, NAME, description);
            }
        } else if (itemName.equalsIgnoreCase(KEYPASS)) {
            if (secure != null) {
                secure.setKeyStorePassword(getNodeAsText(item));
            } else {
                log.error(KEYPASS + " element in non secure connection.");
            }
        } else if (itemName.equalsIgnoreCase(TRUSTSTORE)) {
            String filename = getNodeAsText(item);
            if (filename != null) {
                File keyFile = new File(parent.getParentFile(), filename);
                if (secure != null) {
                    secure.setTrustStore(keyFile.getAbsolutePath());
                } else {
                    log.error(TRUSTSTORE + " element in non secure connection.");
                }
            } else {
                logMissingAttributeWarning(itemName, NAME, description);
            }
        } else if (itemName.equalsIgnoreCase(TRUSTPASS)) {
            if (secure != null) {
                secure.setTrustStorePassword(getNodeAsText(item));
            } else {
                log.error(TRUSTPASS + " element in non secure connection.");
            }
        } else {
            handled = false;
        }
        // Generate warning if needed
        if (handled && (secure == null)) {
            logUnexpectedTagWarning(itemName, description);
        }
        return handled;
    }
 
}
