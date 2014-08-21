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
package org.openhie.openempi.configuration;

import java.util.List;
import java.util.Map;

import org.openhie.openempi.blocking.basicblockinghp.BasicBlockingConstants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;

public class BasicBlockingConfigurationTest extends BaseServiceTestCase
{
	@SuppressWarnings("unchecked")
	public void testConfiguration() {
		Configuration configuration = Context.getConfiguration();
		configuration.init();
		log.debug("Configuration is: " + configuration);
		java.util.List<CustomField> customFields = configuration.getCustomFields();
		for (CustomField customField : customFields) {
			log.debug("Custom field is: " + customField);
		}
		
//		java.util.List<BlockingRound> blockingRounds = (java.util.List<BlockingRound>)
//			configuration.lookupConfigurationEntry(BasicBlockingConstants.BLOCKING_ROUNDS_REGISTRY_KEY);		
		Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
				.lookupConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION);
		java.util.List<BlockingRound> blockingRounds = (List<BlockingRound>) configurationData.get(BasicBlockingConstants.BLOCKING_ROUNDS_REGISTRY_KEY);
		
		for (BlockingRound blockingRound : blockingRounds) {
			log.debug("Blocking round: " + blockingRound);
		}
		
//		java.util.List<MatchField> fields = (java.util.List<MatchField>)
//			configuration.lookupConfigurationEntry(ExactMatchingConstants.EXACT_MATCHING_FIELDS_REGISTRY_KEY);
//		if (fields != null) {
//			for (MatchField field : fields) {
//				log.debug("Match field is: " + field);
//			}
//		}
	}
}
