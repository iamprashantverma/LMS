package com.projects.lms_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LmsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsServerApplication.class, args);
//		 finally i  added this into the git
		System.out.println("Application is Running ");
	}

}
