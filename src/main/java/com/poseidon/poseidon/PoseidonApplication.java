package com.poseidon.poseidon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class PoseidonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoseidonApplication.class, args);
	}

}
