package com.example.expensesb.Controller;

import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Service.MyUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class MyUserController {

    MyUserService myUserService;

    public MyUserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<MyUser> register(@RequestBody MyUser user){
        return ResponseEntity.ok(myUserService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MyUser user){
        String result = myUserService.verify(user);
        return ResponseEntity.ok(result);
    }

}
