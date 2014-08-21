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

import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CLASS;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CLASSSCHEME;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CODE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.CODINGSCHEME;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.DISPLAY;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.ENTRY;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.ENUM;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.EXT;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.NAME;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.NAMESPACEID;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.STRING;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.TYPE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.UNIVERSALID;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.UNIVERSALIDTYPE;
import static org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader.VALUE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
/**
 * This class loads connection description configuration files and creates
 * a set of ConnectionDescription objects.
 *
 * @author Jim Firby
 */
public class DescriptionLoader {

    /* Logger for debugging messages */
	private static final Log log = LogFactory.getLog(DescriptionLoader.class);


    /**
     * Processes a &lt;CodeType&gt; entry and add its contents to the
     * configuration coding scheme set.
     * <p/>
     * <pre>
     * XML: CodeType name="" classScheme=""
     *       Code code="" display="" codingScheme=""
     *       ...
     * </pre>
     *
     * @param description The actor or connection to add the codes to
     * @param codeType   The DOM element of the "CodeType" entry
     */
    protected static void processCodingScheme(BaseDescription description, Node codeType) {
        String typeName = getAttributeValue(codeType, NAME);
        String classScheme = getAttributeValue(codeType, CLASSSCHEME);
        if (typeName == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(codeType.getNodeName(), NAME, description);
        } else {
            // Get the coding scheme for this entry
            CodeSet scheme = description.getCodeSet(typeName);
            if (scheme == null) {
                scheme = new CodeSet(typeName, classScheme);
                description.addCodeSet(scheme);
            }
            // Now add the new entries
            NodeList codes = codeType.getChildNodes();
            for (int i = 0; i < codes.getLength(); i++) {
                Node codeNode = codes.item(i);
                if (codeNode.getNodeName().equalsIgnoreCase(CODE)) {
                    String code = getAttributeValue(codeNode, CODE);
                    if (code == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(codeNode.getNodeName(), CODE, description);
                    } else {
                        // Grab the other attributes
                        String display = getAttributeValue(codeNode, DISPLAY);
                        if (display == null) {
                            if (classScheme != null) {
                                logMissingAttributeWarning(codeNode.getNodeName(), DISPLAY, description);
                            }
                            display = code;
                        }
                        String codingScheme = getAttributeValue(codeNode, CODINGSCHEME);
                        if (codingScheme == null) {
                            if (classScheme != null) {
                                logMissingAttributeWarning(codeNode.getNodeName(), CODINGSCHEME, description);
                            }
                            codingScheme = classScheme;
                        }
                        String ext = getAttributeValue(codeNode, EXT);
                        // Save away the scheme
                        scheme.addEntry(code, display, codingScheme, ext);
                    }
                } else if (codeNode instanceof Element) {
                    logUnexpectedTagWarning(codeNode.getNodeName(), description);
                }
            }
        }
    }

