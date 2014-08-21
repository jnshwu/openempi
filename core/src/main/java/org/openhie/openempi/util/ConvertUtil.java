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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Criteria;
import org.openhie.openempi.model.Criterion;
import org.openhie.openempi.model.ExtendedCriterion;
import org.openhie.openempi.model.LabelValue;
import org.openhie.openempi.model.Operation;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.model.ReviewRecordPair;

/**
 * Utility class to convert one object to another.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Extended by
 *         <a href="mailto:odysseas@sysnetint.com">Odysseas Pentakalos</a> to
 *         support conversion between POJO trees and Hashmap of properties.
 */
public final class ConvertUtil
{
	private static final Log log = LogFactory.getLog(ConvertUtil.class);

	/**
	 * Checkstyle rule: utility classes should not have public constructor
	 */
	private ConvertUtil() {
	}

	/**
	 * Method to convert a ResourceBundle to a Map object.
	 * 
	 * @param rb
	 *            a given resource bundle
	 * @return Map a populated map
	 */
	public static Map<String, String> convertBundleToMap(ResourceBundle rb) {
		Map<String, String> map = new HashMap<String, String>();

		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			map.put(key, rb.getString(key));
		}

		return map;
	}

	public static Record getRecordFromPerson(Person person) {
		Record record = new Record(person);
		if (person.getPersonId() != null) {
			record.setRecordId(new Long(person.getPersonId()));
		}
		return record;
	}

	// private static String extractAttributeNameFromIdentifierDomainName(String
	// name) {
	// if (name == null && name.length() == 0) {
	// return "";
	// }
	// StringBuffer buf = new StringBuffer();
	// for (int i=0; i < name.length(); i++) {
	// if (!Character.isJavaIdentifierPart(name.charAt(i))) {
	// buf.append("_");
	// } else {
	// buf.append(name.charAt(i));
	// }
	// }
	// return buf.toString();
	// }

	/**
	 * Convert a java.util.List of LabelValue objects to a LinkedHashMap.
	 * 
	 * @param list
	 *            the list to convert
	 * @return the populated map with the label as the key
	 */
	public static Map<String, String> convertListToMap(List<LabelValue> list) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		for (LabelValue option : list) {
			map.put(option.getLabel(), option.getValue());
		}

		return map;
	}

	/**
	 * Method to convert a ResourceBundle to a Properties object.
	 * 
	 * @param rb
	 *            a given resource bundle
	 * @return Properties a populated properties object
	 */
	public static Properties convertBundleToProperties(ResourceBundle rb) {
		Properties props = new Properties();

		for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			props.put(key, rb.getString(key));
		}

		return props;
	}

	/**
	 * Convenience method used by tests to populate an object from a
	 * ResourceBundle
	 * 
	 * @param obj
	 *            an initialized object
	 * @param rb
	 *            a resource bundle
	 * @return a populated object
	 */
	public static Object populateObject(Object obj, ResourceBundle rb) {
		try {
			Map<String, String> map = convertBundleToMap(rb);
			BeanUtils.copyProperties(obj, map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception occurred populating object: " + e.getMessage());
		}

		return obj;
	}

	public static Object cloneBean(Object obj) {
		Object clone = null;
		;
		try {
			clone = BeanUtils.cloneBean(obj);
		} catch (Exception e) {
			log.warn("Unable to clone object: " + obj + ". Error: " + e, e);
		}
		return clone;
	}

	public static List<String> extractProperties(Object obj) {
		List<String> properties = new ArrayList<String>();
		Map<String, String> visitMap = new HashMap<String, String>();
		visitMap.put(obj.getClass().getName(), obj.getClass().getName());
		WrapDynaClass topClass = WrapDynaClass.createDynaClass(obj.getClass());
		extractClassProperties(topClass, visitMap, properties, "");
		return properties;
	}

	private static void extractClassProperties(WrapDynaClass theClass, Map<String, String> visitMap,
			List<String> properties, String parent) {
		if (theClass == null) {
			return;
		}
		for (DynaProperty property : theClass.getDynaProperties()) {
			boolean visitedAlready = (visitMap.get(property.getType().getName()) != null);
			log.debug("Checking to see if type " + property.getType().getName() + " has been visited already returns "
					+ visitedAlready);
			if (!property.getType().getName().startsWith("java") && !visitedAlready) {
				WrapDynaClass dynaClass = WrapDynaClass.createDynaClass(property.getType());
				extractClassProperties(dynaClass, visitMap, properties, parent + property.getName() + ".");
			} else {
				if (!property.getType().getName().equalsIgnoreCase("java.lang.Class")) {
					log.debug("Adding type " + property.getType().getName() + " to the list of types visited already.");
					visitMap.put(property.getType().getName(), property.getType().getName());
					properties.add(parent + property.getName());
				}
			}
		}
	}

	public static String getModifiedFieldName(String fieldName, String prefix) {
		StringBuilder modifiedName = new StringBuilder(fieldName);
		modifiedName.setCharAt(0, Character.toUpperCase(modifiedName.charAt(0)));
		modifiedName.insert(0, prefix);
		return modifiedName.toString();
	}

	public static String getSetMethodName(String fieldName) {
		return getModifiedFieldName(fieldName, "set");
	}

	public static String getSerializedFieldName(String fieldName) {
		return getModifiedFieldName(fieldName, "serialized");
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	public static void serializeObject(String configDirectory, String fileName, Object o) {
		String fullFilename = configDirectory + "/" + fileName;
		log.debug("Attempting to serialize object into file: " + fullFilename);
		try {
			ObjectOutputStream ois = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(fullFilename)));
			ois.writeObject(o);
			ois.flush();
			ois.close();
		} catch (Exception e) {
			log.error("Failed while serializing object (into the file" + fullFilename + " ): " + e.getMessage(), e);
			throw new RuntimeException("Failed while serializing object (into the file" + fullFilename + " ): "
					+ e.getMessage());
		}
	}

	public static Object deserializeObject(String configDirectory, String fileName) {
		Object obj;
		String fullFilename = configDirectory + "/" + fileName;
		log.debug("Attempting to deserialize object from file: " + fullFilename);
		try {
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fullFilename)));
			obj = ois.readObject();
			ois.close();
		} catch (Exception e) {
			log.error("Failed while deserializing object (from file " + fullFilename + "): " + e.getMessage(), e);
			throw new RuntimeException("Failed while deserializing object (from file " + fullFilename + "): "
					+ e.getMessage());
		}
		return obj;
	}

	public static boolean isValidCustomFieldName(String fieldName) {
		if (!fieldName.startsWith("custom") && !fieldName.startsWith("bcustom")) {
			return false;
		} else {
			String fieldNumberStr = null;
			if (fieldName.startsWith("custom"))
				fieldNumberStr = fieldName.substring(6);
			else if (fieldName.startsWith("bcustom"))
				fieldNumberStr = fieldName.substring(7);
			Integer fieldNumber = Integer.valueOf(fieldNumberStr); // That
																	// allows
																	// signs and
																	// other
																	// stuff
																	// also
			if (fieldNumber <= 0 || fieldNumber > Constants.CUSTOM_FIELD_MAX_NUMBER) {
				return false;
			}
		}
		return true;
	}

	public static List<String> getAllModelAttributeNames(Record record) {
		java.util.Set<String> propertySet = record.getPropertyNames();
		java.util.List<String> sortedList = new java.util.ArrayList<String>(propertySet.size());
		for (String property : propertySet) {
			sortedList.add(property);
		}
		Collections.sort(sortedList);
		return sortedList;
	}

	public static List<String> getModelAttributeNames(Record record, boolean needCustomFields) {
		java.util.Set<String> propertySet = record.getPropertyNames();
		java.util.List<String> sortedList = new java.util.ArrayList<String>(propertySet.size());
		for (String property : propertySet) {
			if (!(needCustomFields ^ isValidCustomFieldName(property)))
				sortedList.add(property);
		}
		Collections.sort(sortedList);
		return sortedList;
	}

	public static Criteria buildCriteriaFromProperties(Record record) {
		Set<String> propertyNames = record.getPropertyNames();
		Criteria criteria = new Criteria();
		for (String property : propertyNames) {
			Object value = record.get(property);
			if (value != null && (value instanceof java.lang.String || value instanceof java.util.Date)) {
				Criterion criterion = new Criterion();
				criterion.setName(property);
				criterion.setOperation(Operation.LIKE);
				criterion.setValue(value);
				criteria.addCriterion(criterion);
			}
		}
		return criteria;
	}

	public static void addIndirectCriteria(Person person, Criteria criteria) {
		if (person.getGender() != null && person.getGender().getGenderCode() != null) {
			ExtendedCriterion criterion = new ExtendedCriterion();
			criterion.setAlias("gender");
			criterion.setAssociationPath("gender");
			criterion.setName("gender.genderCode");
			criterion.setOperation(Operation.EQ);
			criterion.setValue(person.getGender().getGenderCode());
			criteria.addCriterion(criterion);
		}
	}

	public static List<RecordPair> generateRecordPairs(Record record, Collection<Record> records) {
		List<RecordPair> pairs = new java.util.ArrayList<RecordPair>(records.size());
		for (Record entry : records) {
			if (record.getRecordId() != null && record.getRecordId().longValue() == entry.getRecordId().longValue()) {
				// Skip the record itself if it happens to be one of the entries
				// in the repository
				continue;
			}
			log.debug("BLink, " + record.getRecordId() + "," + entry.getRecordId());
			pairs.add(new RecordPair(record, entry));
		}
		return pairs;
	}

	public static ReviewRecordPair buildReviewPair(RecordPair pair) {
		ReviewRecordPair review = new ReviewRecordPair();
		review.setDateCreated(new java.util.Date());
		review.setLinkSource(pair.getLinkSource());
		review.setPersonLeft((Person) pair.getLeftRecord().getObject());
		review.setPersonRight((Person) pair.getRightRecord().getObject());
		review.setUserCreatedBy(Context.getUserContext().getUser());
		review.setWeight(pair.getWeight());
		return review;
	}
}
