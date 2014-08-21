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
 * <p>Java class for CaseTransmissionMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CaseTransmissionMode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AIRTRNS"/>
 *     &lt;enumeration value="ANANTRNS"/>
 *     &lt;enumeration value="ANHUMTRNS"/>
 *     &lt;enumeration value="BLDTRNS"/>
 *     &lt;enumeration value="BDYFLDTRNS"/>
 *     &lt;enumeration value="ENVTRNS"/>
 *     &lt;enumeration value="FECTRNS"/>
 *     &lt;enumeration value="FOMTRNS"/>
 *     &lt;enumeration value="FOODTRNS"/>
 *     &lt;enumeration value="HUMHUMTRNS"/>
 *     &lt;enumeration value="INDTRNS"/>
 *     &lt;enumeration value="LACTTRNS"/>
 *     &lt;enumeration value="NOSTRNS"/>
 *     &lt;enumeration value="PARTRNS"/>
 *     &lt;enumeration value="SEXTRNS"/>
 *     &lt;enumeration value="DERMTRNS"/>
 *     &lt;enumeration value="TRNSFTRNS"/>
 *     &lt;enumeration value="PLACTRNS"/>
 *     &lt;enumeration value="VECTRNS"/>
 *     &lt;enumeration value="WATTRNS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CaseTransmissionMode")
@XmlEnum
public enum CaseTransmissionMode {

    AIRTRNS,
    ANANTRNS,
    ANHUMTRNS,
    BLDTRNS,
    BDYFLDTRNS,
    ENVTRNS,
    FECTRNS,
    FOMTRNS,
    FOODTRNS,
    HUMHUMTRNS,
    INDTRNS,
    LACTTRNS,
    NOSTRNS,
    PARTRNS,
    SEXTRNS,
    DERMTRNS,
    TRNSFTRNS,
    PLACTRNS,
    VECTRNS,
    WATTRNS;

    public String value() {
        return name();
    }

    public static CaseTransmissionMode fromValue(String v) {
        return valueOf(v);
    }

}
