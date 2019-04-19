package com.paazl;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class SpringWebservicesTestCaseClientApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringWebservicesTestCaseClientApplication.class)
			.headless(false)
			.web(false)
			.run(args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