    /**
     * Processes an &lt;EnumMap&gt; entry and add its contents to the
     * configuration enum map set.
     * <p/>
     * <pre>
     * XML: EnumMap class=""
     *       Entry enum="" code=""
     *       ...
     * </pre>
     *
     * @param description The actor or connection to add the codes to
     * @param enumMap    The DOM element of the "EnumMap" entry
     */
    protected static void processEnumMap(BaseDescription description, Node enumMap) {
        String enumName = getAttributeValue(enumMap, CLASS);
        if (enumName == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(enumMap.getNodeName(), CLASS, description);
        } else {
            // Create an enum map for this entry
            EnumMap theMap = null;
            try {
                theMap = new EnumMap(enumName);
            } catch (ClassNotFoundException e) {
                logEnumMapError(enumName, description, e);
                return;
            }
            description.addEnumMap(theMap);
            // Now add the new entries
            NodeList entries = enumMap.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase(ENTRY)) {
                    String enumValue = getAttributeValue(entry, ENUM);
                    if (enumValue == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(entry.getNodeName(), ENUM, description);
                    } else {
                        // Grab the other attributes
                        String codeValue = getAttributeValue(entry, CODE);
                        if (codeValue == null) {
                            logMissingAttributeWarning(entry.getNodeName(), CODE, description);
                        } else {
                            // Save away the entry
                            theMap.addEntry(enumValue, codeValue);
                        }
                    }
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), description);
                }
            }
        }
    }

    /**
     * Processes an &lt;StringMap&gt; entry and add its contents to the
     * configuration string map set.
     * <p/>
     * <pre>
     * XML: StringMap name=""
     *       Entry string="" code=""
     *       ...
     * </pre>
     *
     * @param description The actor or connection to add the codes to
     * @param stringMap  The DOM element of the "StringMap" entry
     */
    protected static void processStringMap(BaseDescription description, Node stringMap) {
        String mapName = getAttributeValue(stringMap, NAME);
        if (mapName == null) {
            // No class scheme, can't save codes
            logMissingAttributeWarning(stringMap.getNodeName(), NAME, description);
        } else {
            // Create a string map for this entry
            StringMap theMap = new StringMap(mapName);
            description.addStringMap(theMap);
            // Now add the new entries
            NodeList entries = stringMap.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase(ENTRY)) {
                    String stringValue = getAttributeValue(entry, STRING);
                    if (stringValue == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(entry.getNodeName(), STRING, description);
                    } else {
                        // Grab the other attributes
                        String codeValue = getAttributeValue(entry, CODE);
                        if (codeValue == null) {
                            logMissingAttributeWarning(entry.getNodeName(), CODE, description);
                        } else {
                            // Save away the entry
                            theMap.addEntry(stringValue, codeValue);
                        }
                    }
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), description);
                }
            }
        }
    }

    /**
     * Processes a &lt;PropertySet&gt; entry and add its contents to the
     * actor or connection configuration.
     * <p/>
     * <pre>
     * XML: PropertySet name=""
     *       Entry name="" value=""
     *       ...
     * </pre>
     *
     * @param description  The actor or connection to add the property set to
     * @param propertySet The DOM element of the "PropertySet" entry
     */
    protected static void processPropertySet(BaseDescription description, Node propertySet) {
        String setName = getAttributeValue(propertySet, NAME);
        if (setName == null) {
            // No property set name, can't save values
            logMissingAttributeWarning(propertySet.getNodeName(), NAME, description);
        } else {
            // Create a property set for this entry
            PropertySet theSet = new PropertySet(setName);
            description.addPropertySet(theSet);
            // Now add the new entries
            NodeList entries = propertySet.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeName().equalsIgnoreCase(ENTRY)) {
                    String stringValue = getAttributeValue(entry, NAME);
                    if (stringValue == null) {
                        // No code, nothing to save
                        logMissingAttributeWarning(entry.getNodeName(), NAME, description);
                    } else {
                        // Grab the other attributes
                        String codeValue = getAttributeValue(entry, VALUE);
                        if (codeValue == null) {
                            logMissingAttributeWarning(entry.getNodeName(), VALUE, description);
                        } else {
                            // Save away the entry
                            theSet.addValue(stringValue, codeValue);
                        }
                    }
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), description);
                }
            }
        }
    }

    /**
     * Processes an &lt;AssigningAuthority&gt; entry and add its contents to the
     * actor or connection configuration.
     * <p/>
     * <pre>
     * XML: AssigningAuthority name="" type=""
     *       NamespaceId
     *       UniversalId
     *       UniversalIdType
     * </pre>
     *
     * @param description The actor or connection description to add the assigning authority to
     * @param authority  The DOM element of the "AssigningAuthority" entry
     */
    protected static void processIdentifier(BaseDescription description, Node authority) {
        String setName = getAttributeValue(authority, NAME);
        String type = getAttributeValue(authority, TYPE);

        if (setName == null) {
            // No authority name, can't save values
            logMissingAttributeWarning(authority.getNodeName(), NAME, description);
        } else {
            // Get the expected parts
            String namespaceId = null;
            String universalId = null;
            String universalIdType = null;
            NodeList entries = authority.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                String entryName = entry.getNodeName();
                if (entryName.equalsIgnoreCase(NAMESPACEID)) {
                    namespaceId = getNodeAsText(entry);
                } else if (entryName.equalsIgnoreCase(UNIVERSALID)) {
                    universalId = getNodeAsText(entry);
                } else if (entryName.equalsIgnoreCase(UNIVERSALIDTYPE)) {
                    universalIdType = getNodeAsText(entry);
                } else if (entry instanceof Element) {
                    logUnexpectedTagWarning(entry.getNodeName(), description);
                }
            }
            // Did we find enough parts?
            if ((namespaceId == null) && (universalId == null)) {
                logMissingElementWarning(authority.getNodeName(), UNIVERSALID, description);
                return;
            }
            // We did, add the authority
            // Create a property set for this entry
            Identifier theAuthority = new Identifier(namespaceId, universalId, universalIdType);
            description.addIdentifier(setName, type, theAuthority);
        }
    }

    /**
     * Generates a log warning message that an unexpected tag was found in the
     * description.
     *
     * @param tagName     The name of the unexpected XML tag
     * @param description The actor/connection description being loaded
     */
    protected static void logUnexpectedTagWarning(String tagName, IBaseDescription description) {
        String name = null;
        if (description != null) {
            name = description.getName();
        }
        if (name != null) {
            name = name.trim();
        }
        if ((name == null) || (name.equals(""))) {
            log.warn("Unexpected '" + tagName + "' element in actor/connection description");
        } else {
            log.warn("Unexpected '" + tagName + "' element in actor/connection description \"" + name + "\"");
        }
    }

    /**
     * Generates a log warning for a missing required attribute in a description.
     *
     * @param tagName     The name of the element requiring the attribute
     * @param attribute   The name of the missing attribute
     * @param description The actor/connection description being loaded
     */
    protected static void logMissingAttributeWarning(String tagName, String attribute, IBaseDescription description) {
        String name = null;
        if (description != null) {
            name = description.getName();
        }
        if (name != null) {
            name = name.trim();
        }
        if ((name == null) || (name.equals(""))) {
            log.warn("Missing attribute '" + attribute + "' in '" + tagName + "' element in actor/connection description");
        } else {
            log.warn("Missing attribute '" + attribute + "' in '" + tagName + "' element in actor/connection description \"" + name + "\"");
        }
    }

    /**
     * Generates a log warning for a missing required component in a connection description.
     *
     * @param tagName     The name of the element requiring the attribute
     * @param element     The name of the missing component
     * @param description The actor/connection description being loaded
     */
    protected static void logMissingElementWarning(String tagName, String element, IBaseDescription description) {
        String name = null;
        if (description != null) {
            name = description.getName();
        }
        if (name != null) {
            name = name.trim();
        }
        if ((name == null) || (name.equals(""))) {
            log.warn("Missing subelement '" + element + "' in '" + tagName + "' element in actor/connection description");
        } else {
            log.warn("Missing subelement '" + element + "' in '" + tagName + "' element in actor/connection description \"" + name + "\"");
        }
    }

    /**
     * Generates a log message when there is an error processing an included configuration file.
     *
     * @param filename    The name of the included configuration file
     * @param description The connection description being loaded
     * @param e           The exception describing the error
     */
    protected static void logIncludeFileError(String filename, IBaseDescription description, Exception e) {
        if ((filename == null) || (filename.equals(""))) {
            log.error("Error reading file included in actor/connection description", e);
        } else if (description == null) {
            log.error("Error reading actor/connection description include file: \"" + filename + "\"", e);
        } else {
            String name = description.getName();
            if (name != null) {
                name = name.trim();
            }
            log.warn("Error reading actor/connection description \"" + name + "\" include file: \"" + filename + "\"", e);
        }
    }

    /**
     * Generates a log message when there is an error processing an enum map.
     *
     * @param enumClassName The name of the enum class the map is for
     * @param description   The connection description being loaded
     * @param e             The exception describing the error
     */
    protected static void logEnumMapError(String enumClassName, IBaseDescription description, Exception e) {
        if ((enumClassName == null) || (enumClassName.equals(""))) {
            log.error("Error reading file included in actor/connection description", e);
        } else {
            String name = description.getName();
            if (name != null) {
                name = name.trim();
            }
            log.warn("Error reading actor/connection description \"" + name + "\" enum map \"" + enumClassName + "\"", e);
        }
    }

    /**
     * Gets an attribute value
     *
     * @param node The XML DOM node holding the attribute
     * @param name The name of the attribute
     * @return The value of the attribute
     */
    protected static String getAttributeValue(Node node, String name) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return null;
        }
        Node attribute = attributes.getNamedItem(name);
        if (attribute == null) {
            return null;
        }
        return attribute.getNodeValue();
    }
    
    /**
     * Gets an attribute value as boolean
     *
     * @param node The XML DOM node holding the attribute
     * @param name The name of the attribute
     * @return The boolean value of the attribute
     */
    protected static boolean getAttributeValueAsBoolean(Node node, String name) {
    	String value = getAttributeValue(node, name);
    	if (value == null) 
    		return false;
    	
    	return Boolean.parseBoolean(value);
    }

    /**
     * Gets the text included within an XML DOM element
     *
     * @param node The XML DOM node holding the text
     * @return The text
     */
    protected static String getNodeAsText(Node node) {
        if (!node.hasChildNodes()) {
            return null;
        }
        Text nodeTextContents = (Text) node.getFirstChild();
        return nodeTextContents.getData();
    }

    /**
     * Gets the text included within an XML DOM element as an integer
     *
     * @param node The XML DOM node holding the text
     * @return The text converted to an integer.
     */
    protected static int getNodeAsInt(Node node) {
        Text nodeTextContents = (Text) node.getFirstChild();
        String value = nodeTextContents.getData();
        return Integer.parseInt(value);
    }

    /**
     * Gets the text included within an XML DOM element
     *
     * @param node The XML DOM node holding the text
     * @return The text
     */
    protected static String getChildNodeAsText(Node node, String childElemName) {
        if (!node.hasChildNodes()) {
            return null;
        }
        if (childElemName == null) {
        	return null;
        }
        NodeList children = node.getChildNodes();
        for (int valueIndex = 0; valueIndex < children.getLength(); valueIndex++) {
            Node item = children.item(valueIndex);
            String itemName = item.getNodeName();
            if (childElemName.equalsIgnoreCase(itemName)) {
            	return getNodeAsText(item);
            }
        }
        
        return null;
    }

}
