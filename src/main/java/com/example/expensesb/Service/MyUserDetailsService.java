package com.example.expensesb.Service;

import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Entity.MyUserDetails;
import com.example.expensesb.Repository.MyUserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    MyUserRepo myUserRepo;
    public MyUserDetailsService(MyUserRepo myUserRepo) {
        this.myUserRepo = myUserRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = myUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new MyUserDetails(myUser);
    }
}
