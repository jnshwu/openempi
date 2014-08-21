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

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LoggedLinkSearchCriteriaWeb extends BaseModelData implements Serializable
{
	public LoggedLinkSearchCriteriaWeb() {
	}
	
	public java.lang.Integer getVector() {
		return get("vector");
	}

	public void setVector(java.lang.Integer vector) {
		set("vector", vector);
	}
	
	public java.lang.Integer getFirstResult() {
		return get("firstResult");
	}

	public void setFirstResult(java.lang.Integer firstResult) {
		set("firstResult", firstResult);
	}
	
	public java.lang.Integer getMaxResults() {
		return get("maxResults");
	}

	public void setMaxResults(java.lang.Integer maxResults) {
		set("maxResults", maxResults);
	}
}
