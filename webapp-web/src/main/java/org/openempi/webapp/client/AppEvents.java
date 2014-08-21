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

import com.extjs.gxt.ui.client.event.EventType;

public class AppEvents
{
	public static final EventType AddPersonComplete = new EventType();
	
	public static final EventType AddPersonInitiate = new EventType();
	
	public static final EventType AddToUpdatePersonView = new EventType();
	
	public static final EventType AddToUpdatePersonRenderData = new EventType();
	
	public static final EventType AddToUpdatePersonViewCancel = new EventType();
	
	public static final EventType AddToUpdatePersonViewFinished = new EventType();
	
	public static final EventType CheckDuplicatePersonInitiate = new EventType();
	
	public static final EventType CheckDuplicatePersonComplete = new EventType();
	
	public static final EventType AdvancedSearchInitiate = new EventType();

	public static final EventType AdvancedSearchFuzzyMatchInitiate = new EventType();
	
	public static final EventType AdvancedSearchRenderData = new EventType();
	
	public static final EventType AdvancedSearchView = new EventType();

	public static final EventType AdvancedUpdatePersonView = new EventType();
	
	public static final EventType AdvancedUpdatePersonRenderData = new EventType();
	
	public static final EventType AdvancedUpdatePersonInitiate = new EventType();
	
	public static final EventType AdvancedUpdatePersonComplete = new EventType();
	
	public static final EventType AdvancedUpdatePersonFinished = new EventType();
	
	public static final EventType AdvancedUpdatePersonCancel = new EventType();

	public static final EventType AdvancedDeletePersonView = new EventType();
	
	public static final EventType AdvancedDeletePersonRenderData = new EventType();
	
	public static final EventType AdvancedDeletePersonInitiate = new EventType();
	
	public static final EventType AdvancedDeletePersonComplete = new EventType();
	
	public static final EventType AdvancedDeletePersonFinished = new EventType();
	
	public static final EventType AdvancedDeletePersonCancel = new EventType();
	
	public static final EventType AddPersonView = new EventType();

	public static final EventType AdminStartPixPdqServer = new EventType();

	public static final EventType AdminStopPixPdqServer = new EventType();
	
	public static final EventType AssignGlobalIdentifier = new EventType();

	public static final EventType AuditEventTypeCodesReceived = new EventType();
	
	public static final EventType AuditEventView = new EventType();
	
	public static final EventType AuditEventRequest = new EventType();
	
	public static final EventType AuditEventReceived = new EventType();

	public static final EventType MessageLogView = new EventType();
	
	public static final EventType MessageTypeCodesReceived = new EventType();
	
	public static final EventType MessageLogRequest = new EventType();
	
	public static final EventType MessageLogReceived = new EventType();
	
	public static final EventType MessageLogDetailRequest = new EventType();
	
	public static final EventType MessageLogDetailReceived = new EventType();
	
	public static final EventType BasicSearchInitiate = new EventType();

	public static final EventType BasicSearchRenderData = new EventType();

	public static final EventType BasicSearchView = new EventType();

	public static final EventType BasicUpdatePersonView = new EventType();
	
	public static final EventType BasicUpdatePersonRenderData = new EventType();
	
	public static final EventType BasicUpdatePersonInitiate = new EventType();
	
	public static final EventType BasicUpdatePersonComplete = new EventType();
	
	public static final EventType BasicUpdatePersonFinished = new EventType();
	
	public static final EventType BasicUpdatePersonCancel = new EventType();
	
	public static final EventType BasicDeletePersonView = new EventType();

	public static final EventType BasicDeletePersonRenderData = new EventType();
	
	public static final EventType BasicDeletePersonInitiate = new EventType();
	
	public static final EventType BasicDeletePersonComplete = new EventType();
	
	public static final EventType BasicDeletePersonFinished = new EventType();
	
	public static final EventType BasicDeletePersonCancel = new EventType();
	
	public static final EventType BlockingConfigurationReceived = new EventType();

	public static final EventType BlockingConfigurationSave = new EventType();
	
	public static final EventType BlockingConfigurationSaveComplete = new EventType();

	public static final EventType BlockingConfigurationView = new EventType();

	public static final EventType BlockingConfigurationRequest = new EventType();

	public static final EventType ComparatorFunctionNamesReceived = new EventType();

	public static final EventType CustomFieldsConfigurationReceived = new EventType();

	public static final EventType CustomFieldsConfigurationRequest = new EventType();

	public static final EventType CustomFieldsConfigurationSave = new EventType();
	
	public static final EventType CustomFieldsConfigurationSaveComplete = new EventType();

	public static final EventType CustomFieldsConfigurationView = new EventType();
	
	public static final EventType DataProfileView = new EventType();
	
	public static final EventType DataProfileAttributeRequest = new EventType();
	
	public static final EventType DataProfileAttributeReceived = new EventType();
	
