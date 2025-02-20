package com.security.securityImpl.security.service;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.security.securityImpl.security.model.UserPrincipal;
import com.security.securityImpl.security.entity.Users;
import com.security.securityImpl.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            System.out.println("User not found in DB for email: " + email);  // Debugging
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Users user = optionalUser.get();

        // Optional: Check if the user is active
//        if (!user.isActive()) {
//            System.out.println("User is inactive: " + email);
//            throw new UsernameNotFoundException("User is inactive: " + email);
//        }

        System.out.println("User found: " + user.getEmail());  // Debugging
        return new UserPrincipal(user);
    }


    public UserDetails loadUserByUserId(Integer id) throws UsernameNotFoundException {
        Optional<Users> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            System.out.println("User not found in DB for id: " + id);  // Debugging
            throw new UsernameNotFoundException("User not found with id: " + id);
        }

        Users user = optionalUser.get();

        // Optional: Check if the user is active
//        if (!user.isActive()) {
//            System.out.println("User is inactive: " + email);
//            throw new UsernameNotFoundException("User is inactive: " + email);
//        }

        System.out.println("User found: " + user.getId());  // Debugging
        return new UserPrincipal(user);
    }


//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        Optional<Users> optionalUser = userRepository.findByEmail(email);
//
//        if (optionalUser.isEmpty()) {
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }
//
//        Users user = optionalUser.get();
//        return new UserPrincipal(user);
//
//    }
}
