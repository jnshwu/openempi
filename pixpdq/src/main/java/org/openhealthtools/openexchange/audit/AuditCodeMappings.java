/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openexchange.audit;

/** Mappings for the data contained in the audit records.
 * 
 * The actual layout of the data is described elsewhere.  The
 * valid values are described in RFC 3881, Dicomm Supplimental 95,
 * and the IHE IT Technical Framework Vol 2 sec. 3.20. <p />
 * 
 * This class provides a number of enumerataions which should be used
 * by the audit trail classes to define what type of messages they are.
 * These types are defined in the above documents.  The object factory 
 * is then used to turn these enumerations into properly formatted
 * audit messages.  <p />
 * 
 * For each enumeration there is a "getValue" function
 * which provides the information for that code mapping in the official
 * format.  These should only be used by the audit object factory. 
 *
 * @see AuditObjectFactory 
 * @author Josh Flachsbart
 * @version 2.0 - Nov 13, 2005
 */
public class AuditCodeMappings {
	public String codingScheme;
	public String codingSchemeVersion;
	public String codeValue;
	public String codeMeaning;
	
	/** Creates a code mapping with the given data.  Called by factory functions only. */
	private AuditCodeMappings(String scheme, String value, String meaning) {
		codingScheme = scheme;
		codeValue = value;
		codeMeaning = meaning;
		codingSchemeVersion = "2004xxxx";
	}
	
	/** The type of event that is being sent.
	 * 
	 *  These are defined in DICOM Sup95 p. 38 and IHT ITI Vol 2. p. 184. 
	 *  aka ccc1, version 2004xxxx, extensible (with IHE extensions). */
	public static enum AuditEventIds {
		ApplicationActivity,
		AuditLogUsed,
		BeginDICOMTransfer,
		DicomInstancesAccessed,
		DicomInstancesTransferred,
		DicomStudyDeleted,
		Export,
		Import,
		NetworkEntry,
		OrderRecord,
		PatientRecord,
		ProcedureRecord,
		Query,
		SecurityAlert,
		UserAuthentication,
		HealthServicesProvisionEvent,
		MedicationEvent,
		PatientCareResourceAssignment,
		PatientCareEpisode,
		PatientCareProtocol		
	};
	
	/** Used by the object factory to properly format this type of event.
	 * 
	 * @param id The event id enumeration to format.
	 * @return The properly formatted data strings.
	 */
	public static AuditCodeMappings getCodeMapping(AuditEventIds id) {
		AuditCodeMappings codeMapping = null;
		switch (id) {
		case ApplicationActivity:
			codeMapping = new AuditCodeMappings("DCM", "110100", "Application Activity"); break;
		case AuditLogUsed:
			codeMapping = new AuditCodeMappings("DCM", "110101", "Audit Log Used"); break;
		case BeginDICOMTransfer:
			codeMapping = new AuditCodeMappings("DCM", "110102", "Begin DICOM Transfer"); break;
		case DicomInstancesAccessed:
			codeMapping = new AuditCodeMappings("DCM", "110103", "DICOM Instances Accessed"); break;
		case	 DicomInstancesTransferred:
			codeMapping = new AuditCodeMappings("DCM", "110104", "DICOM Instances Transferred"); break;
		case DicomStudyDeleted:
			codeMapping = new AuditCodeMappings("DCM", "110105", "DICOM Study Deleted"); break;
		case Export:
			codeMapping = new AuditCodeMappings("DCM", "110106", "Export"); break;
		case Import:
			codeMapping = new AuditCodeMappings("DCM", "110107", "Import"); break;
		case NetworkEntry:
			codeMapping = new AuditCodeMappings("DCM", "110108", "Network Entry"); break;
		case OrderRecord:
			codeMapping = new AuditCodeMappings("DCM", "110109", "Order Record"); break;
		case PatientRecord:
			codeMapping = new AuditCodeMappings("DCM", "110110", "Patient Record"); break;
		case ProcedureRecord:
			codeMapping = new AuditCodeMappings("DCM", "110111", "Procedure Record"); break;
		case Query:
			codeMapping = new AuditCodeMappings("DCM", "110112", "Query"); break;
		case SecurityAlert:
			codeMapping = new AuditCodeMappings("DCM", "110113", "Security Alert"); break;
		case UserAuthentication:
			codeMapping = new AuditCodeMappings("DCM", "110114", "User Authentication"); break;
		case HealthServicesProvisionEvent:
			codeMapping = new AuditCodeMappings("IHE", "IHE0001", "Health Services Provision Event"); break;
		case MedicationEvent:
			codeMapping = new AuditCodeMappings("IHE", "IHE0002", "Medication Event"); break;
		case PatientCareResourceAssignment:
			codeMapping = new AuditCodeMappings("IHE", "IHE0003", "Patient Care Resource Assignment"); break;
		case PatientCareEpisode:
			codeMapping = new AuditCodeMappings("IHE", "IHE0004", "Patient Care Episode"); break;
		case PatientCareProtocol:
			codeMapping = new AuditCodeMappings("IHE", "IHE0005", "Patient Care Protocol"); break;
		}
		return codeMapping;
	}
	
