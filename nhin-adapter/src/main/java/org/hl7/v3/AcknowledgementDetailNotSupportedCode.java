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
 * <p>Java class for AcknowledgementDetailNotSupportedCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AcknowledgementDetailNotSupportedCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NS260"/>
 *     &lt;enumeration value="NS261"/>
 *     &lt;enumeration value="NS200"/>
 *     &lt;enumeration value="NS250"/>
 *     &lt;enumeration value="NS202"/>
 *     &lt;enumeration value="NS203"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AcknowledgementDetailNotSupportedCode")
@XmlEnum
public enum AcknowledgementDetailNotSupportedCode {

    @XmlEnumValue("NS260")
    NS_260("NS260"),
    @XmlEnumValue("NS261")
    NS_261("NS261"),
    @XmlEnumValue("NS200")
    NS_200("NS200"),
    @XmlEnumValue("NS250")
    NS_250("NS250"),
    @XmlEnumValue("NS202")
    NS_202("NS202"),
    @XmlEnumValue("NS203")
    NS_203("NS203");
    private final String value;

    AcknowledgementDetailNotSupportedCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AcknowledgementDetailNotSupportedCode fromValue(String v) {
        for (AcknowledgementDetailNotSupportedCode c: AcknowledgementDetailNotSupportedCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
