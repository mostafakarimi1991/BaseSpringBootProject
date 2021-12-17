package com.BaseSpringBootProject;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.BaseSpringBootProject.*"})

/*
This annotation scan all classes in package and find classes with restController , controller
and configuration annotations
*/
public class BaseSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseSpringBootApplication.class, args);
	}

}
