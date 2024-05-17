package com.security.jwttoken.dto;

import java.util.Set;

import com.security.jwttoken.model.Role;

import lombok.Builder;

@Builder
public record CreateUserRequest(
  String name,
  String username,
  String password,
  Set<Role> authorities
){


}
