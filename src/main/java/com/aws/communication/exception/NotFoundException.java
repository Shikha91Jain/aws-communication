package com.aws.communication.exception;

/**
 * Exception class for 404 Not Found error scenarios
 *
 */
public class NotFoundException extends APIException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for NotFoundException to instantiate code, reason with possible
	 * parameters
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public NotFoundException(String code, String reason, String... parameters) {
		super(code, reason, parameters);
	}
	
	/**
	 * Constructor for BadRequesNotFoundExceptiontException to instantiate code, reason with possible
	 * parameters and the original exception trace
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param exception - Original Exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public NotFoundException(String code, String reason, Exception exception, String... parameters) {
		super(code, reason, exception, parameters);
	}

}
