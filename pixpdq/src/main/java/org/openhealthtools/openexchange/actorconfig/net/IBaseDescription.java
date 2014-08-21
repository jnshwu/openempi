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

import java.util.List;
import java.util.Set;

import org.openhealthtools.openexchange.datamodel.Identifier;


/**
 * The base description (actor or connection description) interface.
 * <p/>
 * It should be used to get information about a specific
 * description.  
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @see IConnectDescription, IActorConnection
 */
public interface IBaseDescription {

	/**
     * Used to get the actor type of this actor. The actor type is
     * predefined by the system instead of users. 
     *
     * @return The type of this actor.
     */
    public String getType();

    /**
     * Used to determine the name of this description.
     *
     * @return Name of this description.
     */
    public String getName();

    /**
     * Used to get a useful, human-readable description of this
     * description (actor or connection description) for debugging 
     * and log messages.
     *
     * @return A human-readable description of this description
     */
    public String getDescription();

    /**
     * Gets all the code type names.
     *
     * @return a set of coding type names
     */
    public Set<String> getAllCodeTypeNames();

    /**
     * Gets an ebXML coding scheme that is defined for this description.
     *
     * @param typeName The name of the coding/classification scheme
     * @return The coding scheme definition
     */
    public CodeSet getCodeSet(String typeName);

    /**
     * Gets a named property that is defined for this description.
     *
     * @param name The name of the property
     * @return The value of the property
     */
    public String getProperty(String name);

    /**
     * Gets a property set that is defined for this description.
     *
     * @param name The name of the property set
     * @return The property set
     */
    public PropertySet getPropertySet(String name);

    /**
     * Gets an enum map that is defined for this description.
     *
     * @param enumClass The enum class being mapped
     * @return The enum map definition
     */
    public EnumMap getEnumMap(Class enumClass);

    /**
     * Get a string map that is defined for this description.
     *
     * @param name The string value type being mapped
     * @return The string map definition
     */
    public StringMap getStringMap(String name);

    /**
     * Get a hierarchical identifier that is defined for this description.
     *
     * @param name The name of the identifier
     * @return The hierarchical identifier
     */
    public Identifier getIdentifier(String name);

    /**
     * Get all identifiers of a given type.
     *
     * @param type the type of identifier
     * @return The list of Identifiers whose type is matched with the given type.
     *         Returns an empty list if nothing is found by this type.
     */
    public List<Identifier> getAllIdentifiersByType(String type);
}
