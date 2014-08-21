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

package org.openhealthtools.openexchange.datamodel;

/**
 * This class contains shared ENUM constants.
 * 
 * @author : Shantanu Paul
 */
public class SharedEnums {

	/**
	 * Address types
	 */
	@SuppressWarnings("unused")
	public enum AddressType {
        HOME("HOME", "Home", "H"),
		WORK("WORK", "Work", "O"),  //Office
        UNKNOWN("UNKNOWN", "Unknown", null),
        TEMPORARY("TEMPORARY", "Temporary", "C"), //Current Or Temporary   
        PERMANENT("PERMANENT", "Permanent", "P"), //Permanent   
        MAILING("MAILING",  "Mailing", "M"), //Mailing  
        BUSINESS("BUSINESS", "Business", "B"), // Firm/Business    
        BIRTH ("BIRTH", "Birth",  "N"),  //Birth (nee) (birth address, not otherwise specified)    
        BIRTH_DELIVERY("BIRTH DELIVERY", "Birth Delivery", "BDL"), //Birth delivery location (address where birth occurred) 
        RESIDENCE_AT_BIRTH("RESIDENCE AT BIRTH", "Residence at Birth", "BR"), // Residence at birth (home address at time of birth)
        ORIGIN("ORIGIN", "Origin", "F"), // Country Of Origin    
        LEGAL("LEGAL", "Legal", "L"), // Legal Address   
        REGISTRY_HOME("REGISTRY HOME", "Registry Home", "RH"), // Registry home. Refers to the information system, typically managed by a public health agency, that stores patient information such as immunization histories or cancer data, regardless of where the patient obtains services.    
        BAD_ADDRESS("BAD ADDRESS", "Bad Address", "BA"); //  Bad address 

        private String value = null;
        private String printValue = null;
        private String hl7Value = null;
        
        private AddressType(String value, String printValue, String hl7Value) {
            this.value = value;
            this.printValue = printValue;
            this.hl7Value = hl7Value;
        }
        public String getValue() { return this.value; }
        public String getPrintValue() { return  this.printValue; }
        public String getHL7Value() { return this.hl7Value; }
        /**
         * Maps a value from its String format to corresponding AddressType.
         * @param value
         * @return the AddressType
         */
        public static AddressType mapValueOf(String value) {
            if (null == value)  return UNKNOWN;
            value = value.toUpperCase();
            AddressType[] types = AddressType.values();
            for (AddressType type : types) {
                if (type.getValue().equals( value ) ||
                    type.getValue().startsWith(value)  ) {
                    return type;
                }
            }
            if (value.equals("OFFICE")) return WORK;
            if (value.equals("RESIDENCE")) return HOME;
            return UNKNOWN;
        }
        
