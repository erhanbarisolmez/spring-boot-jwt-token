package com.security.jwttoken.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwttoken.dto.AuthRequest;
import com.security.jwttoken.dto.CreateUserRequest;
import com.security.jwttoken.model.User;
import com.security.jwttoken.services.JWTService;
import com.security.jwttoken.services.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

  private final UserService userService;
  private final JWTService jwtService;
  private final AuthenticationManager authenticationManager;

  public UserController(UserService userService, JWTService jwtService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/addNewUser")
  public User addUser(@RequestBody CreateUserRequest request) {
    return userService.createUser(request);
  }

  @PostMapping("/generateToken")
  public String generateToken(@RequestBody AuthRequest authRequest){
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
    if (authentication.isAuthenticated()){
      return jwtService.generateToken(authRequest.username());
    }
    log.info("invalid username" + authRequest.username());
    throw new UsernameNotFoundException("invalid username {}" + authRequest.username());
  }

  @GetMapping("/user")
  public String getUserString() {
    return "Hello User";
  }

  @GetMapping("/admin")
  public String getAdminString() {
    return "Hello Admin";
  }

}
