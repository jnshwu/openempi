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
package org.openhie.openempi.configuration;

public interface ConfigurationRegistry
{
	public final static String CUSTOM_FIELDS_MAP = "customFieldsMap";
	public final static String CUSTOM_FIELDS_LIST = "customFieldsList";
	public final static String SCHEDULED_TASK_LIST = "scheduledTaskList";
	public final static String BLOCKING_CONFIGURATION = "blockingConfiguration";
	public final static String MATCH_CONFIGURATION = "matchConfiguration";
	public final static String FILE_LOADER_CONFIGURATION = "fileLoaderConfiguration";
	public final static String SINGLE_BEST_RECORD_CONFIGURATION = "singleBestRecordConfiguration";
	
	public final static String BLOCKING_ALGORITHM_NAME_KEY = "blockingAlgorithmName";
	public final static String MATCHING_ALGORITHM_NAME_KEY = "matchingAlgorithmName";
	

	/**
	 * Used to lookup a configuration entry using a key. Extension components may
	 * register a new configuration entry that specifically supports the configuration
	 * options needed by the component and then lookup this entry during runtime
	 * using this interface.
	 *  
	 * @param key Should uniquely identify the configuration entry
	 * @return
	 */
	public Object lookupConfigurationEntry(String key);
	
	/**
	 * Register a new configuration entry for a component and make it available for
	 * the other modules that make up the component during runtime.
	 *  
	 * @param key A string that uniquely identifies the configuration entry
	 * @param entry The actual configuration entry object itself.
	 */
	public void registerConfigurationEntry(String key, Object entry);
}
