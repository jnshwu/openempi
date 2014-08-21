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
package org.openhie.openempi.profiling;

import java.util.List;
import java.util.Map;
import java.lang.Math;

import org.openhie.openempi.dao.DataProfileAttributeDao;
import org.openhie.openempi.model.DataProfileAttribute;
import org.openhie.openempi.model.DataProfileAttributeValue;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.springframework.util.StringUtils;

public class DataProfiler extends BaseServiceImpl implements Runnable
{
	private RecordDataSource recordDataSource;
	private DataProfileAttributeDao dataProfileAttributeDao;
	private java.util.Map<String, AttributeMetadata> metadata;
	private java.util.List<String> attributeNames;
	private int attributeCount;
	private int attributeBlockSize;

	public DataProfiler() {
		metadata = new java.util.HashMap<String,AttributeMetadata>();
	}
	
	// The data profiler uses up quite a bit of memory for keeping track of all the possible values
	// that an attribute can take in a data set. When the data set has millions of records, trying
	// to keep all this information in memory at once will demand a lot of memory.
	//
	// To solve this problem and make this analysis scalable, we allow the caller to configure
	// the number of attributes that we will attempt to process during each iteration over all the
	// data in the repository. This is wasteful on the query side since we will have to run over
	// the entire data set multiple times but at least it will scale in terms of memory.
	//
	public void run() {
		log.info("Executing the scheduled task: " + getClass());
		try {

			if (getRecordDataSource().isEmpty()) {
				log.info("Record count is zero. Skipping Data Profiler processing ");
				return;
			}

			clearCurrentMetrics();
			int recordCount=0;
			boolean done=false;
			int attributeBlockStartIndex = 0;
			int totalRecordCount=0;
			long startTime = getTimestamp();
			List<AttributeMetadata> attribMetadata = getRecordDataSource().getAttributeMetadata();
			buildMetadataMapAndNameList(attribMetadata);
			do {
				java.util.List<String> attributesToBeProcessed = null;
				java.util.Map<String,AttributeMetrics> attributeMetricsMap = new java.util.HashMap<String,AttributeMetrics>();
				for (Record record : recordDataSource) {
					 if (recordCount == 0) {
						if(totalRecordCount == 0 ) {
							attributeBlockStartIndex = 0;
						} else {
							attributeBlockStartIndex = setAttributeBlockStartIndex(attributeBlockStartIndex, attributeBlockSize);
						}
						attributesToBeProcessed = getAttributeBlock(attributeNames, attributeBlockStartIndex, attributeBlockSize);
						setupMetricsMap(attributeMetricsMap, attributesToBeProcessed);
					}
					analyzeRecord(record, attributesToBeProcessed, attributeMetricsMap);
					recordCount++;
				}
				if (attributesToBeProcessed != null) {
					persistAttributeMetrics(attributesToBeProcessed, attributeMetricsMap);
				}
				done = isMoreAttributeBlocksLeft(attributeBlockStartIndex, attributeBlockSize);
				totalRecordCount += recordCount;
				recordCount = 0;				
			} while (!done);
			recordDataSource.close("Completed");
			long endTime = getTimestamp();
			log.info("Data Profiler finished processing " + totalRecordCount + " records in " + (endTime-startTime) + " msec.");
		} catch (Throwable e) {
			recordDataSource.close("Failed");
			log.warn("Failed while generating the data profiling data: " + e, e);
		}
	}

	private void clearCurrentMetrics() {
		log.info("Clearing the repository of any existing data profile attribute metrics.");
		int recordCount = dataProfileAttributeDao.removeAllDataProfileAttributes(recordDataSource.getRecordDataSourceId());
		log.info("Removed existing data profile attribute metrics of count: " + recordCount);
	}

