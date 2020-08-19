package com.telran.phonebookapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/test-1")
    public ResponseEntity<Map<String, String>> getBumbum() {
        Map<String, String> bumbum = new HashMap<>();
        bumbum.put("bum", "bam");
        return new ResponseEntity<>(bumbum, HttpStatus.OK);
    }
}
