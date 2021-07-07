package com.aws.communication.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.aws.communication.exception.BadRequestException;
import com.aws.communication.utils.Constants;
import com.aws.communication.utils.StatusCodes;


/**
 * The Intercepter class which will have all validation logic for mandatory
 * request parameter
 *
 */
@Component
@SuppressWarnings("rawtypes")
public class ValidationInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationInterceptor.class);

	/**
	 * Method to validate the incoming request. This method will check the method
	 * being invoked in the Rest controller and validate if all the required
	 * parameters of the method are being provided in the request.
	 *
	 * If the required parameters for a given method are not present in the request,
	 * then it will throw proper Bad Request exception on missing mandatory
	 * parameters.
	 *
	 * This method will only validate path, query and header parameters coming in
	 * the request.
	 *
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			LOGGER.debug("handler instanceof HandlerMethod request {}", request);
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			Method method = handlerMethod.getMethod();

			for (Parameter parameter : method.getParameters()) {
				Map<String, Object> parameterDetails = retrieveParameterAttributes(parameter, request);
				mandatoryParameterCheck(parameterDetails);
			}
		}
		return true;
	}

	/**
	 * Method to check mandatory parameter value
	 *
	 * @param parameterDetails - parameterDetails
	 * @throws BadRequestException - Exception caught in validation
	 */
	private void mandatoryParameterCheck(Map<String, Object> parameterDetails) throws BadRequestException {

		String parameterName = (String) parameterDetails.get(Constants.NAME);

		// Check if the given request parameter is required and if its required then
		// validate that the value for the parameter is present in the request.
		// If not provided, then throw Bad request exception for missing mandatory parameter
		if ((parameterDetails.get(Constants.REQUIRED) != null) && Boolean.TRUE.equals(parameterDetails
				.get(Constants.REQUIRED))) {

			Object parameterValue = parameterDetails.get(Constants.VALUE);

			if (this.isNullOrEmpty(parameterValue)) {

				throw new BadRequestException(StatusCodes.MISSING_MANDATORTY_PARAM.getCode(),
						StatusCodes.MISSING_MANDATORTY_PARAM.getReason(), parameterName);
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// over riding parent method
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// over riding parent method
	}

	/**
	 * Method to retrieve parameter details such as parameter name, parameter is
	 * required and its value based on the parameter annotations and the request
	 *
	 * @param parameter - parameter value
	 * @param request   - request coming to API
	 * @return Map<String, Object>
	 */
	private Map<String, Object> retrieveParameterAttributes(Parameter parameter, HttpServletRequest request) {

		Map<String, Object> parameterDetails = new HashMap<>();
		Annotation[] annotations = parameter.getAnnotations();

		for (Annotation ann : annotations) {

			if (ann.annotationType().equals(RequestHeader.class)) {

				RequestHeader dv = (RequestHeader) ann;
				parameterDetails.put(Constants.REQUIRED, dv.required());
				parameterDetails.put(Constants.NAME, dv.value());
				parameterDetails.put(Constants.VALUE, request.getHeader(dv.value()));
			}
			if (ann.annotationType().equals(RequestParam.class)) {

				RequestParam dv = (RequestParam) ann;
				parameterDetails.put(Constants.REQUIRED, dv.required());
				parameterDetails.put(Constants.NAME, dv.value());
				parameterDetails.put(Constants.VALUE, request.getParameter(dv.value()));
			}
			if (ann.annotationType().equals(PathVariable.class)) {
				PathVariable dv = (PathVariable) ann;
				parameterDetails.put(Constants.REQUIRED, dv.required());
				parameterDetails.put(Constants.NAME, dv.value());

				Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
				parameterDetails.put(Constants.VALUE, pathVariables.get(dv.value()));
			}
		}
		return parameterDetails;
	}

	/**
	 * Method to validate if the given object is not null or empty
	 *
	 * If the object is instance of string, then it will check if the string is not
	 * blank. If object is instance of collection, then it will check if the
	 * collection is null or empty.
	 *
	 * @param object - object to be passed
	 * @return boolean
	 */
	private boolean isNullOrEmpty(Object object) {

		if (object == null) {
			return true;
		}
		if (object instanceof String) {
			return StringUtils.isBlank((String) object);
		}
		if (object instanceof Collection<?>) {
			return CollectionUtils.isEmpty((Collection<?>) object);
		}
		return false;
	}

}