	private void persistAttributeMetrics(List<String> attributesToBeProcessed, Map<String, AttributeMetrics> attributeMetricsMap) {
		for (String attribute : attributesToBeProcessed) {
			AttributeMetrics metrics = attributeMetricsMap.get(attribute);
			DataProfileAttribute dataProfileAttribute = dataProfileAttributeDao.saveDataProfileAttribute(metrics);
			Map<Object,Integer> valueFrequencyMap = metrics.getValueFrequencyMap();
			double entropy = 0;
			double uValue = 0;
			for (Object value : valueFrequencyMap.keySet()) {
				
				Integer frequency = valueFrequencyMap.get(value);
				if (frequency.intValue() == 1) {
					metrics.incrementUniqueCount();
				}
				if (frequency.intValue() > 1) {
					metrics.incrementDuplicateCount();
				}
				double probability = calculateProbability(frequency.doubleValue(), metrics.getRowCount());
				entropy += calculateInformation(probability);
				if (attribute.equals("state")) {
					log.debug(frequency + "," + probability + "," + entropy);
				}
				uValue += calculateUValue(probability, metrics.getRowCount());
				DataProfileAttributeValue avalue = new DataProfileAttributeValue();
				avalue.setAttributeId(dataProfileAttribute.getAttributeId());
				avalue.setAttributeValue(value.toString());
				avalue.setFrequency(frequency);
				dataProfileAttributeDao.saveDataProfileAttributeValue(avalue);
			}
			calculateAdditionalMetrics(attribute, metrics);
			metrics.setEntropy(entropy);
			metrics.setuValue(uValue);
			dataProfileAttributeDao.saveDataProfileAttribute(metrics);
			log.debug(metrics);
		}
	}

	private void calculateAdditionalMetrics(String attribute, AttributeMetrics metrics) {
		// Null rate
		if (metrics.getNullCount() != null) {
			double nullCount = (double) metrics.getNullCount();
			metrics.setNullRate(nullCount/((double) metrics.getRowCount()));
		}
		// Average Token Frequency & Maximum Entropy
		if (metrics.getDistinctCount() != null && metrics.getDistinctCount() > 0) {
			double uniques = (double) metrics.getDistinctCount();
			metrics.setAverageTokenFrequency(((double) metrics.getRowCount())/uniques);
			double probability = calculateProbability(metrics.getAverageTokenFrequency(), metrics.getRowCount());
			double info = calculateInformation(probability);
			metrics.setMaximumEntropy(info*metrics.getDistinctCount());
		}
		
		// Finalize the calculation of the variance
		if (isNumericDatatype(metrics.getAttributeMetadata().getDatatype()) &&
				metrics.getAttributeMetadata().getDatatype() != AttributeMetrics.DATE_DATA_TYPE) {
			// variance = M2/(n - 1)
			double valueCount = metrics.getRowCount();
			if (metrics.getNullCount() != null) {
				valueCount -= metrics.getNullCount();
			}
			valueCount--;
			
			if (valueCount <= 1) {
				metrics.setVariance(0.0);
				metrics.setStandardDeviation(0.0);
			} else {
				double variance = metrics.tempSquareDiff / ((double) valueCount);
				metrics.setVariance(variance);
				metrics.setStandardDeviation(Math.sqrt(variance));
			}
		}
		int nonNulls = metrics.getRowCount() - (metrics.getNullCount() != null ? metrics.getNullCount() : 0);
		metrics.setBlockingPairs(nonNulls * (nonNulls-1)/2);		
	}

	private double calculateProbability(double frequency, Integer rowCount) {
		double p = (frequency/((double) rowCount));
		return p;
	}

	private double calculateUValue(double probability, Integer rowCount) {
		return probability*probability;
	}

	private double calculateInformation(double p) {
		double info = -p * (Math.log(p)/Math.log(2));
		return info;
	}

	/**
	 * Allocate one attribute metrics object per attribute in the map so that we can keep track of the calculations
	 * that we are making for each attribute as we iterate over all the records. The map is indexed by attribute name.
	 * 
	 */
	private void setupMetricsMap(Map<String, AttributeMetrics> attributeMetricsMap, List<String> attributesToBeProcessed) {
		for (String attribute : attributesToBeProcessed) {
			AttributeMetadata meta = metadata.get(attribute);
			if (meta == null) {
				log.error("Found attribute in record for which we have no metadata; this is an unexpected error condition.");
				throw new RuntimeException("Unable to continue to to an unexpected error condition; please check the logs.");
			}
			AttributeMetrics metrics = new AttributeMetrics(attribute, meta.getDatatype(), meta);
			metrics.setDataSourceId(recordDataSource.getRecordDataSourceId());
			attributeMetricsMap.put(attribute,  metrics);
		}
	}

