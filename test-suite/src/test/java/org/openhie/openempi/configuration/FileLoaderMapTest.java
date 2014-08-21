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
package org.openhie.openempi.configuration;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.openhie.openempi.configuration.xml.model.FieldType;
import org.openhie.openempi.configuration.xml.model.FileLoaderMap;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;

public class FileLoaderMapTest extends BaseServiceTestCase
{
	public void testFileParsing() {
		try {
			JAXBContext jc = JAXBContext.newInstance( FileLoaderMap.class );
			Unmarshaller u = jc.createUnmarshaller();
	        File dir = new File(Context.getOpenEmpiHome() + "/conf");
	        File file = new File(dir, "file-loader-map.xml");
	        FileLoaderMap fileMap = (FileLoaderMap) u.unmarshal(new FileInputStream(file));
	        log.debug("File will be parsed using the delimeter: " + fileMap.getDelimeter());
	        for (int i=0; i < fileMap.getFields().getField().size(); i++) {
	        	FieldType field = fileMap.getFields().getField().get(i);
	        	if (field.isOneToMany()) {
	        		log.debug("Field will be decomposed into subfields using delimeter: " + field.getDelimeter());
	        		for (FieldType subfield : field.getSubfields().getField()) {
		        		log.debug("Will import field " + subfield.getFieldName() + " which should appear at column " + i);	        			
	        		}
	        	} else {
	        		log.debug("Will import field " + field.getFieldName() + " which should appear at column " + i);
	        	}
	        }
	        log.debug("Finished processing file loader map");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
