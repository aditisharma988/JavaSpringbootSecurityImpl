package com.security.securityImpl.Service;

import com.security.securityImpl.Model.Users;
import com.security.securityImpl.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    public Users register(Users users){

        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
         return userRepository.save(users);

    }


    public String verify(Users users) {
        Authentication authentication=
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));
       if( authentication.isAuthenticated())
        return jwtService.generateToken(users.getUsername());

       return "fail";
    }
}
