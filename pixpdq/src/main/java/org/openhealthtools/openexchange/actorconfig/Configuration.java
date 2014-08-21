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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.net.EnumMap;
import org.openhealthtools.openexchange.actorconfig.net.IBaseDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.actorconfig.net.PropertySet;
import org.openhealthtools.openexchange.actorconfig.net.StringMap;
import org.openhealthtools.openexchange.datamodel.Identifier;

/**
 * This class contains a number of utilities for getting configuration
 * and using configuration information from a connection description.
 * 
 * @author Jim Firby
 * @version 1.0 - Nov 22, 2005
 */
public class Configuration {

	private static Log log = LogFactory.getLog(Configuration.class);
	/**
	 * Gets an identifier description from this actor or the connection
	 * of this actor is using.
	 * 
	 * @param description the actor/connection description being used
	 * @param name the name of the identifier in the configuration
	 * @param isRequired <code>true</code> if this identifier must exist
	 * @return the identifier
	 * @throws IheConfigurationException If this identifier must be in the configuration and it is not
	 */
	public static Identifier getIdentifier(IBaseDescription description, String name, boolean isRequired) throws IheConfigurationException {
		if (description == null)
			throw new IheConfigurationException("Invalid connection description (NULL)");
		if (name == null)
			throw new IheConfigurationException("Invalid identifier name (NULL)");
		Identifier identifier = description.getIdentifier(name);
		if ((identifier == null) && isRequired) {
			throw new IheConfigurationException("No identifier '" + name + "' defined for actor/connection \"" + description.getName() + "\"");
		}
		return identifier;
	}
	
	/**
	 * Gets a property set from this actor or connection being used by this
	 * actor.
	 * 
	 * @param description the actor or connection description being used
	 * @param name the name of the property set in the configuration
	 * @param isRequired <code>true</code> if this property set must exist
	 * @return The property set
	 * @throws IheConfigurationException  If this property set must be in the configuration and it is not
	 */
	public static PropertySet getPropertySet(IBaseDescription description, String name, boolean isRequired) throws IheConfigurationException {
		if (description == null)
			throw new IheConfigurationException("Invalid actor/connection description (NULL)");
		if (name == null)
			throw new IheConfigurationException("Invalid property set name (NULL)");
		PropertySet set = description.getPropertySet(name);
		if ((set == null) && isRequired) {
			throw new IheConfigurationException("No property set '" + name + "' defined for actor/connection \"" + description.getName() + "\"");
		}
		return set;
	}

	
	/**
	 * Gets a value from a property set defined for an actor or connection.
	 * 
	 * @param description the actor or connection description holding the set
	 * @param setName the name of the property set
	 * @param valueName the name of the value within the property set
	 * @param isRequired <code>true</code> if this value must be defined
	 * @return The value of the property
	 * @throws IheConfigurationException If this value must be in the configuration and it is not
	 */
	public static String getPropertySetValue(IBaseDescription description, String setName, String valueName, boolean isRequired) throws IheConfigurationException {
		if (valueName == null)
			throw new IheConfigurationException("Invalid property set value name (NULL)");
		PropertySet pset = getPropertySet(description, setName, isRequired);
		if (pset == null) return null;
		String result = pset.getValue(valueName);
		if ((result == null) && isRequired)
			throw new IheConfigurationException("Property set '" + setName + "' for actor/connection \"" + description.getName() + "\" has no value for \'" + valueName + "'");
		return result;
	}

    /**
     * Gets a value of a property defined for an actor or connection.
     *
     * @param description the actor or connection description holding the set
     * @param valueName the name of property
     * @param isRequired <code>true</code> if this property must be defined
     * @return The value of the property
     * @throws IheConfigurationException If this value must be in the configuration and it is not
     */
    public static String getPropertyValue(IBaseDescription description, String valueName, boolean isRequired) throws IheConfigurationException {
		if (valueName == null)
			throw new IheConfigurationException("Invalid property value name (NULL)");
		String result = description.getProperty(valueName);
		if ((result == null) && isRequired)
			throw new IheConfigurationException("Actor/Connection \"" + description.getName() + "\" has no value for \'" + valueName + "'");
		return result;
	}

    /**
	 * Translates an enum value into a Code appropriate for this actor or connection.
	 * 
	 * @param description the actor or connection being submitted to
	 * @param value the enum value used
	 * @param enumType the enum Class for this value, ie. the name of the EnumMap to use for translation
	 * @param isRequired <code>true</code> if a translation of this value is required, False if the value can default to itself
	 * @return The translated Code
	 * @throws IheConfigurationException When the required EnumMap is not defined for this connection
	 */
	
	public static String applyEnumMap(IBaseDescription description, Enum value, Class enumType, boolean isRequired) throws IheConfigurationException {
		if (value == null) {
			if (isRequired) throw new IheConfigurationException("Invalid enum value (NULL)");
			return null;
		}
		if (enumType == null)
			throw new IheConfigurationException("Invalid enum type (NULL)");
		if (description == null)
			throw new IheConfigurationException("Invalid connection description (NULL)");
		EnumMap emap = description.getEnumMap(enumType);
		if (emap == null) {
			if (isRequired)
				throw new IheConfigurationException("No enum map to translate '" + enumType.getSimpleName() + "' values for actor/connection \"" + description.getName() + "\"");
			return null;
		} else {
			String translation = emap.getCodeValue(value);
			if (isRequired && (translation == null))
				throw new IheConfigurationException("No translation of '" + value.toString() + "' in string map '" + enumType.getSimpleName() + "' for actor/connection \"" + description.getName() + "\"");
			return translation;
		}
	}