	private long getTimestamp() {
		return new java.util.Date().getTime();
	}

	private void analyzeRecord(Record record, List<String> attributesToBeProcessed, Map<String, AttributeMetrics> attributeMetricsMap) {
		for (String attribute : attributesToBeProcessed) {
			Object value = record.get(attribute);
			AttributeMetrics metrics = attributeMetricsMap.get(attribute);
			if (metrics == null) {
				log.debug("Unable to find a metrics value for :  " + attribute);
			}

			if (metrics.getDatatypeId() < 0) {
				metrics.setDatatypeId(extractDatatypeFromValue(attribute, value));
			}
			int datatypeId = metrics.getDatatypeId();
			/*
			 * If the value is null/empty string there are
			 * only a couple of metrics we can update
			 */
			if (datatypeId == DataProfileAttribute.STRING_DATA_TYPE) {
				if (!StringUtils.hasText((String) value)) {
					metrics.incrementNullCount();
					metrics.incrementRowCount();
					continue;
				}
			} else {
				if (value == null) {
					metrics.incrementNullCount();
					metrics.incrementRowCount();
					continue;
				}
			}

			
			// Update the value frequency map; if this is the first time we encounter this
			// value for the field, increment the distinct count
			Integer frequency = metrics.getValueFrequencyMap().get(value);
			if (frequency == null) {
				frequency = 0;
				metrics.incrementDistinctCount();
			}
			frequency = frequency.intValue() + 1;
			metrics.getValueFrequencyMap().put(value, frequency);
			metrics.incrementRowCount();

			if (isNumericDatatype(datatypeId)) {
				double normalValue = normalizeValue(value, datatypeId);
				metrics.updateMinimumValue(normalValue);
				metrics.updateMaximumValue(normalValue);
				metrics.updateAverageValueAndVariance(normalValue);
			} else if (datatypeId == DataProfileAttribute.STRING_DATA_TYPE) {
				String svalue = (String) value;
				int length = svalue.length();
				metrics.updateAverageLength(length);
				metrics.updateMinimumLength(length);
				metrics.updateMaximumLength(length);
			}
		}
	}

	private List<String> getAttributeBlock(java.util.List<String> attributeNames, int attributeBlockStartIndex, int blockSize) {
		java.util.List<String> list = new java.util.ArrayList<String>();
		if (blockSize == 0) {
			list.addAll(attributeNames);
		} else {
			int number = Math.min(attributeBlockStartIndex + blockSize, getAttributeCount() );
			for (int i=attributeBlockStartIndex; i < number; i++) {
				list.add(attributeNames.get(i));
			}
		}
		StringBuffer sb = new StringBuffer("[");
		for (String attribute : list) {
			sb.append(attribute).append(",");
		}
		sb.append("]");
		log.debug("Processing attribute block: " + sb);
		return list;
	}

	// If we are processing all the attributes in one iteration (blockSize = 0) or
	// we have already processed all the attribute blocks, then we are done.
	// Otherwise, there is more work to be done.
	//
	private boolean isMoreAttributeBlocksLeft(int attributeBlockStartIndex, int blockSize) {
		if (blockSize == 0 ||
				attributeBlockStartIndex+blockSize >= getAttributeCount()) {
			return true;
		}
		return false;
	}

	// If we are processing all the attributes in one run through the data in the source
	// then the starting index is zero.
	// Otherwise, just increment the current number of the attribute index by the size of the
	// attribute block.
	//
	private int setAttributeBlockStartIndex(int attributeBlockStartIndex, int blockSize) {
		if (blockSize == 0) {
			return 0;
		}
		attributeBlockStartIndex += blockSize;
		return attributeBlockStartIndex;
	}

