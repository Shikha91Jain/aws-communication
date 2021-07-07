package com.aws.communication.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SendEmailRequest
 */
@Validated
public class EmailRequest {
	
	 @JsonProperty("templateName")
	 private String templateName = null;
	 
	 @JsonProperty("communicationCharacteristics")
	 private List<CommunicationCharacteristic> communicationCharacteristics = null;
	 
	 @JsonProperty("sender")
	 private Sender sender = null;
	 
	 @JsonProperty("receiver")
	 private Receiver receiver = null;

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the communicationCharacteristics
	 */
	public List<CommunicationCharacteristic> getCommunicationCharacteristics() {
		if(communicationCharacteristics == null) {
			communicationCharacteristics = new ArrayList<>();
		}
		return communicationCharacteristics;
	}

	/**
	 * @param communicationCharacteristics the communicationCharacteristics to set
	 */
	public void setCommunicationCharacteristics(List<CommunicationCharacteristic> communicationCharacteristics) {
		this.communicationCharacteristics = communicationCharacteristics;
	}

	/**
	 * @return the sender
	 */
	public Sender getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(Sender sender) {
		this.sender = sender;
	}

	/**
	 * @return the receiver
	 */
	public Receiver getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

}
