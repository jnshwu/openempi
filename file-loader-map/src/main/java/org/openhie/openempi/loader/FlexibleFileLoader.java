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
package org.openhie.openempi.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.configuration.xml.model.Datatype;
import org.openhie.openempi.configuration.xml.model.FieldType;
import org.openhie.openempi.configuration.xml.model.FileLoaderMap;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.context.UserContext;
import org.openhie.openempi.model.FormEntryDisplayType;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.ParameterType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.Race;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class FlexibleFileLoader extends AbstractFileLoader
{
	public final static String DEFAULT_DATE_FORMAT = "yyyyMMdd";
	
	private ThreadPoolTaskExecutor taskExecutor;
	private UserContext userContext;
	private FileLoaderMap fileMap;
	private Map<Integer,FieldType> fieldMapByColumnIndex = new HashMap<Integer,FieldType>();
	private Map<String,Method> methodByFieldName = new HashMap<String,Method>();
	private String delimiter;
	private Pattern delimitingPattern;
	private TrainingDataExtractor trainingDataExtractor;
	private Map<Serializable,Set<Integer>> cacheOfRecordsLoaded;

	public final static String GENDER = "gender";
	public final static String ETHNIC_GROUP = "ethnicGroup";
	public final static String RACE = "race";
	public final static String NATIONALITY = "nationality";
	public final static String LANGUAGE = "language";
	public final static String RELIGION = "religion";
	
	private String[] referenceTypes = { GENDER, ETHNIC_GROUP, RACE, NATIONALITY, LANGUAGE, RELIGION };
	private Map<String,String> referenceTypeMap = new HashMap<String,String>();

	public FlexibleFileLoader() {
		userContext = Context.getUserContext();
		for (String referenceType : referenceTypes) {
			referenceTypeMap.put(referenceType, referenceType);
		}
	}
	
	@Override
	public void init() {
		log.debug("Initializing the loader in " + getClass() + " using configuration map: " + getPersonLoaderManager().getPropertyMap());
		String mappingFilename = getParameterAsString(MAPPING_FILE);
		if (mappingFilename == null || mappingFilename.length() == 0) {
			log.error("Unable to initialize the flexible file loader since a mapping file has not been specified.");
			throw new RuntimeException("Unable to initialize the flexible file loader since a mapping file has not been specified.");
		}
		File file = null;
		if (mappingFilename.startsWith("/")) {
			log.debug("Opening file loader mapping file at: " + mappingFilename);
			file = new File(mappingFilename);
		} else {
			String openEmpiHome = Context.getOpenEmpiHome();
			String filename = openEmpiHome + "/conf/" + mappingFilename;
			log.debug("Opening file loader mapping file at: " + filename);
			file = new File(filename);
		}
		if (!file.exists() || !file.canRead()) {
			log.error("Unable to initialize the flexible file loader; the mapping file must exist and be readable.");
			throw new RuntimeException("Unable to initialize the flexible file loader; the mapping file must exist and be readable.");			
		}
		loadMappingConfiguration(file);
		
		// If the dataset includes training data then need to keep track of common link identifiers. 
		if (trainingDataExtractor != null) {
			cacheOfRecordsLoaded = new HashMap<Serializable,Set<Integer>>();
		}
	}
	
	private void loadMappingConfiguration(File file) {
		try {
			JAXBContext jc = JAXBContext.newInstance( FileLoaderMap.class );
			Unmarshaller u = jc.createUnmarshaller();
	        fileMap = (FileLoaderMap) u.unmarshal(new FileInputStream(file));
	        if (fileMap.isHeaderFirstLine() != null && fileMap.isHeaderFirstLine()) {
	        	this.setParameter(SKIP_HEADER_LINE, Boolean.TRUE);
	        }
	        delimiter = fileMap.getDelimeter();
	        setupTrainingDataExtractor(fileMap.getTrainingDataExtractor());
	        log.debug("File will be parsed using the delimeter: " + delimiter);
	        delimitingPattern = Pattern.compile(delimiter);
	        for (int i=0; i < fileMap.getFields().getField().size(); i++) {
	        	FieldType field = fileMap.getFields().getField().get(i);
	        	fieldMapByColumnIndex.put(new Integer(i+1), field);
	        	if (field.isOneToMany()) {
	        		log.debug("Field will be decomposed into subfields using delimeter: " + field.getDelimeter());
	        		for (FieldType subfield : field.getSubfields().getField()) {
		        		log.debug("Will import field " + subfield.getFieldName() + " which should appear at column " + i);	        			
	        		}
	        	} else {
	        		log.debug("Will import field " + field.getFieldName() + " which should appear at column " + i);
	        	}
	        }
	        log.debug("Finished processing file loader map");
		} catch (Exception e) {
			log.error("Unable to initialize the flexible file loader; the mapping file is not valid: " + e, e);
			throw new RuntimeException("Unable to initialize the flexible file loader; the mapping file is not valid.");
		}		
	}

	private void setupTrainingDataExtractor(String trainingDataExtractorName) {
		if (trainingDataExtractorName == null) {
			return;
		}
		try {
			trainingDataExtractor = (TrainingDataExtractor) Context.getApplicationContext()
					.getBean(trainingDataExtractorName);
			log.info("Will extract training data links using implementation: " + trainingDataExtractorName);
		} catch (Exception e) {
			log.warn("Unable to find a training data extractor named " + trainingDataExtractor + " so, no training data links will be generated.");
		}
	}

	public ParameterType[] getParameterTypes() {
		List<ParameterType> types = new ArrayList<ParameterType>();
		Boolean[] trueOrFalse = { Boolean.TRUE, Boolean.FALSE };
		types.add(new ParameterType(SKIP_HEADER_LINE, SKIP_HEADER_LINE_DISPLAY, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		types.add(new ParameterType(IS_IMPORT, IS_IMPORT_DISPLAY, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		types.add(new ParameterType(MAPPING_FILE, MAPPING_FILE_DISPLAY, FormEntryDisplayType.TEXT_FIELD));
		types.add(new ParameterType(PREVIEW_ONLY, PREVIEW_ONLY_DISPLAY, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		return types.toArray(new ParameterType[]{});
	}
	
	@Override
	protected boolean processLine(String line, int lineIndex) {
		RecordParseTask parser = new RecordParseTask(line, lineIndex);
		Future<Object> future = taskExecutor.getThreadPoolExecutor().submit(parser, new Object());
		try {
			future.get();
			return true;
		} catch (InterruptedException e) {
			log.warn("Failed while processing the following line from position " + lineIndex + " in the file.\nError message: " + e + "\n" + line, e);
			return false;
		} catch (ExecutionException e) {
			return false;
		} catch (Throwable e) {
			log.warn("Failed while processing the following line from position " + lineIndex + " in the file.\nError message: " + e + "\n" + line, e);
			return false;
		}
	}
	
	public void loadPerson(Serializable key, Person person) {
		person.setUserCreatedBy(Context.getUserContext().getUser());
		person.setUserChangedBy(Context.getUserContext().getUser());
		RecordLoaderTask task = new RecordLoaderTask(getPersonLoaderManager(), person, userContext);
		task.setKey(key);
		int attempts = 0;
		while (attempts < 50) {
			try {
				taskExecutor.execute(task);
				return;
			} catch (TaskRejectedException e) {
				log.error("Exception is: " + e, e);
				try { Thread.sleep(5000); } catch (Exception exe) {};
				attempts++;
			}
		}
		log.warn("Unable to process person load after " + attempts + " attempts. Need to tune the multi-threading.");
	}
	
	public void generateKnownLinks(Serializable key, Person person) {
		if (key == null) {
			return;
		}
		Set<Integer> recordIds = cacheOfRecordsLoaded.get(key);
		if (recordIds == null) {
			recordIds = new HashSet<Integer>();
			recordIds.add(person.getPersonId());
			cacheOfRecordsLoaded.put(key, recordIds);
		} else {
			Integer leftId = person.getPersonId();
			for (Integer rightId : recordIds) {
				if (leftId.equals(rightId)) {
					continue;
				}
				Person leftPerson = new Person();
				leftPerson.setPersonId(leftId);
				Person rightPerson = new Person();
				rightPerson.setPersonId(rightId);
				PersonLink link = new PersonLink();
				link.setPersonLeft(leftPerson);
				link.setPersonRight(rightPerson);
//				log.info("Creating a link between persons: (" + leftId + "," + rightId + ")");
				log.info("Link, " + leftId + "," + rightId);
				try {
					Context.getPersonManagerService().linkPersons(link);
				} catch (ApplicationException e) {
					log.warn("Failed while trying to create a link: " + e, e);
				}
			}
			recordIds.add(leftId);
		}		
	}

	@Override
	public void shutdown() {
		taskExecutor.shutdown();
	}
	
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	
	private class RecordParseTask implements Runnable {

		private String lineRecord;
		private int lineIndex;
		private Person person;

		public RecordParseTask(String lineRecord, int lineIndex) {
			this.lineRecord = lineRecord;
			this.lineIndex = lineIndex;
		}

		public void run() {
			log.debug("Processing record: " + lineRecord);
			person = processLine(lineRecord, lineIndex);
			if (person == null) {
				log.warn("Unable to process record at line: " + lineIndex);
				throw new RuntimeException("Invalid record at line " + lineIndex);
			}
			
			Serializable key = null;
			if (trainingDataExtractor != null) {
				key = trainingDataExtractor.extractKey(lineRecord);
			}
			loadPerson(key, person);
			if (log.isDebugEnabled()) {
				log.debug("Created the entity instance: " + person);
			}
		}
		
		protected Person processLine(String line, int lineIndex) {
			if (line == null || line.length() == 0) {
				return null;
			}
			
			log.debug("Needs to parse the line " + line);
			String[] tokens = delimitingPattern.split(line);
			log.debug("Parsed the line into " + tokens.length + " tokens.");
			Person person = getPersonFromTokens(tokens);
			log.debug("Finished parsing the line.");
			return person;
		}

		private Person getPersonFromTokens(String[] tokens) {
			Person entity = new Person();
			for (int i=0; i < tokens.length; i++) {
				String token = tokens[i];
				FieldType field = fieldMapByColumnIndex.get(new Integer(i+1));
				if (log.isTraceEnabled()) {
					log.trace("Will process token: " + tokens[i] + " as field " + field);
				}
				if (field.isIsIgnored()) {
					if (log.isTraceEnabled()) {
						log.trace("Skiping loading of field at index " + i + " since it is to be ignored.");
					}
					continue;
				}
				if (token != null && token.length() > 0) {
					token = token.trim();
				}
				if (field.getEnclosingCharacter() != null) {
					token = removeEnclosingCharacters(token, field.getEnclosingCharacter());
				}
				if (field.isIsIdentifier()) {
					PersonIdentifier identifier = buildIdentifier(entity, token, field);
					if (identifier != null) {
						if (log.isTraceEnabled()) {
							log.trace("Added identifier to enSimpleDateFormattity: " + identifier);
						}
						entity.addPersonIdentifier(identifier);
					}
					continue;
				}
				Object value = buildFieldValue(token, field);
				if (value != null) {
					if (log.isTraceEnabled()) {
						log.trace("Setting field: " + field.getFieldName() + " to value " + value);
					}
					setEntityValue(value, field.getFieldName(), entity);
				}
			}
			return entity;
		}

		private void setEntityValue(Object value, String fieldName, Object entityInstance) {
			Method method = getMethod(entityInstance.getClass(), fieldName, value.getClass());
			if (method == null) {
				return;
			}
			try {
				method.invoke(entityInstance, value);
			} catch (Exception e) {
				log.warn("Unable to set the value of field " + fieldName + " to a value " + value + 
						" of type " + value.getClass());
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private Method getMethod(Class theClass, String fieldName, Class paramClass) {
			Method method = methodByFieldName.get(fieldName);
			if (method != null) {
				return method;
			}
			String methodName = getMethodName(fieldName);
			try {
				method = theClass.getDeclaredMethod(methodName, paramClass);
				methodByFieldName.put(fieldName, method);
			} catch (Exception e) {
				log.warn("Unable to field entity method for setting field " + fieldName + " to a value of type " + paramClass);
				return null;
			}
			return method;
		}

		private String getMethodName(String fieldName) {
			return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		}

		private Object buildFieldValue(String token, FieldType field) {
			if (isOfReferenceType(field)) {
				return buildReferenceValue(token, field);
			}
			Datatype datatype = field.getDatatype();
			if (datatype == Datatype.STRING) {
				return token;
			}
			if (datatype == Datatype.INTEGER) {
				try {
					Integer value = Integer.parseInt(token);
					return value;
				} catch (NumberFormatException e) {
					log.error("For field in position " + field.getColumnIndex() + " expected integer but found a value of: '" + token + "'");
					return null;
				}
			}
			if (datatype == Datatype.DATE) {
				String dateFormatString = field.getDateFormatString();
				if (dateFormatString == null) {
					dateFormatString = DEFAULT_DATE_FORMAT;
				}
				try {
					SimpleDateFormat format = new SimpleDateFormat(dateFormatString);
					Date date = format.parse(token);
					return date;
				} catch (ParseException e) {
					log.error("For field in position " + field.getColumnIndex() + " expected a date with format " + dateFormatString + 
							" but found a value of: '" + token + "'");
					return null;
				}
			}
			log.error("Cannot handle token with value '" + token + "' and of type: " + datatype);
			return null;
		}

		private Object buildReferenceValue(String token, FieldType field) {
			// TODO Need to implement support for the other reference types.
			String fieldName = field.getFieldName();
			if (fieldName.equalsIgnoreCase(GENDER)) {
				Gender gender = findGenderByCode(token);
				return gender;
			}
			
			if (fieldName.equalsIgnoreCase(RACE)) {
				Race race = findRaceByCode(token);
				return race;
			}
			return null;
		}

		private boolean isOfReferenceType(FieldType field) {
			String fieldName = field.getFieldName();
			if (referenceTypeMap.get(fieldName) != null) {
				return true;
			}
			return false;
		}

		private String removeEnclosingCharacters(String token, String enclosingCharacter) {
			if (token == null || token.length() == 0 || enclosingCharacter.length() == 0) {
				return token;
			}
			int index = token.indexOf(enclosingCharacter);
			if (index < 0) {
				return token;
			}
			int endIndex = token.indexOf(enclosingCharacter, index+1);
			if (endIndex < 0 || endIndex < token.length()-1) {
				return token;
			}
			return token.substring(index+1,endIndex);
		}

		private PersonIdentifier buildIdentifier(Person entity, String token, FieldType field) {
			if (token == null || token.length() == 0) {
				return null;
			}
			org.openhie.openempi.model.PersonIdentifier id = new org.openhie.openempi.model.PersonIdentifier();
			if (field.getSubfields() == null || field.getDelimeter() == null) {
				id.setIdentifier(token);
				id.setPerson(person);
				IdentifierDomain idDomain = new IdentifierDomain();
				id.setIdentifierDomain(idDomain);
				idDomain.setIdentifierDomainName(field.getIdentifierDomainName());
				idDomain.setNamespaceIdentifier(field.getNamespaceIdentifier());
				idDomain.setUniversalIdentifier(field.getUniversalIdentifier());
				idDomain.setUniversalIdentifierTypeCode(field.getUniversalIdentifierTypeCode());
			} else {
				// This is the case where the identifier and its assigning authority 
				// are specified as subfields.
				EntityIdentifier entityIdentifier = new EntityIdentifier();
				Map<Integer,FieldType> subfieldMapByColumnIndex = new HashMap<Integer,FieldType>();
				for (int i=0; i < field.getSubfields().getField().size(); i++) {
					FieldType subfield = field.getSubfields().getField().get(i);
					subfieldMapByColumnIndex.put(new Integer(i+1), subfield);
				}
				Pattern subPattern = Pattern.compile(field.getDelimeter());
				String[] subtokens = subPattern.split(token);
				for (int i=0; i < subtokens.length; i++) {
					String subtoken = subtokens[i];
					FieldType subfield = subfieldMapByColumnIndex.get(new Integer(i+1));
					log.debug("Will process subtoken: " + subtokens[i] + " as field " + subfield);
					if (subfield.isIsIgnored()) {
						if (log.isTraceEnabled()) {
							log.trace("Skiping loading of subfield at index " + i + " since it is to be ignored.");
						}
						continue;
					}
					if (subtoken != null && subtoken.length() > 0) {
						subtoken = subtoken.trim();
					}
					if (subfield.getEnclosingCharacter() != null) {
						subtoken = removeEnclosingCharacters(subtoken, subfield.getEnclosingCharacter());
					}
					Object value = buildFieldValue(subtoken, subfield);
					if (value != null) {
						if (log.isTraceEnabled()) {
							log.trace("Setting field: " + subfield.getFieldName() + " to value " + value);
						}
						setEntityValue(value, subfield.getFieldName(), entityIdentifier);
					}
				}
				populatePersonIdentifier(id, entityIdentifier);
			}
			return id;
		}
	}

	public class RecordLoaderTask implements Runnable
	{
		private Logger log = Logger.getLogger(getClass());
		protected PersonLoaderManager personLoaderManager;
		private UserContext userContext;
		private Person person;
		private Serializable key;
		
		public RecordLoaderTask(PersonLoaderManager personLoaderManager, Person person, UserContext userContext) {
			this.personLoaderManager = personLoaderManager;
			this.person = person;
			this.userContext = userContext;
		}
		
		public void run() {
			log.debug("With User Context: " + userContext + " attempting to load person entry " + person);
			Context.setUserContext(userContext);
			try {
				synchronized(userContext) {
					Person thePerson = personLoaderManager.addPerson(person);
					if (key != null) {
						generateKnownLinks(key, person);
					}
				}
			} catch (Exception e) {
				log.error("Failed while adding person entry to the system. Error: " + e, e);
				if (e.getCause() instanceof org.hibernate.exception.SQLGrammarException) {
					org.hibernate.exception.SQLGrammarException sge = (org.hibernate.exception.SQLGrammarException) e;
					log.error("Cause is: " + sge.getSQL());
				}
			}
		}

		public Serializable getKey() {
			return key;
		}

		public void setKey(Serializable key) {
			this.key = key;
		}
	}
	
	private void populatePersonIdentifier(PersonIdentifier id, EntityIdentifier eid) {
		id.setIdentifier(eid.getIdentifier());
		IdentifierDomain idDomain = new IdentifierDomain();
		id.setIdentifierDomain(idDomain);
		idDomain.setIdentifierDomainName(eid.getIdentifierDomainName());
		idDomain.setNamespaceIdentifier(eid.getNamespaceIdentifier());
		idDomain.setUniversalIdentifier(eid.getUniversalIdentifier());
		idDomain.setUniversalIdentifierTypeCode(eid.getUniversalIdentifierTypeCode());
		if (idDomain.getIdentifierDomainName() == null) {
			idDomain.setIdentifierDomainName(idDomain.getNamespaceIdentifier());
		}
		if (idDomain.getNamespaceIdentifier() == null) {
			idDomain.setNamespaceIdentifier(idDomain.getIdentifierDomainName());
		}
	}

	public class EntityIdentifier
	{
		private String identifier;
		private String identifierDomainName;
		private String namespaceIdentifier;
		private String universalIdentifier;
		private String universalIdentifierTypeCode;
		
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public String getIdentifierDomainName() {
			return identifierDomainName;
		}
		public void setIdentifierDomainName(String identifierDomainName) {
			this.identifierDomainName = identifierDomainName;
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
			if (!(other instanceof EntityIdentifier))
				return false;
			EntityIdentifier castOther = (EntityIdentifier) other;
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
