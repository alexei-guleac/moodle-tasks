package com.example.jasper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DbApplication {

	public static void main(String[] args) {
//		SpringApplication.run(DbApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DbApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}


}