	/** A description of what made the event be sent.
	 * 
	 * From DICOM Supp95 p. 38.
	 * aka ccc2, version 2004xxxx, extensible. */
	public static enum AuditTypeCodes {
		ApplicationStart,
		ApplicationStop,
		Login,
		Logout,
		Attach,
		Detach,
		NodeAuthentication,
		EmergencyOverride,
		NetworkConfiguration,
		SecurityConfiguration,
		HardwareConfiguration,
		SoftwareConfiguration,
		UseOfRestrictedFunction,
		AuditRecordingStopped,
		AuditRecordingStarted,
		ObjectSecurityAttributesChanged,
		SecurityRolesChanged,
		UserSecurityAtttributesChanged,
		RegistrySQLQuery,
		RegistryStoredQuery,
		RegisterDocumentSet,
		ProvideAndRegisterDocumentSet,
		RetrieveDocument,
		RetrieveDocumentSet,
		PatientIdentityFeed,
		PixQuery,
		PixUpdateNotification,
		PDQQuery,
		RegisterDocumentSet_b,
		ProvideAndRegisterDocumentSet_b,
		MultiPatientQuery,
		DocumentMetadataSubscribe,
		DocumentMetadataNotify,
		DocumentMetadataPublish,
	};

	/** Used by the object factory to properly format the event cause.
	 * 
	 * @param id The audit type enumeration to encode.
	 * @return The properly formatted data strings.
	 */
	public static AuditCodeMappings getCodeMapping(AuditTypeCodes id) {
		AuditCodeMappings codeMapping = null;
		switch (id) {
		case ApplicationStart:
			codeMapping = new AuditCodeMappings("DCM", "110120", "Application Start"); break;
		case ApplicationStop:
			codeMapping = new AuditCodeMappings("DCM", "110121", "Application Stop"); break;
		case Login:
			codeMapping = new AuditCodeMappings("DCM", "110122", "Login"); break;
		case Logout:
			codeMapping = new AuditCodeMappings("DCM", "110123", "Logout"); break;
		case	 Attach:
			codeMapping = new AuditCodeMappings("DCM", "110124", "Attach"); break;
		case Detach:
			codeMapping = new AuditCodeMappings("DCM", "110125", "Detach"); break;
		case NodeAuthentication:
			codeMapping = new AuditCodeMappings("DCM", "110126", "Node Authentication"); break;
		case EmergencyOverride:
			codeMapping = new AuditCodeMappings("DCM", "110127", "Emergency Override"); break;
		case NetworkConfiguration:
			codeMapping = new AuditCodeMappings("DCM", "110128", "Network Configuration"); break;
		case SecurityConfiguration:
			codeMapping = new AuditCodeMappings("DCM", "110129", "Security Configuration"); break;
		case HardwareConfiguration:
			codeMapping = new AuditCodeMappings("DCM", "110130", "Hardware Configuration"); break;
		case SoftwareConfiguration:
			codeMapping = new AuditCodeMappings("DCM", "110131", "Software Configuration"); break;
		case UseOfRestrictedFunction:
			codeMapping = new AuditCodeMappings("DCM", "110133", "Use of Restricted Function"); break;
		case AuditRecordingStopped:
			codeMapping = new AuditCodeMappings("DCM", "110134", "Audit Recording Stopped"); break;
		case AuditRecordingStarted:
			codeMapping = new AuditCodeMappings("DCM", "110135", "Audit Recording Started"); break;
		case ObjectSecurityAttributesChanged:
			codeMapping = new AuditCodeMappings("DCM", "110136", "Object Security Attributes Changed"); break;
		case SecurityRolesChanged:
			codeMapping = new AuditCodeMappings("DCM", "110137", "Security Roles Changed"); break;
		case RegisterDocumentSet:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-14", "Register Document Set"); break;
		case ProvideAndRegisterDocumentSet:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-15", "Provide and Register Document Set"); break;
		case RegistrySQLQuery:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-16", "Registry SQL Query"); break;
		case RetrieveDocument:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-17", "Retrieve Document"); break;
		case RegistryStoredQuery:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-18", "Registry Stored Query"); break;						
		case RetrieveDocumentSet:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-43", "Retrieve Document Set"); break;
		case PatientIdentityFeed:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-8", "Patient Identity Feed"); break;
		case PixQuery:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-9", "PixQuery"); break;		
		case PixUpdateNotification:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-10", "PIX Update Notification"); break;
		case PDQQuery:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-21", "Patient Demographics Query"); break;
		case RegisterDocumentSet_b:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-42", "Register Document Set-b"); break;
		case ProvideAndRegisterDocumentSet_b:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-41", "Provide and Register Document Set-b"); break;
		case MultiPatientQuery:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-51", "Multi-Patient Query"); break;	
		case DocumentMetadataSubscribe:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-52", "Document Metadata Subscribe"); break;
		case DocumentMetadataNotify:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-53", "Document Metadata Notify"); break;
		case DocumentMetadataPublish:
			codeMapping = new AuditCodeMappings("IHE Transactions", "ITI-54", "Document Metadata Publish"); break;
		}		
		return codeMapping;
	}
	