        /**
         * Converts from String HL7 value to its corresponding enum type.
         *
         * @param hl7Value the String value to be converted
         * @return the AddressType enum
         */
        public static AddressType hl7ValueOf(String hl7Value) {
        	if (hl7Value == null) return UNKNOWN;
        	
            AddressType[] types = AddressType.values();
            for (AddressType type : types) {
            	String value = type.getHL7Value();
            	if (value == null) 
            		continue;
                if (type.getHL7Value().equalsIgnoreCase( hl7Value ) ) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

	/**
	 * Phone number types
	 */
	@SuppressWarnings("unused")
	public enum PhoneType {
		HOME ("HOME", "Home", "H"),
		PRIMARY_HOME ("PRIMARY HOME", "Primary Home", "HP"),
		VACATION_HOME ("VACATION HOME", "Vacation Home", "HV"),
        BIRTH ("BIRTH", "Birth Place", "BIR"),
        TEMP ("TEMPORARY", "Temporary Address", "TMP"),
        WORK ("WORK", "Work Place", "WP"),
        SERVICE ("SERVICE", "Answering Service", "AS"),
        EMERGENCY("EMERGENCY", "Emergency Contact", "EC"),
        CELL("CELL", "Mobile Contact", "MC"),
        PAGER("PAGER", "Pager", "PG"),
        FAX ("FAX", "Fax", null),
        UNKNOWN("UNKNOWN", "Unknown", null);

        private String value = null;
        private String printValue = null;
        private String hl7Value = null;

        private PhoneType(String value, String printValue, String hl7Value) {
            this.value = value;
            this.printValue = printValue;
            this.hl7Value = hl7Value;
        }
        public String getValue() { return this.value; }
        public String getPrintValue() { return this.printValue; }
        public String getHL7Value() { return this.hl7Value;  }

        /**
         * Maps a value from its String format to corresponding PhoneType.
         * @param value
         * @return the PhoneType
         */
        public static PhoneType mapValueOf(String value) {
            if (null == value)  return UNKNOWN;
            value = value.toUpperCase();
            PhoneType[] types = PhoneType.values();
            for (PhoneType type : types) {
                if (type.getValue().equals( value ) ||
                    type.getValue().startsWith(value)  ) {
                    return type;
                }
            }
            if (value.equals("OFFICE")) return WORK;
            if (value.equals("RESIDENCE")) return HOME;
            return UNKNOWN;
        }
        
        /**
         * Converts from String HL7 value to its corresponding enum type.
         *
         * @param hl7Value the hl7 TelecommunicationAddressUse value to be converted
         * @return the PhoneType enum
         */
        public static PhoneType hl7ValueOf(String hl7Value) {
        	if (hl7Value == null) return UNKNOWN;
        	
        	PhoneType[] types = PhoneType.values();
            for (PhoneType type : types) {
            	String value = type.getHL7Value();
            	if (value == null) 
            		continue;
                if (type.getHL7Value().equalsIgnoreCase( hl7Value ) ) {
                    return type;
                }
            }
            return UNKNOWN;
        }

    }

    /**
	 * Telecommunication Use Code
	 */
	@SuppressWarnings("unused")
	public enum TelecomUseCode {
		ASN("ASN", "Answering Service Number"),
		BPN("BPN", "Beeper Number"),
		EMR("EMR", "Emergency Number"),
		NET("NET", "Network (email) Address"),
		ORN("ORN", "Other Residence Number"),
		PRN("PRN", "Primary Residence Number"),
		VHN("VHN", "Vacation Home Number"),
		WPN("WPN", "Work Number");

        private String code = null;
        private String description = null;

        private TelecomUseCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return this.code;
        }

        public String getDescription() {
            return  this.description;
        }

        /**
         * Converts from String code value to its corresponding enum type.
         *
         * @param code the String value to be converted
         * @return the TelecomUseCode enum
         */
        public TelecomUseCode codeToEnum(String code) {
            TelecomUseCode[] types = TelecomUseCode.values();
            for (TelecomUseCode type : types) {
                if (type.getCode().equalsIgnoreCase( code ) ) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * Clinical Note types
     */
    @SuppressWarnings("unused")
    public enum ClinicalNoteType {
        HP_NOTES ("HP Notes", "HP_NOTES"),
        MD_NOTES ("MD Notes", "MD_NOTES"),
        REF_CONS ("REF Cons", "REF_CONS"),
        DISC_SUMM("Discharge Summary", "DISC_SUMM"),
        OTHER_NOTES("Other Notes", "OTHER_NOTES"),
        UNKNOWN ("Unknown", "UKNOWN");

        private String value = null;
        private String cprValue = null;
        private ClinicalNoteType(String value, String cprValue){
            this.value = value;
            this.cprValue = cprValue;
        }
        public String getValue(){ return this.value; }
        public String getCPRValue() {return this.cprValue; }

        public ClinicalNoteType cprValueOf(String cprValue) {
            ClinicalNoteType[] types = ClinicalNoteType.values();
            for (ClinicalNoteType type : types) {
                if (type.getCPRValue().equalsIgnoreCase( cprValue ) ) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }
    /**
	 * Adminstratice sex types
	 */
	@SuppressWarnings("unused")
	public enum SexType {
		MALE("Male", "M"),
		FEMALE("Female", "F"),
		OTHER("Other", "O"),    // IHE value (I leave it to your imagination)
		UNKNOWN("Unknown", "U");

        private String value = null;
        private String cdaValue = null;

        private SexType(String value, String cdaValue) {
            this.value = value;
            this.cdaValue = cdaValue;
        }
        public static SexType getByString(String sex){
        	if (sex == null){
        		return UNKNOWN;
           	}
        	if (sex.equalsIgnoreCase("male") || sex.equalsIgnoreCase("m")){
        		return MALE;
        	}
        	if (sex.equalsIgnoreCase("female") || sex.equalsIgnoreCase("f")){
        		return FEMALE;
        	}
        	if (sex.equalsIgnoreCase("other") || sex.equalsIgnoreCase("o")){
        		return OTHER;
        	}
        	else {
        		return UNKNOWN;
        	}
        }
        public String getValue() {
            return this.value;
        }

        /**
         * Gets CDA value used by CDA documents
         * @return the CDA value
         */
        public String getCDAValue() {
            return  this.cdaValue;
        }

        /**
         * Converts from String CDA value to its corresponding enum type.
         *
         * @param cdaValue the String value to be converted
         * @return the SexType enum
         */
        public SexType cdaValueOf(String cdaValue) {
            SexType[] types = SexType.values();
            for (SexType type : types) {
                if (type.getCDAValue().equalsIgnoreCase( cdaValue ) ) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    /**
	 * Adminstratice sex types
	 */
	@SuppressWarnings("unused")
	public enum MartitalStatusType {
		SEPARATED("Separated", "A"),
		DIVORCED("Divorced", "D"),
		MARRIED("Married", "M"),  
		SINGLE("Single", "S"),
		WIDOWED("Widowed", "W"),
		COMMON_LAW("Common Law", "C"),
		LIVING_TOGETHER("Living Together", "G"),
		DOMESTIC_PARTNER("Domestic Partner", "P"),
		REGISTERED_DOMESTIC_PARTNER("Registered Domestic Partner", "R"),
		LEGALLY_SEPARATED("Legally Separated", "E"),
		ANNULLED("Annulled", "N"),
		INTERLOCUTORY("Interlocutory", "I"),
		UNMARRIED("Unmarried", "B"),
		UNREPORTED("Unreported", "T"),
		OTHER("Other", "O"),
		UNKNOWN("Unknown", "U");

        private String value = null;
        private String hl7Value = null;

        private MartitalStatusType(String value, String hl7Value) {
            this.value = value;
            this.hl7Value = hl7Value;
        }
        
        public String getValue() {
            return this.value;
        }

        /**
         * Gets the HL7 value
         *  
         * @return the HL7 value
         */
        public String getHL7Value() {
            return  this.hl7Value;
        }

        /**
         * Converts from String HL7 value to its corresponding enum type.
         *
         * @param hl7Value the String value to be converted
         * @return the MartitalStatusType enum
         */
        public MartitalStatusType hl7ValueOf(String hl7Value) {
        	MartitalStatusType[] types = MartitalStatusType.values();
            for (MartitalStatusType type : types) {
                if (type.getHL7Value().equalsIgnoreCase( hl7Value ) ) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }
	
	public enum PatientContactType {
		GUARDIAN("Guardian", "GUARD"),
		AGENT_PATIENT("Agents of Patient", "AGNT"),
		CARE_GIVERS("Care Givers", "CAREGIVER"),
		EMERGENCY_CONTACTS("Emergency Contacts", "ECON"),
		NEXT_OF_KIN("Next of Kin", "NOK"),
		OTHER("Other Relations", "PRS");
		
		private String value = null;
		private String cdaValue = null;
		private PatientContactType(String name, String cdaValue) {
			this.value = name;
			this.cdaValue = cdaValue;
		}
		public String getValue() {
			return this.value;
		}
		public String getCdaValue() {
			return this.cdaValue;
		}
	}
	
	/**
	 * XDS document format codes
	 */
	@SuppressWarnings("unused")
	public enum XdsFormatCode {
        UNKNOWN { public String getValue() { return "Unknown format"; }},
        IHE_PDF_10 { public String getValue() { return "IHE PDF 1.0"; }},
        IHE_CDA_10 { public String getValue() { return "IHE CDA 1.0"; }},
        IHE_CDAR2_10 { public String getValue() { return "IHE CDAR2 1.0"; }},
        IHE_CCR_09 { public String getValue() { return "IHE CCR 0.9"; }},
        HL7_LAB_25 { public String getValue() { return "HL7 Lab 2.5"; }},
        IHE_SCAN_TEXT_1X { public String getValue() { return "Scanned Text"; }},
        IHE_SCAN_PDF_1X { public String getValue() { return "Scanned PDF"; }},
        XDS_MS { public String getValue() { return "Medical Summary"; }},
        PPHP { public String getValue() { return "PPHP"; }},
        EDR { public String getValue() { return "Emergency Department Referral"; }},
        BPPC { public String getValue() { return "Basic Patient Privacy Consent"; }},
        XPHR_Extract { public String getValue() { return "XPHR Extract"; }},
        XPHR_Update { public String getValue() { return "XPHR Update"; }};

        public abstract String getValue();
	}

    /**
	 * XDS document class codes
	 */
	@SuppressWarnings("unused")
	public enum XdsClassCode {
		UNKNOWN { public String getValue() { return "Unknown class code"; }},
        Communication { public String getValue() {return "Communication"; }},
        Evaluation_and_Management { public String getValue() {return "Evaluation and Management"; }},
        Conference { public String getValue() {return "Conference"; }},
        Case_Conference { public String getValue() {return "Case Conference"; }},
        Consult { public String getValue() {return "Consult"; }},
        Confirmatory_Consultation { public String getValue() {return "Confirmatory Consultation"; }},
        Counseling { public String getValue() {return "Counseling"; }},
        Group_Counseling { public String getValue() {return "Group Counseling"; }},
        Education { public String getValue() {return "Education"; }},
        History_and_Physical { public String getValue() {return "History and Physical"; }},
        Admission_History_and_Physical { public String getValue() {return "Admission History and Physical"; }},
        Comprehensive_History_and_Physical { public String getValue() {return "Comprehensive History and Physical"; }},
        Targeted_History_and_Physical { public String getValue() {return "Targeted History and Physical"; }},
        Initial_Evaluation { public String getValue() {return "Initial Evaluation"; }},
        Admission_Evaluation { public String getValue() {return "Admission Evaluation"; }},
        Preoperative_Evaluation_and_Management { public String getValue() {return "Pre-operative Evaluation and Management"; }},
        Subsequent_Evaluation { public String getValue() {return "Subsequent Evaluation"; }},
        Summarization_of_Episode { public String getValue() {return "Summarization of Episode"; }},
        Transfer_Summarization { public String getValue() {return "Transfer Summarization"; }},
        Discharge_Summarization { public String getValue() {return "Discharge Summarization"; }},
        Summary_of_Death { public String getValue() {return "Summary of Death"; }},
        Transfer_of_care_Referral { public String getValue() {return "Transfer of Care Referral"; }},
        Supervisory_Direction { public String getValue() {return "Supervisory Direction"; }},
        Telephone_Encounter { public String getValue() {return "Telephone Encounter"; }},
        Interventional_Procedure { public String getValue() {return "Interventional Procedure"; }},
        Operative { public String getValue() {return "Operative"; }},
        Pathology_Procedure { public String getValue() {return "Pathology Procedure"; }},
        Autopsy { public String getValue() {return "Autopsy"; }};

        public abstract String getValue();

        public static XdsClassCode fromValue(String value) {
            if (value.equals(Communication.getValue()) ) return Communication;
            else if (value.equals(Evaluation_and_Management.getValue())) return Evaluation_and_Management;
            else if (value.equals(Conference.getValue()) ) return Conference;
            else if (value.equals(Case_Conference.getValue()) ) return Case_Conference;
            else if (value.equals(Consult.getValue()) ) return Consult;
            else if (value.equals(Confirmatory_Consultation.getValue()) ) return Confirmatory_Consultation;
            else if (value.equals(Counseling.getValue()) ) return Counseling;
            else if (value.equals(Group_Counseling.getValue()) ) return Group_Counseling;
            else if (value.equals(Education.getValue()) ) return Education;
            else if (value.equals(History_and_Physical.getValue()) ) return History_and_Physical;
            else if (value.equals(Admission_History_and_Physical.getValue()) ) return Admission_History_and_Physical;
            else if (value.equals(Comprehensive_History_and_Physical.getValue()) ) return Comprehensive_History_and_Physical;
            else if (value.equals(Targeted_History_and_Physical.getValue()) ) return Targeted_History_and_Physical;
            else if (value.equals(Initial_Evaluation.getValue()) ) return Initial_Evaluation;
            else if (value.equals(Admission_Evaluation.getValue()) ) return Admission_Evaluation;
            else if (value.equals(Preoperative_Evaluation_and_Management.getValue()) ) return Preoperative_Evaluation_and_Management;
            else if (value.equals(Subsequent_Evaluation.getValue()) ) return Subsequent_Evaluation;
            else if (value.equals(Summarization_of_Episode.getValue()) ) return Summarization_of_Episode;
            else if (value.equals(Transfer_Summarization.getValue()) ) return Transfer_Summarization;
            else if (value.equals(Discharge_Summarization.getValue()) ) return Discharge_Summarization;
            else if (value.equals(Summary_of_Death.getValue()) ) return Summary_of_Death;
            else if (value.equals(Transfer_of_care_Referral.getValue()) ) return Transfer_of_care_Referral;
            else if (value.equals(Supervisory_Direction.getValue()) ) return Supervisory_Direction;
            else if (value.equals(Telephone_Encounter.getValue()) ) return Telephone_Encounter;
            else if (value.equals(Interventional_Procedure.getValue()) ) return Interventional_Procedure;
            else if (value.equals(Operative.getValue()) ) return Operative;
            else if (value.equals(Pathology_Procedure.getValue()) ) return Pathology_Procedure;
            else if (value.equals(Autopsy.getValue()) ) return Autopsy;
            else return UNKNOWN;
        }
	}

	/**
	 * XDS document type codes
	 */
	@SuppressWarnings("unused")
	public enum XdsTypeCode {
		UNKNOWN { public String getValue() { return "Unknown Document Type"; }},
        Interventional_Procedure_Note { public String getValue() { return "Interventional Procedure Note";}},
        Autopsy_Note { public String getValue() {return "Autopsy Note"; }},
        Conference_Evaluation_Note { public String getValue() {return "Conference Evaluation Note"; }},
        Consultation_Note { public String getValue() {return "Consultation Note"; }},
        Discharge_Note { public String getValue() {return "Discharge Note"; }},
        Discharge_Summarization_Note { public String getValue() {return "Discharge Summarization Note"; }},
        Evaluation_And_Management_Note { public String getValue() {return "Evaluation And Management Note"; }},
        History_And_Physical_Note { public String getValue() {return "History And Physical Note"; }},
        Initial_Evaluation_Note { public String getValue() {return "Initial Evaluation Note"; }},
        Procedure_Note { public String getValue() {return "Procedure Note"; }},
        Subsequent_Visit_Evaluation_Note { public String getValue() {return "Subsequent Visit Evaluation Note"; }},
        Summarization_of_Episode_Note { public String getValue() {return "Summarization of Episode Note"; }},
        Surgical_Operation_Note { public String getValue() {return "Surgical Operation Note"; }},
        Targeted_History_And_Physical_Note { public String getValue() {return "Targeted History And Physical Note"; }},
        Transfer_of_Care_Referral_Note { public String getValue() {return "Transfer of Care Referral Note"; }},
        Transfer_Summarization_Note { public String getValue() {return "Transfer Summarization Note"; }};
        public abstract String getValue();

        public static XdsTypeCode fromValue(String value) {
            if (value.equals(Interventional_Procedure_Note.getValue()) ) return Interventional_Procedure_Note;
            else if (value.equals(Autopsy_Note.getValue()) ) return Autopsy_Note;
            else if (value.equals(Conference_Evaluation_Note.getValue()) ) return Conference_Evaluation_Note;
            else if (value.equals(Consultation_Note.getValue()) ) return Consultation_Note;
            else if (value.equals(Discharge_Note.getValue()) ) return Discharge_Note;
            else if (value.equals(Discharge_Summarization_Note.getValue()) ) return Discharge_Summarization_Note;
            else if (value.equals(Evaluation_And_Management_Note.getValue()) ) return Evaluation_And_Management_Note;
            else if (value.equals(History_And_Physical_Note.getValue()) ) return History_And_Physical_Note;
            else if (value.equals(Initial_Evaluation_Note.getValue()) ) return Initial_Evaluation_Note;
            else if (value.equals(Procedure_Note.getValue()) ) return Procedure_Note;
            else if (value.equals(Subsequent_Visit_Evaluation_Note.getValue()) ) return Subsequent_Visit_Evaluation_Note;
            else if (value.equals(Summarization_of_Episode_Note.getValue()) ) return Summarization_of_Episode_Note;
            else if (value.equals(Surgical_Operation_Note.getValue()) ) return Surgical_Operation_Note;
            else if (value.equals(Targeted_History_And_Physical_Note.getValue()) ) return Targeted_History_And_Physical_Note;
            else if (value.equals(Transfer_of_Care_Referral_Note.getValue()) ) return Transfer_of_Care_Referral_Note;
            else if (value.equals(Transfer_Summarization_Note.getValue()) ) return Transfer_Summarization_Note;
            else return UNKNOWN;
        }
	}
    
	/**
	 * XDS document content codes
	 */
	@SuppressWarnings("unused")
	public enum XdsContentCode {
		UNKNOWN { public String getValue() { return "Unknown submission set content"; }},
        Communication { public String getValue() {return "Communication"; }},
        Evaluation_and_Management { public String getValue() {return "Evaluation and Management"; }},
        Conference { public String getValue() {return "Conference"; }},
        Case_Conference { public String getValue() {return "Case Conference"; }},
        Consult { public String getValue() {return "Consult"; }},
        Confirmatory_Consultation { public String getValue() {return "Confirmatory Consultation"; }},
        Counseling { public String getValue() {return "Counseling"; }},
        Group_Counseling { public String getValue() {return "Group Counseling"; }},
        Education { public String getValue() {return "Education"; }},
        History_and_Physical { public String getValue() {return "History and Physical"; }},
        Admission_History_and_Physical { public String getValue() {return "Admission History and Physical"; }},
        Comprehensive_History_and_Physical { public String getValue() {return "Comprehensive History and Physical"; }},
        Targeted_History_and_Physical { public String getValue() {return "Targeted History and Physical"; }},
        Initial_Evaluation { public String getValue() {return "Initial Evaluation"; }},
        Admission_Evaluation { public String getValue() {return "Admission Evaluation"; }},
        Preoperative_Evaluation_and_Management { public String getValue() {return "Pre-operative Evaluation and Management"; }},
        Subsequent_Evaluation { public String getValue() {return "Subsequent Evaluation"; }},
        Summarization_of_Episode { public String getValue() {return "Summarization of Episode"; }},
        Transfer_Summarization { public String getValue() {return "Transfer Summarization"; }},
        Discharge_Summarization { public String getValue() {return "Discharge Summarization"; }},
        Summary_of_Death { public String getValue() {return "Summary of Death"; }},
        Transfer_of_care_Referral { public String getValue() {return "Transfer of Care Referral"; }},
        Supervisory_Direction { public String getValue() {return "Supervisory Direction"; }},
        Telephone_Encounter { public String getValue() {return "Telephone Encounter"; }},
        Interventional_Procedure { public String getValue() {return "Interventional Procedure"; }},
        Operative { public String getValue() {return "Operative"; }},
        Pathology_Procedure { public String getValue() {return "Pathology Procedure"; }},
        Autopsy { public String getValue() {return "Autopsy"; }};

        public abstract String getValue();

        public static XdsContentCode fromValue(String value) {
            if (value.equals(Communication.getValue()) ) return Communication;
            else if (value.equals(Evaluation_and_Management.getValue())) return Evaluation_and_Management;
            else if (value.equals(Conference.getValue()) ) return Conference;
            else if (value.equals(Case_Conference.getValue()) ) return Case_Conference;
            else if (value.equals(Consult.getValue()) ) return Consult;
            else if (value.equals(Confirmatory_Consultation.getValue()) ) return Confirmatory_Consultation;
            else if (value.equals(Counseling.getValue()) ) return Counseling;
            else if (value.equals(Group_Counseling.getValue()) ) return Group_Counseling;
            else if (value.equals(Education.getValue()) ) return Education;
            else if (value.equals(History_and_Physical.getValue()) ) return History_and_Physical;
            else if (value.equals(Admission_History_and_Physical.getValue()) ) return Admission_History_and_Physical;
            else if (value.equals(Comprehensive_History_and_Physical.getValue()) ) return Comprehensive_History_and_Physical;
            else if (value.equals(Targeted_History_and_Physical.getValue()) ) return Targeted_History_and_Physical;
            else if (value.equals(Initial_Evaluation.getValue()) ) return Initial_Evaluation;
            else if (value.equals(Admission_Evaluation.getValue()) ) return Admission_Evaluation;
            else if (value.equals(Preoperative_Evaluation_and_Management.getValue()) ) return Preoperative_Evaluation_and_Management;
            else if (value.equals(Subsequent_Evaluation.getValue()) ) return Subsequent_Evaluation;
            else if (value.equals(Summarization_of_Episode.getValue()) ) return Summarization_of_Episode;
            else if (value.equals(Transfer_Summarization.getValue()) ) return Transfer_Summarization;
            else if (value.equals(Discharge_Summarization.getValue()) ) return Discharge_Summarization;
            else if (value.equals(Summary_of_Death.getValue()) ) return Summary_of_Death;
            else if (value.equals(Transfer_of_care_Referral.getValue()) ) return Transfer_of_care_Referral;
            else if (value.equals(Supervisory_Direction.getValue()) ) return Supervisory_Direction;
            else if (value.equals(Telephone_Encounter.getValue()) ) return Telephone_Encounter;
            else if (value.equals(Interventional_Procedure.getValue()) ) return Interventional_Procedure;
            else if (value.equals(Operative.getValue()) ) return Operative;
            else if (value.equals(Pathology_Procedure.getValue()) ) return Pathology_Procedure;
            else if (value.equals(Autopsy.getValue()) ) return Autopsy;
            else return UNKNOWN;
        }
	}
  
    /**
     * Problem status type codes
     */
    @SuppressWarnings("unused")
    public enum ClinicalStatusCode {
        ACTIVE("active",     "active",  "55561003"),
        INACTIVE("Inactive", "inactive","73425007" ),
        CHRONIC("Chronic", "chronic",   "90734009"),
        INTERMITTENT("Intermittent", "intermittent", "7087005"),
        RECURRENT("Recurrent", "recurrent", "255227004"),
        RULE_OUT("Rule out", "rule out",    "415684004"),
        RULED_OUT("Ruled out", "ruled out", "410516002"),
        RESOLVED("completed", "resolved",    "413322009"),
        ABORTED("aborted", "aborted",    null),
        UNKNOWN("Unknown", "UNK", null);

        private String value = null;
        private String cdaValue = null;
        private String code = null;
        private String codeSystem = "2.16.840.1.113883.6.96";
        private String codeSystemName = "SNOMED CT";
        
        private ClinicalStatusCode(String value, String cdaValue, String code) {
            this.value = value;
            this.cdaValue = cdaValue;
            this.code = code;
        }
        public String getValue() { return this.value; }
       // public String getCDAValue() { return  this.cdaValue; }
        public String getCode() { return this.code; }
        public String getCodeSystem() {
        	return this.codeSystem;
        }
        public String getCodeSystemName() {
        	return this.codeSystemName;
        }
        /**
         * Maps a value from its String format to corresponding ClinicalStatusCode.
         * @param value
         * @return ClinicalStatusCode
         */
        public static ClinicalStatusCode mapValueOf(String value) {
            if (null == value) return UNKNOWN;
            if (value.equalsIgnoreCase("Active") || value.equalsIgnoreCase("Current")) {
                return SharedEnums.ClinicalStatusCode.ACTIVE;
            } else if (value.equalsIgnoreCase("Inactive")) {
                return SharedEnums.ClinicalStatusCode.INACTIVE;
            } else if (value.equalsIgnoreCase("Chronic")) {
                return SharedEnums.ClinicalStatusCode.CHRONIC;
            } else if (value.equalsIgnoreCase("Intermittent")) {
                return SharedEnums.ClinicalStatusCode.INTERMITTENT;
            } else if (value.equalsIgnoreCase("Recurrent")) {
                return SharedEnums.ClinicalStatusCode.RECURRENT;
            } else if (value.equalsIgnoreCase("Rule out")) {
                return SharedEnums.ClinicalStatusCode.RULE_OUT;
            } else if (value.equalsIgnoreCase("Ruled out")) {
                return SharedEnums.ClinicalStatusCode.RULED_OUT;
            } else if (value.equalsIgnoreCase("Resolved")) {
                return SharedEnums.ClinicalStatusCode.RESOLVED;
            } else
                return SharedEnums.ClinicalStatusCode.UNKNOWN;
        }
    }
    
    public enum StatusCode {
    	COMPLETED("completed"),
    	ACTIVE("active"),
    	ABORTED("aborted"),
    	CANCELLED("cancelled");
    	 
    	private String value;
    	private StatusCode(String value) {
    		this.value = value;
    	}
        public String getValue() { return this.value; }      
    }

    /**
     * Subscriber to patient relationship
     */
    @SuppressWarnings("unused")
    public enum SubscriberToPatientRelationshipType {
        SELF    ("SELF", "Self", "1"),
        PHFAMDEP("PHFAMDEP", "Policy holder for a family dependent", ""),  //TODO: find emrValue
        UNKNOWN ("UNKNOWN", "Unknown",  "Unknown");

        private String value = null;
        private String displayName = null;
        private String emrValue = null;

        private SubscriberToPatientRelationshipType(String value, String displayName, String emrValue) {
            this.value = value;
            this.displayName = displayName;
            this.emrValue = emrValue;
        }
        public String getValue() { return this.value; }
        public String getDisplayName() { return this.displayName;  }
        public String getEmrValue() { return this.emrValue; }
        public SubscriberToPatientRelationshipType emrValueOf(String emrValue) {
            SubscriberToPatientRelationshipType[] types = SubscriberToPatientRelationshipType.values();
            for (SubscriberToPatientRelationshipType type : types) {
                if (type.getEmrValue().equalsIgnoreCase(emrValue))
                    return type;
            }
            return PHFAMDEP; //TODO: defaults to Family dependent
        }
    }
    
    /**
	 * Supported Mime Types
	 */
	@SuppressWarnings("unused")
	public enum MimeTypes {
		PDF { public String getValue() { return "application/pdf"; }},
        XML { public String getValue() { return "text/xml"; }},
        XML2 { public String getValue() { return "application/xml"; }},
		CDAR2 { public String getValue() { return "text/x-cda-r2+xml"; }},
		//HL7Lab { public String getValue() { return "application/x-hl7"; }}, don't think we'll support this
		//DICOM { public String getValue() { return "application/dicom"; }}, don't think we'll use this
		PLAIN{ public String getValue() { return "text/plain"; }},
        HTML { public String getValue() { return "text/html"; }};

        public abstract String getValue();
	}
	/**
	 * Supported User Fields
	 */
	@SuppressWarnings("unused")
	public enum UserFields {
		FNAME { public String getValue() { return "fname"; }},
		MNAME { public String getValue() { return "mname"; }},
		LNAME { public String getValue() { return "lname"; }},
		EMAIL{ public String getValue() { return "email"; }};

        public abstract String getValue();
	}
	
	/**
	 * Security Alert Settings
	 */
	 @SuppressWarnings("unused")
	 public enum SecurityAlertSettings {
		NEVER { public String getValue() { return "never"; }},
		ALWAYS { public String getValue() { return "always"; }},
		NORELATION { public String getValue() { return "norelation"; }};

	    public abstract String getValue();
	 }

    /**
     * Document relationship types as defined by IHE ITI technical framework profile.
     * see section 3.14.4.1.2.6 of ITI TF vol2, 2005
     */
    public enum DocumentRelationship {
       APND { public String getValue() { return "APND"; }},
       RPLC { public String getValue() { return "RPLC"; }},
       XFRM  { public String getValue() { return "XFRM";}},
       XFRM_RPLC {public String getValue() { return "XFRM_RPLC";}};

       public abstract String getValue();
    }

    public enum ConfidentialityCode {
        Low("L", "1.3.6.1.4.1.21367.2006.7.108", "normal plus Research", "Low Confidentiality, e.g. Normal Sharing + Researching"),
        Normal("N", "1.3.6.1.4.1.21367.2006.7.107", "normal", "Normal Sharing"),
        Restricted("R", "1.3.6.1.4.1.21367.2006.7.109", "restricted", "Restricted Sharing, e.g, VIP Sharing"),
        OptOut("O", "1.3.6.1.4.1.21367.2006.7.106", "Opt-Out", "Opt-Out namely, No Sharing");

        private String code = null;
        private String displayName = null;
        private String description = null;
        private String codeValue = null;
        private ConfidentialityCode(String code,String codeValue, String displayName, String descriptoin) {
            this.code = code;
            this.displayName = displayName;
            this.description = descriptoin;
            this.codeValue = codeValue;
        }

        public String getCode(){ return code; }
        public String getCodeValue(){ return codeValue; }
        public static ConfidentialityCode fromCode(String code) {
            if (code.equalsIgnoreCase(ConfidentialityCode.Low.getCode())) return ConfidentialityCode.Low;
            else if (code.equalsIgnoreCase(ConfidentialityCode.Normal.getCode())) return ConfidentialityCode.Normal;
            else if (code.equalsIgnoreCase(ConfidentialityCode.Restricted.getCode())) return ConfidentialityCode.Restricted;
            else if (code.equalsIgnoreCase(ConfidentialityCode.OptOut.getCode())) return ConfidentialityCode.OptOut;
            else return null;
        }
        public String getCodeSystem() { return "2.16.840.1.113883.5.25"; }
        public String getCodeSystemName() { return "Confidentiality"; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    /**
     * An enumeration of Discharge Disposition Code for Emergency Department.
     * See http://www.cdc.gov/ncipc/pub-res/pdf/deeds.pdf page 90
     */
    public enum DischargeDispositionCode {
        DISCHARGE_TO_HOME_OR_SELFCARE("10", "Discharge to home or self-care (routine discharge)"),
        DISCHARGE_TO_ANOTHER_SHORT_TERM_HOSPITAL("20", "Transferred/discharged to another short-term general hospital"),
        DISCHARGE_TO_SKILLED_NURSING_FACILITY("30", "Thransferred/discharged to skilled nursing facility"),
        DISCHARGE_TO_INTERMEDIATE_CARE_FACILITY("40", "Transferred/discharged to intermediate care facility"),
        DISCHARGE_TO_ANOTHER_TYPE_OF_INSTITUTION("50", "Transferred/discharged to another type of institution"),
        DISCHARGE_TO_HOME_WITH_IV_DRUG_THERAPY("60", "Transferred/discharged to home under care of a home IV drug therapy provider"),
        DISCHARGE_TO_HOME_WITH_HOME_CARE_PROVIDER("70", "Transferred/discharged to home under care of certified home care provider/program"),
        LEFT_WITHOUT_RECEIVING_MEDICAL_ADVICE_AGAINST_LEAVING("80", "Left without receiving medical advice against leaving"),
        LEFT_AFTER_RECEIVING_MEDICAL_ADVICE_AGAINST_LEAVING("90", "Left after receiving medical advice against leaving"),
        PLACED_IN_DESIGNATED_OBSERVATION_UNIT("100", "Placed in designated observation unit (not an inpatient hospital admission"),
        ADMITTED_TO_HOSPITAL_FLOOR_BED("110", "Admitted to hospital floor bed"),
        ADMITTED_TO_INTERMEDIATE_CARE("120", "Admitted to intermediate care/telemetry unit"),
        ADMITTED_TO_INTENSIVE_CARE_UNIT("130", "Admitted to intensive care unit"),
        ADMITTED_TO_OPERATING_ROOM("140", "Admitted to operating room"),
        DIED("150", "Died"),
        OTHER("888", "OTHER"),
        UNKNOWN("999", "Unknown");

        private String code;
        private String description;
        private String codeSystem = "2.16.840.1.113883.6.102.4.2";
        private String codeSystemName = "DEEDS8.02";
        private DischargeDispositionCode(String code, String description) {
            this.code = code;
            this.description = description;
        }
        public String getCode() { return this.code; }
        public String getDescription() { return this.description; }
        public String getCodeSystem() { return this.codeSystem; }
        public String getCodeSystemName() { return this.codeSystemName; }
        public static DischargeDispositionCode fromCode(String fromCode) {
            DischargeDispositionCode[] codes = DischargeDispositionCode.values();
            for (DischargeDispositionCode code : codes) {
                if (code.getCode().equals(fromCode))
                    return code;
            }
            return DischargeDispositionCode.UNKNOWN;
        }
    }

    public enum TransportModeCode {
        GROUND_AMBULANCE("10", "Ground ambulance"),
        HELICOPTER_AMBULANCE("20", "Helicopter ambulance"),
        FIXED_WING_AIR_AMBULANCE("30", "Fixed-wing air ambulance"),
        OTHER_AMBULANCE("40", "Ambulance, not otherwise specified"),
        WALKIN_FOLLOWING_TRANSPORT_VIA_PRIVATE_TRANSPORTATION("50", "Walk-in following transport via private transportation"),
        WALKIN_FOLLOWING_TRANSPORT_VIA_PUBLIC_TRANSPORTATION("60", "Walk-in following transport via public transport"),
        WALKIN_FOLLOWING_NONAMBULANCE("70", "Walk-in following nonambulance, law enforcement transport"),
        OTHER_WALKIN("80", "Walk-in, not otherwise specified"),
        OTHER("88", "Other modef of transport"),
        UNKNOWN("99", "Unknown mode of transport");

        private String code;
        private String description;
        private String codeSystem = "2.16.840.1.113883.6.102.4.2";
        private String codeSystemName = "DEEDS4.02";
        private TransportModeCode(String code, String description) {
            this.code = code;
            this.description = description;
        }
        public String getCode() { return this.code; }
        public String getDescription() { return this.description; }
        public String getCodeSystem() { return this.codeSystem; }
        public String getCodeSystemName() { return this.codeSystemName; }
        public static TransportModeCode fromCode(String fromCode) {
            TransportModeCode[] codes = TransportModeCode.values();
            for (TransportModeCode code : codes) {
                if (code.getCode().equals(fromCode))
                    return code;
            }
            return TransportModeCode.UNKNOWN;
        }
    }
}
