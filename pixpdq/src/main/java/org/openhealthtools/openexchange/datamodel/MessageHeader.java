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

import java.util.Calendar;



/**
 * This class represents a HL7 message header.
 *
 * @author Wenzhi Li
 * @version 1.0, Nov 15, 2008
 */
public class MessageHeader {
	private Identifier sendingFacility;
	private Identifier sendingApplication;
	private Identifier receivingFacility;
	private Identifier receivingApplication;
	private String messageCode;
	private String triggerEvent;	
	private String messageStructure;
	private Calendar messgeDate;
	
	
	
	/**
	 * Gets sendingFacility.
	 *
	 * @return the sendingFacility
	 */
	public Identifier getSendingFacility() {
		return sendingFacility;
	}

	/**
	 * Sets sendingFacility.
	 *
	 * @param sendingFacility the sendingFacility to set
	 */
	public void setSendingFacility(Identifier sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	/**
	 * Gets sendingApplication.
	 *
	 * @return the sendingApplication
	 */
	public Identifier getSendingApplication() {
		return sendingApplication;
	}

	/**
	 * Sets sendingApplication.
	 *
	 * @param sendingApplication the sendingApplication to set
	 */
	public void setSendingApplication(Identifier sendingApplication) {
		this.sendingApplication = sendingApplication;
	}

	/**
	 * Gets receivingFacility.
	 *
	 * @return the receivingFacility
	 */
	public Identifier getReceivingFacility() {
		return receivingFacility;
	}

	/**
	 * Sets receivingFacility.
	 *
	 * @param receivingFacility the receivingFacility to set
	 */
	public void setReceivingFacility(Identifier receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

	/**
	 * Gets receivingApplication.
	 *
	 * @return the receivingApplication
	 */
	public Identifier getReceivingApplication() {
		return receivingApplication;
	}

	/**
	 * Sets receivingApplication.
	 *
	 * @param receivingApplication the receivingApplication to set
	 */
	public void setReceivingApplication(Identifier receivingApplication) {
		this.receivingApplication = receivingApplication;
	}

	/**
	 * Gets messageCode.
	 *
	 * @return the messageCode
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * Sets messageCode.
	 *
	 * @param messageCode the messageCode to set
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/**
	 * Gets triggerEvent.
	 *
	 * @return the triggerEvent
	 */
	public String getTriggerEvent() {
		return triggerEvent;
	}

	/**
	 * Sets triggerEvent.
	 *
	 * @param triggerEvent the triggerEvent to set
	 */
	public void setTriggerEvent(String triggerEvent) {
		this.triggerEvent = triggerEvent;
	}

	/**
	 * Gets messageStructure.
	 *
	 * @return the messageStructure
	 */
	public String getMessageStructure() {
		return messageStructure;
	}

	/**
	 * Sets messageStructure.
	 *
	 * @param messageStructure the messageStructure to set
	 */
	public void setMessageStructure(String messageStructure) {
		this.messageStructure = messageStructure;
	}

	/**
	 * Gets messgeDate.
	 *
	 * @return the messgeDate
	 */
	public Calendar getMessgeDate() {
		return messgeDate;
	}

	/**
	 * Sets messgeDate.
	 *
	 * @param messgeDate the messgeDate to set
	 */
	public void setMessgeDate(Calendar messgeDate) {
		this.messgeDate = messgeDate;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sendingFacility == null) ? 0 : sendingFacility.hashCode());
		result = prime
				* result
				+ ((sendingApplication == null) ? 0 : sendingApplication
						.hashCode());
		result = prime * result
				+ ((receivingFacility == null) ? 0 : receivingFacility.hashCode());
		result = prime * result
				+ ((receivingApplication == null) ? 0 : receivingApplication.hashCode());
		result = prime * result
				+ ((messageCode == null) ? 0 : messageCode.hashCode());
		result = prime * result
				+ ((triggerEvent == null) ? 0 : triggerEvent.hashCode());
		result = prime * result
		+ ((messageStructure == null) ? 0 : messageStructure.hashCode());
		
		result = prime * result
				+ ((messgeDate == null) ? 0 : messgeDate.hashCode());
		return result;
	}


	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MessageHeader))
			return false;
		final MessageHeader other = (MessageHeader) obj;
		if (sendingFacility == null) {
			if (other.sendingFacility != null)
				return false;
		} else if (!sendingFacility.equals(other.sendingFacility))
			return false;
		if (sendingApplication == null) {
			if (other.sendingApplication != null)
				return false;
		} else if (!sendingApplication.equals(other.sendingApplication))
			return false;
		if (receivingFacility == null) {
			if (other.receivingFacility != null)
				return false;
		} else if (!receivingFacility.equals(other.receivingFacility))
			return false;
		if (receivingApplication == null) {
			if (other.receivingApplication != null)
				return false;
		} else if (!receivingApplication.equals(other.receivingApplication))
			return false;
		if (messageCode == null) {
			if (other.messageCode != null)
				return false;
		} else if (!messageCode.equals(other.messageCode))
			return false;
		if (triggerEvent == null) {
			if (other.triggerEvent != null)
				return false;
		} else if (!triggerEvent.equals(other.triggerEvent))
			return false;	
		if (messageStructure == null) {
			if (other.messageStructure != null)
				return false;
		} else if (!messageStructure.equals(other.messageStructure))
			return false;	
		if (messgeDate == null) {
			if (other.messgeDate != null)
				return false;
		} else if (!messgeDate.equals(other.messgeDate))
			return false;
		return true;
	}
}
