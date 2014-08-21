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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.utils.Pair;


/**
 * A base description that implements the {@link IBaseDescription} 
 * <p/>
 * This base description should be extended by either ActorDescription
 * or ConnectionDescription 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public abstract class BaseDescription implements IBaseDescription {

	private static final Log log = LogFactory.getLog(BaseDescription.class);
    
	/* The name of this description*/
    protected String name = null;
    /* The predefined type of this description */
    private String type = null;
	/* Used by both this and secure connection descriptions */
    protected boolean complete = false;
    /*Used to track the nest level that this description is invoket*/
    protected int invokedLevel = 0;

    /* Loaded via XML configuration files */
    protected Hashtable<String, String> properties = null;
    protected Hashtable<String, PropertySet> propertySets = null;
    protected Hashtable<String, CodeSet> codingSchemes = null;
    protected Hashtable<Class, EnumMap> enumMaps = null;
    protected Hashtable<String, StringMap> stringMaps = null;
    //Pair<String(type), Identifier>
    protected Hashtable<String, Pair<String, Identifier>> identifiers = null;

    /**
     * Increment the invocation level of this description is called.
     *
     * @return the invocation nested level
     */
    public int invoke() {
        return invokedLevel++;
    }

    /**
     * Called by the factory to note that the initalization is complete. <p />
     * <p/>
     * This also checks to make sure that all the instantiation has happened
     * properly.  If not it returns false.
     */
    public boolean complete() {
        invokedLevel--;
        //If there is still an invoked level, the description process is not yet finished.
        if (invokedLevel >= 1) {
            return false;
        }
        return complete;
    }


    /**
     * Only used for init.  Not for use outside of factory.
     */
    public void setName(String name) {
        if (!complete) {
            this.name = name;
        } else {
            log.warn("Actor/Connection Descriptor setter used outside of factory.");
        }
    }
    
    @Override
    public String getName() {
        return name;
    }

	@Override
	public String getType() {
		return this.type;
	}
	
	/**
	 * @param type the actor type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
    
    @Override
    public Set<String> getAllCodeTypeNames() {
        if (codingSchemes != null) {
            return codingSchemes.keySet();
        } else {
            log.debug("Looking up non-existent coding scheme: " + name);
            return null;
        }

    }

    @Override
    public CodeSet getCodeSet(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((codingSchemes != null) && (codingSchemes.containsKey(key))) {
            return codingSchemes.get(key);
        } else {
            log.debug("Looking up non-existent coding scheme: " + name);
            return null;
        }
    }

    /**
     * Adds a new coding set to this description.  This method is used when loading
     * configuration files.
     *
     * @param codingScheme The CodeSet to add
     */
    public void addCodeSet(CodeSet codingScheme) {
        if (codingScheme != null) {
            String schemeName = codingScheme.getCodeType();
            if (schemeName != null) {
                if (codingSchemes == null) {
                    codingSchemes = new Hashtable<String, CodeSet>();
                }
                codingSchemes.put(schemeName.toLowerCase(), codingScheme);
            } else {
                log.debug("Adding coding scheme with no name to actor/connection: " + name);
            }
        }
    }

    @Override
    public String getProperty(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((properties != null) && (properties.containsKey(key))) {
            return properties.get(key);
        } else {
            log.debug("Looking up non-existent property: " + name);
            return null;
        }
    }

    /**
     * Sets the value of a property for this description.  This method is typically used only
     * in initialization and testing.
     *
     * @param name  The name of the property
     * @param value The value of the property
     */
    public void setProperty(String name, String value) {
        if (name != null) {
            if (value != null) {
                if (properties == null) {
                    properties = new Hashtable<String, String>();
                }
                properties.put(name.toLowerCase(), value);
            } else {
                if (properties != null) {
                    properties.remove(name.toLowerCase());
                }
            }
        }
    }

    @Override
    public PropertySet getPropertySet(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((propertySets != null) && (propertySets.containsKey(key))) {
            return propertySets.get(key);
        } else {
            log.debug("Looking up non-existent property set: " + name);
            return null;
        }
    }

    /**
     * Adds a new property set to this description.  This method is used when loading
     * configuration files.
     *
     * @param set The property set to add
     */
    public void addPropertySet(PropertySet set) {
        if (set != null) {
            String setName = set.getName();
            if (setName != null) {
                if (propertySets == null) {
                    propertySets = new Hashtable<String, PropertySet>();
                }
                propertySets.put(setName.toLowerCase(), set);
            } else {
                log.debug("Adding property set with no name to actor/connection: " + name);
            }
        }
    }

    @Override
    public EnumMap getEnumMap(Class enumClass) {
        if (enumClass == null) {
            return null;
        }
        if ((enumMaps != null) && (enumMaps.containsKey(enumClass))) {
            return enumMaps.get(enumClass);
        } else {
            log.debug("Looking up non-existent enum map: " + enumClass.getSimpleName());
            return null;
        }
    }

    /**
     * Adds a new enum map to this description.  This method is used when loading
     * configuration files.
     *
     * @param enumMap The enum map to add
     */
    public void addEnumMap(EnumMap enumMap) {
        if (enumMap != null) {
            Class enumClass = enumMap.getEnumClass();
            if (enumClass != null) {
                if (enumMaps == null) {
                    enumMaps = new Hashtable<Class, EnumMap>();
                }
                enumMaps.put(enumClass, enumMap);
            } else {
                log.debug("Adding enum map with no enum class to actor/connection: " + name);
            }
        }
    }

    @Override
    public StringMap getStringMap(String name) {
        if (name == null) {
            return null;
        }
        if ((stringMaps != null) && (stringMaps.containsKey(name))) {
            return stringMaps.get(name);
        } else {
            log.debug("Looking up non-existent string map: " + name);
            return null;
        }
    }

    /**
     * Adds a new string map to this description.  This method is used when loading
     * configuration files.
     *
     * @param stringMap The string map to add
     */
    public void addStringMap(StringMap stringMap) {
        if (stringMap != null) {
            String mapName = stringMap.getName();
            if (mapName != null) {
                if (stringMaps == null) {
                    stringMaps = new Hashtable<String, StringMap>();
                }
                stringMaps.put(mapName, stringMap);
            } else {
                log.debug("Adding string map with no name to actor/connection: " + name);
            }
        }
    }

    @Override
    public Identifier getIdentifier(String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase();
        if ((identifiers != null) && (identifiers.containsKey(key))) {
            return identifiers.get(key).second;
        } else {
            log.debug("Looking up non-existent identifier: " + name);
            return null;
        }
    }


    @Override
    public List<Identifier> getAllIdentifiersByType(String type) {
        List<Identifier> ret = new ArrayList<Identifier>();
        if (type == null || identifiers == null) {
            return ret;
        }

        String lowType = type.toLowerCase();
        Set<String> keys = identifiers.keySet();
        for (String key : keys) {
            //_first is type; _second is Identifier
            String tempType = identifiers.get(key).first;
            if (tempType == null) {
                continue;
            }
            if (lowType.equalsIgnoreCase(tempType)) {
                ret.add(identifiers.get(key).second);
            }
        }
        return ret;
    }


    /**
     * Adds a new identifier with a given type to this description.
     * This method is used when loading configuration files.
     *
     * @param name      The required name of this Identifier
     * @param type      The optional type of this Identifier
     * @param authority The identifier to add
     */
    public void addIdentifier(String name, String type, Identifier authority) {
        if (authority != null) {
            if (name != null) {
                if (identifiers == null) {
                    identifiers = new Hashtable<String, Pair<String, Identifier>>();
                }
                identifiers.put(name.toLowerCase(), new Pair<String,Identifier>(type, authority));
            } else {
                log.debug("Adding identifier with no name to actor/connection: " + this.name);
            }
        }
    }
}
