package it.client.rest.fabricktest.configuration;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.client.rest.fabricktest.error.RestTemplateResponseErrorHandler;
import it.client.rest.fabricktest.loginterceptor.RequestResponseLoggingInterceptor;
import it.client.rest.fabricktest.service.AccountServiceImpl;


/**
 * 
 * ConfigurationClass
 *  
 */
@Configuration
public class ConfigurationClass {

	@Bean
	RestTemplate getRestTemplate() {
		 final Logger log = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);
		 if (log.isDebugEnabled()) {
			 /*
			  *  Wrapper for a ClientHttpRequestFactory that buffers all outgoing and incoming streams in memory.
			  *  Using this wrapper allows for multiple reads of the response body.
			  */
			 ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
			 RestTemplate restTemplate = new RestTemplate(factory);
			 restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
			 restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
			 return restTemplate;
	    }
		
		return new RestTemplateBuilder()
				.errorHandler(new RestTemplateResponseErrorHandler())
				.build();
	}
	
	@Bean
	public AccountServiceImpl accountService() {
		return new AccountServiceImpl();
	}
	
	
}