	private void buildMetadataMapAndNameList(List<AttributeMetadata> attribMetadata) {
		attributeNames = new java.util.ArrayList<String>(attribMetadata.size());
		attributeCount = 0;
		for (AttributeMetadata meta : attribMetadata) {
			String attributeName = meta.getAttributeName();
			if(!attributeName.contains("<null>")) {
				log.info("Metadata for attribute " + attributeName + " is " + meta);
				attributeNames.add(attributeName);
				metadata.put(attributeName, meta);
				attributeCount++;
			}
		}
	}
	
	private int extractDatatypeFromValue(String attribute, Object value) {
		int datatype = -1;
		if (value instanceof String) {
			datatype = DataProfileAttribute.STRING_DATA_TYPE;
		} else if (value instanceof Integer) {
			datatype = DataProfileAttribute.INTEGER_DATA_TYPE;
		} else if (value instanceof Long) {
			datatype = DataProfileAttribute.LONG_DATA_TYPE;
		} else if (value instanceof Float) {
			datatype = DataProfileAttribute.FLOAT_DATA_TYPE;
		} else if (value instanceof Double) {
			datatype = DataProfileAttribute.DOUBLE_DATA_TYPE;
		} else if (value instanceof java.util.Date) {
			datatype = DataProfileAttribute.DATE_DATA_TYPE;
		} else {
			log.warn("Attribute " + attribute + " is of unknown data type " + value.getClass() + " and will be ignored.");
		}
		return datatype;
	}

	private double normalizeValue(Object value, int datatypeId) {
		double norm;
		if (datatypeId == DataProfileAttribute.DOUBLE_DATA_TYPE) {
			norm = (Double) value;
			return norm;
		}
		if (datatypeId == DataProfileAttribute.FLOAT_DATA_TYPE) {
			norm = ((Float) value).doubleValue();
			return norm;
		}
		if (datatypeId == DataProfileAttribute.INTEGER_DATA_TYPE) {
			norm = ((Integer) value);
			return norm;
		}
		if (datatypeId == DataProfileAttribute.LONG_DATA_TYPE) {
			norm = (Double) value;
			return norm;
		}
		if (datatypeId == DataProfileAttribute.DATE_DATA_TYPE) {
			norm = ((java.util.Date) value).getTime();
			return norm;
		}
		log.warn("This should not happend; we are unable to normalize a value of unknown datatype.");
		return 0;
	}
	
	public boolean isNumericDatatype(int datatypeId) {
		if (datatypeId == DataProfileAttribute.DOUBLE_DATA_TYPE ||
				datatypeId == DataProfileAttribute.FLOAT_DATA_TYPE ||
				datatypeId == DataProfileAttribute.INTEGER_DATA_TYPE ||
				datatypeId == DataProfileAttribute.DATE_DATA_TYPE ||
				datatypeId == DataProfileAttribute.LONG_DATA_TYPE) {
			return true;
		}
		return false;
	}
	
	public int getAttributeCount() {
		return attributeCount;
	}

	public void setAttributeCount(int attributeCount) {
		this.attributeCount = attributeCount;
	}

	public RecordDataSource getRecordDataSource() {
		return recordDataSource;
	}

	public void setRecordDataSource(RecordDataSource recordDataSource) {
		this.recordDataSource = recordDataSource;
	}
	
	public DataProfileAttributeDao getDataProfileAttributeDao() {
		return dataProfileAttributeDao;
	}

	public void setDataProfileAttributeDao(DataProfileAttributeDao dataProfileAttributeDao) {
		this.dataProfileAttributeDao = dataProfileAttributeDao;
	}

	public int getAttributeBlockSize() {
		return attributeBlockSize;
	}

	public void setAttributeBlockSize(int attributeBlockSize) {
		this.attributeBlockSize = attributeBlockSize;
	}
	
	public class AttributeMetrics extends DataProfileAttribute
	{
		private static final long serialVersionUID = -5805814946755578692L;

