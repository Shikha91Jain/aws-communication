package com.aws.communication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.communication.exception.BadRequestException;
import com.aws.communication.exception.NotFoundException;
import com.aws.communication.model.CommunicationResponse;
import com.aws.communication.model.EmailRequest;
import com.aws.communication.model.ErrorResponse;
import com.aws.communication.model.SMSRequest;
import com.aws.communication.service.AwsCommunicationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * Controller class for AWS Communication Related resources
 *
 */

@RestController
@Validated
@Api(value = "aws-communication")
@RequestMapping(value = "/aws-communication/v1")
public class AwsCommunicationApiController {

    @Autowired
    private AwsCommunicationService service;

    @ApiOperation(value = "API to send email for given request using AWS SES", nickname = "sendEmail", 
    		notes = "API to send email for given request using AWS SES", response = CommunicationResponse.class, tags={ "AWSCommunication", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success response", response = CommunicationResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server encountered unexpected condition", response = ErrorResponse.class) })
    @PostMapping(value = "/email/send",
    	consumes = { "application/json; charset=UTF-8" },
        produces = { "application/json; charset=UTF-8" })
    public ResponseEntity<CommunicationResponse> sendEmail(@ApiParam(value = "Request payload for sending email via AWS SES",required=true) 
    @Valid @RequestBody EmailRequest request) throws NotFoundException, BadRequestException {
    	return ResponseEntity.ok(service.sendEmail(request));
    }

    @ApiOperation(value = "API to send SMS for given request using AWS SNS", nickname = "sendSMS", 
    		notes = "API to send SMS for given request using AWS SNS", response = CommunicationResponse.class, tags={ "AWSCommunication", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success response", response = CommunicationResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server encountered unexpected condition", response = ErrorResponse.class) })
    @PostMapping(value = "/sms/send",
    	consumes = { "application/json; charset=UTF-8" },
        produces = { "application/json; charset=UTF-8" })
    public ResponseEntity<CommunicationResponse> sendSMS(@ApiParam(value = "Request payload for sending email via AWS SES",required=true) 
    @Valid @RequestBody SMSRequest request) throws BadRequestException {
    	return ResponseEntity.ok(service.sendSMS(request));
    }

}
