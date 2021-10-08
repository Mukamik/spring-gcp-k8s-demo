package com.wanjala.gcpdemo.controller;

import com.wanjala.gcpdemo.models.DemoRecord;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

  @GetMapping("/okay")
  public ResponseEntity<DemoRecord> okay() {
    System.out.println("Replying with test record:");
    return new ResponseEntity<>(new DemoRecord(1, "Test record", UUID.randomUUID()), HttpStatus.OK);
  }
}
