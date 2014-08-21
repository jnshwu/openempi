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
package org.openempi.webapp.server.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.openempi.webapp.client.domain.Options;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

/**
 * 
 * Marshal and unmarshal XML 
 * 
 */
public abstract class Marshaler {
    private static XStream xStream = initializeXStream();

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    public static String toXML(Object xmlTree) {
        return XML_HEADER + toXMLNoHeader(xmlTree);
    }
    
    public static String toXMLNoHeader(Object xmlTree) {
        return xStream.toXML(xmlTree);
    }

    public static void toXML(Object xmlTree, FileOutputStream fos) {
        xStream.toXML(xmlTree, fos);
    }

    public static Options optionsFromFile(FileInputStream fis) {
    	Options options = new Options();
        xStream.fromXML(fis, options);
        return options;
    }

    private static XStream initializeXStream() {
        // This constructor prevents underscores from being replaced with double
        // underscores
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyReplacer("|", "_")));

        //if you use annotated xstream POJOs register them here
//      xStream.processAnnotations(CssParameter.class);
        return xStream;
    }
}
