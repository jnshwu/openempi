/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConvertingWrapDynaBean extends org.apache.commons.beanutils.ConvertingWrapDynaBean
{
	private static final long serialVersionUID = -370897935825063946L;
	
	protected final Log log = LogFactory.getLog(getClass());

	public ConvertingWrapDynaBean(Object instance) {
		 super(instance);
	 }

	/**
	 * In addition to be able to retrieve properties stored in the record using
	 * the built-in capabilities of the BeansUtils interface, we also added the
	 * capability of retrieving a value from a property that has a one-to-many
	 * relationship to the base object.
	 * 
	 * For example, we want to be able to retrieve one of the identifiers associated
	 * with a person object and person has a one-to-many association with person
	 * identifiers. To access a specific identifier using the identifier domain
	 * as the key to the search, you need to use the following syntax in the
	 * addressing of the property.
	 * 
	 * basePropertyName:searchKey:property
	 * basePropertyName is the name of the property in the base object that has
	 * 		the one-to-many association. This property should be of type Set.
	 * searchKey: is the string to use to identify the one of multiple possible
	 * 		values in the set.
	 * property: the attribute from the instance of the object in the set that
	 * 		should be returned.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public Object get(String name) {
		 Object value = null;
		 try {	
			 if (name.indexOf(':') >= 0) {
				 int posOfColon = name.indexOf(':');
				 String key = name.substring(0, posOfColon);
				 value = PropertyUtils.getNestedProperty(instance, key);
				 if (value instanceof java.util.Set) {
					 java.util.Set set = (java.util.Set) value;
					 int secondColon = name.indexOf(':', posOfColon+1);
					 String searchKey = name.substring(posOfColon+1,secondColon);
					 for (Object val : set) {
						 if (val.toString().indexOf(searchKey) >= 0) {
							 String propertyName = name.substring(secondColon+1, name.length());
							 Object pval = PropertyUtils.getNestedProperty(val, propertyName);
							 return pval;
						 }
					 }
					 return null;
				 }
			 }
			 value = PropertyUtils.getNestedProperty(instance, name);
		 } catch (InvocationTargetException ite) {
			 Throwable cause = ite.getTargetException();
			 throw new IllegalArgumentException("Error reading property '" + name +
					 "' nested exception - " + cause);
		 } catch (Throwable t) {
			 throw new IllegalArgumentException("Error reading property '" + name +
					 "', exception - " + t);
		 }
		 return (value);
	 }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public Set<String> getPropertyNames() {
		 Set<String> properties = new HashSet<String>();
		 try {
			 Map map = BeanUtils.describe(getInstance());
			 for (Iterator<String> iter = (Iterator<String>) map.keySet().iterator(); iter.hasNext(); ) {
				 String name = iter.next();
				 if (!name.equals("class")) {
					 properties.add(name);
				 }
			 }
		 } catch (Exception e) {
			 log.warn("Failed while extracting bean properties: " + e, e);
		 }
		 return properties;
	 }
}
