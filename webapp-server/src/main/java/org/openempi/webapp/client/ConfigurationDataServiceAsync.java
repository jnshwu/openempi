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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigurationDataServiceAsync
{
	public void getStringComparatorFunctionList(AsyncCallback<List<StringComparatorFunctionWeb>> callback);

	public void getTransfromationFunctionList(AsyncCallback<List<TransformationFunctionWeb>> callback);

	public void loadCustomFieldsConfiguration(AsyncCallback<List<CustomFieldWeb>> callback);
	
	public void loadProbabilisticMatchingConfiguration(AsyncCallback<MatchConfigurationWeb> callback);

	public void loadExactMatchingConfiguration(AsyncCallback<ExactMatchingConfigurationWeb> callback);
	
	public void loadVectorConfiguration(AsyncCallback<List<VectorConfigurationWeb>> callback);

	public void saveCustomFieldsConfiguration(List<CustomFieldWeb> customFieldsConfiguration, AsyncCallback<String> callback);

	public void saveExactMatchingConfiguration(ExactMatchingConfigurationWeb matchingConfiguration, AsyncCallback<String> callback);
	
	public void saveProbabilisticMatchingConfiguration(MatchConfigurationWeb matchingConfiguration, AsyncCallback<String> callback);
}
