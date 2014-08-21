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

public class SuffixArrayBlockingConfigurationWeb extends BaseModelData
{
	public final static String BLOCKING_ROUNDS = "blockingRounds";
	public final static String MINIMUM_SUFFIX_LENGTH = "minimumSuffixLength";
	public final static String MAXIMUM_BLOCK_SIZE = "maximumBlockSize";
	public final static String SIMILARITY_METRIC = "similarityMetric";
	public final static String SIMILARITY_THRESHOLD = "similarityThreshold";
	
	public SuffixArrayBlockingConfigurationWeb() {
	}
	
	public SuffixArrayBlockingConfigurationWeb(List<BaseFieldWeb> blockingRounds, Integer minimumSuffixLength,
			Integer maximumBlockSize, String similarityMetric, Float similarityThreshold) {
		set(BLOCKING_ROUNDS, blockingRounds);
		set(MINIMUM_SUFFIX_LENGTH, minimumSuffixLength);
		set(MAXIMUM_BLOCK_SIZE, maximumBlockSize);
		set(SIMILARITY_METRIC, similarityMetric);
		set(SIMILARITY_THRESHOLD, similarityThreshold);
	}
	
	public List<BaseFieldWeb> getBlockingRounds() {
		return get(BLOCKING_ROUNDS);
	}
	
	public void setBlockingRounds(List<BaseFieldWeb> blockingRounds) {
		set(BLOCKING_ROUNDS, blockingRounds);
	}
	
	public Integer getMinimumSuffixLength() {
		return get(MINIMUM_SUFFIX_LENGTH);
	}
	
	public void setMinimumSuffixLength(Integer minimumSuffixLength) {
		set(MINIMUM_SUFFIX_LENGTH, minimumSuffixLength);
	}
	
	public Integer getMaximumBlockSize() {
		return get(MAXIMUM_BLOCK_SIZE);
	}
	
	public void setMaximumBlockSize(Integer maximumBlockSize) {
		set(MAXIMUM_BLOCK_SIZE, maximumBlockSize);
	}
	
	public String getSimilarityMetric() {
		return get(SIMILARITY_METRIC);
	}
	
	public void setSimilarityMetric(String similarityMetric) {
		set(SIMILARITY_METRIC, similarityMetric);
	}
	
	public Float getSimilarityThreshold() {
		return get(SIMILARITY_THRESHOLD);
	}
	
	public void setSimilarityThreshold(Float similarityThreshold) {
		set(SIMILARITY_THRESHOLD, similarityThreshold);
	}
}
