package com.security.securityImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = "com.security.securityImpl")
@EnableJpaAuditing
public class SecurityImplApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityImplApplication.class, args);
	}

}
