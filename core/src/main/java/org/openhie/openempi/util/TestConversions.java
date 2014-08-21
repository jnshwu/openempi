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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.Record;

public class TestConversions
{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Person person = new Person();
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Oak Hill");
		person.setFamilyName("Pentakalos");
		person.setGivenName("Odysseas");
		Nationality nationality = new Nationality();
		nationality.setNationalityCd(100);
		person.setNationality(nationality);
		person.setDateOfBirth(new java.util.Date());

		PersonIdentifier id = new PersonIdentifier();
		id.setIdentifier("1234");
		IdentifierDomain domain = new IdentifierDomain();
		domain.setIdentifierDomainName("testDomain");
		domain.setNamespaceIdentifier("testDomain");
		id.setIdentifierDomain(domain);
		person.addPersonIdentifier(id);
		
		id = new PersonIdentifier();
		id.setIdentifier("4444");
		domain = new IdentifierDomain();
		domain.setIdentifierDomainName("anotherDomain");
		domain.setNamespaceIdentifier("anotherDomain");
		id.setIdentifierDomain(domain);
		person.addPersonIdentifier(id);
		
		Record record = new Record(person);
		String[] matchingAttributes = {"givenName", "familyName"};
		for (String attribute : matchingAttributes) {
			System.out.println("The record has a value of " + record.getAsString(attribute) + " for matching field " + attribute);
		}
		
		String attribute = "personIdentifiers:testDomain:identifier";
		System.out.println("The value of attribute " + attribute + " is " + record.get(attribute));
		attribute = "personIdentifiers:anotherDomain:identifier";
		System.out.println("The value of attribute " + attribute + " is " + record.get(attribute));
		
		java.util.Set<String> propertySet = record.getPropertyNames();
		for (String property : propertySet) {
			System.out.println("Object has the following property: " + property);
			Object value = record.get(property);
			if (value != null) {
				System.out.println("The record has a value of " + value + " of type " + value.getClass() + " for field " + property);
				if (property.equalsIgnoreCase("personIdentifiers")) {
					@SuppressWarnings("unchecked")
					java.util.Set<Object> set = (java.util.Set<Object>) value;
					for (Object key : set) {
						System.out.println("Property  " + property + " key value pair of (" + key.toString());
					}
				}
			}
		}
	}

	public static void exploringBeanUtils() {
		Person person = new Person();
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Oak Hill");
		PersonIdentifier id = new PersonIdentifier();
		id.setIdentifier("1234");
		IdentifierDomain domain = new IdentifierDomain();
		domain.setIdentifierDomainName("testDomain");
		domain.setNamespaceIdentifier("testDomain");
		id.setIdentifierDomain(domain);
		person.addPersonIdentifier(id);
		
		Nationality nationality = new Nationality();
		nationality.setNationalityCd(100);
		person.setNationality(nationality);
		person.setDateOfBirth(new java.util.Date());
		
		ConvertingWrapDynaBean bean = new ConvertingWrapDynaBean(person);
		System.out.println("Build a dyna bean using my person:");
		System.out.println(bean.get("address1"));
		System.out.println(bean.get("dateOfBirth"));
		
		System.out.println("Changing some of the values.");
		bean.set("givenName", "Odysseas");
		bean.set("familyName", "Pentakalos");
		System.out.println(bean.get("nationality.nationalityCd"));
		bean.set("nationality.nationalityCd", "150");
		System.out.println("Value " + bean.get("nationality.nationalityCd") + " is of type " + bean.get("nationality.nationalityCd").getClass());
		person = (Person) bean.getInstance();
		System.out.println(person);
		
		List<String> properties = ConvertUtil.extractProperties(person);
		for (String property : properties) {
			System.out.println("Property name is: " + property);
		}

//		DynaProperty[] properties = bean.getDynaClass().getDynaProperties();
//		for (DynaProperty property : properties) {
//			System.out.println("The map has the property: " + property.getName() + " which is mapped " + property.getType());
//			if (property.getType().getName().startsWith("org.openhie")) {
//				WrapDynaClass dynaClass = WrapDynaClass.createDynaClass(property.getType());
//				DynaProperty[] internalProperties = dynaClass.getDynaProperties();
//				for (DynaProperty internalProperty : internalProperties) {
//					System.out.println("The map has the property: " + property.getName() + "." + internalProperty.getName());
//				}
//			}
//		}
		
//		BeanMap beanMap = new BeanMap(person);
//		Set<String> properties = beanMap.keySet();
//		for (String key : properties) {
//			System.out.println("The map has the property: " + key);
//		}
		
		org.apache.commons.beanutils.converters.DateConverter converter = new org.apache.commons.beanutils.converters.DateConverter();
		converter.setPattern("yyyy.MM.dd HH:mm:ss z");
		String[] patterns = converter.getPatterns();
		for (String pattern : patterns) {
			System.out.println("Pattern is " + pattern);
		}
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
		System.out.println(sdf.format(now));
		
		ConvertUtils.register(converter, java.util.Date.class);
		ConvertUtils convertUtils = new ConvertUtils();
		System.out.println(convertUtils.convert("2009.03.06 15:13:29 EST", java.util.Date.class));
		
		try {
			BeanUtils.setProperty(person, "dateOfBirth", "2009.03.06 15:13:29 EST");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(bean.get("dateOfBirth"));
		
		System.out.println(bean.getDynaClass().getDynaProperty("dateOfBirth"));
		bean.set("dateOfBirth", "2009.03.06 15:13:29 EST");
		System.out.println(bean.get("dateOfBirth"));	
	}
}
