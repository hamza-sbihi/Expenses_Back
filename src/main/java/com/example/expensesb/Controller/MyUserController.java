package com.example.expensesb.Controller;

import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Service.MyUserService;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class MyUserController {

    MyUserService myUserService;

    public MyUserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<MyUser> register(@RequestBody MyUser user){
        return ResponseEntity.ok(myUserService.save(user));
    }

}
