package com.aws.communication.exception;

/**
 * Exception class for 500 Internal Server error scenarios
 *
 */
public class InternalServerException extends APIException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for InternalServerException to instantiate code, reason with possible
	 * parameters
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public InternalServerException(String code, String reason, String... parameters) {
		super(code, reason, parameters);
	}
	
	/**
	 * Constructor for InternalServerException to instantiate code, reason with possible
	 * parameters and the original exception trace
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param exception - Original Exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public InternalServerException(String code, String reason, Exception exception, String... parameters) {
		super(code, reason, exception, parameters);
	}

}
