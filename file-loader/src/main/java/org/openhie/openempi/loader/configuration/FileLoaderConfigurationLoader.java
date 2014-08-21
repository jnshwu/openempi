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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.xml.fileloader.Composition;
import org.openhie.openempi.configuration.xml.fileloader.DataField;
import org.openhie.openempi.configuration.xml.fileloader.DataFields;
import org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig;
import org.openhie.openempi.configuration.xml.fileloader.FileLoaderType;
import org.openhie.openempi.configuration.xml.fileloader.Substring;
import org.openhie.openempi.configuration.xml.fileloader.Substrings;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.FileLoaderConstants;
import org.openhie.openempi.loader.LoaderDataField;
import org.openhie.openempi.loader.LoaderFieldComposition;
import org.openhie.openempi.loader.LoaderSubField;

/**
 * @author ctoth 
 * @version $Revision: $ $Date:  $
 */
public class FileLoaderConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(Configuration.class);
	
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the file loader configuration service
		//
		if (!(configurationFragment instanceof FileLoaderType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration Registry so that
		// it is available for the file loader configuration service to use when needed.
		//
		Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
		registry.registerConfigurationEntry(ConfigurationRegistry.FILE_LOADER_CONFIGURATION,  configurationData);
		
		FileLoaderType loaderConfig = (FileLoaderType) configurationFragment;
		log.debug("Received xml fragment to parse: " + loaderConfig);
		if (loaderConfig == null || loaderConfig.getFileLoaderConfig().getDataFields().sizeOfDataFieldArray() == 0) {
			log.warn("No data fields were configured; probably a configuration issue.");
			return;
		}
		
		boolean headerLinePresent = (loaderConfig.getFileLoaderConfig().getHeaderLinePresent()) ? true : false;
		configurationData.put(FileLoaderConstants.HEADER_LINE_PRESENT_KEY, new Boolean(headerLinePresent));
		for (int i = 0; i < loaderConfig.getFileLoaderConfig().getDataFields().sizeOfDataFieldArray(); i++) {
			org.openhie.openempi.configuration.xml.fileloader.DataField dataField =
				loaderConfig.getFileLoaderConfig().getDataFields().getDataFieldArray(i);
			java.util.List<LoaderDataField> fields = new java.util.ArrayList<LoaderDataField>();
			configurationData.put(FileLoaderConstants.FILE_LOADER_FIELDS_LIST_KEY, fields);
			LoaderDataField loaderDataField = new LoaderDataField();
			if (dataField.getTargetFieldName() != null)
				loaderDataField.setFieldName(dataField.getTargetFieldName());
			if (dataField.getFormat() != null)
				loaderDataField.setFormatString(dataField.getFormat());
			if (dataField.getFunction() != null)
				loaderDataField.setFunctionName(dataField.getFunction());
			if (dataField.getSubstrings() != null) {
				List<LoaderSubField> loaderSubFields = new ArrayList<LoaderSubField>();
				for (int j = 0; j < dataField.getSubstrings().getSubstringArray().length; j++) {
					org.openhie.openempi.configuration.xml.fileloader.Substring substring =
						dataField.getSubstrings().getSubstringArray(j);
					LoaderSubField loaderSubField = new LoaderSubField();
					loaderSubField.setBeginIndex(substring.getBeginIndex());
					loaderSubField.setEndIndex(substring.getEndIndex());
					loaderSubField.setFieldName(substring.getTargetFieldName());
					loaderSubFields.add(loaderSubField);
				}
				loaderDataField.setSubFields(loaderSubFields);
			}
			if (dataField.getComposition() != null) {
				org.openhie.openempi.configuration.xml.fileloader.Composition composition =
					dataField.getComposition();
				LoaderFieldComposition loaderFieldComposition = new LoaderFieldComposition();
				loaderFieldComposition.setIndex(composition.getIndex());
				loaderFieldComposition.setSeparator(composition.getSeparator());
				loaderFieldComposition.setValue(dataField.getTargetFieldName()); // redundant
			}
			fields.add(loaderDataField);
		}
	}

	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Map<String,Object> configurationData)
			throws InitializationException {
		Boolean headerLinePresent = (Boolean) configurationData.get(FileLoaderConstants.HEADER_LINE_PRESENT_KEY);
		@SuppressWarnings("unchecked")
		List<LoaderDataField> fields = (List<LoaderDataField>) configurationData
				.get(FileLoaderConstants.FILE_LOADER_FIELDS_LIST_KEY);
		FileLoaderType xmlConfigurationFragment = buildConfigurationFileFragment(headerLinePresent, fields);
		log.debug("Saving file loader configuration info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveFileLoaderConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated file loader configuration in configuration registry.");
		registry.registerConfigurationEntry(ConfigurationRegistry.FILE_LOADER_CONFIGURATION, configurationData);
	}

	private FileLoaderType buildConfigurationFileFragment(Boolean headerLinePresent, List<LoaderDataField> fields) {
		FileLoaderType newFileLoaderConfig = FileLoaderType.Factory.newInstance();
		FileLoaderConfig fileLoaderConfigNode = newFileLoaderConfig.addNewFileLoaderConfig();
		fileLoaderConfigNode.setHeaderLinePresent(headerLinePresent);
		DataFields dataFieldsNode = fileLoaderConfigNode.addNewDataFields();
		for (LoaderDataField loaderDataField : fields) {
			DataField dataFieldNode = dataFieldsNode.addNewDataField();
			if (loaderDataField.getFieldName() != null)
				dataFieldNode.setTargetFieldName(loaderDataField.getFieldName());
			if (loaderDataField.getFormatString() != null)
				dataFieldNode.setFormat(loaderDataField.getFormatString());
			if (loaderDataField.getFunctionName() != null)
				dataFieldNode.setFunction(loaderDataField.getFunctionName());
			if (loaderDataField.getSubFields() != null) {
				Substrings substrings = dataFieldNode.addNewSubstrings();
				for (LoaderSubField loaderSubField : loaderDataField.getSubFields()) {
					Substring substring = substrings.addNewSubstring();
					substring.setBeginIndex(loaderSubField.getBeginIndex());
					substring.setEndIndex(loaderSubField.getEndIndex());
					substring.setTargetFieldName(loaderSubField.getFieldName());
				}
			}
			if (loaderDataField.getFieldComposition() != null) {
				Composition composition = dataFieldNode.addNewComposition();
				composition.setIndex(loaderDataField.getFieldComposition().getIndex());
				composition.setSeparator(loaderDataField.getFieldComposition().getSeparator());
			}
		}
		return newFileLoaderConfig;
	}
}