	/** What is sending the event.
	 * 
	 * From DICOM Supp95 p. 39.
	 * aka ccc3, version 2004xxxx, extensible. */
	public static enum ActiveParticipantIds {
		Application         ("110150", "Application"),
		ApplicationLauncher ("110151", "Application Launcher"),
		Destination         ("110152", "Destination"),
		Source              ("110153", "Source"),
		DestinationMedia    ("110154", "Destination Media"),
		SourceMedia         ("110155", "Source Media");
		
		AuditCodeMappings value;
		ActiveParticipantIds(String code, String name) { value = new AuditCodeMappings("DCM", code, name); }
		public AuditCodeMappings getValue() { return value; }
	};

	/** The type of security alert event that is being sent.
	 * 
	 *  This is a subset of the Audit event ids that are used specifically
	 *  for security events.  This is a passthrough for convience when
	 *  building a message that uses the ccc4 profile.
	 * 
	 *  These are defined in DICOM Sup95 p. 38 and IHT ITI Vol 2. p. 184. 
	 *  aka ccc4, version 2004xxxx, extensible. */
	public static enum SecurityAlertType {
		NodeAuthentication(AuditTypeCodes.NodeAuthentication),
		EmergencyOverride(AuditTypeCodes.EmergencyOverride),
		NetworkConfiguration(AuditTypeCodes.NetworkConfiguration),
		SecurityConfiguration(AuditTypeCodes.SecurityConfiguration),
		HardwareConfiguration(AuditTypeCodes.SecurityConfiguration),
		SoftwareConfiguration(AuditTypeCodes.SoftwareConfiguration),
		UseOfRestrictedFunction(AuditTypeCodes.UseOfRestrictedFunction),
		AuditRecordingStopped(AuditTypeCodes.UseOfRestrictedFunction),
		AuditRecordingStarted(AuditTypeCodes.AuditRecordingStarted),
		ObjectSecurityAttributesChanged(AuditTypeCodes.AuditRecordingStarted),
		SecurityRolesChanged(AuditTypeCodes.AuditRecordingStarted),
		UserSecurityAtttributesChanged(AuditTypeCodes.AuditRecordingStarted);
		
