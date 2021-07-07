package com.aws.communication.exception.advice;

import java.util.Iterator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.aws.communication.exception.BadRequestException;
import com.aws.communication.exception.InternalServerException;
import com.aws.communication.exception.NotFoundException;
import com.aws.communication.model.ErrorResponse;
import com.aws.communication.utils.Constants;
import com.aws.communication.utils.GenericUtils;
import com.aws.communication.utils.StatusCodes;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice("com.aws.communication")
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public class ExceptionHandlerAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
	
	/**
	 * Method to handle Bad Request Exception raised for an API. Method will form
	 * the error response body for the exception raised and return the response with
	 * HTTP 400 status.
	 *
	 *
	 * @param exception - BadRequestException object that needs to be 
	 * handled.
	 * @return ResponseEntity<ErrorResponse> - Response entity being returned
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapErrorResponse(exception.getCode(), 
				exception.getReason(), exception.getParameters()));
	}
	
	/**
	 * Method to handle Not Found Exception raised for an API. Method will form
	 * the error response body for the exception raised and return the response
	 * with HTTP 404 status.
	 *
	 *
	 * @param exception - NotFoundException object that needs to be handled.
	 * @return ResponseEntity<ErrorResponse> - Response entity being returned
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapErrorResponse(exception.getCode(), 
				exception.getReason(), exception.getParameters()));
	}
	
	/**
	 * Method to handle Internal Server Error Exception raised for an API.
	 * Method will form the error response body for the exception raised and
	 * return the response with HTTP 500 status.
	 *
	 *
	 * @param exception - InternalServerErrorException object that needs to be
	 * handled.
	 * @return ResponseEntity<ErrorResponse> - Response entity being returned
	 */
	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<ErrorResponse> handleInternalServerExceptionn(InternalServerException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapErrorResponse(exception.getCode(), 
				exception.getReason(), exception.getParameters()));
	}
	
	/**
	 * Method to handle HttpMessageNotReadableException.
	 * HttpMessageNotReadableException will be thrown when the incoming
	 * request body is not readable. 
	 * 
	 * It can be due to invalid JSON being
	 * provided in request, or the data type mismatch or invalid values
	 * provided for sub types etc.
	 * 
	 * Thus this method will check the cause of the 
	 * HttpMessageNotReadableException and return appropriate error
	 * response indicating the issue with the request body.
	 * 
	 * @param exception - HttpMessageNotReadableException object that needs
	 * to be handled
	 * @return ResponseEntity<ErrorResponse> - Returned Response Entity object
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		LOGGER.error("handleHttpMessageNotReadableException : ", exception);
		ErrorResponse response = null;
		
		if(exception.getCause() instanceof InvalidFormatException) {
			response = this.handleInvalidFormatException((InvalidFormatException)exception.getCause());
		} else if(exception.getCause() instanceof JsonParseException) {
			response = this.handleJsonParseException();
		} else if(exception.getCause() instanceof JsonMappingException) {
			response = this.handleJsonMappingException((JsonMappingException)exception.getCause());
		} else {
			response = this.mapErrorResponse(StatusCodes.UNABLE_TO_READ_REQUEST_BODY.getCode(), 
					StatusCodes.UNABLE_TO_READ_REQUEST_BODY.getReason());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	/**
	 * Method to handle JsonParseException for the incoming
	 * request body.
	 * This method will be invoked if application is not able to parse
	 * the input JSON.
	 * 
	 * This will form the JSON response body indicating 
	 * its invalid JSON
	 * 
	 * @return ErrorResponse - Response object that will be returned for 
	 * JsonParse Exception.
	 */
	private ErrorResponse handleJsonParseException() {
		return this.mapErrorResponse(StatusCodes.INVALID_JSON_REQUEST.getCode(), 
				StatusCodes.INVALID_JSON_REQUEST.getReason());
		
	}
	
	/**
	 * Method to handle InvalidFormatException for the incoming request
	 * body.
	 * This method will be invoked when the data provided in JSON is
	 * in bad format to map.
	 * 
	 * This will form the JSON response body indicating 
	 * that the provided value is in bad format. Also it will indicate
	 * the field in JSON causing the issue as well.
	 * 
	 * @param exception - InvalidFormatException object which needs to be handled
	 * @return ErrorResponse - Response object that will be returned for 
	 * InvalidFormatException
	 */
	private ErrorResponse handleInvalidFormatException(InvalidFormatException exception) {
		return this.mapErrorResponse(StatusCodes.INVALID_DATA_FORMAT_JSON.getCode(), 
				StatusCodes.INVALID_DATA_FORMAT_JSON.getReason(), 
				this.extractJsonPathReference(exception));
	}
	
	/**
	 * Method to handle JsonMappingException for the incoming request
	 * body.
	 * This method will be invoked when the application in not able to map
	 * the incoming JSON request to the POJO classes.
	 * 
	 * This will for the JSON response body indicating that 
	 * its unable to map the JSON and will indicate the field
	 * in JSON as well for which the issue is happening.
	 * 
	 * @param exception - JsonMappingException object which needs to be handled
	 * @return ErrorResponse - Response object that will be returned for
	 * JsonMappingException
	 */
	private ErrorResponse handleJsonMappingException(JsonMappingException exception) {
		if(exception.getCause() instanceof JsonParseException) {
			// check if the cause of JsonMappingException is JsonParseException then
			// return Invalid JSON request response
			return this.handleJsonParseException();
		} else {
			return this.mapErrorResponse(StatusCodes.INVALID_DATA_FORMAT_JSON.getCode(), 
					StatusCodes.INVALID_DATA_FORMAT_JSON.getReason(), 
					this.extractJsonPathReference(exception));
		}
	}
	
	/**
	 * Method to extract the location or reference in JSON request
	 * where the issue was observed in mapping the data.
	 * 
	 * This will extract the path reference linked to the exception class
	 * which will indicate the path in JSON which has the issue.
	 * 
	 * This method will first check if the path reference linked to the exception
	 * is not null/empty. If its null/empty, then it will return empty string.
	 * If path reference is not null/empty, then it will iterate the
	 * path reference list and form the path and return in output.
	 * 
	 * @param exception - JSON Mapping Exception object from which the path
	 * reference needs to be extracted
	 * @return String - Extracted path reference
	 */
	private String extractJsonPathReference(JsonMappingException exception) {
		final StringBuilder sb = new StringBuilder();
		
		
		// check if the path reference is not null/empty
		if(CollectionUtils.isNotEmpty(exception.getPath())) {
			final Iterator<JsonMappingException.Reference> it = exception.getPath().iterator();
			
			while(it.hasNext()) {
				Reference reference = it.next();
				
				if(reference != null) {
					
					// If field name is null, that can indicate that its a element in the JSON Array.
					// Thus retrieving the index of the field causing the issue.
					if(StringUtils.isBlank(reference.getFieldName())) {
						sb.append(Constants.OPENING_SQUARE_BRACKET).append(reference.getIndex())
						.append(Constants.CLOSING_SQUARE_BRACKET);
					} else {
						// appending the field name
						sb.append(reference.getFieldName());
					}
					
					if(it.hasNext()) {
						// If there are more elements in the path reference, then place
						// dot after appending the field name.
						sb.append(Constants.DOT);
					}
				}
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Method to handle MethodArgumentTypeMismatchException  that occurs in
	 * application
	 * 
	 * @param exception - Exception object that needs to be handled
	 * @return ResponseEntity<ErrorResponse> - Returned response entity
	 * for the exception
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapErrorResponse(StatusCodes.INVALID_VALUE_FOR_PARAM
				.getCode(), StatusCodes.INVALID_VALUE_FOR_PARAM.getReason(), exception.getName()));
	}
	
	/**
	 * Method to handle generic exception that occurs in
	 * the application
	 * 
	 * @param exception - Exception object that needs to be handled
	 * @return ResponseEntity<ErrorResponse> - Returned response entity
	 * for the exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		LOGGER.error("Exception in handleException : ", exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapErrorResponse(StatusCodes.INTERNAL_SERVER_ERROR
				.getCode(), StatusCodes.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage()));
	}
	
	
	/**
	 * Method to map error response with the given code, reason,
	 * and parameters
	 * 
	 * @param code - Code that needs to be set in response
	 * @param reason - Reason that needs to be set in response
	 * @param parameters - Array of parameters that needs to be
	 * used to format the error reason
	 * @return ErrorResponse - Mapped object
	 */
	private ErrorResponse mapErrorResponse(String code, String reason, String... parameters) {
		ErrorResponse response = new ErrorResponse();
		response.setCode(code);
		response.setReason(GenericUtils.formatErrorMessage(reason, parameters));
		return response;
	}

}
