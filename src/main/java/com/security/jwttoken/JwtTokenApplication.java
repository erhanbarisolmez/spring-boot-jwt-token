package com.security.jwttoken;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.security.jwttoken.dto.CreateUserRequest;
import com.security.jwttoken.model.Role;
import com.security.jwttoken.services.UserService;

@SpringBootApplication
public class JwtTokenApplication implements CommandLineRunner{

	private final UserService userService;

	public JwtTokenApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(JwtTokenApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// createDummyData();
	}

	private void createDummyData(){


    CreateUserRequest request = CreateUserRequest.builder()
			.name("Barış")
			.username("baris")
			.password("pass")
			.authorities(Set.of(Role.ROLE_USER))
		.build();
		
		userService.createUser(request);
		
		CreateUserRequest request2 = CreateUserRequest.builder()
			.name("Erhan")
			.username("erhan")
			.password("pass")
			.authorities(Set.of(Role.ROLE_ADMIN))
		.build();

		userService.createUser(request2);

		CreateUserRequest request3 = CreateUserRequest.builder()
			.name("ölmez")
			.username("olmez")
			.password("pass")
			.authorities(Set.of(Role.ROLE_SADMIN))
		.build();

		userService.createUser(request3);

	}

	

}
