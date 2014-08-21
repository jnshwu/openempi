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
 * <p>Java class for MedicationObservationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MedicationObservationType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="SPLCOATING"/>
 *     &lt;enumeration value="SPLCOLOR"/>
 *     &lt;enumeration value="SPLIMAGE"/>
 *     &lt;enumeration value="SPLIMPRINT"/>
 *     &lt;enumeration value="REP_HALF_LIFE"/>
 *     &lt;enumeration value="SPLSCORING"/>
 *     &lt;enumeration value="SPLSHAPE"/>
 *     &lt;enumeration value="SPLSIZE"/>
 *     &lt;enumeration value="SPLSYMBOL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MedicationObservationType")
@XmlEnum
public enum MedicationObservationType {

    SPLCOATING,
    SPLCOLOR,
    SPLIMAGE,
    SPLIMPRINT,
    REP_HALF_LIFE,
    SPLSCORING,
    SPLSHAPE,
    SPLSIZE,
    SPLSYMBOL;

    public String value() {
        return name();
    }

    public static MedicationObservationType fromValue(String v) {
        return valueOf(v);
    }

}
