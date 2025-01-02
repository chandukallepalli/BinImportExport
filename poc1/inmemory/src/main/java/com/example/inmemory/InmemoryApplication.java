package com.example.inmemory;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.example.inmemory")

public class InmemoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InmemoryApplication.class, args);
	}

}
