package com.aws.communication.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.aws.communication.exception.BadRequestException;
import com.aws.communication.model.CommunicationCharacteristic;
import com.aws.communication.model.EmailRequest;
import com.aws.communication.model.SMSRequest;

/**
 * Validator class to perform validations on the incoming
 * request to the application.
 *
 */
public final class RequestValidator {
	
	private RequestValidator() {
		
	}
	
	/**
	 * Method to validate send email request
	 * 
	 * @param request - Request to be validated
	 * @throws BadRequestException Thrown when mandatory parameter is missing in request
	 */
	public static void validateSendEmailRequest(EmailRequest request) throws BadRequestException {
		validateMandatoryParameter(request.getTemplateName(), Constants.TEMPLATE_NAME);
		if(request.getSender() == null) {
			throw new BadRequestException(StatusCodes.MISSING_MANDATORTY_PARAM.getCode(), 
					StatusCodes.MISSING_MANDATORTY_PARAM.getReason(), Constants.SENDER);
		}
		if(request.getReceiver() == null) {
			throw new BadRequestException(StatusCodes.MISSING_MANDATORTY_PARAM.getCode(), 
					StatusCodes.MISSING_MANDATORTY_PARAM.getReason(), Constants.RECEIVER);
		}
		
		validateMandatoryParameter(request.getSender().getEmailAddress(), Constants
				.SENDER_EMAIL_ADDRESS);
		validateMandatoryParameter(request.getReceiver().getEmailAddress(), Constants
				.RECEIVER_EMAIL_ADDRESS);
		
		if(CollectionUtils.isNotEmpty(request.getCommunicationCharacteristics())) {
			for(CommunicationCharacteristic characteristic : request.getCommunicationCharacteristics()) {
				validateMandatoryParameter(characteristic.getName(), Constants
						.NAME);
				validateMandatoryParameter(characteristic.getValue(), Constants
						.VALUE);
			}
		}
		
	}
	
	
	/**
	 * Method to validate send SMS request
	 * 
	 * @param request - Request to be validated
	 * @throws BadRequestException Thrown when mandatory parameter is missing in request
	 */
	public static void validateSendSMSRequest(SMSRequest request) throws BadRequestException {
		validateMandatoryParameter(request.getMessage(), Constants.MESSAGE);
		validateMandatoryParameter(request.getMessageType(), Constants.MESSAGE_TYPE);
		if(request.getSender() == null) {
			throw new BadRequestException(StatusCodes.MISSING_MANDATORTY_PARAM.getCode(), 
					StatusCodes.MISSING_MANDATORTY_PARAM.getReason(), Constants.SENDER);
		}
		if(request.getReceiver() == null) {
			throw new BadRequestException(StatusCodes.MISSING_MANDATORTY_PARAM.getCode(), 
					StatusCodes.MISSING_MANDATORTY_PARAM.getReason(), Constants.RECEIVER);
		}
		
		validateMandatoryParameter(request.getSender().getSenderId(), Constants
				.SENDER_SENDER_ID);
		validateMandatoryParameter(request.getReceiver().getPhoneNumber(), Constants
				.RECEIVER_PHONE_NUMBER);
		
	}
	
	
	/**
	 * Method to validate mandatory parameters 
	 * 
	 * @param value - Value to be validated
	 * @param parameters - parameters for message
	 * @throws BadRequestException Thrown when mandatory parameter
	 * is missing
	 */
	private static void validateMandatoryParameter(String value, String... parameters) 
			throws BadRequestException {
		if(StringUtils.isBlank(value)) {
			throw new BadRequestException(StatusCodes.MISSING_MANDATORTY_PARAM.getCode(), 
					StatusCodes.MISSING_MANDATORTY_PARAM.getReason(), parameters);
		}
	}

}
