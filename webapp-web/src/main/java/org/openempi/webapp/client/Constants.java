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
package org.openempi.webapp.client;

public class Constants
{
	public final static int HEADER_OFFSET = 90;

	// Registry constants used throughout the application for data sharing
	//
	public static final String WEST_PANEL = "west";
	public static final String NORTH_PANEL = "north";
	public static final String VIEWPORT = "viewport";
	public static final String CENTER_PANEL = "center";

	public static final String SERVICE = "mailservice";

	public static final String ADMIN_SERVICE = "adminService";
	public static final String BLOCKING_DATA_SERVICE = "blockingDataService";
	public static final String CONFIGURATION_DATA_SERVICE = "configurationDataService";
	public static final String EVENT_NOTIFICATION_SERVICE = "eventNotificationService";
	public static final String FILE_LOADER_DATA_SERVICE = "fileLoaderDataService";
	public static final String PERSON_DATA_SERVICE = "personDataService";
	public static final String REPORT_DATA_SERVICE = "reportDataService";
	public static final String PROFILE_DATA_SERVICE = "profileDataService";
	public static final String USER_DATA_SERVICE = "userDataService";
	public static final String REF_DATA_SERVICE = "referenceDataService";
	public static final String AUDIT_EVENT_SERVICE = "auditEventDataService";
	
	public static final String IDENTITY_DOMAINS = "identityDomains";
	public static final String IDENTITY_DOMAIN_TYPE_CODES = "identityDomainTypeCodes";
	public static final String PERSON_MODEL_ALL_ATTRIBUTE_NAMES = "personModelAllAtttributeNames";
	public static final String PERSON_MODEL_ATTRIBUTE_NAMES = "personModelAtttributeNames";
	public static final String PERSON_MODEL_CUSTOM_FIELD_NAMES = "personModelCustomFieldNames";
	public static final String TRANSFORMATION_FUNCTION_NAMES = "transformationFunctionNames";
	public static final String COMPARATOR_FUNCTION_NAMES = "comparatorFunctionNames";
	public static final String SYSTEM_CONFIGURATION_INFO = "systemConfigurationInfo";
	
	public static final String ADVANCED_SEARCH_LIST = "advancedSearchList";
	public static final String BASIC_SEARCH_LIST = "basicSearchList";	
	public static final String ADVANCED_SEARCH_CRITERIA = "advancedSearchCriteria";
	public static final String ADVANCED_SEARCH_FUZZY_MATCH = "advancedSearchFuzzyMatch";
	public static final String BASIC_SEARCH_CRITERIA = "basicSearchCriteria";
	public static final String ADD_PERSON = "addPersonInfo";
	
	public static final String MANAGE_USER_LIST = "manageUserList";
	public static final String LOGIN_USER = "loginUser";	
	public static final String LOGIN_USER_PERMISSIONS = "loginUserPermissions";	
	public static final String ROLE_LIST = "roleListInfo";
	public static final String PERMISSION_LIST = "permissionListInfo";	

	public static final String AUDIT_EVENT_TYPE_CODES = "auditEventTypeCodes";
	public static final String MESSAGE_TYPE_CODES = "messageTypeCodes";
	
	public static final String MAX_RECORD_DISPLAING = "maximumRecordDisplaying";	
	
	public static final int MATCH_CLASSIFICATION = 1;
	public static final int PROBABLE_MATCH_CLASSIFICATION = 2;
	public static final int NON_MATCH_CLASSIFICATION = 3;
}
