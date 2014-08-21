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
package org.openhie.openempi.model;

public class RecordPair
{
	public final static int MATCH_OUTCOME_UNLINKED = 0;
	public final static int MATCH_OUTCOME_LINKED = 1;
	public final static int MATCH_OUTCOME_POSSIBLE = 2;
	
	private Record leftRecord;
	private Record rightRecord;
	private Double weight;
	private ComparisonVector comparisonVector;
	private LinkSource linkSource;
	private int matchOutcome;
	
	public RecordPair(Record leftRecord, Record rightRecord) {
		this.leftRecord = leftRecord;
		this.rightRecord = rightRecord;
	}

	public Record getLeftRecord() {
		return leftRecord;
	}

	public void setLeftRecord(Record leftRecord) {
		this.leftRecord = leftRecord;
	}

	public Record getRightRecord() {
		return rightRecord;
	}

	public void setRightRecord(Record rightRecord) {
		this.rightRecord = rightRecord;
	}

	public Record getRecord(int index) {
		if (index == 0) {
			return leftRecord;
		}
		return rightRecord;
	}
	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public ComparisonVector getComparisonVector() {
		return comparisonVector;
	}

	public void setComparisonVector(ComparisonVector comparisonVector) {
		this.comparisonVector = comparisonVector;
	}

	public int getMatchOutcome() {
		return matchOutcome;
	}

	public void setMatchOutcome(int matchingOutcome) {
		this.matchOutcome = matchingOutcome;
	}

	public LinkSource getLinkSource() {
		return linkSource;
	}

	public void setLinkSource(LinkSource linkSource) {
		this.linkSource = linkSource;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordPair other = (RecordPair) obj;
		if (leftRecord == null) {
			if (other.leftRecord != null)
				return false;
		} else if (!leftRecord.equals(other.leftRecord))
			return false;
		if (rightRecord == null) {
			if (other.rightRecord != null)
				return false;
		} else if (!rightRecord.equals(other.rightRecord))
			return false;
//		if (!leftRecord.getRecordId().equals(other.rightRecord.getRecordId()) ||
//				!rightRecord.getRecordId().equals(other.leftRecord.getRecordId()))
//			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftRecord == null) ? 0 : leftRecord.hashCode());
		result = prime * result + ((rightRecord == null) ? 0 : rightRecord.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "RecordPair [leftRecord=" + leftRecord + ", rightRecord=" + rightRecord + ", weight=" + weight
				+ ", comparisonVector=" + comparisonVector + ", linkSource=" + linkSource + ", matchOutcome="
				+ matchOutcome + "]";
	}
}
