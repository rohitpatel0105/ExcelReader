package com.edd.fileio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ExcelReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelReaderApplication.class, args);
	}
	
}
