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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.report.ReportDownloadService;

public class ReportDownloadServlet extends HttpServlet
{
	private Logger log = Logger.getLogger(getClass());

	private ReportDownloadService reportDownloadService;
	
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (!Context.isInitialized()) {
			log.error("The context did not initialize properly.");
			return;
		}
		reportDownloadService = (ReportDownloadService) Context.getApplicationContext().getBean("reportDownloadService");
	}
    
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get the 'file' parameter
		String reportHandle = (String) request.getParameter("reportHandle");
		if (reportHandle == null || reportHandle.equals("")) {
			log.error("Received request to retrieve report using invalid report handle: " + reportHandle);
			throw new ServletException(
					"Invalid or non-existent report handle parameter.");
		}
		
		ServletOutputStream stream = null;
		BufferedInputStream buf = null;
		try {
			stream = response.getOutputStream();
			File reportFile = reportDownloadService.retrieveReportFileUsingHandle(reportHandle);
			if (!reportFile.exists()) {
				log.error("Received request to retrieve non-existant report " + reportHandle);
				throw new ServletException("Report " + reportHandle + " does not exist on the server.");
			}
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "inline; filename=" + reportHandle);
			response.setContentLength((int) reportFile.length());
			FileInputStream input = new FileInputStream(reportFile);
			buf = new BufferedInputStream(input);
			int readBytes = 0;
			while ((readBytes = buf.read()) != -1)
				stream.write(readBytes);
		} catch (IOException e) {
			log.error("Failed while attempting to stream the report data to the client: " + e, e);
			throw new ServletException(e.getMessage());
		} finally {
			if (stream != null)
				stream.close();
			if (buf != null)
				buf.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
