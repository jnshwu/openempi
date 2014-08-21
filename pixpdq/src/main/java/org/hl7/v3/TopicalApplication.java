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
 * <p>Java class for TopicalApplication.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TopicalApplication">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="OCDRESTA"/>
 *     &lt;enumeration value="SUBCONJTA"/>
 *     &lt;enumeration value="TOPICAL"/>
 *     &lt;enumeration value="BUC"/>
 *     &lt;enumeration value="CERV"/>
 *     &lt;enumeration value="DEN"/>
 *     &lt;enumeration value="GIN"/>
 *     &lt;enumeration value="HAIR"/>
 *     &lt;enumeration value="ICORNTA"/>
 *     &lt;enumeration value="ICORONTA"/>
 *     &lt;enumeration value="IESOPHTA"/>
 *     &lt;enumeration value="IILEALTA"/>
 *     &lt;enumeration value="ILTOP"/>
 *     &lt;enumeration value="ILUMTA"/>
 *     &lt;enumeration value="IOTOP"/>
 *     &lt;enumeration value="IONTO"/>
 *     &lt;enumeration value="LARYNGTA"/>
 *     &lt;enumeration value="MUC"/>
 *     &lt;enumeration value="NAIL"/>
 *     &lt;enumeration value="NASAL"/>
 *     &lt;enumeration value="OPTHALTA"/>
 *     &lt;enumeration value="ORALTA"/>
 *     &lt;enumeration value="ORMUC"/>
 *     &lt;enumeration value="OROPHARTA"/>
 *     &lt;enumeration value="PERIANAL"/>
 *     &lt;enumeration value="PERINEAL"/>
 *     &lt;enumeration value="PDONTTA"/>
 *     &lt;enumeration value="RECTAL"/>
 *     &lt;enumeration value="SCALP"/>
 *     &lt;enumeration value="SKIN"/>
 *     &lt;enumeration value="DRESS"/>
 *     &lt;enumeration value="SWAB"/>
 *     &lt;enumeration value="TMUCTA"/>
 *     &lt;enumeration value="VAGINS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TopicalApplication")
@XmlEnum
public enum TopicalApplication {

    OCDRESTA,
    SUBCONJTA,
    TOPICAL,
    BUC,
    CERV,
    DEN,
    GIN,
    HAIR,
    ICORNTA,
    ICORONTA,
    IESOPHTA,
    IILEALTA,
    ILTOP,
    ILUMTA,
    IOTOP,
    IONTO,
    LARYNGTA,
    MUC,
    NAIL,
    NASAL,
    OPTHALTA,
    ORALTA,
    ORMUC,
    OROPHARTA,
    PERIANAL,
    PERINEAL,
    PDONTTA,
    RECTAL,
    SCALP,
    SKIN,
    DRESS,
    SWAB,
    TMUCTA,
    VAGINS;

    public String value() {
        return name();
    }

    public static TopicalApplication fromValue(String v) {
        return valueOf(v);
    }

}
