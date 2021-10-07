package com.wanjala.gcpdemo.controller;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

  @GetMapping
  public ResponseEntity<String> okay() {
    System.out.println("Request received at /test");
    return new ResponseEntity<>("Request received - " + UUID.randomUUID(), HttpStatus.OK);
  }
}