		SecurityAlertType(AuditCodeMappings.AuditTypeCodes value) { this.value = value; }
	    private final AuditTypeCodes value;
	    public AuditTypeCodes getValue() { return value; }
	};
	
	/** Used by the factory to get the character defining the audited action.
	 * 
	 * Defined in RFC 3881 p. 13  Use the getValue function to get the string
	 * value for this event action descrtiptor code.
	 */
	public static enum EventActionCode {
		Create ( "C" ),
		Read   ( "R" ), // Read display, etc...
		Update ( "U" ),
		Delete ( "D" ),
		Execute( "E" ), // most application functions.
		Merge  ( "M" );
		
		private String value;
		EventActionCode(String value) { this.value = value; }
		/** Returns the code for this event action.
		 * @return The encoded event, C, R, U, D, or E. */
		public String getValue() { return value; }
	}
	
	/** Result codes for actions to be audited.
	 * 
	 * Defined in RFC 3881 p. 15 */
	public static enum SuccessCode {
		Success        ( "0" ),
		MinorFailure   ( "4" ),
		SeriousFailure ( "8" ),
		MajorFailure   ( "12");
		
		private String value;
		SuccessCode(String value) { this.value = value; }
		/** Used by the factory to get the number defining a result.
		 * @return The string representation of the result code. */
		public String getValue() { return value; }
	}

    /**
     *  The network access point type.
     *
     * Defined in RFC 3881. Section 5.3.1
     */
    public static enum NetworkAccessPointType {
        MachineName     ((short)1),
        IPAddress       ((short)2),
        TelephoneNumber ((short)3);

        private short value;
        NetworkAccessPointType(short value) { this.value = value; }
        /** Used by the factory to get the number defining a result.
		 * @return The string representation of the result code. */
		public short getValue() { return value; }
    }

    /** Devices that might be referenced in audit messages.
	 * 
	 * Defined in RFC 3881 p. 23 */
	public static enum AuditSourceType {
		EndUserGui            ("1"),
		DataAcquisitionDevice ("2"),
		WebServer             ("3"),
		ApplicationServer     ("4"),
		DatabaseServer        ("5"),
		SecurityServer        ("6"),
		IsoLevel123Network    ("7"),
		IsoLevel456Os         ("8"),
		Unknown               ("9");
		
		private String value;
		AuditSourceType(String value) { this.value = value; }
		/** Used by the factory to get the number defining a result.
		 * @return The string representation of the result code. */
		public String getValue() {
            return this.value;
        }
	}
	
	/** Devices that might be referenced in audit messages.
	 * 
	 * Defined in RFC 3881, Section 5.5.1 */
	public static enum ParticipantObjectTypeCode {
		Person       ((short) 1),
		SystemObject ((short) 2),
		Organization ((short) 3),
		Other        ((short) 4);
		
		private short value;
		ParticipantObjectTypeCode(short value) { this.value = value; }
		/** Used by the factory to get the number defining a result.
		 * @return The string representation of the result code. */
		public short getValue() { return value; }
	}
	
	/** Devices that might be referenced in audit messages.
	 * 
	 * Defined in RFC 3881, Section 5.5.2 */
	public static enum ParticipantObjectRoleCode {
		Patient             (1, ParticipantObjectTypeCode.Person),
		Location            (2, ParticipantObjectTypeCode.Organization),
		Report              (3, ParticipantObjectTypeCode.SystemObject),
		Resource            (4, null),
		MasterFile          (5, ParticipantObjectTypeCode.SystemObject),
		User                (6, null),
		List                (7, ParticipantObjectTypeCode.SystemObject),
		Doctor              (8, ParticipantObjectTypeCode.Person),
		//...
		Customer            (19, ParticipantObjectTypeCode.Organization),
		Job                 (20, ParticipantObjectTypeCode.SystemObject),
		//...
		Query               (24, ParticipantObjectTypeCode.SystemObject);
		
		private short value;
		private ParticipantObjectTypeCode type;
		ParticipantObjectRoleCode(int value, ParticipantObjectTypeCode type) { this.value = (short) value; this.type = type; }
		/** Used by the factory to get the number defining a result.
		 * @return The string representation of the result code. */
		public short getValue() { return value; }
		public ParticipantObjectTypeCode getType() { return type; }
	}
	
