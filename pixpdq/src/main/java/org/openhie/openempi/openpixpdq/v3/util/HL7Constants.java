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
package org.openhie.openempi.openpixpdq.v3.util;

import org.hl7.v3.CD;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7Constants
{
    public static final String AGENT_CLASS_CODE="AGNT";
    public static final String ASSIGNED_DEVICE_CLASS_CODE = "ASSIGNED";
	public static final String CLASS_CODE_ASSIGNED = "ASSIGNED";
	public static final String CLASS_CODE_ORG = "ORG";
	public static final String CLASS_CODE_PATIENT = "PAT";
	public static final String CLASS_CODE_PSN = "PSN";
	public static final String CLASS_CODE_REGISTRATION = "REG";
    public static final String DEFAULT_LOCAL_DEVICE_ID = "1.1";
    public static final String DETECTED_ISSUE_CLASSCODE_ALRT = "ALRT";
    public static final String DETECTED_ISSUE_MOODCODE_EVN = "EVN";
    public static final String DETECTED_ISSUE_CODE_ADMINISTRATIVE = "ActAdministrativeDetectedIssueCode";
    public static final String DETECTED_ISSUE_CODESYSTEM_ERROR_CODE = "2.16.840.1.113883.5.4";
    public static final String DETECTED_ISSUE_MITIGATEDBY_TYPECODE_MITGT = "MITGT";
    public static final String DETECTEDISSUEMANAGEMENT_CLASSCODE = "ACT";
    public static final String DETECTEDISSUEMANAGEMENT_MOODCODE_RQO = "RQO";
    public static final String DETECTEDISSUEMANAGEMENT_CODE_RESPONDER_BUSY = "ResponderBusy";
    public static final String DETECTEDISSUEMANAGEMENT_CODESYSTEM = "1.3.6.1.4.1.19376.1.2.27.3";
    public static final String DETERMINER_CODE_INSTANCE = "INSTANCE";
	public static final String GENDER_CODE_MALE = "M";
	public static final String GENDER_CODE_FEMALE = "F";
	public static final String GENDER_CODE_UNKNOWN = "UN";
    public static final String INTERACTION_ID_ROOT = "2.16.840.1.113883.1.6";
    public static final String INTERACTION_EXTENSION_MCCIIN000002UV01 = "MCCIIN000002UV01";
    public static final String ITS_VERSION = "XML_1.0";
    public static final String MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_COMMIT_ACCEPT = "CA";
    public static final String MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_COMMIT_ERROR = "CE";
    public static final String MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_COMMIT_REJECT = "CR";
    public static final String MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_APPLICATION_ACCEPT = "AA";
    public static final String MESSAGE_ACKNOWLEDGEMENT_TYPE_CODE_APPLICATION_ERROR = "AE";
	public static final String MOOD_COODE_EVN = "EVN";
    public static final String NULL_FLAVOR = "NA";
    public static final String ORG_CLASS_CODE="ORG";
	public static final String QUERY_ACK_AE = "AE";
	public static final String QUERY_ACK_NF = "NF";
    public static final String QUERY_ACK_OK = "OK";
    public static final String QUERY_ACK_QE = "QE";
    public static final String RECEIVER_DETERMINER_CODE = "INSTANCE";
    public static final String SENDER_DETERMINER_CODE = "INSTANCE";
    public static final String SSN_ID_ROOT = "2.16.840.1.113883.4.1";
	public static final String STATUS_CODE_ACTIVE = "active";
	public static final String TYPE_CODE_CST = "CST";
	public static final String TYPE_CODE_SUBJ = "SUBJ";
	public static final String UNIVERSAL_IDENTIFIER_TYPE_CODE_ISO = "ISO";
	public static final String UPDATE_NOTIFICATION_TRIGGER_EVENT_CODE = "PRPA_TE201302UV02";
	public static final String USE_CODE_SRCH = "SRCH";
}
