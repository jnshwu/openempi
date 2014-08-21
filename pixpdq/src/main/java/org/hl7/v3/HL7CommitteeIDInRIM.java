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
 * <p>Java class for HL7CommitteeIDInRIM.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HL7CommitteeIDInRIM">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="C02"/>
 *     &lt;enumeration value="C06"/>
 *     &lt;enumeration value="C09"/>
 *     &lt;enumeration value="C00"/>
 *     &lt;enumeration value="C04"/>
 *     &lt;enumeration value="C03"/>
 *     &lt;enumeration value="C12"/>
 *     &lt;enumeration value="C10"/>
 *     &lt;enumeration value="C20"/>
 *     &lt;enumeration value="C01"/>
 *     &lt;enumeration value="C21"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HL7CommitteeIDInRIM")
@XmlEnum
public enum HL7CommitteeIDInRIM {

    @XmlEnumValue("C02")
    C_02("C02"),
    @XmlEnumValue("C06")
    C_06("C06"),
    @XmlEnumValue("C09")
    C_09("C09"),
    @XmlEnumValue("C00")
    C_00("C00"),
    @XmlEnumValue("C04")
    C_04("C04"),
    @XmlEnumValue("C03")
    C_03("C03"),
    @XmlEnumValue("C12")
    C_12("C12"),
    @XmlEnumValue("C10")
    C_10("C10"),
    @XmlEnumValue("C20")
    C_20("C20"),
    @XmlEnumValue("C01")
    C_01("C01"),
    @XmlEnumValue("C21")
    C_21("C21");
    private final String value;

    HL7CommitteeIDInRIM(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HL7CommitteeIDInRIM fromValue(String v) {
        for (HL7CommitteeIDInRIM c: HL7CommitteeIDInRIM.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
