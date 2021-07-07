package com.aws.communication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot main class for AWS communication application
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.aws.communication", "com.aws.communication.controller"})
public class AwsCommunicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsCommunicationApplication.class, args);
	}

}
