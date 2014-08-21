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
package org.openhie.openempi.webapp.action;

import org.apache.struts2.ServletActionContext;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.FileLoaderManager;
import org.openhie.openempi.loader.NominalSetFileLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Sample action that shows how to do file upload with Struts 2.
 */
public class FileUploadAction extends BaseAction {
    private static final long serialVersionUID = -9208910183310010569L;
    private File file;
    private String fileContentType;
    private String fileFileName;
    private String name;
    private Context context;

    /**
     * Upload the file
     * @return String with result (cancel, input or sucess)
     * @throws Exception if something goes wrong
     */
    public String upload() throws Exception {
        if (this.cancel != null) {
            return "cancel";
        }

        // the directory to upload to
        String uploadDir = ServletActionContext.getServletContext().getRealPath("/resources")
                + "/" + getRequest().getRemoteUser() + "/";

        // write the file to the file specified
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = new FileInputStream(file);

        log.debug("The context has been set to: " + context);
        
        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + fileFileName);
        int bytesRead;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();
        stream.close();
        
        String filename = uploadDir + fileFileName;
        try {
	        FileLoaderManager fileLoaderManager = new FileLoaderManager();
			fileLoaderManager.setUp();
			fileLoaderManager.loadFile(filename, NominalSetFileLoader.LOADER_ALIAS);
        } catch (Exception e) {
        	log.error("Failed to parse and upload the file " + filename + " due to " + e.getMessage());
        	getRequest().setAttribute("javax.servlet.error.exception", e);
        	return ERROR;
        }
        
        // place the data into the request for retrieval on next page
        getRequest().setAttribute("location", dirPath.getAbsolutePath()
                + Constants.FILE_SEP + fileFileName);

        String link = getRequest().getContextPath() + "/resources" + "/"
                + getRequest().getRemoteUser() + "/";

        getRequest().setAttribute("link", link + fileFileName);

        return SUCCESS;
    }

    /**
     * Default method - returns "input"
     * @return "input"
     */
    public String execute() {
        return INPUT;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
    public void validate() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            getFieldErrors().clear();
            if ("".equals(fileFileName) || file == null) {
                super.addFieldError("file", getText("errors.requiredField", new String[] {getText("uploadForm.file")}));
            } else if (file.length() > 2097152) {
                addActionError(getText("maxLengthExceeded"));
            }
        }
    }
}
