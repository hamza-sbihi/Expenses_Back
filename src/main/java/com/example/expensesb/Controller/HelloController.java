package com.example.expensesb.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {

    public HelloController() {

    }

    @GetMapping
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello Hamza");
    }
}
