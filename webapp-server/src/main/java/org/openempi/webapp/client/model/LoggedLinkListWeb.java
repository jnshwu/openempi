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

import com.extjs.gxt.ui.client.data.BaseModel;

public class LoggedLinkListWeb extends BaseModel implements Serializable
{
	@SuppressWarnings("unused")
    private AuditEventTypeWeb unusedReviewRecordPairWeb;
	
	@SuppressWarnings("unused")
    private UserWeb unusedUserWeb;
	
	public LoggedLinkListWeb() {
	}

	public java.lang.Integer getTotalCount() {
		return get("totalCount");
	}
	
	public void setTotalCount(java.lang.Integer totalCount) {
		set("totalCount", totalCount);
	}

	public java.util.List<ReviewRecordPairWeb> reviewRecordPairs() {
		return get("reviewRecordPairs");
	}
	
	public void setReviewRecordPairs(java.util.List<ReviewRecordPairWeb> reviewRecordPairs) {
		set("reviewRecordPairs", reviewRecordPairs);
	}
}