	public static final EventType DataProfileAttributeValueRequest = new EventType();
	
	public static final EventType DataProfileAttributeValueReceived = new EventType();
	
	public static final EventType DeterministicMatchConfigurationReceived = new EventType();

	public static final EventType DeterministicMatchConfigurationRequest = new EventType();

	public static final EventType DeterministicMatchConfigurationSave = new EventType();

	public static final EventType DeterministicMatchConfigurationSaveComplete = new EventType();
	
	public static final EventType DeterministicMatchConfigurationView = new EventType();
	
	public static final EventType EventNotificationEventsComplete = new EventType();
	
	public static final EventType EventNotificationEventsInitiate = new EventType();

	public static final EventType EventNotificationRegistrationComplete = new EventType();
	
	public static final EventType EventNotificationRegistrationInitiate = new EventType();
	
	public static final EventType EventNotificationUnregistrationComplete = new EventType();

	public static final EventType EventNotificationUnregistrationInitiate = new EventType();

	public static final EventType EventNotificationView = new EventType();
	
	public static final EventType Error = new EventType();

	public static final EventType FileEntryRemove = new EventType();
	
	public static final EventType FileEntryDataProfile = new EventType();

	public static final EventType FileEntryDataProfileSuccess = new EventType();
	
	public static final EventType FileEntryImport = new EventType();
	
	public static final EventType FileEntryImportSuccess = new EventType();

	public static final EventType FileListView = new EventType();

	public static final EventType FileListRender = new EventType();

	public static final EventType FileListUpdate = new EventType();
	
	public static final EventType FileListUpdateDataProfile = new EventType();
	
	public static final EventType FileListRenderDataProfile = new EventType();

	public static final EventType FileLoaderConfigurationView = new EventType();

	public static final EventType FileLoaderConfigurationSave = new EventType();

	public static final EventType FileLoaderConfigurationReceived = new EventType();

	public static final EventType FileLoaderConfigurationRequest = new EventType();
	
	public static final EventType FileLoaderConfigurations = new EventType();

	public static final EventType IdentifierDomainsReceived = new EventType();

	public static final EventType IdentifierDomainTypeCodesReceived = new EventType();

	public static final EventType Init = new EventType();
	
	public static final EventType InitMenu = new EventType();
	
	public static final EventType InitializeRepository = new EventType();
	
	public static final EventType InitializeCustomConfiguration = new EventType();
	
	public static final EventType RebuildBlockingIndex = new EventType();
	
	public static final EventType LinkAllRecordPairs = new EventType();
	
	public static final EventType Login = new EventType();
	
	public static final EventType Logout = new EventType();

	public static final EventType MatchConfigurationReceived = new EventType();

	public static final EventType MatchConfigurationRequest = new EventType();

	public static final EventType MatchVectorConfigurationReceived = new EventType();

	public static final EventType MatchVectorConfigurationRequest = new EventType();
	
	public static final EventType MatchConfigurationSave = new EventType();
	
	public static final EventType MatchConfigurationSaveComplete = new EventType();

	public static final EventType MatchConfigurationGetLoggedLinksForVector = new EventType();
	
	public static final EventType MatchConfigurationGetLoggedLinksForVectorReceived = new EventType();
	
	public static final EventType MatchConfigurationView = new EventType();
	
	public static final EventType ManageIdentifierDomainView = new EventType();
	
	public static final EventType ManageIdentifierDomainRequest = new EventType();
	
	public static final EventType ManageIdentifierDomainReceived = new EventType();
	
	public static final EventType ManageIdentifierDomainAdd = new EventType();
	
	public static final EventType ManageIdentifierDomainUpdate = new EventType();
	
	public static final EventType ManageIdentifierDomainDelete = new EventType();

	public static final EventType ManageIdentifierDomainAddComplete = new EventType();
	
	public static final EventType ManageIdentifierDomainUpdateComplete = new EventType();
	
	public static final EventType ManageIdentifierDomainDeleteComplete = new EventType();
	
	public static final EventType ManageUserView = new EventType();

	public static final EventType UserAddView = new EventType();
	
	public static final EventType UserAddRenderData = new EventType();
	
	public static final EventType UserAddInitiate = new EventType();
	
	public static final EventType UserAddComplete = new EventType();
	
	public static final EventType UserAddFinished = new EventType();
	
	public static final EventType UserUpdateView = new EventType();

	public static final EventType UserUpdateViewCancel = new EventType();
	
	public static final EventType UserUpdateRenderData = new EventType();
	
	public static final EventType UserUpdateInitiate = new EventType();
	
	public static final EventType UserUpdateComplete = new EventType();

	public static final EventType UserUpdateFinished = new EventType();
	
	public static final EventType UserDeleteView = new EventType();
	
	public static final EventType UserDeleteRenderData = new EventType();
	
	public static final EventType UserDeleteInitiate = new EventType();
	
