package com.dangdangtrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DangdangtripApplication {

	public static void main(String[] args) {
		SpringApplication.run(DangdangtripApplication.class, args);
	}
}