package com.epam.aditechtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AdiTechTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdiTechTaskApplication.class, args);
	}

}