	/**
	 * Translates a code to an enum value.
	 * 
	 * @param description the actor or connection to use
	 * @param code the code used by this actor or connection
	 * @param enumType the  enum type
	 * @return The enum value
	 */
	public static Enum reverseEnumMap(IBaseDescription description, String code, Class enumType) {
		if (code == null) return null;
		if (description == null) return null;
		EnumMap emap = description.getEnumMap(enumType);
		if (emap == null) return null;
		return emap.getEnumValue(code);
	}

	/**
	 * Translates a symbolic string into a Code appropriate for this actor or connection.
	 * 
	 * @param description the actor or connection being submitted to
	 * @param value the symbolic value  
	 * @param valueType the kind of value this is, ie. the name of the StringMap to use for translation
	 * @param isRequired <code>true</code> if a translation of this value is required, False if the value can default to itself
	 * @return The translated Code
	 * @throws IheConfigurationException When the required StringMap is not defined for this actor or connection
	 */
	public static String applyStringMap(IBaseDescription description, String value, String valueType, boolean isRequired) throws IheConfigurationException {
		if (value == null)  {
            if (isRequired) throw new IheConfigurationException("Invalid string value (NULL) " + (valueType==null? "" : "of " + valueType));
			return null;
		}
		if (valueType == null)
			throw new IheConfigurationException("Invalid string value type (NULL)");
		if (description == null) 
			throw new IheConfigurationException("Invalid actor/connection description (NULL)");
		StringMap smap = description.getStringMap(valueType);
		if (smap == null) {
			if (isRequired)
				throw new IheConfigurationException("No string map to translate '" + valueType + "' values for actor/connection \"" + description.getName() + "\"");
			return value;
		} else {
			String translation = smap.getCodeValue(value);
			if (isRequired && (translation == null))
				throw new IheConfigurationException("No translation of '" + value + "' in string map '" + valueType + "' for actor/connection \"" + description.getName() + "\"");
			return translation;
		}
	}
	
	/**
	 * Translates a Code from this connection into a string value.
	 * 
	 * @param description the actor or connection being submitted to
	 * @param code the Code used by the actor or connection
	 * @param valueType the kind of value this is, ie. the name of the StringMap to use for translation
	 * @return The symbolic value for this Code
	 */
	public static String reverseStringMap(IBaseDescription description, String code, String valueType) {
		if (code == null) return null;
		if (description == null) return null;
		StringMap smap = description.getStringMap(valueType);
		if (smap == null) return null;
		return smap.getStringValue(code);
	}

	/**
	 * Gets a connection description from the actor description this
	 * actor is using.
	 * 
	 * @param actor the actor description being used
	 * @param type the type of the connection in the configuration
	 * @param isRequired <code>true</code> if this connection must exist
	 * @return the connection
	 * @throws IheConfigurationException If this connection must be in the configuration and it is not
	 */
	public static IConnectionDescription getConnection(IActorDescription actor, String type, boolean isRequired) throws IheConfigurationException {
		if (actor == null)
			throw new IheConfigurationException("Invalid actor description (NULL)");
		if (type == null)
			throw new IheConfigurationException("Invalid connection type (NULL)");
		
		IConnectionDescription connection = actor.getConnectionDescriptionByType(type);	
		if ((connection == null) && isRequired) {
			throw new IheConfigurationException("No Connection of type '" + type + "' defined for actor \"" + actor.getName() + "\"");
		}
		return connection;
	}

	/**
	 * Gets a collection of connection descriptions from the actor description this
	 * actor is using.
	 * 
	 * @param actor the actor description being used
	 * @param type the type of the connection in the configuration
	 * @param isRequired <code>true</code> if this connection must exist
	 * @return the collection of connections
	 * @throws IheConfigurationException If this connection must be in the configuration and it is not
	 */
	public static Collection<IConnectionDescription> getConnections(IActorDescription actor, String type, boolean isRequired) throws IheConfigurationException {
		if (actor == null)
			throw new IheConfigurationException("Invalid actor description (NULL)");
		if (type == null)
			throw new IheConfigurationException("Invalid connection type (NULL)");
		
		Collection<IConnectionDescription> connections = actor.getConnectionDescriptionsByType(type);	
		if (connections.isEmpty() && isRequired) {
			throw new IheConfigurationException("No Connection of type '" + type + "' defined for actor \"" + actor.getName() + "\"");
		}
		return connections;
	}

    /**
     * Gets the set of all domains (assigning authorities) defined for this actor or connection.
     * 
	 * @param description the actor/connection description where the domains are defined
     * @return a set of domains including global domain.
     */
    public static Set<Identifier> getAllDomains(IBaseDescription description) {
		Set<Identifier> ret = new HashSet<Identifier>();
    	ret.addAll( description.getAllIdentifiersByType("globalDomain") );
    	ret.addAll( description.getAllIdentifiersByType("domain") );
    	return ret;
    }

    /**
     * Gets the global domain (global assigning authority) defined for this actor.
     * 
	 * @param actor the actor description where the global domain is defined
     * @param isRequired <code>true</code if the globalDomain is required. Otherwise <code>false</code> 
     * @return the global domain
     * @throws IheConfigurationException if no global domain is defined in the connection
     */
    public static Identifier getGlobalDomain(IBaseDescription description, boolean isRequired) throws IheConfigurationException{
    	List<Identifier> globalDomains = description.getAllIdentifiersByType("globalDomain");
    	if (globalDomains.size() > 1) {
    		log.warn("Multiple global domains are found, but one global domain is needed for actor/connection \"" + description.getName() + "\"");
    	}
		if (globalDomains.isEmpty() && isRequired) {
			throw new IheConfigurationException("No global domain is defined for actor/connection \"" + description.getName() + "\"");
		}
    	return globalDomains.get(0);
    }

}
