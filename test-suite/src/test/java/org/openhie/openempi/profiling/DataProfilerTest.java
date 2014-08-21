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
package org.openhie.openempi.profiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.FileLoaderManager;
import org.openhie.openempi.model.DataProfileAttribute;
import org.openhie.openempi.model.DataProfileAttributeValue;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class DataProfilerTest extends BaseServiceTestCase
{	
	protected static Logger log = Logger.getLogger(DataProfilerTest.class);
	private final static int MAX_FIELD_COUNT = 32;
	private final static String DELIMETER = ":";	
	private List<AttributeMetadata> attribMetadata;	
	
	public void testDataProfileView() {
		DataProfileService service = Context.getDataProfileService();
		List<DataProfileAttribute> dataList = service.getDataProfileAttributes(0);
//		assertNotNull("Did not find any data profile attribute data.", dataList);
//		assertTrue("The list of data profile attribute data is empty.", dataList.size() > 0);
		if(  dataList == null || dataList.size() == 0 ) {
			log.debug("Did not find any data profile attribute data");
			return;
		}
		
		DataProfileAttribute givenNameAttribute=null;
		for (DataProfileAttribute attrib : dataList) {
			log.debug(attrib);
			if (attrib.getAttributeName() != null && attrib.getAttributeName().equalsIgnoreCase("givenName")) {
				givenNameAttribute = attrib;
			}
		}
		if (givenNameAttribute != null) {
			List<DataProfileAttributeValue> values = service.getTopDataProfileAttributeValues(givenNameAttribute.getAttributeId(), 10);
//			assertNotNull("Did not find any data profile attribute data values.", values);
//			assertTrue("The list of data profile attribute data values is empty.", values.size() > 0);
			if(  values == null || values.size() == 0 ) {
				log.debug("Did not find any data profile attribute data values");
				return;
			}
			for (DataProfileAttributeValue value : values) {
				log.debug("The value [" + value.getAttributeValue() + "] has a frequence of " + value.getFrequency());
			}
		}
	}	
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		if (args.length < 1) {
			usage();
			System.exit(-1);
		}
		String filename = args[0];
		log.info("Loading the data file " + filename);
		
		Context.startup();
		Context.authenticate("admin", "admin");

		try {				
			 File file = new File(filename);
			 log.debug("Loading file " + file.getAbsolutePath());
			 if (!file.isFile() || !file.canRead()) {
				 log.error("Input file is not available.");
				 throw new RuntimeException("Input file " + filename + " is not readable.");
			 }
			 
			DataProfilerTest dataProfilerTest = new DataProfilerTest();
			dataProfilerTest.parseFile(file);
			
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
		    log.debug("Elapsed time for file load: " + elapsedTime);			
		} catch (Throwable t) {
			log.error("Got an exception: " + t);
		}
		
	}

	public String parseFile(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			log.error("Unable to read the input file. Error: " + e);
			throw new RuntimeException("Unable to read the input file.");
		}
		
		try {
				String line = reader.readLine();
				if (line == null) {					
					return "Failed while loading the input file";
				}
				
				attribMetadata = loadFieldNameAndType(line, DELIMETER);
				reader.close();
				
				DataProfiler dataProfiler = (DataProfiler) Context.getApplicationContext().getBean("dataProfiler");
				if (dataProfiler == null) {
					throw new RuntimeException("The DataProfiler is invalid. Please check with your system administrator.");
				}
				
				FileRecordDataSource fileRecordDataSource = new FileRecordDataSource(file.getPath(), null, attribMetadata );
				dataProfiler.setRecordDataSource(fileRecordDataSource);	
				
				//dataProfiler.run();
				ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) Context.getApplicationContext().getBean("taskExecutor");
				try {
					taskExecutor.execute(dataProfiler);	
				} catch (TaskRejectedException e) {
					log.error("Failed while processing data profile. Error: " + e, e);
					throw new RuntimeException("Failed while processing data profile.");
				}
				
				return "";
			
		} catch (IOException e) {
			log.error("Failed while loading the input file. Error: " + e);
			throw new RuntimeException("Failed while loading the input file.");
		}
	}
	
	protected List<AttributeMetadata> loadFieldNameAndType(String line, String delimeter) {
		String[] fields = loadFieldValues(line);
		List<AttributeMetadata> metadatas = new ArrayList<AttributeMetadata>();
		for (int i=0; i < fields.length; i++) {
			if( fields[i] != null ) {
				String field = fields[i];
				AttributeMetadata attributeMetadata = extractAttributeMetadata(field, delimeter);
				metadatas.add(attributeMetadata);
			}
		}
		return metadatas;
	}	

	private int extractDatatypeFromName(String attribute, String typeName) {
		int datatype = -1;
		if (typeName.equals("String")) {
			datatype = DataProfileAttribute.STRING_DATA_TYPE;
		} else if (typeName.equals("Integer")) {
			datatype = DataProfileAttribute.INTEGER_DATA_TYPE;
		} else if (typeName.equals("Long")) {
			datatype = DataProfileAttribute.LONG_DATA_TYPE;
		} else if (typeName.equals("Float")) {
			datatype = DataProfileAttribute.FLOAT_DATA_TYPE;
		} else if (typeName.equals("Double")) {
			datatype = DataProfileAttribute.DOUBLE_DATA_TYPE;
		} else if (typeName.equals("Date")) {
			datatype = DataProfileAttribute.DATE_DATA_TYPE;
		} else {
			log.warn("Attribute " + attribute + " is of unknown data type " + typeName + " and will be ignored.");
		}
		return datatype;
	}
	
	private AttributeMetadata extractAttributeMetadata(String attribute, String delimeter) {
		StringTokenizer idTokenizer = new StringTokenizer(attribute, delimeter);
		int count=0;
		String attributeName="";
		int type = -1;
		while (idTokenizer.hasMoreTokens()) {
			String field = idTokenizer.nextToken();
			switch (count) {
			case 0:
				attributeName = field;
				break;
			case 1:
				type = extractDatatypeFromName(attributeName, field);
				break;
			}
			count++;
		}	
		return new AttributeMetadata(attributeName, type);
	}
	
	protected String[] loadFieldValues(String line) {
		String[] fields = new String[MAX_FIELD_COUNT];
		int length = line.length();
		int begin=0;
		int end=0;
		int fieldIndex=0;
		while (end < length) {
			while (end < length-1 && line.charAt(end) != ',') {
				end++;
			}
			if (end == length -1 ) {
				break;
			}
			String fieldValue = line.substring(begin, end);
			if (fieldValue != null && fieldValue.length() > 0) {
				fieldValue = fieldValue.trim();
			}
			fields[fieldIndex++] = fieldValue; 
			end++;
			begin=end;
			
			if(fieldIndex == MAX_FIELD_COUNT ) {
				return fields;				
			}				
		}	
		fields[fieldIndex] = line.substring(begin, end+1);
		return fields;
	}	
	public static void usage() {
		System.out.println("Usage: " + DataProfilerTest.class.getName() + " <file-to-load>");
	}

}
