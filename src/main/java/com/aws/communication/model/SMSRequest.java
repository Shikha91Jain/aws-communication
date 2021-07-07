package com.aws.communication.model;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SendSMSRequest
 */
@Validated
public class SMSRequest {

	@JsonProperty("message")
	private String message = null;
	
	@JsonProperty("messageType")
	private String messageType = null;

	@JsonProperty("sender")
	private Sender sender = null;

	@JsonProperty("receiver")
	private Receiver receiver = null;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
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
