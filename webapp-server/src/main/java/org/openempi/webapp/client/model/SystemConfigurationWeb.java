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

public class SystemConfigurationWeb extends BaseFieldWeb implements Serializable
{
	public final static String BLOCKING_ALGORITHM_NAME = "blockingAlgorithmName";
	public final static String MATCHING_ALGORITHM_NAME = "matchingAlgorithmName";
	
	public SystemConfigurationWeb() {
	}
	
	public String getBlockingAlgorithmName() {
		return get(BLOCKING_ALGORITHM_NAME);
	}
	
	public void setBlockingAlgorithmName(String blockingAlgorithmName) {
		set(BLOCKING_ALGORITHM_NAME, blockingAlgorithmName);
	}
		
	public String getMatchingAlgorithmName() {
		return get(MATCHING_ALGORITHM_NAME);
	}
	
	public void setMatchingAlgorithmName(String matchingAlgorithmName) {
		set(MATCHING_ALGORITHM_NAME, matchingAlgorithmName);
	}
}
