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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LinkSource entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "link_source")
public class LinkSource extends BaseObject implements Serializable
{
	private static final long serialVersionUID = -6737992702585965810L;
	public static final Integer MANUAL_MATCHING_SOURCE = 1;
	public static final Integer EXACT_MATCHING_ALGORITHM_SOURCE = 2;
	public static final Integer PROBABILISTIC_MATCHING_ALGORITHM_SOURCE = 3;
	
	private Integer linkSourceId;
	private String sourceName;
	private String sourceDescription;

	public LinkSource() {
	}

	public LinkSource(Integer linkSourceId) {
		this.linkSourceId = linkSourceId;
	}

	@Id
	@Column(name = "link_source_id", unique = true, nullable = false)
	public Integer getLinkSourceId() {
		return linkSourceId;
	}

	public void setLinkSourceId(Integer linkSourceId) {
		this.linkSourceId = linkSourceId;
	}

	@Column(name = "source_name", nullable = false)
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Column(name = "source_description")
	public String getSourceDescription() {
		return sourceDescription;
	}

	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	@Override
	public String toString() {
		return "LinkSource [linkSourceId=" + linkSourceId + ", sourceName=" + sourceName + ", sourceDescription="
				+ sourceDescription + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkSource other = (LinkSource) obj;
		if (linkSourceId == null) {
			if (other.linkSourceId != null)
				return false;
		} else if (!linkSourceId.equals(other.linkSourceId))
			return false;
		if (sourceDescription == null) {
			if (other.sourceDescription != null)
				return false;
		} else if (!sourceDescription.equals(other.sourceDescription))
			return false;
		if (sourceName == null) {
			if (other.sourceName != null)
				return false;
		} else if (!sourceName.equals(other.sourceName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkSourceId == null) ? 0 : linkSourceId.hashCode());
		result = prime * result + ((sourceDescription == null) ? 0 : sourceDescription.hashCode());
		result = prime * result + ((sourceName == null) ? 0 : sourceName.hashCode());
		return result;
	}
}
