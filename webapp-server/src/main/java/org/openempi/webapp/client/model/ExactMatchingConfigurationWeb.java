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
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ExactMatchingConfigurationWeb extends BaseModelData implements Serializable
{
	public static final String MATCH_FIELDS = "matchFields";

	public MatchFieldWeb dummyField;	// This is need so MatchFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	
	public ExactMatchingConfigurationWeb() {
	}

	public List<org.openempi.webapp.client.model.MatchFieldWeb> getMatchFields() {
		return get(MATCH_FIELDS);
	}

	public void setMatchFields(List<org.openempi.webapp.client.model.MatchFieldWeb> matchFields) {
		set(MATCH_FIELDS, matchFields);
	}
}
