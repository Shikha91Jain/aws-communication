package com.aws.communication.utils;

import java.text.MessageFormat;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Utility class containing the utility methods
 * to be used by application
 *
 */
public final class GenericUtils {
	
	private GenericUtils() {
		super();
	}
	
	/**
	 * Method to format the error message with the parameters
	 * provided in the input
	 * 
	 * @param reason - Error message that needs to be formatted
	 * @param parameters - Array of parameters that needs to be
	 * replaced in the error message
	 * @return String - Formatted string
	 */
	public static String formatErrorMessage(String reason, String... parameters) {
		
		String errorMessage = reason;
		
		if(ArrayUtils.isNotEmpty(parameters)) {
			errorMessage = MessageFormat.format(reason, parameters);
		}
		
		return errorMessage;
	}

}
