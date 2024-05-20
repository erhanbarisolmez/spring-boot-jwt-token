package com.security.jwttoken;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JwtTokenApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(JwtTokenApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

}
