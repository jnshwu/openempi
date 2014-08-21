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
package org.openhie.openempi.report.impl;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.report.ReportDownloadService;

public class ReportDownloadServiceImpl implements ReportDownloadService
{
	protected final Log log = LogFactory.getLog(getClass());
	private String reportFolder;
	
	public File retrieveReportFileUsingHandle(String reportHandle) {
		File reportFolder = getReportFolderLocation();
		log.debug("Attempting to retrieve report with handle " + reportHandle + " from folder " + reportFolder.getAbsolutePath());
		File reportFile = new File(reportFolder, reportHandle);
		return reportFile;
	}

	public File getReportFolderLocation() {
		String folderLocation = getReportFolder();
		if (folderLocation.charAt(0) == '/') {
			return new File(folderLocation);
		}
		return new File(Context.getOpenEmpiHome() + "/" + folderLocation);
	}
	
	public String getReportFolder() {
		return reportFolder;
	}

	public void setReportFolder(String reportFolder) {
		this.reportFolder = reportFolder;
	}
}
