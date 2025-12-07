package com.example.expensesb.Service;

import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Repository.MyUserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {

    MyUserRepo myUserRepo;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    public MyUserService(MyUserRepo myUserRepo,AuthenticationManager authenticationManager,JwtService jwtService) {
        this.myUserRepo = myUserRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public MyUser save(MyUser myUser) {
        myUser.setPassword(bCryptPasswordEncoder.encode(myUser.getPassword()));
        return myUserRepo.save(myUser);
    }

    public String verify(MyUser user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return authentication.isAuthenticated()? jwtService.generateKey(user.getUsername()) : "failure" ;

    }
}
