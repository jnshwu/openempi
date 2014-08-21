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
 * <p>Java class for GTSAbbreviationHolidaysUSNational.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GTSAbbreviationHolidaysUSNational">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="JHNUS"/>
 *     &lt;enumeration value="JHNUSCLM"/>
 *     &lt;enumeration value="JHNUSIND"/>
 *     &lt;enumeration value="JHNUSIND1"/>
 *     &lt;enumeration value="JHNUSIND5"/>
 *     &lt;enumeration value="JHNUSLBR"/>
 *     &lt;enumeration value="JHNUSMEM"/>
 *     &lt;enumeration value="JHNUSMEM5"/>
 *     &lt;enumeration value="JHNUSMEM6"/>
 *     &lt;enumeration value="JHNUSMLK"/>
 *     &lt;enumeration value="JHNUSPRE"/>
 *     &lt;enumeration value="JHNUSTKS"/>
 *     &lt;enumeration value="JHNUSTKS5"/>
 *     &lt;enumeration value="JHNUSVET"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GTSAbbreviationHolidaysUSNational")
@XmlEnum
public enum GTSAbbreviationHolidaysUSNational {

    JHNUS("JHNUS"),
    JHNUSCLM("JHNUSCLM"),
    JHNUSIND("JHNUSIND"),
    @XmlEnumValue("JHNUSIND1")
    JHNUSIND_1("JHNUSIND1"),
    @XmlEnumValue("JHNUSIND5")
    JHNUSIND_5("JHNUSIND5"),
    JHNUSLBR("JHNUSLBR"),
    JHNUSMEM("JHNUSMEM"),
    @XmlEnumValue("JHNUSMEM5")
    JHNUSMEM_5("JHNUSMEM5"),
    @XmlEnumValue("JHNUSMEM6")
    JHNUSMEM_6("JHNUSMEM6"),
    JHNUSMLK("JHNUSMLK"),
    JHNUSPRE("JHNUSPRE"),
    JHNUSTKS("JHNUSTKS"),
    @XmlEnumValue("JHNUSTKS5")
    JHNUSTKS_5("JHNUSTKS5"),
    JHNUSVET("JHNUSVET");
    private final String value;

    GTSAbbreviationHolidaysUSNational(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GTSAbbreviationHolidaysUSNational fromValue(String v) {
        for (GTSAbbreviationHolidaysUSNational c: GTSAbbreviationHolidaysUSNational.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
