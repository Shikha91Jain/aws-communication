package com.aws.communication.exception;

/**
 * Base API Exception class
 *
 */
public class APIException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final String code;
	private final String reason;
	private final String parameters[];
	
	/**
	 * Constructor for APIException to instantiate code, reason with possible
	 * parameters
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public APIException(String code, String reason, String... parameters) {
		this.code = code;
		this.reason = reason;
		this.parameters = parameters;
	}
	
	/**
	 * Constructor for APIException to instantiate code, reason with possible
	 * parameters and the original exception trace
	 * 
	 * @param code - Code of the exception
	 * @param reason - Reason of the exception
	 * @param exception - Original Exception
	 * @param parameters - Array of parameters that would be required
	 * to map the reason with dynamic values
	 */
	public APIException(String code, String reason, Exception exception, String... parameters) {
		super(exception);
		this.code = code;
		this.reason = reason;
		this.parameters = parameters;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @return the parameters
	 */
	public String[] getParameters() {
		return parameters;
	}
}
