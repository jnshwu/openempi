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

package org.openhealthtools.openexchange.actorconfig;

import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.ACTOR;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.AUDITTRAIL;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CODETYPE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CONNECTION;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CONSUMER;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.DESCRIPTION;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.ENUMMAP;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.ID;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.IDENTIFIER;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.INCLUDEFILE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.ISSERVER;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.NAME;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.PROPERTY;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.PROPERTYSET;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.QUERY;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.RETRIEVE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.STRINGMAP;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.SUBMIT;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.TRANSACTIONS;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.TRANSACTIONSSET;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.TYPE;
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
import org.openhealthtools.openexchange.actorconfig.net.ConnectionFactory;
import org.openhealthtools.openexchange.actorconfig.net.DescriptionLoader;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.actorconfig.net.StandardConnectionDescription;
import org.openhealthtools.openexchange.utils.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class loads actor description configuration files and creates
 * a set of ActorDescription objects. All valid elements are listed as 
 * follows:  
 * <p>
 * <pre>
 * &lt;Actor name="" type="" (required and system defined)&gt;
 *    &lt;Connection name="" (required) type=""(required and system defined) isServer="true/false" (optional)/&gt; 
 *      
 *    &lt;TransactionsSet type=""(required and system defined)&gt;
 *       &lt;Transactions id=""(required) submit="" query="" retrieve=""/&gt; #Defines connections and at least one is required. 
 *       ...
 *    &lt;/TransactionsSet&gt;
 *    
 *    &lt;Property name="" value=""/&gt;   
 *    
 *    &lt;PropertySet name=""/&gt;
 *      &lt;Entry name="" value=""/&gt;
 *      ...
 *    &lt;/PropertyeSet&gt;      
 *    
 *    &lt;Identifier name=""&gt;
 *    	&lt;NamespaceId&gt; &lt;/NamespaceId&gt;
 *      &lt;UniversalId&gt; &lt;/UniversalId&gt;
 *      &lt;UniversalIdType&gt; &lt;/UniversalIdType&gt;
 *    &lt;/Identifier&gt;
 *    
 *    &lt;StringMap name="" &gt;
 *       &lt;Entry string="" code="" /&gt;
 *       ...
 *    &lt;/StringMap&gt;   
 *    
 *    &lt;EnumMap calss="" &gt;
 *       &lt;Entry enum="" code="" /&gt;
 *       ...
 *    &lt;/StringMap&gt;   
 *    
 *    &lt;CodeType name="" classScheme="" &gt;
 *       &lt;Code code="" display="" codingScheme="" /&gt;
 *       ...
 *    &lt;/CodeType&gt;   
 *    
 *    &lt;IncludeFile name="" &gt;
 * &lt;/Actor&gt;    
 * </pre>
 * 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ActorDescriptionLoader extends DescriptionLoader {
    /* Logger for debugging messages */
	private static final Log log = LogFactory.getLog(ActorDescriptionLoader.class);

    /**
     * Loads actor descriptions from the given filename.  The filename
     * must point to an XML configuration file.
     *
     * @param filename The XML actor configuration file to load
     * @return A list of actor descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     * @throws IheConfigurationException    When IHE Configuration contains invalid element or attribute
     */
    public static List<IActorDescription> loadActorDescriptions(String filename)
            throws SAXException, IOException, ParserConfigurationException, IheConfigurationException {
        return loadActorDescriptions(new File(filename));
    }

    /**
     * Loads actor descriptions from the given File object.  The File
     * must point to an XML configuration file.
     *
     * @param file The Java File object pointing to the XML configuration file
     * @return A list of actor descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     * @throws IheConfigurationException    When IHE Configuration contains invalid element or attribute
     */
    public static List<IActorDescription> loadActorDescriptions(File file) 
    throws SAXException, IOException, ParserConfigurationException, IheConfigurationException {
        ArrayList<IActorDescription> allDescriptions = new ArrayList<IActorDescription>();
        addActorDescriptions(allDescriptions, file);
        return allDescriptions;
    }

    /**
     * Processes a configuration file containing actor descriptions and add any
     * actors found to the list of actor descriptions passed in.
     *
     * @param descriptions The list of actor descriptions to be extended
     * @param file         The configuration file holding more actor descriptions
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     * @throws IheConfigurationException    When IHE Configuration contains invalid element or attribute
     */
    private static void addActorDescriptions(List<IActorDescription> descriptions, File file)
            throws SAXException, IOException, ParserConfigurationException, IheConfigurationException {
        // Create a builder factory and a builder, and get the document.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        Document doc = factory.newDocumentBuilder().parse(file);
        // Get all the actor descriptions from the root node.
        NodeList allDescriptionElements = doc.getDocumentElement().getChildNodes();
        // Process each one
        for (int elementIndex = 0; elementIndex < allDescriptionElements.getLength(); elementIndex++) {
            Node currentDescriptionElement = allDescriptionElements.item(elementIndex);
            String name = currentDescriptionElement.getNodeName();
            if (name.equalsIgnoreCase(ACTOR)) {
                // An actor description, process it
                IActorDescription currentDescription = processDescriptionNode(currentDescriptionElement, file);
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
                        addActorDescriptions(descriptions, includeFile);
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
     * Processes an actor description from an XML node.
     * 
     * @param description the node that contains the actor description details
     * @param parent the parent file from where the node is obtained
     * @return the actor description to process
     */
    static IActorDescription processDescriptionNode(Node description, File parent)
    throws IheConfigurationException {
        ActorDescription actor = null;
        // Build the right sort of connection
        String name = description.getNodeName();
        if (name.equalsIgnoreCase(ACTOR)) {
            actor = new ActorDescription();
        } else {
            logUnexpectedTagWarning(name, null);
        }
        // Load all of its properties from the XML
        if (actor != null) {
            actor.setName(getAttributeValue(description, NAME));
            actor.setType(getAttributeValue(description, TYPE));
            processDescription(actor, description.getChildNodes(), parent);
        }
        // Done
        return actor;
    }

    /**
     * Processes a single actor description DOM object and update the actor
     * description object it is describing.
     *
     * @param actor      The actor description object being described
     * @param descriptionNodes The XML DOM description being processed
     * @param configFile           The file that holds this description
     */
    private static void processDescription(ActorDescription actor, NodeList descriptionNodes, File configFile) 
    throws IheConfigurationException {
        actor.invoke();
        
        String actorName = actor.getName();
        // Loop over every item in the connection description XML
        for (int valueIndex = 0; valueIndex < descriptionNodes.getLength(); valueIndex++) {
            Node item = descriptionNodes.item(valueIndex);
            if (!(item instanceof Element)) continue;
            
            String itemName = item.getNodeName();
			if (itemName.equalsIgnoreCase(CONNECTION)) {
				// It is a connection element, get out the connection description
				IConnectionDescription connection = null;
				//Process the connection type
				String connectionType = getAttributeValue(item, TYPE);
				if (connectionType == null) connectionType = getChildNodeAsText(item, TYPE);
				if (connectionType == null) {
					throwIheConfigurationException("Connection element in actor '" + actorName + "' with no '" + TYPE + "' attribute or element", configFile);					
				}

				//Process the connection name
				String connectionName = getAttributeValue(item, NAME);
				if (connectionName == null) connectionName = getChildNodeAsText(item, NAME);
				if (connectionName == null) {
					throwIheConfigurationException("Connection element in actor '" + actorName + "' with no '" + NAME + "' attribute or element", configFile);					
				}else {
					// Pull out the connection
					if (connection != null) {
						logConfigurationWarning("Duplicate '" + NAME+ "' connection attributes", configFile);
					} else {
						connection = ConnectionFactory.getConnectionDescription(connectionName);
						if (connection == null) {
							throwIheConfigurationException("Connection '" + connectionName + "' in actor '" + actorName + "' is not defined", configFile);
						}
					}
				}
								
				//Process the optional connection isServer 
				String server = getAttributeValue(item, ISSERVER);
				if (server == null) server = getChildNodeAsText(item, ISSERVER);
				boolean isServer = Boolean.parseBoolean(server);
				
				((StandardConnectionDescription)connection).setType( connectionType );
				((StandardConnectionDescription)connection).setServer(isServer);
				actor.addConnectionDescription(connection);
			} else if (itemName.equalsIgnoreCase(DESCRIPTION)) {
				// A description of this actor for GUI presentation
				String desc = getAttributeValue(item, VALUE);
				if (desc == null) desc = getNodeAsText(item);
				if (!StringUtil.goodString(desc)) desc = actorName;
				actor.setDescription( desc );				
			} else if (itemName.equalsIgnoreCase(AUDITTRAIL)) {
				// An ATNA logger definition
				String logName = getAttributeValue(item, NAME);
				// Allow for backward compatibility, look for the consumer attribute
				if (logName == null ) logName = getAttributeValue(item, CONSUMER);
				if (logName == null) {
					logConfigurationWarning(AUDITTRAIL + " element with no '" + NAME + "' attribute", configFile);
				}
				IConnectionDescription logConnection = ConnectionFactory.getConnectionDescription(logName);
				if (logConnection == null) {
					throwIheConfigurationException( AUDITTRAIL + " connection '" + logName + "' in actor '" + actorName + "' is not defined", configFile);
				}
				actor.addAuditLogConnection(logConnection);
			} else if (itemName.equalsIgnoreCase(CODETYPE)) {
                // A set of ebXML Repository Classification codes
                processCodingScheme(actor, item);
            } else if (itemName.equalsIgnoreCase(ENUMMAP)) {
                // An enum map from Connect ENUM types to IHE Affinity Domain Codes.
                processEnumMap(actor, item);
            } else if (itemName.equalsIgnoreCase(STRINGMAP)) {
                // An enum map from Connect string types to IHE Affinity Domain Codes.
                processStringMap(actor, item);
            } else if (itemName.equalsIgnoreCase(PROPERTYSET)) {
                // A set of related properties needed to map to the IHS Affinity Domain.
                processPropertySet(actor, item);
            } else if (itemName.equalsIgnoreCase(IDENTIFIER)) {
                // A set of related properties needed to map to the IHS Affinity Domain.
                processIdentifier(actor, item);
            } else if (itemName.equalsIgnoreCase(PROPERTY)) {
                // A property to associate with this connection
                String propertyName = getAttributeValue(item, NAME);
                String propertyValue = getAttributeValue(item, VALUE);
                if (propertyValue == null) {
                    propertyValue = getNodeAsText(item);
                }
                if (propertyName != null) {
                    actor.setProperty(propertyName, propertyValue);
                } else {
                    logMissingAttributeWarning(itemName, NAME, actor);
                }
            } else if (itemName.equalsIgnoreCase(TRANSACTIONSSET)) {
                // A set of transactions connections
            	processTransactionsSet(actor, item, configFile);
            } else if (itemName.equalsIgnoreCase(INCLUDEFILE)) {
                // A nested file inclusion, process it
                String filename = getAttributeValue(item, NAME);
                if (filename == null) {
                    filename = getNodeAsText(item);
                }
                if (filename != null) {
                    File includeFile = new File(configFile.getParentFile(), filename);
                    processDescriptionIncludeFile(actor, includeFile);
                } else {
                    logMissingAttributeWarning(itemName, NAME, actor);
                }
			} else {
				// Not an element we know about
				logConfigurationWarning("Unknown actor XML element '" + itemName + "'", configFile);
			}
        }
        // Done, finish processing the description
        actor.complete();
    }

    /**
     * Processes a configuration file included within the description of a single actor.
     *
     * @param description The actor description object being described
     * @param file        The included configuration file describing that actor
     * @return True if the included file was processed without error
     */
    public static boolean processDescriptionIncludeFile(ActorDescription description, File file) {
        try {
            return processDescriptionIncludeFile(description, new FileInputStream(file), file);
        } catch (Exception e) {
            logIncludeFileError(file.getAbsolutePath(), description, e);
            return false;
        }
    }

    /**
     * Processes a configuration input stream that describes a single actor.
     *
     * @param description The actor description being described by the input stream
     * @param stream      The input stream holding configuration information
     * @param parent      The file holding this description
     * @return True if the input stream is processed without error
     * @throws SAXException                 When the configuration file contains invalid XML
     * @throws IOException                  When there is a problem reading the file
     * @throws ParserConfigurationException When no XML parser can be found
     * @throws IheConfigurationException    When IHE Configuration contains invalid element or attribute
     * 
     */
    public static boolean processDescriptionIncludeFile(ActorDescription description, InputStream stream, File parent)
            throws SAXException, IOException, ParserConfigurationException, IheConfigurationException {
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
	 * Logs a configuration file warning.
	 * 
	 * @param message the warning to log
	 */
    static void logConfigurationWarning(String message, File configFile) {
		String filename = null;
		if (configFile != null) filename = configFile.getAbsolutePath();
		String warning = message;
		if (filename != null) warning = message + " in configuration file \"" + filename + "\"";
		log.warn(warning);
	}
   
	/**
	 * Throws a new IheConfigurationException.
	 * 
	 * @param message the message to include in the exception
	 * @throws IheConfigurationException The exception
	 */
	public static void throwIheConfigurationException(String message, File configFile) throws IheConfigurationException {
		String filename = null;
		if (configFile != null) filename = configFile.getAbsolutePath();
		String error = message;
		if (filename != null) error = message + " in configuration file \"" + filename + "\"";
		log.error(error);
		throw new IheConfigurationException(message);
	}
    
    /**
     * Processes a &lt;TransactionsSet&gt; entry and add its contents to the
     * actor or connection configuration.
     * <p/>
     * <pre>
     * XML: TransactionsSet type=""(required)
     *       Transactions id=""(required) submit="" query="" retrieve=""
     *       ...
     * </pre>
     *
     * @param description  The actor description to add the transactions set to
     * @param transactionsSet The DOM element of the "TransactionsSet" entry
     */
    protected static void processTransactionsSet(ActorDescription description, Node transactionsSet, File configFile) 
    throws IheConfigurationException {
        String setType = getAttributeValue(transactionsSet, TYPE);
        if (setType == null) {
            // No transaction set type, can't save values
			throwIheConfigurationException(TRANSACTIONSSET + " element in actor '" + description.getName() + "' with no '" + TYPE +"' attribute or element", configFile);					
        } else {
            // Create a transactions set for this entry
        	TransactionsSet theSet = new TransactionsSet(setType);
            description.addTransactionsSet(theSet);
            // Now add the new entries
            NodeList entries = transactionsSet.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase(TRANSACTIONS)) {
                	boolean okay = false;
                	
                    String id = getAttributeValue(entry, ID);
                    if (id == null) id = getChildNodeAsText(entry, ID);
                    if (id == null) {
                        // id is required
            			throwIheConfigurationException(TRANSACTIONSSET + " element in actor '" + description.getName() + "' with no '" + TYPE + "' attribute or element", configFile);					
                    }
                    
                    Transactions transactions = new Transactions();
                    transactions.setId( id );
                    theSet.addValue(id, transactions);
                   
                    String submitName = getAttributeValue(entry, SUBMIT);
                    if (submitName == null) submitName = getChildNodeAsText(entry, SUBMIT);
                	IConnectionDescription submitConnection = null;                	
                    if (StringUtil.goodString( submitName )){
    				    submitConnection = ConnectionFactory.getConnectionDescription(submitName);                    
    					if (submitConnection != null) {
    						okay = true;
    						transactions.setSubmit(submitConnection);
    					}
                    }

                    String queryName = getAttributeValue(entry, QUERY);
                    if (queryName == null) queryName = getChildNodeAsText(entry, QUERY);
                	IConnectionDescription queryConnection = null;                	
                    if (StringUtil.goodString( queryName )){
    				    queryConnection = ConnectionFactory.getConnectionDescription(queryName);                    
    					if (queryConnection != null) {
    						okay = true;
    						transactions.setQuery(queryConnection);
    					}
                    }

                    String retrieveName = getAttributeValue(entry, RETRIEVE);
                    if (retrieveName == null) retrieveName = getChildNodeAsText(entry, RETRIEVE);
                	IConnectionDescription retrieveConnection = null;                	
                    if (StringUtil.goodString( retrieveName )){
    				    retrieveConnection = ConnectionFactory.getConnectionDescription(retrieveName);                    
    					if (retrieveConnection != null) {
    						okay = true;
    						transactions.setRetrieve(retrieveConnection);
    					}
                    }
                    
                    if (!okay) {
                      log.warn("At least one connection is required in Transactions element in actor " + description.getName());
                    }
                    
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), description);
                }
            }
        }
    }
	
}
