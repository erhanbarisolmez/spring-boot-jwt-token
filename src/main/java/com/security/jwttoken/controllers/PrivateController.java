package com.security.jwttoken.controllers;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwttoken.dto.CreateUserRequest;

import com.security.jwttoken.model.User;
import com.security.jwttoken.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;




@RestController
@RequestMapping("/private")

public class PrivateController {

  final UserService  userService;
  
  public PrivateController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public String helloWorldPrivate(){
    return "hello world! from private endpoint";
  }
  
  @GetMapping("/user")
  public String helloWorldUserPrivate(){
    return "hello world! from user private endpoint";
  }

  @GetMapping("/admin")
  public String helloWorldAdminPrivate(){
    return "hello world! from admin private endpoint";
  }

  @GetMapping("/sadmin")
  public String helloWorldSAdminPrivate(){
    return "hello world! from sadmin private endpoint";
  }

  @PostMapping("/create_user")
  @Operation(
  summary = "Create a new user")
  public User createUser(@RequestBody CreateUserRequest createUserRequest){
    return userService.createUser(createUserRequest);
  }

  @Operation(
    description = "User List"
    )
  @GetMapping("/user_all")
  // @ApiOperation(value = "User List")
  public List<User>  get_user_all(){
    return userService.findAll();
  }


}