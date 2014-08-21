/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
package org.openhealthtools.openpixpdq.api;

import java.util.Date;

/**
 * The data container for message persistence.
 * 
 * @author Anil kumar
 * @date Nov 25, 2008
 */
public class MessageStore {

	private String id;
	private String ip;
	private String outMessage;
	private String inMessage;
	private Date messageDate;
	private String messageId;
	private String errorMessage;
	private String messageCode;
	private String triggerEvent;	
	private String messageStructure;
	private String sendingFacility;
	private String sendingApplication;
	private String receivingFacility;
	private String receivingApplication;

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getTriggerEvent() {
		return triggerEvent;
	}

	public void setTriggerEvent(String triggerEvent) {
		this.triggerEvent = triggerEvent;
	}

	public String getMessageStructure() {
		return messageStructure;
	}

	public void setMessageStructure(String messageStructure) {
		this.messageStructure = messageStructure;
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 
	 * @return
	 */
	public String getOutMessage() {
		return outMessage;
	}

	/**
	 * 
	 * @param outMessage
	 */
	public void setOutMessage(String outMessage) {
		this.outMessage = outMessage;
	}

	/**
	 * 
	 * @return
	 */
	public String getInMessage() {
		return inMessage;
	}

	/**
	 * 
	 * @param inMessage
	 */
	public void setInMessage(String inMessage) {
		this.inMessage = inMessage;
	}

	/**
	 * 
	 * @return
	 */
	public Date getMessageDate() {
		return messageDate;
	}

	/**
	 * Set the value in SimpleDateFormat.
	 * 
	 * @param messageDate
	 */
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	/**
	 * 
	 * @return messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * 
	 * @param messageId
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * 
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * 
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * 
	 * @return sendingFacility
	 */
	public String getSendingFacility() {
		return sendingFacility;
	}

	/**
	 * 
	 * @param sendingFacility
	 */
	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	/**
	 * 
	 * @return sendingApplication
	 */
	public String getSendingApplication() {
		return sendingApplication;
	}

	/**
	 * 
	 * @param sendingApplication
	 */
	public void setSendingApplication(String sendingApplication) {
		this.sendingApplication = sendingApplication;
	}

	/**
	 * 
	 * @return receivingFacility
	 */
	public String getReceivingFacility() {
		return receivingFacility;
	}

	/**
	 * 
	 * @param receivingFacility
	 */
	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

	/**
	 * 
	 * @return receivingApplication
	 */
	public String getReceivingApplication() {
		return receivingApplication;
	}

	/**
	 * 
	 * @param receivingApplication
	 */
	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}
}