	public static final EventType UserDeleteComplete = new EventType();

	public static final EventType UserDeleteFinished = new EventType();
	
	public static final EventType UserAuthenticate = new EventType();
	
	public static final EventType UserAuthenticateFailure = new EventType();
	
	public static final EventType UserAuthenticateSuccess = new EventType();
	
	public static final EventType ManageRoleView = new EventType();

	public static final EventType ManageRoleRequest = new EventType();
	
	public static final EventType ManageRoleReceived = new EventType();

	public static final EventType ManageGetUpdateRole = new EventType();
	
	public static final EventType ManageGetDeleteRole = new EventType();
	
	public static final EventType RoleUpdateRenderData = new EventType();
	
	public static final EventType RoleDeleteRenderData = new EventType();
	
	public static final EventType ManageRoleAdd = new EventType();
	
	public static final EventType ManageRoleUpdate = new EventType();
	
	public static final EventType ManageRoleDelete = new EventType();
	
	public static final EventType ManageRoleAddComplete = new EventType();
	
	public static final EventType ManageRoleUpdateComplete = new EventType();
	
	public static final EventType ManageRoleDeleteComplete = new EventType();
	
	public static final EventType MatchView = new EventType();

	public static final EventType MatchInitiate = new EventType();

	public static final EventType MatchRenderData = new EventType();

	public static final EventType NavMail = new EventType();

	public static final EventType NavTasks = new EventType();

	public static final EventType NavContacts = new EventType();

	public static final EventType PermissionListReceived = new EventType();
	
	public static final EventType PersonModelAllAttributeNamesReceived = new EventType();
	
	public static final EventType PersonModelAttributeNamesReceived = new EventType();

	public static final EventType PersonModelCustomFieldNamesReceived = new EventType();
	
	public static final EventType ProcessLinkView = new EventType();
	
	public static final EventType ProcessLink = new EventType();
	
	public static final EventType ProcessUnlink = new EventType();

	public static final EventType ProcessPairLinkedView = new EventType();

	public static final EventType ProcessPairUnlinkedView = new EventType();

	public static final EventType ProfileView = new EventType();

	public static final EventType ProfileUpdateInitiate = new EventType();
	
	public static final EventType ProfileUpdateComplete = new EventType();

	public static final EventType ProfileVarifyPasswordInitiate = new EventType();

	public static final EventType ProfileVarifyPasswordSuccess = new EventType();
	
	public static final EventType ProfileVarifyPasswordFailure = new EventType();
	
	public static final EventType ReportDesignView = new EventType();
	
	public static final EventType ReportRequest = new EventType();
	
	public static final EventType ReportReceived = new EventType();
	
	public static final EventType ReportAdd = new EventType();
	
	public static final EventType ReportAddComplete = new EventType();	
	
	public static final EventType ReportDelete = new EventType();
	
	public static final EventType ReportDeleteComplete = new EventType();	
	
	public static final EventType ReportUpdate = new EventType();
	
	public static final EventType ReportUpdateComplete = new EventType();	
	
	public static final EventType ReportGenerateView = new EventType();
	
	public static final EventType ReportGenerate = new EventType();
	
	public static final EventType ReportGenerateComplete = new EventType();
	
	public static final EventType ReportRequestEntryRequest = new EventType();
	
	public static final EventType ReportRequestEntryReceived = new EventType();
	
	public static final EventType ReportReloadRequest = new EventType();
	
	public static final EventType RoleListReceived = new EventType();
	
	public static final EventType SortedNeighborhoodBlockingConfigurationView = new EventType();

	public static final EventType SortedNeighborhoodBlockingConfigurationRequest = new EventType();

	public static final EventType SortedNeighborhoodBlockingConfigurationReceived = new EventType();

	public static final EventType SortedNeighborhoodBlockingConfigurationSave = new EventType();
	
	public static final EventType SortedNeighborhoodBlockingConfigurationSaveComplete = new EventType();
	
	public static final EventType SuffixArrayBlockingConfigurationView = new EventType();

	public static final EventType SuffixArrayBlockingConfigurationRequest = new EventType();

	public static final EventType SuffixArrayBlockingConfigurationReceived = new EventType();

	public static final EventType SuffixArrayBlockingConfigurationSave = new EventType();
	
	public static final EventType SuffixArrayBlockingConfigurationSaveComplete = new EventType();

	public static final EventType SystemConfigurationInfoReceived = new EventType();
	
	public static final EventType TransformationFunctionNamesReceived = new EventType();

	public static final EventType UnlinkPersonsView = new EventType();
	
	public static final EventType UnlinkPersons = new EventType();
	
	public static final EventType UnlinkPersonsComplete = new EventType();
	
	public static final EventType ViewMailFolders = new EventType();

	public static final EventType ViewMailItems = new EventType();

	public static final EventType ViewMailItem = new EventType();
}
