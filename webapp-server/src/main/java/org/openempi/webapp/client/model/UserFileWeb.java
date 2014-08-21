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
package org.openempi.webapp.client.model;

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserFileWeb extends BaseModelData
{
	public UserFileWeb() {
		set("imported", "N");
		set("profiled", "N");
	}

	public java.util.Date getDateCreated() {
		return get("dateCreated");
	}

	public void setDateCreated(java.util.Date dateCreated) {
		set("dateCreated", dateCreated);
	}

	public java.lang.String getFilename() {
		return get("filename");
	}

	public void setFilename(java.lang.String filename) {
		set("filename", filename);
	}

	public java.lang.String getImported() {
		return get("imported");
	}

	public void setImported(java.lang.String imported) {
		set("imported", imported);
	}

	public java.lang.String getProfiled() {
		return get("profiled");
	}

	public void setProfiled(java.lang.String profiled) {
		set("profiled", profiled);
	}
	
	public java.lang.Integer getRowsImported() {
		return get("rowsImported");
	}

	public void setRowsImported(java.lang.Integer rowsImported) {
		set("rowsImported", rowsImported);
	}
	
	public java.lang.Integer getRowsProcessed() {
		return get("rowsProcessed");
	}

	public void setRowsProcessed(java.lang.Integer rowsProcessed) {
		set("rowsProcessed", rowsProcessed);
	}
	
	public java.lang.String getProfileProcessed() {
		return get("profileProcessed");
	}

	public void setProfileProcessed(java.lang.String profileProcessed) {
		set("profileProcessed", profileProcessed);
	}
	
	public java.lang.String getName() {
		return get("name");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.Integer getUserFileId() {
		return get("userFileId");
	}

	public void setUserFileId(java.lang.Integer userFileId) {
		set("userFileId", userFileId);
	}
	
	public java.lang.Boolean getImportOnly() {
		return get("importOnly");
	}

	public void setImportOnly(java.lang.Boolean importOnly) {
		set("importOnly", importOnly);
	}	
	
	public java.lang.Boolean getSkipHeaderLine() {
		return get("skipHeaderLine");
	}

	public void setSkipHeaderLine(java.lang.Boolean skipHeaderLine) {
		set("skipHeaderLine", skipHeaderLine);
	}
	
	public java.lang.String getFileLoaderName() {
		return get("fileLoaderName");
	}

	public void setFileLoaderName(java.lang.String fileLoaderName) {
		set("fileLoaderName", fileLoaderName);
	}
	public HashMap<String, Object> getFileLoaderMap() {
		return get("fileLoaderMap");
	}

	public void setFileLoaderMap(HashMap<String, Object> fileLoaderMap) {
		set("fileLoaderMap", fileLoaderMap);
	}	
}
