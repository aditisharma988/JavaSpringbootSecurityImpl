package com.security.securityImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class})
public class SecurityImplApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityImplApplication.class, args);
	}

}
