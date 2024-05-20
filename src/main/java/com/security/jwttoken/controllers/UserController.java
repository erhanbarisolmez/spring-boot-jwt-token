package com.security.jwttoken.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwttoken.dto.AuthRequest;
import com.security.jwttoken.dto.CreateUserRequest;
import com.security.jwttoken.model.User;
import com.security.jwttoken.services.JWTService;
import com.security.jwttoken.services.UserService;

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

  @GetMapping("/welcome")
  public String welcome(){
    return "Welcome to the application";

  }
  @PostMapping("/addNewUser/{request}")
  public ResponseEntity<User> addUser(CreateUserRequest request) {
    User newUser = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @PostMapping("/generateToken/")
  public String generateToken(AuthRequest request){
    log.info("Received authentication request for user: {}", request.username());
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.password(), request.username()));
    if (authentication.isAuthenticated()){
      log.info("JWT Token generated successfully for user: {}"+ request.username());
      return jwtService.generateToken(request.username());
    }
    log.error("*************invalid username***********" + request.username());
    throw new UsernameNotFoundException("invalid username {}" + request.username());
  }
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable Long id){
    userService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
  }

  @GetMapping("/all_user")
  public List<User> getAllUser(){
    return userService.findAll();
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
