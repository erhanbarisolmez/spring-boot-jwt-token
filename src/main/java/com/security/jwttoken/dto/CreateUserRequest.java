package com.security.jwttoken.dto;

import java.util.Set;

import com.security.jwttoken.model.Role;


public record CreateUserRequest(
  String name,
  String username,
  String password,
  Set<Role> authorities
){



}


// localhost:8080/auth/addNewUser/?name=baris&username=baris&password=pass&authorities=ROLE_USER