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
package org.openhie.openempi.loader.configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.AbstractFileLoader;
import org.openhie.openempi.loader.LoaderConfig;
import org.openhie.openempi.loader.LoaderDataField;
import org.openhie.openempi.loader.LoaderFieldComposition;
import org.openhie.openempi.loader.LoaderSubField;
import org.openhie.openempi.model.FormEntryDisplayType;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.ParameterType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Race;
import org.openhie.openempi.util.ConvertUtil;

public class ConfigurableFileLoader extends AbstractFileLoader
{
	private static final String UNKNOWN_NAMESPACE = "Unknown";
	public static final String LOADER_ALIAS = "configurableDataLoader";
	public final static String SKIP_HEADER_LINE = "skipHeaderLine";
	public final static String IS_IMPORT = "isImport";
	
	private String idSeq;
	protected LoaderConfig loaderConfiguration;

	public String parseFile(boolean skipHeaderLine, File file, boolean populateCustomFields) {
		loaderConfiguration = Context.getFileLoaderConfigurationService().getConfiguration();
		return super.parseFile(skipHeaderLine, file, populateCustomFields);
	}
		
	protected boolean processLine(String line, int lineIndex) {
		if (loaderConfiguration.getHeaderLinePresent()) {
			// Skip the first line if it is a header.
			if (lineIndex == 0) {
				return false;
			}
		}
		log.debug("Needs to parse the line " + line);
		Person person = new Person();
		try {
			idSeq = null;
			getPerson(line, person.getClass(), person);
			if (idSeq != null) {
				extractAndSetPersonIdentifiers(idSeq, person);
			}
			log.debug("Person is:\n" + person);

			loadPerson(person);	
			return true;
	
		} catch (ParseException e) {
			log.warn("Failed to parse file line: " + line + " due to " + e);
			return false;
		}
	}

	/**
	 * Column indexes and field names are configured in the mpi-config.xml
	 */
	private void getPerson(String line, Class personClass, Object personInstance) throws ParseException {
		Map<String,List<LoaderFieldComposition>> fieldCompositions = new HashMap<String,List<LoaderFieldComposition>>();
		String[] fields = line.split("\\,");
		List<LoaderDataField> loaderDataFields = loaderConfiguration.getDataFields();
		int i = 0;
		for (LoaderDataField loaderDataField : loaderDataFields) {
			processLoaderDataField(loaderDataField, fields[i], personClass, personInstance, fieldCompositions);
			i++;
			if (i >= fields.length)
				break;
		}
		processFieldCompositions(fieldCompositions, personClass, personInstance);
	}

	private void processFieldCompositions(Map<String,List<LoaderFieldComposition>> fieldCompositions,
			Class personClass, Object personInstance)
	{
		if (fieldCompositions.size() > 0) {
			for (Map.Entry<String, List<LoaderFieldComposition>> entry : fieldCompositions.entrySet()) {
				List<LoaderFieldComposition> value = entry.getValue();
				Collections.sort(value, new FieldCompositionComparator());
				StringBuilder stringValueBuilder = new StringBuilder("");
				for (LoaderFieldComposition fieldComp : value) {
					stringValueBuilder.append(fieldComp.getValue());
					if (fieldComp.getSeparator() != null)
						stringValueBuilder.append(fieldComp.getSeparator());
				}
				String key = entry.getKey();
				processField(key, stringValueBuilder.toString(), "", personClass, personInstance);
			}
		}
	}

