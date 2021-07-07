package com.aws.communication.exception;

/**
 * Exception class for 400 Bad Request error scenarios
 *
 */
public class BadRequestException extends APIException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for BadRequestException to instantiate code, reason with possible
	 * parameters
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public BadRequestException(String code, String reason, String... parameters) {
		super(code, reason, parameters);
	}
	
	/**
	 * Constructor for BadRequestException to instantiate code, reason with possible
	 * parameters and the original exception trace
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param exception - Original Exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public BadRequestException(String code, String reason, Exception exception, String... parameters) {
		super(code, reason, exception, parameters);
	}

}
