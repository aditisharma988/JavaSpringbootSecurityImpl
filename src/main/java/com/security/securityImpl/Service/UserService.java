package com.security.securityImpl.Service;

import com.security.securityImpl.Model.Users;
import com.security.securityImpl.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;


    public Users register(Users users){
         return userRepository.save(users);

    }


}