	public ParameterType[] getParameterTypes() {
		List<ParameterType> types = new ArrayList<ParameterType>();
		Boolean[] trueOrFalse = { Boolean.TRUE, Boolean.FALSE };
		types.add(new ParameterType(SKIP_HEADER_LINE, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		types.add(new ParameterType(IS_IMPORT, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		return types.toArray(new ParameterType[]{});
	}
	
	private void processLoaderDataField(LoaderDataField loaderDataField, String stringValue,
			Class personClass, Object personInstance, Map<String,List<LoaderFieldComposition>> fieldCompositions)
	{
		if (!isPopulatedField(stringValue))
			return;
		String fieldName = loaderDataField.getFieldName();
		if (loaderDataField.getSubFields() != null) {
			List<LoaderSubField> subFields = loaderDataField.getSubFields();
			for (LoaderSubField subField : subFields) {
				int beginIndex = subField.getBeginIndex();
				int endIndex = subField.getEndIndex();
				if (endIndex == -1)
					endIndex = stringValue.length() - 1;
				if (beginIndex < stringValue.length() && beginIndex < endIndex) {
					endIndex = Math.min(endIndex, stringValue.length() - 1);
					processField(subField.getFieldName(), stringValue.substring(beginIndex,
							endIndex), loaderDataField.getFormatString(), personClass, personInstance);
				}
			}
		} else if (loaderDataField.getFieldComposition() != null) {
			LoaderFieldComposition fieldComp = loaderDataField.getFieldComposition();
			fieldComp.setValue(stringValue);
			if (fieldCompositions.containsKey(fieldName)) {
				List<LoaderFieldComposition> fieldComps = fieldCompositions.get(fieldName);
				fieldComps.add(fieldComp);
			} else {
				List<LoaderFieldComposition> fieldComps = new ArrayList<LoaderFieldComposition>();
				fieldComps.add(fieldComp);
				fieldCompositions.put(fieldName, fieldComps);
			}
		} else if (fieldName.length() > 0) {
			processField(fieldName, stringValue, loaderDataField.getFormatString(), personClass, personInstance);
		}
	}

	private boolean isPopulatedField(String field) {
		if (field != null && field.length() > 0) {
			return true;
		}
		return false;
	}
	
	private void processField(String fieldName, String stringValue, String formatString,
			Class personClass, Object personInstance)
	{
		if (fieldName.length() == 0)
			return;

		if (personClass == Person.class && fieldName.equals("idSequence")) {
			idSeq = stringValue;
			return;
		}

		String setMethodName = ConvertUtil.getSetMethodName(fieldName);
		Class[] paramTypes = new Class[1];

		Field field = null;
		try {
			field = personClass.getDeclaredField(fieldName);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		}

		Object value = null;
		Date dateValue = null;
		Race raceValue = null;
		Gender genderValue = null;
		Class paramClass = field.getType();
		if (paramClass == String.class) {
			value = stringValue;
		} else if (paramClass == Date.class) {
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			try {
				dateValue = format.parse(stringValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			value = dateValue;
		} else if (paramClass == Race.class) {
			raceValue = findRaceByName(stringValue);
			value = raceValue;
		} else if (paramClass == Gender.class) {
			genderValue = findGenderByCode(stringValue);
			value = genderValue;
		}

		if (value == null)
			return;
		paramTypes[0] = paramClass;
		Method setMethod = null;
		try {
			setMethod = personClass.getDeclaredMethod(setMethodName, paramTypes);
			Object[] params = new Object[1];
			params[0] = value;
			try {
				setMethod.invoke(personInstance, params);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e2) {
				e2.printStackTrace();
			} catch (InvocationTargetException e3) {
				e3.printStackTrace();
			}
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private void extractAndSetPersonIdentifiers(String idSeqParam, Person person) {
		Set<PersonIdentifier> identifiers = new HashSet<PersonIdentifier>();
		extractSetOfIdentifiers(idSeqParam, identifiers, person);
		setPersonIdentifiers(identifiers, person);
	}

	/**
	 * The assumption here is that the person identifier sequence has the following format:
	 * identifier0&namespaceIdentifier0&universalIdentifier0&universalIdentifierTypeCode0^.... ('^' separated sequence of ids.)
	 * 
	 * @param idSeq
	 * @return
	 */
	private void extractSetOfIdentifiers(String idSeq, Set<PersonIdentifier> identifiers, Person person) {
		if (idSeq == null) {
			PersonIdentifier id = new PersonIdentifier();
			id.setIdentifier(person.getPersonId().toString());
			id.setNamespaceIdentifier(UNKNOWN_NAMESPACE);
			id.setUniversalIdentifier(UNKNOWN_NAMESPACE);
			id.setUniversalIdentifierTypeCode(UNKNOWN_NAMESPACE);
			identifiers.add(id);
			return;
		}
		
		StringTokenizer idsTokenizer = new StringTokenizer(idSeq, "^");
		while (idsTokenizer.hasMoreTokens()) {
			String idCompound = idsTokenizer.nextToken();
			StringTokenizer idTokenizer = new StringTokenizer(idCompound, "&");
			int count = 0;
			PersonIdentifier id = new PersonIdentifier();
			while (idTokenizer.hasMoreTokens()) {
				String field = idTokenizer.nextToken();
				switch (count) {
				case 0:
					id.setIdentifier(field);
					break;
				case 1:
					id.setNamespaceIdentifier(field);
					break;
				case 2:
					id.setUniversalIdentifier(field);
					break;
				case 3:
					id.setUniversalIdentifierTypeCode(field);
					break;
				}
				count++;
			}
			identifiers.add(id);
		}
	}

	private void setPersonIdentifiers(Set<PersonIdentifier> identifiers, Person person) {
		for (PersonIdentifier pids : identifiers) {
			org.openhie.openempi.model.PersonIdentifier id = new org.openhie.openempi.model.PersonIdentifier();
			id.setIdentifier(pids.getIdentifier());
			id.setPerson(person);
			IdentifierDomain idDomain = new IdentifierDomain();
			idDomain.setNamespaceIdentifier(pids.getNamespaceIdentifier());
			idDomain.setUniversalIdentifier(pids.getUniversalIdentifier());
			idDomain.setUniversalIdentifierTypeCode(pids.getUniversalIdentifierTypeCode());
			id.setIdentifierDomain(idDomain);
			person.addPersonIdentifier(id);
		}
	}

	public class PersonIdentifier
	{
		private String identifier;
		private String namespaceIdentifier;
		private String universalIdentifier;
		private String universalIdentifierTypeCode;
		
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public String getNamespaceIdentifier() {
			return namespaceIdentifier;
		}
		public void setNamespaceIdentifier(String namespaceIdentifier) {
			this.namespaceIdentifier = namespaceIdentifier;
		}
		public String getUniversalIdentifier() {
			return universalIdentifier;
		}
		public void setUniversalIdentifier(String universalIdentifier) {
			this.universalIdentifier = universalIdentifier;
		}
		public String getUniversalIdentifierTypeCode() {
			return universalIdentifierTypeCode;
		}
		public void setUniversalIdentifierTypeCode(String universalIdentifierTypeCode) {
			this.universalIdentifierTypeCode = universalIdentifierTypeCode;
		}
		@Override
		public boolean equals(final Object other) {
			if (!(other instanceof PersonIdentifier))
				return false;
			PersonIdentifier castOther = (PersonIdentifier) other;
			return new EqualsBuilder().append(identifier, castOther.identifier)
					.append(namespaceIdentifier, castOther.namespaceIdentifier)
					.append(universalIdentifier, castOther.universalIdentifier)
					.append(universalIdentifierTypeCode,
							castOther.universalIdentifierTypeCode).isEquals();
		}
		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(identifier).append(
					namespaceIdentifier).append(universalIdentifier).append(
					universalIdentifierTypeCode).toHashCode();
		}
		@Override
		public String toString() {
			return new ToStringBuilder(this).append("identifier", identifier)
					.append("namespaceIdentifier", namespaceIdentifier).append(
							"universalIdentifier", universalIdentifier).append(
							"universalIdentifierTypeCode",
							universalIdentifierTypeCode).toString();
		}
		
	}
}
