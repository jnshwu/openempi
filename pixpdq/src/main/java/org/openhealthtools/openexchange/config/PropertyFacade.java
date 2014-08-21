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

package org.openhealthtools.openexchange.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.utils.StringUtil;

/**
 * The facade class to get the properties defined in property files. Be sure
 * to call the loadProperties method first to load the properties before
 * invoking the getString, getBoolean and getInteger etc methods.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class PropertyFacade {
	private static final Log log = LogFactory.getLog(PropertyFacade.class);

	/**The property configuration*/
	private static PropertyConfig config = null;

    /**
     * Loads properties from an array of property files. Be sure
     * to call the loadProperties method first to load the properties 
     * before invoking the getString, getBoolean and getInteger etc other methods.
     *
     * @param name the property file names
     *  
     * @return True if the specified property config loaded successfully
     */
    public synchronized static boolean loadProperties(String[] fileNames) throws ConfigurationException {
    	return loadProperties(fileNames, true);
    }
    
    /**
     * Loads properties from an array of property files. Be sure
     * to call the loadProperties method first to load the properties 
     * before invoking the getString, getBoolean and getInteger etc other methods.
     *
     * @param name the property file names
     * @param required whether the property files are required
     *  
     * @return True if the specified property config loaded successfully
     */
    public synchronized static boolean loadProperties(String[] fileNames, boolean required) throws ConfigurationException {
        //Get the bootstrap property config class
        String propertyConfigClass = BootStrapProperties.getPropertyConfigClass();

		if (!StringUtil.goodString(propertyConfigClass) || 
			propertyConfigClass.equals("org.openhealthtools.openexchange.config.DefaultPropertyConfig")) {
    		//Use the default property config
			if (log.isInfoEnabled()) {
				log.info("Loading properties from " + propertyConfigClass);
			}
			if (null == config) 
				config = new DefaultPropertyConfig(fileNames, required);
			else 
				((DefaultPropertyConfig)config).load(fileNames, required);
    	} else {
	    	try {
				Class clazz = Class.forName(propertyConfigClass);
				
				if (log.isInfoEnabled()) {
					log.info("Loading properties from " + propertyConfigClass);
				}
				config = (PropertyConfig)clazz.newInstance();
			} catch (Exception e) {
				String message = "Failed to load properties. Nested message is " + e.getMessage();
				log.fatal(message, e);
				throw new ConfigurationException(message, e);
			}
    	}
		
		return true;
    }

    /**
     * Clears the existing configured properties.
     */
    public synchronized static void clearProperties() {
    	config = null;
    }
    
    /**
     * Checks whether the properties are loaded. 
     * @return <code>true</code> if loaded, otherwise <code>false</code>
     */
    public static boolean isPropertyConfiged() {
    	return config == null ? false : true;
    }
    
    /**
     * Gets the property configuration. Be sure {@link #loadProperties(String[])} is
     * invoked before calling this method.
     * 
     * @return the PropertyConfig object
     * @see #loadProperties(String[])  
     */
    public static PropertyConfig getPropertyConfig() {
    	return config;
    }
    
	/**
	 * Gets the String value of the given property name
	 * 
	 * @param name the property name
	 * @return the String value; null if not found
	 */
	public static String getString(String name) {
		validateName(name);
		
		return config.getString(name);
	}
	
	/**
	 * Gets the String value of the given property name
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the String value
	 * @throws ConfigurationException if the required property is not found
	 */
	public static String getString(String name, boolean required) throws ConfigurationException {
		validateName(name);
		
        return config.getString(name, required);
	}
	
	/**
	 * Gets the String value of the given property name
	 * 
	 * @param name the property name
	 * @defaultVal the default value
	 * @return the String value; return defaultVal if not found
	 */
	public static String getString(String name, String defaultVal) {
		validateName(name);
		
		return config.getString(name, defaultVal);
	}
	
	/** 
	 * Gets the String Array of values of the given property name
	 * 
	 * @param name the property name
	 * @return the String Array; null if not found
	 */
	public static String[] getStringArray(String name) {
		validateName(name);
		
		return config.getStringArray(name);
	}
	
	/**
	 * Gets the String Array of values of the given property name
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the String Array
	 * @throws ConfigurationException if the required property is not found
	 */
	public static String[] getStringArray(String name, boolean required) throws ConfigurationException {
		validateName(name);
		
		return config.getStringArray(name, required);
	}
	
	/**
	 * Gets the boolean value of the given property name.
	 *  
	 * @param name the property name
	 * @return the boolean value; false if not found
	 */
	public static boolean getBoolean(String name) {
		validateName(name);
		
		return config.getBoolean(name);
	}

	/**
	 * Gets the boolean value of the given property name.
	 *  
	 * @param name the property name
	 * @defaultVal the default value
	 * @return the boolean value; defaultValue if not found
	 */
	public static boolean getBoolean(String name, boolean defaultVal) {
		validateName(name);
		
		return config.getBoolean(name, defaultVal);
	}
	
	/**
	 * Gets the int value of the given property name.
	 * 
	 * @param name the property name
	 * @return the integer value; -1 if not found
	 */
	public static int getInteger(String name) {
		validateName(name);
		
		return config.getInteger(name);
	}

	/**
	 * Gets the int value of the given property name.
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the integer value
	 * @throws ConfigurationException if the required property is not found
	 */
	public static int getInteger(String name, boolean required) throws ConfigurationException {
		validateName(name);

		return config.getInteger(name, required);
	}

	/**
	 * Gets the int value of the given property name.
	 * 
	 * @param name the property name
	 * @defaultVal the default value
	 * @return the integer value; if not found, return defaultValue
	 */
	public static int getInteger(String name, int defaultVal) {
		validateName(name);
		
		return config.getInteger(name, defaultVal);
	}
 
	/** 
	 * Gets the Integer Array of values of the given property name
	 * 
	 * @param name the property name
	 * @return the integer Array; null if the property not found.
	 */
	public static int[] getIntegerArray(String name) {
		validateName(name);
		
		return config.getIntegerArray(name);
	}
	
	/** 
	 * Gets the Integer Array of values of the given property name
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the integer Array
	 * @throws ConfigurationException if the required property is not found
	 */
	public static int[] getIntegerArray(String name, boolean required) throws ConfigurationException {
		validateName(name);
		
		return config.getIntegerArray(name, required);
	}
	
	/**
	 * Checks to make sure the property name is a valid String.
	 * 
	 * @param name the property name
	 */
	private static void validateName(String name) {
		if (!StringUtil.goodString(name))
			throw new IllegalArgumentException("Invalid Property Name");		
	}
	
	/**
	 * Checks whether a given property name is contained in the properties.
	 * 
	 * @param name the property name to check
	 * @return <code>true</code> if the property name is found.
	 */
	public static boolean contains(String name) {
		validateName(name);

		return config.contains(name);
	}
}
