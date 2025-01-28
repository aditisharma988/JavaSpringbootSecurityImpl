package com.security.securityImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.security.securityImpl")
public class SecurityImplApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityImplApplication.class, args);
	}

}
