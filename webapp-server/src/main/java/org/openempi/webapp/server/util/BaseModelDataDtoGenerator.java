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
package org.openempi.webapp.server.util;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class BaseModelDataDtoGenerator
{
	private static Logger log = Logger.getLogger(BaseModelDataDtoGenerator.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: BaseModelDataDtoGenerator <FQ-Class-Name> <Destination-Package-Name>");
			System.exit(-1);
		}
		
		String fqClassName = args[0];
		String className = extractClassName(fqClassName);
		String packageName = args[1];
		log.debug("Generating DTO for class: " + fqClassName + " into class " + className);
		StringBuilder sourceCode = generateClassHeader(packageName, className);
		Class<?> beanClass = Class.forName(fqClassName);
		PropertyDescriptor[] descs = PropertyUtils.getPropertyDescriptors(beanClass);
		for (PropertyDescriptor desc : descs) {
			log.debug(desc.getName() + " of type " + desc.getPropertyType());
			
			if (!desc.getName().equalsIgnoreCase("class") && !desc.getPropertyType().getCanonicalName().startsWith("org")) {
				generateGetterSetter(sourceCode, desc);
			}
		}
		sourceCode.append("}");
		System.out.println(sourceCode.toString());
	}

	private static void generateGetterSetter(StringBuilder sourceCode, PropertyDescriptor desc) {
		// getter method declaration
		sourceCode.append("\tpublic ").append(desc.getPropertyType().getCanonicalName()).append(" ").append(desc.getReadMethod().getName()).append("() {\n");
		
		// getter method body
		sourceCode.append("\t\treturn get(\"").append(desc.getName()).append("\");\n\t}\n\n");
		
		// setter method declaration
		sourceCode.append("\tpublic void ").append(desc.getWriteMethod().getName()).append("(")
			.append(desc.getPropertyType().getCanonicalName()).append(" ").append(desc.getName()).append(") {\n");

		// getter method body
		sourceCode.append("\t\tset(\"").append(desc.getName()).append("\", ").append(desc.getName()).append(");\n\t}\n\n");
	}

	private static StringBuilder generateClassHeader(String packageName, String className) {
		StringBuilder code = new StringBuilder("package ");
		// package
		code.append(packageName).append(";\n\n");
		
		// import section
		code.append("import com.extjs.gxt.ui.client.data.BaseModelData;\n\n");
		
		// class definition section
		code.append("public class ").append(className).append(" extends BaseModelData\n{\n");
		
		// class constructor
		code.append("\tpublic ").append(className).append("() {\n\t}\n\n");

		return code;
	}

	private static String extractClassName(String fqClassName) {
		int index = fqClassName.lastIndexOf('.');
		return fqClassName.substring(index + 1);
	}

}
