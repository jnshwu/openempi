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

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class SortedNeighborhoodConfigurationWeb extends BaseModelData
{
	public final static String BLOCKING_ROUNDS = "blockingRounds";
	public final static String WINDOW_SIZE = "windowSize";
	
	public SortedNeighborhoodConfigurationWeb() {
	}
	
	public SortedNeighborhoodConfigurationWeb(List<BaseFieldWeb> blockingRounds, Integer windowSize) {
		set(BLOCKING_ROUNDS, blockingRounds);
		set(WINDOW_SIZE, windowSize);
	}
	
	public List<BaseFieldWeb> getBlockingRounds() {
		return get(BLOCKING_ROUNDS);
	}
	
	public void setBlockingRounds(List<BaseFieldWeb> blockingRounds) {
		set(BLOCKING_ROUNDS, blockingRounds);
	}
	
	public Integer getWindowSize() {
		return get(WINDOW_SIZE);
	}
	
	public void setWindowSize(Integer windowSize) {
		set(WINDOW_SIZE, windowSize);
	}
}
