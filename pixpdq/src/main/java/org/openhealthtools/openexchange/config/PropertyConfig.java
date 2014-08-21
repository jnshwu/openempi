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


/**
 * This interface defines the methods such as getString, getBoolean,
 * and getInteger etc from property files. 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @see SpringConfig
 */
public interface PropertyConfig {

	/**
	 * Gets the String value of the given property name
	 * 
	 * @param name the property name
	 * @return the String value; null if not found
	 */
	public String getString(String name);

	
	/**
	 * Gets the String value of the given property name
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the String value
	 * @throws ConfigurationException if the required property is not found
	 */
	public String getString(String name, boolean required) throws ConfigurationException;

	/**
	 * Gets the String value of the given property name
	 * 
	 * @param name the property name
	 * @defaultVal the default value
	 * @return the String value; return defaultVal if not found
	 */
	public String getString(String name, String defaultVal);
	
	/** 
	 * Gets the String Array of values of the given property name
	 * 
	 * @param name the property name
	 * @return the String Array; null if not found
	 */
	public String[] getStringArray(String name);
	
	/**
	 * Gets the String Array of values of the given property name
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the String Array
	 * @throws ConfigurationException if the required property is not found
	 */
	public String[] getStringArray(String name, boolean required) throws ConfigurationException;

	/**
	 * Gets the boolean value of the given property name.
	 *  
	 * @param name the property name
	 * @return the boolean value; false if not found
	 */
	public boolean getBoolean(String name); 
	
	/**
	 * Gets the boolean value of the given property name.
	 *  
	 * @param name the property name
	 * @defaultVal the default value
	 * @return the boolean value; defaultValue if not found
	 */
	public boolean getBoolean(String name, boolean defaultVal); 

	/**
	 * Gets the int value of the given property name.
	 * 
	 * @param name the property name
	 * @return the integer value; -1 if not found
	 */
	public int getInteger(String name);  

	/**
	 * Gets the int value of the given property name.
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the integer value
	 * @throws ConfigurationException if the required property is not found
	 */
	public int getInteger(String name, boolean required) throws ConfigurationException;  
	
	/**
	 * Gets the int value of the given property name.
	 * 
	 * @param name the property name
	 * @defaultVal the default value
	 * @return the integer value;  if not found, return defaultValue
	 */
	public int getInteger(String name, int defaultVal);  

	/** 
	 * Gets the Integer Array of values of the given property name
	 * 
	 * @param name the property name
	 * @return the integer Array; null if the property not found.
	 */
	public int[] getIntegerArray(String name);
	
	/** 
	 * Gets the Integer Array of values of the given property name
	 * 
	 * @param name the property name
	 * @param required whether the property is required
	 * @return the integer Array
	 * @throws ConfigurationException if the required property is not found
	 */
	public int[] getIntegerArray(String name, boolean required) throws ConfigurationException;

	/**
	 * Checks whether a given property name is contained in the properties.
	 * 
	 * @param name the property name to check
	 * @return <code>true</code> if the property name is found.
	 */
	public boolean contains(String name);
}
