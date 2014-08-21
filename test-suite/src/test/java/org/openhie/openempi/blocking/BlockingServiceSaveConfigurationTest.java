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
package org.openhie.openempi.blocking;

import org.openhie.openempi.configuration.xml.BlockingConfigurationType;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingField;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingFields;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingRound;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds;
import org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;

public class BlockingServiceSaveConfigurationTest extends BaseServiceTestCase
{
	public void testUpdateBlockingConfiguration() {
		try {
			BlockingConfigurationType blockingNode = Context.getConfiguration().getBlockingConfiguration();
			BasicBlockingType basicBlocking = (BasicBlockingType) blockingNode;
			log.debug("Basic blocking info is: " + basicBlocking);
			
			BasicBlockingType newBasicBlocking = BasicBlockingType.Factory.newInstance();
			BlockingRounds roundsNode = newBasicBlocking.addNewBlockingRounds();
			BlockingRound roundNode = roundsNode.addNewBlockingRound();
			BlockingFields blockingFields = roundNode.addNewBlockingFields();
			BlockingField field = blockingFields.addNewBlockingField();
			field.setFieldName("givenName");
			field = blockingFields.addNewBlockingField();
			field.setFieldName("familyName");
			
			log.debug("Modified blocking info is: " + newBasicBlocking);
			
			blockingNode = Context.getConfiguration().saveBlockingConfiguration(newBasicBlocking);
			log.debug("Basic blocking info is: " + basicBlocking);			
			
		} catch (Exception e) {
			log.error("Failed while saving configuration: " + e, e);
		}
	}
}
