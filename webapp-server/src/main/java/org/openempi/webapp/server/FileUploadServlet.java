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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.UserFile;
import org.openhie.openempi.service.UserManager;

public class FileUploadServlet extends HttpServlet
{
	private static File uploadDirectory;
	
	private UserManager userManager; 
	private Logger log = Logger.getLogger(getClass());
	
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (!Context.isInitialized()) {
			log.error("The context did not initialize properly.");
			return;
		}
		userManager = Context.getUserManager();
		String uploadDirectoryName = Context.getConfiguration().getAdminConfiguration().getConfigFileDirectory();
		uploadDirectory = new File(uploadDirectoryName);
        if (!uploadDirectory.exists()) {
        	uploadDirectory.mkdir();
        }
		log.info("Set the upload directory to: " + uploadDirectory.getAbsolutePath());
	}

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String message = "success";
        // process only multipart requests
        if (ServletFileUpload.isMultipartContent(req)) {

            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            try {
                List<FileItem> items = upload.parseRequest(req);
                String name = null;
                for (FileItem item : items) {
                	// Store field names
                	if (item.isFormField())  {
                    	if (item.getFieldName().equals("name")) {
                    		name = new String(item.get());
                    	}
                    	continue;
                    }
                    
                    String fileName = item.getName();
                    // get only the file name not whole path
                    if (fileName != null) {
                        fileName = FilenameUtils. getName(fileName);
                    }
                    
                    File uploadedFile = new File(uploadDirectory, fileName);
                    if (uploadedFile.createNewFile()) {
                    	log.debug("Wrote out " + message.length() + " bytes.");
                        item.write(uploadedFile);
                        saveUploadFileEntry(name, fileName, uploadedFile.getAbsolutePath());
                    } else {
                        log.warn("The file already exists in repository: " + uploadedFile.getAbsolutePath());
                        message = "This file already exists in the repository.";
                    }
                }

            } catch (Exception e) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                        "An error occurred while creating the file : " + e.getMessage());
            	log.error("Failed while attempting to upload file: " + e.getMessage(), e);
            	message = "Failed to upload file. Error is: " + e.getMessage();
            }

        } else {
//            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
//                            "Request contents type is not supported by the servlet.");
        	log.warn("Request contents type is not supported by the service.");
        	message = "Upload request contents type is not supported.";
        }
    	// Set to expire far in the past.
    	resp.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
    	// Set standard HTTP/1.1 no-cache headers.
    	resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    	// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
    	resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
    	// Set standard HTTP/1.0 no-cache header.
    	resp.setHeader("Pragma", "no-cache");
    	
    	resp.setContentType("text/html");
    	resp.setContentLength(message.length());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().printf(message);
    }

	private void saveUploadFileEntry(String name, String filename, String absolutePath) throws IOException {
		
		if (name == null || name.length() == 0) {
			log.error("Attempted to upload a file with a blank name of " + name + " and filename of " + filename);
			throw new IOException("File name is not valid.");
		}
		
		// TODO: Once user management is plugged in this won't be needed here
		checkLoggedInUser();
		
		UserFile userFile = new UserFile(name, absolutePath);
		userFile = userManager.saveUserFile(userFile);
		log.debug("Saved user file entry: " + userFile);
	}

	private void checkLoggedInUser() {
		String sessionKey = Context.getUserContext().getSessionKey();
		if (sessionKey == null) {
			Context.authenticate("admin", "admin");
		}
		log.debug("Currently logged in user is " + Context.getUserContext().getUser());
	}
}
