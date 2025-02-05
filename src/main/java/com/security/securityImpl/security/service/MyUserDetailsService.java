package com.security.securityImpl.security.service;

import com.security.securityImpl.security.model.UserPrincipal;
import com.security.securityImpl.security.model.Users;
import com.security.securityImpl.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            return new UserPrincipal(user);


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = userRepository.findByUsername(username);
//
//        if(user ==null){
//            System.out.println("User not found");
//            throw new UsernameNotFoundException("User not found");
//        }


//
//        return new UserPrincipal(user);
    }
}
