package com.security.securityImpl.security.service;


import com.security.securityImpl.security.entity.Token;
import com.security.securityImpl.security.repository.TokenRepository;
import com.security.securityImpl.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService {


    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void logout(HttpServletRequest httpServletRequest) {


        String authHeader = httpServletRequest.getHeader("Authorisation");
        final String jwt;

        if (authHeader != null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);

        final Optional<Token> accessToken = tokenRepository.findByAccessToken(jwt);

        accessToken.ifPresent(token -> {
            tokenRepository.delete(token);
            Optional.ofNullable(token.getUsers()).ifPresent(user -> {
                userRepository.findUserByCreatedBy(user.getEmail()).ifPresent(userRepository::delete);
            });
        });


    }
}






