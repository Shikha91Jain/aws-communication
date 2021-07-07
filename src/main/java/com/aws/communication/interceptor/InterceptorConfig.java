package com.aws.communication.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to configure the interceptor for
 * the application
 *
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	ValidationInterceptor validationInterceptor;
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// add ValidationInterceptor in the registry
		registry.addInterceptor(validationInterceptor);
	}
	
}
