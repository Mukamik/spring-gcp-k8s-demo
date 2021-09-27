package com.wanjala.gcpdemo.controller;

import com.wanjala.gcpdemo.models.ExampleRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/test")
public class TestController {

  @GetMapping("/okay")
  public ResponseEntity<ExampleRecord> okay() {
    System.out.println("Replying with test record:");
    return new ResponseEntity<>(new ExampleRecord(1, "Test record", UUID.randomUUID()), HttpStatus.OK);
  }
}
