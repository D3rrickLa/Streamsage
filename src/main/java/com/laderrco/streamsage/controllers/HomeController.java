package com.laderrco.streamsage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/webapp")
public class HomeController {
  @GetMapping("/")
  public String index() {
      return "index.html";
  } 
}