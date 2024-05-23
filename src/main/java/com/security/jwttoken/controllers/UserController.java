package com.security.jwttoken.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwttoken.dto.AuthRequest;
import com.security.jwttoken.dto.CreateUserRequest;
import com.security.jwttoken.model.User;
import com.security.jwttoken.services.JWTService;
import com.security.jwttoken.services.UserService;

import io.swagger.v3.oas.annotations.Operation;


// import lombok.extern.slf4j.Slf4j; // log

@RestController
@RequestMapping("/auth")

public class UserController {

  private final UserService userService;
  private final JWTService jwtService;
  private final AuthenticationManager authenticationManager;

  public UserController(UserService userService, JWTService jwtService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }


  @GetMapping("/all_user")
  @Operation(summary = "Read User All", tags = "auth", description = "Logged in reader")
  public List<User> getAllUser() {
    return userService.findAll();
  }


  @PostMapping("/create_user/{user}")
  @Operation(summary = "Create User", tags = "auth")
  public ResponseEntity<User> addUser(CreateUserRequest request) {
    User newUser = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @PostMapping("/token")
  @Operation(summary = "Login For Access Token", tags="auth")
  public ResponseEntity<String> generateToken(@RequestBody AuthRequest request) {
      try {
          Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.username(), request.password()));
          if (authentication.isAuthenticated()) {
              String token = jwtService.generateToken(authentication.getName());
              return ResponseEntity.ok(token);
          } else {
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
          }
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while generating token");
      }
  }

  
  @DeleteMapping("/delete/{id}")
  @Operation(summary = "Delete User", description = "Only super admin can delete",tags = "auth")
  public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
  }

}
