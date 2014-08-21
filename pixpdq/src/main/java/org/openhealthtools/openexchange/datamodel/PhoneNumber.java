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

import org.openhealthtools.openexchange.datamodel.SharedEnums.PhoneType;
import org.openhealthtools.openexchange.datamodel.SharedEnums.TelecomUseCode;

/**
 * This class represents an international telephone number.
 * 
 * @author Jim Firby
 * @version 2.0 - Oct 27, 2005
 */
public class PhoneNumber {
	
	private PhoneType type = null;
	private String countryCode = null;
	private String areaCode = null;
	private String number = null;
	private String extension = null;
	private String note = null;
	private String email = null;
	private String rawValue = null;
	private TelecomUseCode useCode = null; 
	
	/* ---------- Constructors ---------------------- */
	
	public PhoneNumber() {}
	
	public PhoneNumber(PhoneType type) {
		this.type = type;
	}
	
	public PhoneNumber(PhoneType type, String areaCode, String number) {
		this.type = type;
		this.areaCode = areaCode;
		this.number = number;
	}

	/* ---------- Getters and Setters --------------- */
	
	/**
	 * @return Returns the rawValue.
	 */
	public String getRawValue() {
		return rawValue;
	}

	/**
	 * @param rawValue The rawValue to set.
	 */
	public void setRawValue(String rawValue) {
		this.rawValue = rawValue;
	}	
	
	/**
	 * @return Returns the areaCode.
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode The areaCode to set.
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @return Returns the countryCode.
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode The countryCode to set.
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return Returns the extension.
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension The extension to set.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return Returns the number.
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number The number to set.
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return Returns the type.
	 */
	public PhoneType getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(PhoneType type) {
		this.type = type;
	}

	/**
	 * @return Returns the use code.
	 */
	public TelecomUseCode getUseCode() {
		return useCode;
	}

	/**
	 * @param useCode The useCode to set.
	 */
	public void setUseCode(TelecomUseCode useCode) {
		this.useCode=useCode;
	}

	/**
	 * Check if this number actually holds a telephone number.
	 * 
	 * @return True if this phone number is populated.
	 */
	public boolean isEmpty() {
		return ((number == null) && (extension == null));
	}
	
	/**
	 * @return Returns the type.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email, The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