		private java.util.Map<Object,Integer> valueFrequencyMap;
		private AttributeMetadata metadata;
		private double tempAverageLength;
		private double tempAverageValue;
		private double tempSquareDiff;

		public AttributeMetrics(String attributeName, int datatypeId, AttributeMetadata metadata) {
			super(attributeName, datatypeId);
			valueFrequencyMap = new java.util.HashMap<Object,Integer>();
			this.metadata = metadata;
			setDatatypeId(metadata.getDatatype());
			this.tempAverageLength = 0;
			this.tempAverageValue = 0;
			this.tempSquareDiff = 0;
		}
		
		public java.util.Map<Object,Integer> getValueFrequencyMap() {
			return valueFrequencyMap;
		}
		
		public void incrementRowCount() {
			if (getRowCount() == null) {
				setRowCount(0);
			}
			setRowCount(getRowCount() + 1);
		}
		
		public void incrementNullCount() {
			if (getNullCount() == null) {
				setNullCount(0);
			}
			setNullCount(getNullCount() + 1);
		}
		
		public void incrementDistinctCount() {
			if (getDistinctCount() == null) {
				setDistinctCount(0);
			}
			setDistinctCount(getDistinctCount() + 1);
		}
		
		public void incrementUniqueCount() {
			if (getUniqueCount() == null) {
				setUniqueCount(0);
			}
			setUniqueCount(getUniqueCount() + 1);
		}
		
		
		public void incrementDuplicateCount() {
			if (getDuplicateCount() == null) {
				setDuplicateCount(0);
			}
			setDuplicateCount(getDuplicateCount() + 1);
		}
		
		public void updateMaximumValue(double value) {
			Double max = getMaximumValue();
			if (max == null) {
				max = -Double.MAX_VALUE;
			}
			if (max < value) {
				setMaximumValue(value);
			}
		}
		
		public void updateMinimumValue(double value) {
			Double min = getMinimumValue();
			if (min == null) {
				min = Double.MAX_VALUE;
			}
			if (min > value) {
				setMinimumValue(value);
			}
		}
		
		public void updateMaximumLength(int value) {
			Integer max = getMaximumLength();
			if (max == null) {
				max = Integer.MIN_VALUE;
			}
			if (max < value) {
				setMaximumLength(value);
			}
		}
		
		public void updateMinimumLength(int value) {
			Integer min = getMinimumLength();
			if (min == null) {
				min = Integer.MAX_VALUE;
			}
			if (min > value) {
				setMinimumLength(value);
			}
		}
		
		/**
		 * 
		 * One-pass algorithm for computing the mean and variance
		 * variance_n is the population variance and
		 * variance is the sample variance.
		 * 
		 * def online_variance(data):
		 * 		n = 0
		 *      mean = 0
		 *      M2 = 0
		 *      for x in data:
		 *      	n = n + 1
		 *      	delta = x - mean
		 *      	mean = mean + delta/n
		 *      	M2 = M2 + delta*(x - mean)
		 *      variance_n = M2/n
		 *      variance = M2/(n - 1)
		 *      return (variance, variance_n)
		 *      Alternative approach:
		 *      M_{2,n}\! = M_{2,n-1} + (x_n - \bar x_{n-1})(x_n - \bar x_n)
 		 */
		public void updateAverageValueAndVariance(double value) {
			double delta = value - tempAverageValue;
			double n = getRowCount();
			if (getNullCount() != null) {
				n = n - getNullCount();
			}
			double prevAverageValue = tempAverageValue;
			tempAverageValue = tempAverageValue + delta/n;
			tempSquareDiff = tempSquareDiff + (value - prevAverageValue)*(value - tempAverageValue);

			setAverageValue(tempAverageValue);
		}
		
		public void updateAverageLength(int length) {
			double delta = ((double) length) - tempAverageLength;
			tempAverageLength = tempAverageLength + delta/(double) getRowCount();
			setAverageLength(tempAverageLength);
		}
		
		public AttributeMetadata getAttributeMetadata() {
			return metadata;
		}
	}	
}
