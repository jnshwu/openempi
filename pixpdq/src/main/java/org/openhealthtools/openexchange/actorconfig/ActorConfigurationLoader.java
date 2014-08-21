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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openhealthtools.openexchange.actorconfig.net.ConnectionFactory;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.config.ConfigProcessorFactory;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.log.IMesaLogger;
import org.openhealthtools.openexchange.log.Log4jLogger;
import org.openhealthtools.openexchange.utils.LibraryConfig;
import org.openhealthtools.openexchange.utils.StringUtil;
import org.openhealthtools.openexchange.utils.LibraryConfig.ILogContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * This class loads an IHE Actor configuration files and creates
 * a collection of ActorDescription objects.
 * 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public abstract class ActorConfigurationLoader {
	// public tags
	public static final String ACTOR              = "Actor";
	public static final String ACTORFILE          = "ActorFile";
	public static final String AUDITTRAIL         = "AuditTrail";
	public static final String CONNECTION         = "Connection";
	public static final String CONNECTIONFILE     = "ConnectionFile";
	public static final String INCLUDEFILE        = "IncludeFile";
	
	public static final String DESCRIPTION        = "Description";
	public static final String CODETYPE           = "CodeType";
	public static final String ENUMMAP            = "EnumMap";
	public static final String STRINGMAP          = "StringMap";
	public static final String PROPERTY           = "Property";	
	public static final String PROPERTYSET        = "PropertySet";
	public static final String ENTRY     		  = "Entry";	
	public static final String TRANSACTIONSSET    = "TransactionsSet";
	public static final String TRANSACTIONS       = "Transactions";

	public static final String IDENTIFIER         = "Identifier";
	public static final String NAMESPACEID        = "NameSpaceID";	
	public static final String UNIVERSALID        = "UniversalID";	
	public static final String UNIVERSALIDTYPE    = "UniversalIDType";		

	public static final String HOSTNAME           = "HostName";
	public static final String PORT               = "Port";
	public static final String URLPATH            = "UrlPath";
	
	public static final String KEYSTORE           = "KeyStore";
	public static final String KEYPASS            = "KeyPass";
	public static final String TRUSTSTORE         = "TrustStore";
	public static final String TRUSTPASS          = "TrustPass";
	
	public static final String STANDARDCONNECTION = "StandardConnection";
	public static final String SECURECONNECTION   = "SecureConnection";
	//Attribute name	
	public static final String CLASS        = "class";
	public static final String CLASSSCHEME  = "classScheme";
	public static final String CODE         = "code";
	public static final String CODINGSCHEME = "codingScheme";
	public static final String CONSUMER = "consumer";	
	public static final String DISPLAY      = "display";
	public static final String ENUM         = "enum";	
	public static final String EXT          = "ext";	
	public static final String FILE         = "file";
	public static final String ID           = "id";	
	public static final String ISSERVER     = "isServer";	
	public static final String NAME         = "name";
	public static final String STRING       = "string";
	public static final String TYPE         = "type";
	public static final String VALUE        = "value";		
	public static final String SUBMIT       = "submit";
	public static final String RETRIEVE     = "retrieve";
	public static final String QUERY        = "query";
	//Actor Type
	public static final String SECURENODE = "SecureNode";
	

	/* Logger for debugging messages */
	private static final Log log = LogFactory.getLog(ActorConfigurationLoader.class);
	/* Logger for IHE Actor message traffic */
	private static final Log4jLogger iheLog = new Log4jLogger();
	/* Current root logger appender */
	private Appender currentAppender = null;
	
	private boolean initialized = false;

	/* The actor definitions loaded by the config file */
	protected Collection<IActorDescription> actorDefinitions = Collections.synchronizedList(new ArrayList<IActorDescription>());
	/* The actor object map <String(Actor), IheActor> */
	protected Map<String, IheActor> actors = Collections.synchronizedMap(new HashMap<String, IheActor>());
	/* The Names of the actors installed */
	protected Collection<String> actorsInstalled = Collections.synchronizedList(new ArrayList<String>());

	/**
	 * Loads the supplied configuration file and
	 * creates all of the IHE actors that it defines.
	 * 
	 * @param filename the name of the configuration file
	 * @param logContext the LogContext to be used for audit logging
	 * @return True if the configuration file was processed successfully
	 * @throws IheConfigurationException When there is something wrong with the specified configuration
	 */
	public synchronized boolean loadConfiguration(String filename, ILogContext logContext) throws IheConfigurationException {
		if (filename == null) return false;
		return loadConfiguration(new File(filename), true, logContext);
	}
	
	/**
	 * Loads the supplied configuration file and
	 * create all of the IHE actors that it defines.
	 * 
	 * @param file The configuration file
	 * @param logContext the LogContext to be used for audit logging
	 * @return True if the configuration file was processed successfully
	 * @throws IheConfigurationException When there is something wrong with the specified configuration
	 */
	public synchronized boolean loadConfiguration(File file, ILogContext logContext) throws IheConfigurationException {
		if (file == null) return false;
		return loadConfiguration(file, true, logContext);
	}
	
	/**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 * 
	 * @param filename the name of the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
	 * @param logContext the LogContext to be used for audit logging
	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean loadConfiguration(String filename, boolean autoInstallActors, ILogContext logContext) throws IheConfigurationException {
		return loadConfiguration(new File(filename), autoInstallActors, logContext);
	}
	
	public synchronized boolean loadConfigurationFromClassPath(String filename, boolean autoInstallActors) throws IheConfigurationException {
		URL url = this.getClass().getResource("/" + filename.trim());
		File file;
		try {
	      file = new File(url.toURI());
		} catch(URISyntaxException e) {
	      file = new File(url.getPath());
		}
		return loadConfiguration(file, autoInstallActors, null);
	}

	/**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 * 
	 * @param filename the name of the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean loadConfiguration(String filename, boolean autoInstallActors) throws IheConfigurationException {
		return loadConfiguration(filename, autoInstallActors, null);
	}
	
    /**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 *
	 * @param file the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
	 * @param logContext the LogContext to be used for audit logging
 	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */	
	public synchronized boolean loadConfiguration(File file, boolean autoInstallActors, ILogContext logContext) throws IheConfigurationException {
		return loadConfiguration(file, autoInstallActors, true, logContext);
	}     
	
    /**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 * 
	 * @param file the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
     * @param reset whether to reset actorDefinitions or resetAllBrokers
	 * @param logContext the LogContext to be used for audit logging
	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	private boolean loadConfiguration(File file, boolean autoInstallActors, boolean reset,  
			    ILogContext logContext) throws IheConfigurationException {
		LibraryConfig libConfig = LibraryConfig.getInstance();
		libConfig.setLogContext(logContext);
		boolean okay = true;
		// Reset the list of loaded actors
		if (reset) actorDefinitions = Collections.synchronizedList(new ArrayList<IActorDescription>());
		// If we are auto-installing, reset all the brokers
		if (autoInstallActors && reset) destroyAllActors();
		// Make sure we have a configuration file
		File configFile = file;
		if (configFile == null) {
			throw new IheConfigurationException("No file given to configuration loader");
		} else if (!configFile.exists()) {
			throw new IheConfigurationException("The configuration file \"" + configFile.getAbsolutePath() + "\" does not exist");
		}
		// Create a builder factory and a builder, and get the configuration document.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		Document configuration = null;
		try {
			configuration = factory.newDocumentBuilder().parse(configFile);
		} catch (SAXException e) {
			// An XML exception
			throw new IheConfigurationException("Invalid XML in configuration file '" + configFile.getAbsolutePath() + "'", e);
		} catch (IOException e) {
			// A problem reading the file
			throw new IheConfigurationException("Cannot read configuration file '" + configFile.getAbsolutePath() + "'", e);
		} catch (ParserConfigurationException e) {
			// No XML implementation
			throw new IheConfigurationException("No XML implementation to process configuration file '" + configFile.getAbsolutePath() + "'", e);
		}
		// Get the list of XML elements in the configuration file
		NodeList configurationElements = configuration.getDocumentElement().getChildNodes();
		// Load all the connection definitions first
		for (int elementIndex = 0; elementIndex < configurationElements.getLength(); elementIndex++) {
			Node element = configurationElements.item(elementIndex);
			if (element instanceof Element) {
				// See what type of element it is
				String name = element.getNodeName();
				if (name.equalsIgnoreCase(CONNECTIONFILE)) {
					// An included connection file, load it
					if (!processConnectionFile((Element) element, configFile)) okay = false;
				} else if (name.equalsIgnoreCase(SECURECONNECTION) || name.equalsIgnoreCase(STANDARDCONNECTION)) {
					// An included connection, load it
					if (!ConnectionFactory.loadConnectionDescriptionsFromXmlNode(element, configFile)) {
						ActorDescriptionLoader.throwIheConfigurationException("Error loading configuration file '" + configFile.getAbsolutePath() + "'", configFile);
						okay = false;
					}
				}
			}
		}
		// If all the connection files loaded okay, define the various actors
		if (okay) {
			for (int elementIndex = 0; elementIndex < configurationElements.getLength(); elementIndex++) {
				Node element = configurationElements.item(elementIndex);
				if (element instanceof Element) {
					// See what type of element it is
					String name = element.getNodeName();
                    if (name.equalsIgnoreCase(ACTORFILE)) {
                        if (!processActorFile((Element) element, false /*autoInstallActors*/, configFile, false /*reset*/)) okay = false;
                    } else if (name.equalsIgnoreCase(ACTOR)) {
						// An IHE actor definition
						if (!processActorDefinition((Element) element, configFile)) okay = false;
					}
				}
			}
		}
        if (autoInstallActors) {
            Collection config = actorDefinitions;
            resetConfiguration(config);
        }		
		// Done
        initialized = true;
		return true;
	}
	
	/**
	 * Clears all brokers and stops all active actors.
	 */
	abstract protected void destroyAllActors();
	
	/**
	 * Validates this actor to make sure it has defined all required elements and attributes. 
	 * 
	 * @param actor the actor description to validate
	 * @param configFile the config file of this actor
	 * @return <code>true</code> if validation is passed
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	abstract protected boolean validateActor(IActorDescription actor, File configFile) throws IheConfigurationException;	

	/**
	 * Creates an IHE actor and install it into the PatientBroker or DocumentBroker.
	 * 
	 * @param actor the {@link IActorDescription} which contains the information about 
	 * actor name, actor type, connections and properties etc.  
	 * @param auditLogs the collection audit log connections (each connection corresponds to an audit record repository)
	 * @param logger the IHE actor message logger to use for this actor, null means no message logging
	 * @param configFile the config file of this actor
	 * @return <code>true</code> if the actor is created successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	abstract protected boolean createIheActor(IActorDescription actor, Collection<IConnectionDescription> auditLogs, IMesaLogger logger, File configFile) 
		throws IheConfigurationException;	
	
	/**
	 * Gets a human-readable string naming type of this actor
	 * (ie. "PDQ Server", "PIX Manager", "Audit Repository").  All
	 * actors of the same type will return the same string.  The
	 * type is designed to be used in a GUI.
	 * 
	 * @param type the machine-readable actor type
	 * @param configFile the config file of this actor
	 * @return the human readable type name of this actor
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	abstract protected String getHumanReadableActorType(String type, File configFile) throws IheConfigurationException;

	/**
	 * Processes a ConnectionFile element.  This element will specify a file that includes
	 * definitions of various connections that can be used by the IHE actors.
	 * 
	 * @param element the XML DOM element defining the connection file
	 * @return <code>true</code> if the file was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean processConnectionFile(Element element, File configFile) throws IheConfigurationException {
		boolean okay = false;
		// Get out the file name
		String filename = getAttributeValue(element, FILE);
		if (filename == null) filename = getAttributeValue(element, NAME);
		if (filename == null) filename = getNodeAsText(element);
		if (filename != null) {
			// Got the connection file name, load it
			File includeFile = new File(configFile.getParentFile(), filename);
			if (ConnectionFactory.loadConnectionDescriptionsFromFile(includeFile)) {
				okay = true;
			} else {
				ActorDescriptionLoader.throwIheConfigurationException("Error loading connection file \"" + filename + "\"", configFile);
			}
		} else {
			// No connection file name given
			ActorDescriptionLoader.logConfigurationWarning("Missing attribute 'name' in '" + CONNECTIONFILE + "' definition", configFile);
		}
		// Done
		return okay;
	}
	
	/**
	 * Gets an attribute value
	 * 
	 * @param node the XML DOM node holding the attribute
	 * @param name the name of the attribute
	 * @return the value of the attribute
	 */
	private String getAttributeValue(Node node, String name) {
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) return null;
		Node attribute = attributes.getNamedItem(name);
		if (attribute == null) return null;
		return attribute.getNodeValue();
	}
	
	/**
	 * Gets the text included within an XML DOM element
	 * 
	 * @param node the XML DOM node holding the text
	 * @return the text
	 */
	private String getNodeAsText(Node node) {
		if (!node.hasChildNodes()) return null;
		Text nodeTextContents = (Text) node.getFirstChild();
		return nodeTextContents.getData();
	}	
	
	 
	/**
	 * Processes an ActorFile element. This element will specify a file that includes
	 * definitions of various related actors that can be used by the IHE actors.
	 * 
	 * @param element the XML DOM element defining the connection file
	 * @param autoInstall whether to auto install the actor
	 * @param configFile the actor config file
	 * @param resetActorDefinition whether to reset the actor definition
	 * @return <code>true</code> if the file was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
    private boolean processActorFile(Element element, boolean autoInstall, File configFile, boolean resetActorDefinition) throws IheConfigurationException {
        boolean okay = false;
        // Get out the file name
        String filename = getAttributeValue(element, FILE);
        if (filename == null) filename = getAttributeValue(element, NAME);
        if (filename == null) filename = getNodeAsText(element);
        if (filename != null) {
            // Got the actor file name, load it
            File includeFile = new File(configFile.getParentFile(), filename);
            if (loadConfiguration(includeFile, autoInstall, false,
                   LibraryConfig.getInstance().getLogContext())) {
                okay = true;
            } else {
                ActorDescriptionLoader.throwIheConfigurationException("Error loading actor file \"" + filename + "\"", configFile);
            }
        } else {
            // No connection file name given
        	ActorDescriptionLoader.logConfigurationWarning("Missing attribute 'name' in '" + ACTORFILE + "' definition", configFile);
        }
        // Done
        return okay;
    }
    
    /**
	 * Processes an Actor element.  This element will specify a single IHE actor
	 * and the connection(s) it should use.
	 * 
	 * @param element the XML DOM element defining the actor
	 * @param config the parent config file
	 * @return True if the actor was created successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean processActorDefinition(Element element, File configFile) throws IheConfigurationException {
		boolean okay = false;
		// Get out the actor name and type
		String actorName = getAttributeValue(element, NAME);
		if (actorName == null)
			ActorDescriptionLoader.throwIheConfigurationException("Missing attribute '" + NAME + "' in 'Actor' definition", configFile);
		String actorType = getAttributeValue(element, TYPE);
		if (actorType == null)
			ActorDescriptionLoader.throwIheConfigurationException("Missing attribute '" + TYPE + "' in 'Actor' definition", configFile);
		// Process the definition
		okay = processIheActorDefinition(actorType, actorName, element, configFile);
		return okay;
	}
 
	/**
	 * Processes an Actor element to extract the parameters and create and install the appropriate
	 * object.
	 * 
	 * @param actorType the type of the actor to create
	 * @param actorName the name for this actor within the configuration file
	 * @param definition the XML DOM element defining the actor
	 * @return <code>true</code> if the actor was create successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean processIheActorDefinition(String actorType, String actorName, Element definition, File configFile) throws IheConfigurationException {
		// Actually create the actor
        IActorDescription actor = ActorDescriptionLoader.processDescriptionNode(definition, configFile);	
        
		// Make sure we got out a valid definition
		if ( !validateActor(actor, configFile) ) 
			throw new IheConfigurationException("Actor '" + actorName + "' is not valid in configuration file \"" + configFile.getAbsolutePath() + "\"");			

		actor.setHumanReadableType(getHumanReadableActorType(actor.getType(), configFile));
		
		actorDefinitions.add(actor);			
		return true;
	}
	
	/**
	 * Resets the current IHE brokers to use the actors described in the list passed
	 * in.  These may be actor descriptions objects or the IDs of actor description
	 * objects.  This call will not do any logging.
	 * 
	 * @param actorDescriptions the actor descriptions or IDs to use to define the actors
	 * @return 'true' if the actors were created and initialized successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean resetConfiguration(Collection<Object> actorDescriptions) throws IheConfigurationException {
		return resetConfiguration(actorDescriptions, null);
	}
	
	/**
	 * Resets the current IHE brokers to use the actors described in the list passed
	 * in.  These may be actor descriptions objects or the IDs of actor description
	 * objects.
	 * 
	 * @param actorDescriptions the actor descriptions or IDs to use to define the actors
	 * @param logFilename the log file to install for this set of actors
	 * @return 'true' if the actors were created and initialized successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean resetConfiguration(Collection<Object> actorDescriptions, String logFilename) throws IheConfigurationException {  
		return resetConfiguration(actorDescriptions, logFilename, null);
	}

	/**
	 * Resets the current IHE brokers to use the actors described in the list passed
	 * in.  These may be actor descriptions objects or the IDs of actor description
	 * objects.
	 * 
	 * @param actorDescriptions the actor descriptions or IDs to use to define the actors
	 * @param logFilename the log file to install for this set of actors
     * @param mesaLog the mesa log used for mesa tests
	 * @return 'true' if the actors were created and initialized successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean resetConfiguration(Collection<Object> actorDescriptions, String logFilename, IMesaLogger mesaLog) throws IheConfigurationException {
		// Reset all the brokers
		destroyAllActors();
		// Setup the log
		log.debug("Log file closed.");
		setLoggingFile(logFilename, null, null);
		log.debug("Log file opened.");
		// Jump out if nothing to start up
		if (actorDescriptions == null) return true;
		// First, map all the supplied actor/actor names into descriptions
		ArrayList<IActorDescription> actors = new ArrayList<IActorDescription>();
		for (Object thing: actorDescriptions) {
			if (thing instanceof IActorDescription) {
				// Its an actor
				actors.add((IActorDescription) thing);
			} else if (thing instanceof String) {
				// Its an actor name
				IActorDescription actor = getActorDescription((String) thing);
				if (actor != null) actors.add(actor);
			}
		}
		// Second, pull out the connections from any Secure Node actors
		ArrayList<IConnectionDescription> auditConnections = new ArrayList<IConnectionDescription>();
		for (IActorDescription actor: actors) {
			if (actor.getType().equalsIgnoreCase(SECURENODE)) {
				// This is an audit repository, save its connections for all actors to use
				Collection<IConnectionDescription> logConnections = actor.getAuditLogConnection();
				if (logConnections.size() > 0) auditConnections.addAll(logConnections);
			}
		}
		// Third, create all the actors
		boolean okay = true;
		IMesaLogger log = mesaLog==null ? iheLog : mesaLog;
		for (IActorDescription actor: actors) {
			Collection<IConnectionDescription> logConnections = actor.getAuditLogConnection();
			if (logConnections.isEmpty()) logConnections = auditConnections;
			
			if (!createIheActor(actor, logConnections, log, null))
				okay = false;
			else 
				actor.setInstalled();
		}
		
		if(okay) {
	        //post config process
	        ConfigProcessorFactory.getConfigProcessor().postProcess(actors);			
		}
		// Done
		return okay;
	}
	
	/** Sets the default file to write to for the root.
	 * Also sets the level.  Note: if level is null 
	 * the rool level will be set to INFO.  Note that
	 * if there already is a file appender set this way
	 * it will be removed before the new one is added.
	 * All other appenders are left untouched.
	 * 
	 * @param fullPathToLogFile the Path to append the log to.
	 * @param level the Level to log, null for INFO.
	 * @param pattern Some other pattern to use for logging.  null ok.
	 */
	public synchronized void setLoggingFile(String fullPathToLogFile, Level level, String pattern) {
		Logger root = Logger.getRootLogger();
		try {
			if (pattern == null)
				pattern =  "Milliseconds since program start: %r %n" +
					"Date of message: %d %n" +
					//"Classname of caller: %C %n" +
					"Location: %l %n" +
					"Message: %m %n %n";
			if (currentAppender != null) root.removeAppender(currentAppender);
			if (fullPathToLogFile != null) {
				currentAppender = new FileAppender(new PatternLayout(pattern), fullPathToLogFile);
				if (currentAppender != null) root.addAppender(currentAppender);
				if (level == null) level = Level.INFO;
				root.setLevel(level);
			}
		} catch(Exception e) { log.error("Unable to set output file for logger: " + fullPathToLogFile, e); }
	}
	
	/**
	 * Gets the actor descriptions loaded in the configuration.
	 * 
	 * @return the actor descriptions
	 */
	public synchronized Collection<IActorDescription> getActorDescriptions() {
		List<IActorDescription> actors = new ArrayList<IActorDescription>();
		if (actorDefinitions != null) {
			for (IActorDescription actor: actorDefinitions) {
				actors.add(actor);
			}
		}
		return actors;
	}
		
	/**
	 * Looks up an actor description given the actor name.
	 *  
	 * @param name the actor description name
	 * @return the actor description, if there is one
	 */
	public synchronized IActorDescription getActorDescription(String name) {
		if (name == null) return null;
		for (IActorDescription actor: actorDefinitions) {
			if (name.equalsIgnoreCase(actor.getName())) return actor;
		}
		return null;
	}

	/**
	 * Gets the actor created from the configuration.
	 * 
	 * @return the actor 
	 */
	public synchronized Map<String, IheActor> getActors() {
		return actors;
	}
	
	/**
	 * Looks up an actor given the actor type name
	 *  
	 * @param name the actor type name
	 * @return the actor object
	 */
	public synchronized IheActor getActor(String name) {
		if (name == null) return null;
		
		return actors.get(name);
	}

	/**
	 * Loading actor configuration from an actor file specified by the 
	 * property file. The actor file is loaded by default from the 
	 * classpath. If the actor file is not found in the classpath, then
	 * it will read from the ihe.actors.dir and ihe.actors.file properties.
	 * 
	 * @return
	 */
    public boolean loadActorConfiguration() {
    	boolean success = false;
    	//First, try loading actor configuration from classpath
        String actorPath = PropertyFacade.getString("ihe.actors.path");
        if (StringUtil.goodString(actorPath)) {
            log.info("Loading actor configuration from classpath " + actorPath);
            try {
                success = this.loadConfigurationFromClassPath(actorPath, true);
            } catch (IheConfigurationException e) {
                log.warn("Failed to load actor configuration from classpath", e);
            }
            
            if (success) return success;
        } 

        //Loading actor configuration from File system
        try {
        	String actorFile = getActorFile();

            if (StringUtil.goodString(actorFile)) {
                log.info("Loading actor configuration from " + actorFile);
                success = this.loadConfiguration(actorFile, true);
            }
        } catch (IheConfigurationException e) {
            log.error("Failed to load actor configuration", e);
        } catch (Exception e) {
            log.error("Failed to load actor configuration", e);
        }
        
        if (!success) {
            log.warn("Actor configuration not loaded");        	
        } else {
            if (log.isDebugEnabled())
            	log.debug("Actor configuration succesfully loaded");
        }

        return success;
    }

    private String getActorFile() {
        String actorDir = PropertyFacade.getString("ihe.actors.dir");
        String actorFile = null;
        
        if (actorDir != null) {
            File dir = new File(actorDir);
            
            if (dir.exists()) {
                actorFile = dir.getAbsolutePath();
                //remove the current . folder from the path
                actorFile = actorFile.replace(File.separator + "." + File.separator, File.separator);

                String file = PropertyFacade.getString("ihe.actors.file");

                actorFile = actorFile + File.separator + file;

                if (log.isDebugEnabled())
                	log.debug("actor file is " + actorFile);
                
            } else {
                log.info("ihe.actors.dir" + " does not exist: " + actorDir);
            }
        }

        if (!StringUtil.goodString(actorFile)) {
            actorFile = System.getProperty("ihe.actors.file");
            
            if (log.isDebugEnabled())
            	log.debug("ihe.actors.file system property: " + actorFile);
        }
        
        return actorFile;
    }
    
    public boolean isInitialized() {
    	return initialized;
    }
}
