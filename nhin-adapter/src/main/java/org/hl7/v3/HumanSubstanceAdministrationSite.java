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
 * <p>Java class for HumanSubstanceAdministrationSite.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HumanSubstanceAdministrationSite">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="BE"/>
 *     &lt;enumeration value="OU"/>
 *     &lt;enumeration value="BU"/>
 *     &lt;enumeration value="LACF"/>
 *     &lt;enumeration value="LAC"/>
 *     &lt;enumeration value="LA"/>
 *     &lt;enumeration value="LD"/>
 *     &lt;enumeration value="LE"/>
 *     &lt;enumeration value="LEJ"/>
 *     &lt;enumeration value="OS"/>
 *     &lt;enumeration value="LF"/>
 *     &lt;enumeration value="LG"/>
 *     &lt;enumeration value="LH"/>
 *     &lt;enumeration value="LIJ"/>
 *     &lt;enumeration value="LLAQ"/>
 *     &lt;enumeration value="LLFA"/>
 *     &lt;enumeration value="LMFA"/>
 *     &lt;enumeration value="LN"/>
 *     &lt;enumeration value="LPC"/>
 *     &lt;enumeration value="LSC"/>
 *     &lt;enumeration value="LT"/>
 *     &lt;enumeration value="LUAQ"/>
 *     &lt;enumeration value="LUA"/>
 *     &lt;enumeration value="LUFA"/>
 *     &lt;enumeration value="LVL"/>
 *     &lt;enumeration value="LVG"/>
 *     &lt;enumeration value="PA"/>
 *     &lt;enumeration value="PERIN"/>
 *     &lt;enumeration value="RACF"/>
 *     &lt;enumeration value="RAC"/>
 *     &lt;enumeration value="RA"/>
 *     &lt;enumeration value="RD"/>
 *     &lt;enumeration value="RE"/>
 *     &lt;enumeration value="REJ"/>
 *     &lt;enumeration value="OD"/>
 *     &lt;enumeration value="RF"/>
 *     &lt;enumeration value="RG"/>
 *     &lt;enumeration value="RH"/>
 *     &lt;enumeration value="RIJ"/>
 *     &lt;enumeration value="RLAQ"/>
 *     &lt;enumeration value="RLFA"/>
 *     &lt;enumeration value="RMFA"/>
 *     &lt;enumeration value="RPC"/>
 *     &lt;enumeration value="RSC"/>
 *     &lt;enumeration value="RT"/>
 *     &lt;enumeration value="RUAQ"/>
 *     &lt;enumeration value="RUA"/>
 *     &lt;enumeration value="RUFA"/>
 *     &lt;enumeration value="RVL"/>
 *     &lt;enumeration value="RVG"/>
 *     &lt;enumeration value="BN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HumanSubstanceAdministrationSite")
@XmlEnum
public enum HumanSubstanceAdministrationSite {

    BE,
    OU,
    BU,
    LACF,
    LAC,
    LA,
    LD,
    LE,
    LEJ,
    OS,
    LF,
    LG,
    LH,
    LIJ,
    LLAQ,
    LLFA,
    LMFA,
    LN,
    LPC,
    LSC,
    LT,
    LUAQ,
    LUA,
    LUFA,
    LVL,
    LVG,
    PA,
    PERIN,
    RACF,
    RAC,
    RA,
    RD,
    RE,
    REJ,
    OD,
    RF,
    RG,
    RH,
    RIJ,
    RLAQ,
    RLFA,
    RMFA,
    RPC,
    RSC,
    RT,
    RUAQ,
    RUA,
    RUFA,
    RVL,
    RVG,
    BN;

    public String value() {
        return name();
    }

    public static HumanSubstanceAdministrationSite fromValue(String v) {
        return valueOf(v);
    }

}
