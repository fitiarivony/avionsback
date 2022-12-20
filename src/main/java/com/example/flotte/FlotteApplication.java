package com.example.flotte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins="*",allowedHeaders="*")
public class FlotteApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlotteApplication.class, args);
		
	}

}
