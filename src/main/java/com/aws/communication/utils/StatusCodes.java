package com.aws.communication.utils;

/**
 * Class containing the error codes and reasons that can
 * be returned by the application
 *
 */
public enum StatusCodes {
	
	// HTTP status 400 related status codes
	MISSING_MANDATORTY_PARAM("400001","Missing mandatory parameter: {0}"),
	INVALID_VALUE_FOR_PARAM("400002","Invalid value for parmater: {0}"),
	
	// JSON Request body validation related status codes
	INVALID_JSON_REQUEST("400003","Invalid JSON Request"),
	INVALID_DATA_FORMAT_JSON("400004","Invalid data format provided in JSON request for {0}"),
	UNABLE_TO_READ_REQUEST_BODY("400005","Unable to parse the request body"),
	
	// HTTP status 404 related status codes
	DATA_NOT_FOUND("404001","{0} not found for given input: {1}"),
	
	// HTTP status 500 related status codes
	INTERNAL_SERVER_ERROR("500001","Service encountered an unexpected condition");
	
	private String code;
	private String reason;
	
	/**
	 * @param code
	 * @param reason
	 */
	private StatusCodes(String code, String reason) {
		this.code = code;
		this.reason = reason;
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

}
