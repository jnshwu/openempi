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
package org.openhie.openempi.transformation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ValidationException;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.openhie.openempi.transformation.function.TransformationFunction;
import org.openhie.openempi.transformation.TransformationFunctionType;
import org.openhie.openempi.transformation.TransformationService;

public class TransformationServiceImpl extends BaseServiceImpl implements TransformationService
{
	private HashMap<String,TransformationFunction> transformationFunctionTypeMap;

	public TransformationServiceImpl() {
		super();
	}

	public void init(String transformationFunctionType, Map<String,String> configParameters) {
		TransformationFunction transformationFunction = getTransformationFunction(transformationFunctionType);
		transformationFunction.init(configParameters);
	}

	public Object transform(String transformationFunctionType, Object field) {
		TransformationFunction transformationFunction = getTransformationFunction(transformationFunctionType);
		return transformationFunction.transform(field);
	}
	
	public Object transform(String transformationFunctionType, Object field, Map<String,String> functionConfigurationParameters) {
		TransformationFunction transformationFunction = getTransformationFunction(transformationFunctionType);
		transformationFunction.init(functionConfigurationParameters);
		return transformationFunction.transform(field);
	}

	// Factory method pattern
	private TransformationFunction getTransformationFunction(String transformationFunctionType) {
		TransformationFunction transformationFunction = transformationFunctionTypeMap.get(transformationFunctionType);
		if (transformationFunction == null) {
			log.error("Unknown transformation function requested: " + transformationFunctionType);
			throw new ValidationException("Unknown transformation function requested for field transformation: " + transformationFunction);
		}
		return transformationFunction;
	}
	
	public TransformationFunctionType getTransformationFunctionType(String name) {
		TransformationFunction function = transformationFunctionTypeMap.get(name);
		if (function == null) {
			return null;
		}
		return new TransformationFunctionType(name, function);
	}

	public TransformationFunctionType[] getTransformationFunctionTypes() {
		TransformationFunctionType[] list = new TransformationFunctionType[transformationFunctionTypeMap.keySet().size()];
		int index = 0;
		for (String key : transformationFunctionTypeMap.keySet()) {
			list[index++] = new TransformationFunctionType(key, transformationFunctionTypeMap.get(key));
		}
		return list;
	}
	
	public HashMap<String, TransformationFunction> getTransformationFunctionTypeMap() {
		return transformationFunctionTypeMap;
	}

	public List<String> getTransformationFunctionNames() {
		List<String> transformationFunctionNames = new ArrayList<String>();
		for (String key : transformationFunctionTypeMap.keySet()) {
			transformationFunctionNames.add(key);
		}
		return transformationFunctionNames;
	}
	
	public void setTransformationFunctionTypeMap(HashMap<String, TransformationFunction> transformationFunctionTypeMap) {
		this.transformationFunctionTypeMap = transformationFunctionTypeMap;
	}
}
