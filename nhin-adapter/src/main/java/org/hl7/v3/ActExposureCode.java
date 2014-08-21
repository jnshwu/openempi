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
 * <p>Java class for ActExposureCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActExposureCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="HOMECARE"/>
 *     &lt;enumeration value="CONVEYNC"/>
 *     &lt;enumeration value="PLACE"/>
 *     &lt;enumeration value="SUBSTNCE"/>
 *     &lt;enumeration value="TRAVINT"/>
 *     &lt;enumeration value="CHLDCARE"/>
 *     &lt;enumeration value="HLTHCARE"/>
 *     &lt;enumeration value="PTNTCARE"/>
 *     &lt;enumeration value="HOSPPTNT"/>
 *     &lt;enumeration value="HOSPVSTR"/>
 *     &lt;enumeration value="HOUSEHLD"/>
 *     &lt;enumeration value="INMATE"/>
 *     &lt;enumeration value="INTIMATE"/>
 *     &lt;enumeration value="LTRMCARE"/>
 *     &lt;enumeration value="SCHOOL2"/>
 *     &lt;enumeration value="SOCIAL2"/>
 *     &lt;enumeration value="WORK2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActExposureCode")
@XmlEnum
public enum ActExposureCode {

    HOMECARE("HOMECARE"),
    CONVEYNC("CONVEYNC"),
    PLACE("PLACE"),
    SUBSTNCE("SUBSTNCE"),
    TRAVINT("TRAVINT"),
    CHLDCARE("CHLDCARE"),
    HLTHCARE("HLTHCARE"),
    PTNTCARE("PTNTCARE"),
    HOSPPTNT("HOSPPTNT"),
    HOSPVSTR("HOSPVSTR"),
    HOUSEHLD("HOUSEHLD"),
    INMATE("INMATE"),
    INTIMATE("INTIMATE"),
    LTRMCARE("LTRMCARE"),
    @XmlEnumValue("SCHOOL2")
    SCHOOL_2("SCHOOL2"),
    @XmlEnumValue("SOCIAL2")
    SOCIAL_2("SOCIAL2"),
    @XmlEnumValue("WORK2")
    WORK_2("WORK2");
    private final String value;

    ActExposureCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActExposureCode fromValue(String v) {
        for (ActExposureCode c: ActExposureCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
