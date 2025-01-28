package com.security.securityImpl.Service;

import com.security.securityImpl.Model.Users;
import com.security.securityImpl.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    public Users register(Users users){

        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
         return userRepository.save(users);

    }


}
