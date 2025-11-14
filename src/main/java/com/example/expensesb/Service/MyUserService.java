package com.example.expensesb.Service;

import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Repository.MyUserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {

    MyUserRepo myUserRepo;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public MyUserService(MyUserRepo myUserRepo) {
        this.myUserRepo = myUserRepo;
    }

    public MyUser save(MyUser myUser) {
        myUser.setPassword(bCryptPasswordEncoder.encode(myUser.getPassword()));
        return myUserRepo.save(myUser);
    }
}