	/** Defines the type of this ID.
	 * 
	 * Defined in RFC 3881, Section 5.5.4 */
	public static enum ParticipantObjectIdTypeCode {
		MedicalRecord    ("1", ParticipantObjectTypeCode.Person),
		Patient          ("2", ParticipantObjectTypeCode.Person, "Patient Number", null, "RFC-3881"),
		Encounter        ("3", ParticipantObjectTypeCode.Person),
		Enrollee         ("4", ParticipantObjectTypeCode.Person),
		SocialSecurity   ("5", ParticipantObjectTypeCode.Person),
		Account          ("6", null),
		Guarantor        ("7", null),
		ReportName       ("8", ParticipantObjectTypeCode.SystemObject, "Report Name", null, "RFC-3881"),
		ReportNumber     ("9", ParticipantObjectTypeCode.SystemObject, "Report Number", null, "RFC-3881"),
		SearchCriteria   ("10", ParticipantObjectTypeCode.SystemObject),
		User             ("11", ParticipantObjectTypeCode.Person),
		Uri              ("12", ParticipantObjectTypeCode.SystemObject),
		StudyInstanceUid ("110180", ParticipantObjectTypeCode.SystemObject, "Study Instance UID", "DCM"),
		SopClassUid      ("110181", ParticipantObjectTypeCode.SystemObject, "SOP Class UID", "DCM"),
		NodeId           ("110182", ParticipantObjectTypeCode.SystemObject, "Node ID", "DCM"),
		//Defined in Stored Query Sup.
		PIXQuery         ("ITI-9", ParticipantObjectTypeCode.SystemObject, "PIX Query", "IHE Transactions", "IHE Transactions"),
		PDQQuery         ("ITI-21", ParticipantObjectTypeCode.SystemObject, "PDQ Query", "IHE Transactions", "IHE Transactions"),
		RegistryStoredQuery("ITI-18", ParticipantObjectTypeCode.SystemObject, "Registry Stored Query", "IHE Transactions", "IHE Transactions"),
		RegistrySQLQuery ("ITI-16", ParticipantObjectTypeCode.SystemObject, "Registry SQL Query", "IHE Transactions", "IHE Transactions"),
		MultiPatientQuery("ITI-51", ParticipantObjectTypeCode.SystemObject, "Multi-Patient Query", "IHE Transactions", "IHE Transactions"),
		DocumentMetadataSubscribe("ITI-52", ParticipantObjectTypeCode.SystemObject, "Document Metadata Subscribe", "IHE Transactions", "IHE Transactions"),
		DocumentMetadataNotify("ITI-53", ParticipantObjectTypeCode.SystemObject, "Document Metadata Notify", "IHE Transactions", "IHE Transactions"),
		DocumentMetadataPublish("ITI-54", ParticipantObjectTypeCode.SystemObject, "Document Metadata Publish", "IHE Transactions", "IHE Transactions"),
        //SubmissionSet IdTypeCode is defined in ITI V4 vol2 Section 3.15.4.1.4
		SubmissionSet    ("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", ParticipantObjectTypeCode.SystemObject, null, "IHE XDS Metadata");				
		private String value;
		private String name;
		private ParticipantObjectTypeCode type;
		private String codeSystem;
		private String codeSystemName;
		ParticipantObjectIdTypeCode(String value, ParticipantObjectTypeCode type) {
			this(value, type, null, null);
		}
		ParticipantObjectIdTypeCode(String value, ParticipantObjectTypeCode type, String name, String codeSystem) {
			this(value, type, name, codeSystem, null);
		}
		ParticipantObjectIdTypeCode(String value, ParticipantObjectTypeCode type, String name, String codeSystem, String codeSystemName) {
			this.name = name; 
			this.value = value; 
			this.type = type; 
			this.codeSystem = codeSystem;
			this.codeSystemName = codeSystemName;
		}
		public String getValue() { return value; }
		protected ParticipantObjectTypeCode getType() { return type; }
		public String getCodeSystem() { return codeSystem; }
		public String getName() { return name; }
		public String getCodeSystemName() { return codeSystemName; }
	}
	
}
