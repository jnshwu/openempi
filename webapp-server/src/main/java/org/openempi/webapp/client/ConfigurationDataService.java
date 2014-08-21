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

import java.util.List;

import org.openempi.webapp.client.model.CustomFieldWeb;
import org.openempi.webapp.client.model.MatchConfigurationWeb;
import org.openempi.webapp.client.model.ExactMatchingConfigurationWeb;
import org.openempi.webapp.client.model.StringComparatorFunctionWeb;
import org.openempi.webapp.client.model.TransformationFunctionWeb;
import org.openempi.webapp.client.model.VectorConfigurationWeb;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ConfigurationDataService extends RemoteService
{
	public List<StringComparatorFunctionWeb> getStringComparatorFunctionList();

	public List<TransformationFunctionWeb> getTransfromationFunctionList();

	public List<CustomFieldWeb> loadCustomFieldsConfiguration() throws Exception;
	
	public List<VectorConfigurationWeb> loadVectorConfiguration() throws Exception;

	public MatchConfigurationWeb loadProbabilisticMatchingConfiguration() throws Exception;
	
	public ExactMatchingConfigurationWeb loadExactMatchingConfiguration() throws Exception;

	public String saveCustomFieldsConfiguration(List<CustomFieldWeb> customFieldsConfiguration) throws Exception;

	public String saveExactMatchingConfiguration(ExactMatchingConfigurationWeb matchConfiguration) throws Exception;
	
	public String saveProbabilisticMatchingConfiguration(MatchConfigurationWeb matchConfiguration) throws Exception;
}
