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
package org.openhie.openempi.transformation;

import java.io.Serializable;

import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.transformation.function.TransformationFunction;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class TransformationFunctionType extends BaseObject implements Serializable
{
	private static final long serialVersionUID = -4578201112531798226L;

	private String name;
	private TransformationFunction transformationFunction;
	
	public TransformationFunctionType(String name, TransformationFunction transformationFunction) {
		this.name = name;
		this.transformationFunction = transformationFunction;
	}

	public TransformationFunction getTransformationFunction() {
		return transformationFunction;
	}

	public void setTransformationFunction(TransformationFunction transformationFunction) {
		this.transformationFunction = transformationFunction;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof TransformationFunctionType))
			return false;
		TransformationFunctionType castOther = (TransformationFunctionType) other;
		return new EqualsBuilder().append(name, castOther.name).append(
				transformationFunction, castOther.transformationFunction).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(transformationFunction)
				.toHashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
