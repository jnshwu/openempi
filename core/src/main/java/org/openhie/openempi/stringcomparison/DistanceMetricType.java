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
package org.openhie.openempi.stringcomparison;

import java.io.Serializable;

import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.stringcomparison.metrics.DistanceMetric;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class DistanceMetricType extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1850164680144032068L;

	private String name;
	private DistanceMetric distanceMetric;
	
	public DistanceMetricType(String name, DistanceMetric distanceMetric) {
		this.name = name;
		this.distanceMetric = distanceMetric;
	}

	public DistanceMetric getDistanceMetric() {
		return distanceMetric;
	}

	public void setDistanceMetric(DistanceMetric distanceMetric) {
		this.distanceMetric = distanceMetric;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof DistanceMetricType))
			return false;
		DistanceMetricType castOther = (DistanceMetricType) other;
		return new EqualsBuilder().append(name, castOther.name).append(
				distanceMetric, castOther.distanceMetric).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(distanceMetric)
				.toHashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
