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
package org.openhie.openempi;


/**
 * Constant values used throughout the application.
 * 
 */
public class Constants
{
    //~ Static fields/initializers =============================================

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /**
     * User home from System properties
     */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /**
     * OpenEMPI home directory
     */
    public static final String OPENEMPI_HOME = "openempi.home";
    
    public static final String OPENEMPI_HOME_VALUE = System.getProperty(OPENEMPI_HOME);

	public static final String OPENEMPI_HOME_ENV_VARIABLE = "OPENEMPI_HOME";
    
	public static final String OPENEMPI_HOME_ENV_VALUE = System.getenv(OPENEMPI_HOME_ENV_VARIABLE);
	
    /**
     * System parameter name for list of extension contexts
     */
    public static final String OPENEMPI_EXTENSION_CONTEXTS = "openempi.extension.contexts";
    
    /**
     * System parameter that specifies an alternate name to use for loading the extension contexts
     */
    public static final String OPENEMPI_EXTENSION_CONTEXTS_FILENAME = "openempi.extension.contexts.filename";
    
    /**
     * Syste parameter that specifies an alternate name to use for configuring OpenEMPI
     */
    public static final String OPENEMPI_CONFIGURATION_FILENAME = "openempi.configuration.filename";
    
    /**
     * Name for extension contexts property file name
     */
    public static final String OPENEMPI_EXTENSION_CONTEXTS_PROPERTY_FILENAME = "openempi-extension-contexts.properties";
    	
    /**
     * Default upload file directory
     */
    public static final String DEFAULT_FILE_REPOSITORY_DIRECTORY = "fileRepository";
    
    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";

    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

    /**
     * The request scope attribute under which an editable user form is stored
     */
    public static final String USER_KEY = "userForm";

    /**
     * The request scope attribute that holds the user list
     */
    public static final String USER_LIST = "userList";

    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    /**
     * The name of the User role, as specified in web.xml
     */
    public static final String USER_ROLE = "ROLE_USER";

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";

    /**
     * The weight that is assigned to a pair of records when they are merged intentionally and not by the matching algorithm
     */
	public static final Double MERGE_RECORDS_WEIGHT = 1.0;

	/**
	 * File name of the serialized FellegiSunterConfiguration information file
	 */
	public static final String FELLEGI_SUNTER_CONFIG_FILE_NAME = "FellegiSunterConfiguration.ser";

	/**
	 * Maximum number of custom fields
	 */
	public static final Integer CUSTOM_FIELD_MAX_NUMBER = 20;

	/**
	 * Defines the bean that implements the default blocking service
	 */
	public static final String NAIVE_BLOCKING_SERVICE = "naiveBlockingService";

	/**
	 * Defines the directory below the OPENEMPI_HOME where the reports are stored
	 */
	public static final String REPORT_DATA_DIRECTORY = "reports";
	
	/**
	 * Defines the key that is used in the registry for storing matching field configuration
	 */
	public static final String MATCHING_FIELDS_REGISTRY_KEY = "matchingFields";
	
	public static final Integer MATCH_CLASSIFICATION = 1;
	public static final Integer PROBABLE_MATCH_CLASSIFICATION = 2;
	public static final Integer NON_MATCH_CLASSIFICATION = 3;
	
	public final static String PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_FILE = "log-to-file";
	public final static String PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_DB = "log-to-db";
}
