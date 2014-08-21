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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CoverageEligibilityReason.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CoverageEligibilityReason">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AGE"/>
 *     &lt;enumeration value="CRIME"/>
 *     &lt;enumeration value="DIS"/>
 *     &lt;enumeration value="EMPLOY"/>
 *     &lt;enumeration value="FINAN"/>
 *     &lt;enumeration value="HEALTH"/>
 *     &lt;enumeration value="VEHIC"/>
 *     &lt;enumeration value="MULTI"/>
 *     &lt;enumeration value="PNC"/>
 *     &lt;enumeration value="STATUTORY"/>
 *     &lt;enumeration value="WORK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CoverageEligibilityReason")
@XmlEnum
public enum CoverageEligibilityReason {

    AGE,
    CRIME,
    DIS,
    EMPLOY,
    FINAN,
    HEALTH,
    VEHIC,
    MULTI,
    PNC,
    STATUTORY,
    WORK;

    public String value() {
        return name();
    }

    public static CoverageEligibilityReason fromValue(String v) {
        return valueOf(v);
    }

}
