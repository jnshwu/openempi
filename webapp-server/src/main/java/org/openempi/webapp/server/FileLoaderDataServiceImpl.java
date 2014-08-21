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
package org.openempi.webapp.server;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.FileLoaderDataService;
import org.openempi.webapp.client.model.LoaderConfigWeb;
import org.openempi.webapp.client.model.LoaderDataFieldWeb;
import org.openempi.webapp.client.model.LoaderFieldCompositionWeb;
import org.openempi.webapp.client.model.LoaderSubFieldWeb;
import org.openhie.openempi.configuration.Component;
import org.openhie.openempi.configuration.Component.ComponentType;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.LoaderDataField;
import org.openhie.openempi.loader.LoaderFieldComposition;
import org.openhie.openempi.loader.LoaderSubField;

public class FileLoaderDataServiceImpl extends AbstractRemoteServiceServlet implements FileLoaderDataService
{
	public final static String HEADER_LINE_PRESENT_KEY = "headerLinePresentFlag";
	public final static String FILE_LOADER_FIELDS_LIST_KEY = "fileLoaderFieldsList";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	public LoaderConfigWeb loadFileLoaderConfigurationData() {
		log.debug("Received request to load the file loader configuration data.");
		
		authenticateCaller();
		try {
			Configuration configuration = Context.getConfiguration();
			@SuppressWarnings("unchecked")
			Map<String,Object> configurationData = (Map<String,Object>) configuration
					.lookupConfigurationEntry(ConfigurationRegistry.FILE_LOADER_CONFIGURATION);
			return convertToClientModel(configurationData);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private LoaderConfigWeb convertToClientModel(Map<String,Object> loaderConfiguration) {
		LoaderConfigWeb loaderConfigWeb = new LoaderConfigWeb();
		Boolean headerLinePresent = (Boolean) loaderConfiguration.get(HEADER_LINE_PRESENT_KEY);
		loaderConfigWeb.setHeaderLinePresent(headerLinePresent);
		@SuppressWarnings("unchecked")
		List<LoaderDataField> fields = (List<LoaderDataField>) loaderConfiguration.get(FILE_LOADER_FIELDS_LIST_KEY);
		List<LoaderDataFieldWeb> loaderDataFieldsWeb =
			new java.util.ArrayList<LoaderDataFieldWeb>(fields.size());
		for (LoaderDataField loaderDataField : fields) {
			LoaderDataFieldWeb loaderDataFieldWeb = new LoaderDataFieldWeb(
					loaderDataField.getFieldName(),
					loaderDataField.getFormatString(),
					loaderDataField.getFunctionName());
			if (loaderDataField.getFieldComposition() != null) {
				LoaderFieldCompositionWeb loaderFieldCompositionWeb = new LoaderFieldCompositionWeb(
						loaderDataField.getFieldComposition().getIndex(),
						loaderDataField.getFieldComposition().getSeparator());
				loaderDataFieldWeb.setLoaderFieldComposition(loaderFieldCompositionWeb);
			}
			if (loaderDataField.getSubFields() != null) {
				List<LoaderSubFieldWeb> loaderSubFieldsWeb =
					new java.util.ArrayList<LoaderSubFieldWeb>(loaderDataField.getSubFields().size());
				for (LoaderSubField loaderSubField : loaderDataField.getSubFields()) {
					LoaderSubFieldWeb loaderSubFieldWeb = new LoaderSubFieldWeb(
							loaderSubField.getFieldName(),
							loaderSubField.getBeginIndex(),
							loaderSubField.getEndIndex());
					loaderSubFieldsWeb.add(loaderSubFieldWeb);
				}
				loaderDataFieldWeb.setLoaderSubFields(loaderSubFieldsWeb);
			}
			loaderDataFieldsWeb.add(loaderDataFieldWeb);
		}
		loaderConfigWeb.setLoaderDataFields(loaderDataFieldsWeb);
		return loaderConfigWeb;
	}

	public String saveFileLoaderConfigurationData(LoaderConfigWeb loaderConfigWeb) {
		
		authenticateCaller();
		Configuration configuration = Context.getConfiguration();
		String returnMessage = "";
		try {
			Component component = configuration.lookupExtensionComponentByComponentType(ComponentType.FILELOADER);
			String loaderBeanName = configuration.getExtensionBeanNameFromComponent(component);
			ConfigurationLoader loader = (ConfigurationLoader) Context.getApplicationContext().getBean(loaderBeanName);
			Map<String,Object> configurationData = convertFromClientModel(loaderConfigWeb);
			loader.saveAndRegisterComponentConfiguration(configuration, configurationData);
		} catch (Exception e) {
			log.warn("Failed while saving the file loader configuration: " + e, e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private Map<String,Object> convertFromClientModel(LoaderConfigWeb loaderConfigWeb) {
		Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
		configurationData.put(HEADER_LINE_PRESENT_KEY, loaderConfigWeb.getHeaderLinePresent());
		List<LoaderDataField> loaderDataFields =
			new java.util.ArrayList<LoaderDataField>(loaderConfigWeb.getLoaderDataFields().size());
		for (LoaderDataFieldWeb loaderDataFieldWeb : loaderConfigWeb.getLoaderDataFields()) {
			LoaderDataField loaderDataField = new LoaderDataField();
			loaderDataField.setFieldName(loaderDataFieldWeb.getFieldName());
			loaderDataField.setFormatString(loaderDataFieldWeb.getFormatString());
			loaderDataField.setFunctionName(loaderDataFieldWeb.getFunctionName());
			if (loaderDataFieldWeb.getLoaderFieldComposition() != null) {
				LoaderFieldComposition loaderFieldComposition = new LoaderFieldComposition();
				loaderFieldComposition.setIndex(loaderDataFieldWeb.getLoaderFieldComposition().getIndex());
				loaderFieldComposition.setSeparator(loaderDataFieldWeb.getLoaderFieldComposition().getSeparator());
				loaderDataField.setFieldComposition(loaderFieldComposition);
			}
			if (loaderDataFieldWeb.getLoaderSubFields() != null) {
				List<LoaderSubField> loaderSubFields =
					new java.util.ArrayList<LoaderSubField>(loaderDataFieldWeb.getLoaderSubFields().size());
				for (LoaderSubFieldWeb loaderSubFieldWeb : loaderDataFieldWeb.getLoaderSubFields()) {
					LoaderSubField loaderSubField = new LoaderSubField();
					loaderSubField.setFieldName(loaderSubFieldWeb.getFieldName());
					loaderSubField.setBeginIndex(loaderSubFieldWeb.getBeginIndex());
					loaderSubField.setEndIndex(loaderSubFieldWeb.getEndIndex());
					loaderSubFields.add(loaderSubField);
				}
				loaderDataField.setSubFields(loaderSubFields);
			}
			loaderDataFields.add(loaderDataField);
		}
		configurationData.put(FILE_LOADER_FIELDS_LIST_KEY, loaderDataFields);
		return configurationData;
	}

}
