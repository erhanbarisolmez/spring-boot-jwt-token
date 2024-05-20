package com.security.jwttoken.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
  
  @GetMapping
  public String helloWorld(){
    return "hello world! from public endpoint";
  }
}