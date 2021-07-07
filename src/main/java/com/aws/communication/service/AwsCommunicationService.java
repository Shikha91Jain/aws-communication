package com.aws.communication.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailResult;
import com.amazonaws.services.simpleemail.model.TemplateDoesNotExistException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.aws.communication.exception.BadRequestException;
import com.aws.communication.exception.NotFoundException;
import com.aws.communication.model.CommunicationCharacteristic;
import com.aws.communication.model.CommunicationResponse;
import com.aws.communication.model.EmailRequest;
import com.aws.communication.model.SMSRequest;
import com.aws.communication.utils.Constants;
import com.aws.communication.utils.RequestValidator;
import com.aws.communication.utils.StatusCodes;
import com.google.gson.Gson;

/**
 * Service class to handle the AWS communication related operations
 *
 */
@Service
public class AwsCommunicationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AwsCommunicationService.class);
	
	@Autowired
	private AmazonSimpleEmailService simpleEmailService;
	
	@Autowired
	private AmazonSNS snsClient;
	
	public CommunicationResponse sendEmail(EmailRequest request) throws NotFoundException, BadRequestException {
		
		LOGGER.debug("In sendEmail method");
		RequestValidator.validateSendEmailRequest(request);
		
		try {
			
			SendTemplatedEmailRequest emailRequest = new SendTemplatedEmailRequest()
					.withDestination(new Destination().withToAddresses(request.getReceiver().getEmailAddress()))
					.withSource(request.getSender().getEmailAddress())
					.withTemplate(request.getTemplateName())
					.withTemplateData(mapTemplateData(request.getCommunicationCharacteristics()));
			SendTemplatedEmailResult result = simpleEmailService.sendTemplatedEmail(emailRequest);
			
			CommunicationResponse response = new CommunicationResponse();
			response.setMessageId(result.getMessageId());
			
			return response;
		} catch(TemplateDoesNotExistException e) {
			throw new NotFoundException(StatusCodes.DATA_NOT_FOUND.getCode(), 
					StatusCodes.DATA_NOT_FOUND.getReason(), Constants.TEMPLATE, request.getTemplateName());
		}
		
	}
	
	/**
	 * Method to map the template data provided for the 
	 * request
	 * 
	 * @param characterisitics - List of template placeholders
	 * @return String - JSON payload containing the template
	 * data
	 */
	private String mapTemplateData(List<CommunicationCharacteristic> characterisitics) {
		Map<String, String> templateDataMap = new HashMap<>();
		for(CommunicationCharacteristic characteristic : characterisitics) {
			templateDataMap.put(characteristic.getName(), characteristic.getValue());
		}
		Gson gson = new Gson();
		return gson.toJson(templateDataMap);
	}
	
	/**
	 * Method to send SMS for given request
	 * 
	 * @param request - SMS Request
	 * @return CommunicationResponse - response
	 * @throws BadRequestException 
	 */
	public CommunicationResponse sendSMS(SMSRequest request) throws BadRequestException {
		
		RequestValidator.validateSendSMSRequest(request);
		
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessage(request.getMessage());
		publishRequest.setMessageAttributes(mapSMSAttributes(request));
		publishRequest.setPhoneNumber(request.getReceiver().getPhoneNumber());
		
		PublishResult result = snsClient.publish(publishRequest);
		
		CommunicationResponse response = new CommunicationResponse();
		response.setMessageId(result.getMessageId());
		
		return response;
	}
	
	/**
	 * Method to map SMS attributes
	 * 
	 * @param request - SMS Request
	 * @return Map<String, MessageAttributeValue> - SMS Attributes
	 */
	private Map<String, MessageAttributeValue> mapSMSAttributes(SMSRequest request) {
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
		
		smsAttributes.put(Constants.AWS_SNS_SMS_SENDERID, create(request.getSender().getSenderId(), 
				Constants.STRING));
		smsAttributes.put(Constants.AWS_SNS_SMS_TYPE, create(request.getMessageType(), 
				Constants.STRING));
		
		return smsAttributes;
	}
	
	/**
	 * Method to create Message Attribute Value object
	 * 
	 * @param value - Value to be configured
	 * @param dataType - Data type of value
	 * @return MessageAttributeValue - Created object
	 */
	private MessageAttributeValue create(String value, String dataType) {
		return new MessageAttributeValue()
				.withStringValue(value)
				.withDataType(dataType);
		
	}

}
