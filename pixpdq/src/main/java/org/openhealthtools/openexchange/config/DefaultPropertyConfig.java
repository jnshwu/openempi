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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.utils.StringUtil;

/**
 * The class represents the default implementation of loading property 
 * configuration. Don't use the methods here to get a property. Instead,
 * always use the factory methods in PorpertyFacade.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @see PropertyFacade
 */
public class DefaultPropertyConfig implements PropertyConfig {
	private final static Log log = LogFactory.getLog(DefaultPropertyConfig.class);
	
    /** Cached set of known Properties. Map<String(filename), Properties> */
    private Map<String, Properties> allproperties = Collections.synchronizedMap(new HashMap<String, Properties>());
    /** Cached set of known local properties. Map<String(filename), Properties> */
    private Map<String, Properties> allLocalProperties = Collections.synchronizedMap(new HashMap<String, Properties>());

    /**
     * Constructor which takes an array of property files.
     * 
     * @param propertyFiles the property files
     * @throws ConfigurationException
     */
	public DefaultPropertyConfig(String[] propertyFiles, boolean required) throws ConfigurationException {  
		load(propertyFiles, required); 
	}
	
	void load(String[] propertyFiles, boolean required) throws ConfigurationException {
		if (propertyFiles == null)
			throw new ConfigurationException("No valid property file");
		
		for (String propertyFile : propertyFiles) {

			Properties properties = loadProperties(propertyFile, required);
			allproperties.put(propertyFile, properties);
			
			String localPropertFile = "local." + propertyFile;
			Properties localProperties = loadProperties(localPropertFile, false);
			if (localProperties != null) {
				allLocalProperties.put(localPropertFile, localProperties);
			}
		}
	}

	private Properties loadProperties(String propertyFile, boolean required) throws ConfigurationException {
		InputStream is = this.getClass().getResourceAsStream("/" + propertyFile.trim());
		
		if (is == null) { 
			if (propertyFile.startsWith("local.")) {
				//for local properties, just ignore it.
				return null;
			}
			if (required) {
				log.error("Cannot load " + propertyFile );
				throw new ConfigurationException("Cannot load " + propertyFile);
			}else
				return null;
		}

		log.info("Loading property file " + propertyFile );
		
		Properties properties = new java.util.Properties();
		
		try {
			properties.load(is);
		}
		catch (Exception e) {
			String message = "Failed to load properties from " + propertyFile + ". Nested message is " + e.getMessage();
			log.error(message, e);
			throw new ConfigurationException(message, e);
		}
				
		if (is != null) {
			try {
				is.close();
			}catch(IOException e) {
				log.warn("Cannot close input stream", e);
			}
		}
		return properties;
	}
	
	@Override
	public String getString(String name) {
		return getString(name, null);
	}

	@Override
	public String getString(String name, boolean required) throws ConfigurationException {
		String value = getString(name);
		if (required && !StringUtil.goodString(value)){
			throw new ConfigurationException("Required properties " + name + " not found");
		}
		return value;
	}
	
	@Override
	public String getString(String name, String defaultVal) {
		Collection<Properties> localProperties =  allLocalProperties.values();
		for (Properties property : localProperties) {
			if (!property.containsKey(name)) 
				continue;
			
			String value = property.getProperty(name);
			if (StringUtil.goodString(value))
				return value.trim();
		}

		Collection<Properties> properties =  allproperties.values();
		for (Properties property : properties) {
			if (!property.containsKey(name)) 
				continue;
			
			String value = property.getProperty(name);
			if (StringUtil.goodString(value))
				return value.trim();
		}
		return defaultVal;
	}

	@Override
	public String[] getStringArray(String name){
		String values = getString(name);
		
		if (!StringUtil.goodString(values)) {
			return null;
		}

		String[] ret = values.split("\\s*[,:;]\\s*");
		return ret;
	}
	
	@Override
	public String[] getStringArray(String name, boolean required) throws ConfigurationException {
		String[] values = getStringArray(name);

		if (required && null == values){
			throw new ConfigurationException("Required properties " + name + " not found");
		}
		
		return values;
	}

	@Override
	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	@Override
	public boolean getBoolean(String name, boolean defaultVal) {
		Collection<Properties> localProperties =  allLocalProperties.values();
		for (Properties property : localProperties) {
			if (!property.containsKey(name))
				continue;
			
			String value = property.getProperty(name);
			if (StringUtil.goodString(value)) {
				if (value.equalsIgnoreCase("true"))
					return true;
				else if (value.equalsIgnoreCase("false"))
					return false;
			}
		}

		Collection<Properties> properties =  allproperties.values();
		for (Properties property : properties) {
			if (!property.containsKey(name))
				continue;
			
			String value = property.getProperty(name);
			if (StringUtil.goodString(value)) {
				if (value.equalsIgnoreCase("true"))
					return true;
				else if (value.equalsIgnoreCase("false"))
					return false;
			}
		}
		return defaultVal;
	}

	@Override
	public int getInteger(String name) {
		return getInteger(name, -1);
	}

	@Override
	public int getInteger(String name, boolean required) throws ConfigurationException {
		int ret = -1;
    	try {
    		ret = Integer.parseInt(getString(name, required));        
    	}catch(Exception e) {
    		log.error("The " + name + " property cannot be parsed", e);
    		//Set to the default value 
    		if (required){
    			throw new ConfigurationException("Required properties " + name + " not found");
    		}
    	}
    	return ret;
	}
	
	@Override
	public int getInteger(String name, int defaultVal) {
		int ret;
    	try {
    		if(getString(name) != null){
    			ret = Integer.parseInt(getString(name));
    		}else{
    			ret = defaultVal;
    		}       
    	}catch(Exception e) {
    		log.warn("The " + name + " property cannot be parsed", e);
    		//Set to the default value 
    		ret = defaultVal; 
    	}
    	return ret;
	}

	@Override
	public int[] getIntegerArray(String name){
		String[] stringArray = getStringArray(name);
		
		if (null == stringArray)
			return null;
		
	    int[] intArray = new int[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			intArray[i] = Integer.parseInt(stringArray[i]);
		}
		return intArray;
	}
	
	@Override
	public int[] getIntegerArray(String name, boolean required) throws ConfigurationException {
		int[] values = getIntegerArray(name);
		
		if (required && null == values){
			throw new ConfigurationException("Required properties " + name + " not found");
		}
		
		return values;
	}

	@Override
	public boolean contains(String name) {
		Collection<Properties> localProperties =  allLocalProperties.values();
		for (Properties property : localProperties) {
			if (property.containsKey(name))
				return true;
		}
		
		Collection<Properties> properties =  allproperties.values();
		for (Properties property : properties) {
			if (property.containsKey(name))
				return true;
		}
		     
		return false;
	}

}
