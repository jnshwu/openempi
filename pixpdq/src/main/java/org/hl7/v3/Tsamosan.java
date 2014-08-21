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

package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Tsamosan.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Tsamosan">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="x-COW"/>
 *     &lt;enumeration value="x-CEA"/>
 *     &lt;enumeration value="x-QUN"/>
 *     &lt;enumeration value="x-CJH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Tsamosan")
@XmlEnum
public enum Tsamosan {

    @XmlEnumValue("x-COW")
    X_COW("x-COW"),
    @XmlEnumValue("x-CEA")
    X_CEA("x-CEA"),
    @XmlEnumValue("x-QUN")
    X_QUN("x-QUN"),
    @XmlEnumValue("x-CJH")
    X_CJH("x-CJH");
    private final String value;

    Tsamosan(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Tsamosan fromValue(String v) {
        for (Tsamosan c: Tsamosan.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
