package com.aws.communication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

/**
 * Configuration for AWS settings
 *
 */
@Configuration
public class AWSConfiguration {
	
	@Value("${ses.region}")
	private String sesRegion;
	
	@Value("${ses.access.key}")
	private String sesAccessKey;
	
	@Value("${ses.secret.key}")
	private String sesSecretKey;
	
	@Value("${sns.region}")
	private String snsRegion;
	
	@Value("${sns.access.key}")
	private String snsAccessKey;
	
	@Value("${sns.secret.key}")
	private String snsSecretKey;
	
	
	/**
	 * Method to create AmazonSimpleEmailService bean
	 * 
	 * @return AmazonSimpleEmailService
	 */
	@Bean
	public AmazonSimpleEmailService simpleEmailService() {
		return AmazonSimpleEmailServiceClientBuilder
				.standard()
				.withRegion(sesRegion)
				.withCredentials(new AWSStaticCredentialsProvider(sesBasicAWSCredentials()))
				.build();
	}
	
	/**
	 * Create bean of BasicAWSCredentials for AWS SES
	 * interactions
	 * 
	 * @return BasicAWSCredentials
	 */
	@Bean(name = "sesBasicAWSCredentials")
	public BasicAWSCredentials sesBasicAWSCredentials() {
		return new BasicAWSCredentials(sesAccessKey, sesSecretKey);
	}
	
	/**
	 * Create bean of BasicAWSCredentials for AWS SNS
	 * communications
	 * 
	 * @return BasicAWSCredentials
	 */
	@Bean(name = "snsBasicAWSCredentials")
	public BasicAWSCredentials snsBasicAWSCredentials() {
		return new BasicAWSCredentials(snsAccessKey, snsSecretKey);
	}
	
	/**
	 * Method to create AmazonSNS bean
	 * 
	 * @return AmazonSNS
	 */
	@Bean
	public AmazonSNS snsClient() {
		return AmazonSNSClientBuilder
				.standard()
				.withRegion(snsRegion)
				.withCredentials(new AWSStaticCredentialsProvider(snsBasicAWSCredentials()))
				.build();
	}
	

}
