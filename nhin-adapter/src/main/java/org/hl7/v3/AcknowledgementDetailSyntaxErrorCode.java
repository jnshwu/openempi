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
 * <p>Java class for AcknowledgementDetailSyntaxErrorCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AcknowledgementDetailSyntaxErrorCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SYN"/>
 *     &lt;enumeration value="SYN102"/>
 *     &lt;enumeration value="SYN104"/>
 *     &lt;enumeration value="SYN110"/>
 *     &lt;enumeration value="SYN112"/>
 *     &lt;enumeration value="SYN100"/>
 *     &lt;enumeration value="SYN101"/>
 *     &lt;enumeration value="SYN103"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AcknowledgementDetailSyntaxErrorCode")
@XmlEnum
public enum AcknowledgementDetailSyntaxErrorCode {

    SYN("SYN"),
    @XmlEnumValue("SYN102")
    SYN_102("SYN102"),
    @XmlEnumValue("SYN104")
    SYN_104("SYN104"),
    @XmlEnumValue("SYN110")
    SYN_110("SYN110"),
    @XmlEnumValue("SYN112")
    SYN_112("SYN112"),
    @XmlEnumValue("SYN100")
    SYN_100("SYN100"),
    @XmlEnumValue("SYN101")
    SYN_101("SYN101"),
    @XmlEnumValue("SYN103")
    SYN_103("SYN103");
    private final String value;

    AcknowledgementDetailSyntaxErrorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AcknowledgementDetailSyntaxErrorCode fromValue(String v) {
        for (AcknowledgementDetailSyntaxErrorCode c: AcknowledgementDetailSyntaxErrorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
