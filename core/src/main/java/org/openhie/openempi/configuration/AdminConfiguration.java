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

import java.util.List;

public class AdminConfiguration
{
	private String configFileDirectory;
	private boolean autoStartPIXPDQ;
	private List<UpdateNotificationRegistrationEntry> updateNotificationRegistrationEntries;
	
	public AdminConfiguration() {
		updateNotificationRegistrationEntries = new java.util.ArrayList<UpdateNotificationRegistrationEntry>();
	}

	public boolean isAutoStartPIXPDQ() {
		return autoStartPIXPDQ;
	}

	public void setAutoStartPIXPDQ(boolean autoStartPIXPDQ) {
		this.autoStartPIXPDQ = autoStartPIXPDQ;
	}

	public String getConfigFileDirectory() {
		return configFileDirectory;
	}

	public void setConfigFileDirectory(String configFileDirectory) {
		this.configFileDirectory = configFileDirectory;
	}

	public List<UpdateNotificationRegistrationEntry> getUpdateNotificationRegistrationEntries() {
		return updateNotificationRegistrationEntries;
	}

	public void setUpdateNotificationRegistrationEntries(List<UpdateNotificationRegistrationEntry> updateNotificationRegistrationEntries) {
		this.updateNotificationRegistrationEntries = updateNotificationRegistrationEntries;
	}

	public void addUpdateNotificationRegistrationEntries(UpdateNotificationRegistrationEntry updateNotificationRegistrationEntry) {
		updateNotificationRegistrationEntries.add(updateNotificationRegistrationEntry);
	}

	@Override
	public String toString() {
		return "AdminConfiguration [configFileDirectory=" + configFileDirectory + ", autoStartPIXPDQ="
				+ autoStartPIXPDQ + ", updateNotificationRegistrationEntries=" + updateNotificationRegistrationEntries
				+ "]";
	}
}
