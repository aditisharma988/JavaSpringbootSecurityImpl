package com.security.securityImpl.security.service.impl;

import com.security.securityImpl.security.dto.AuthResponse;
import com.security.securityImpl.security.dto.response.UsersResponseDto;
import com.security.securityImpl.security.mapper.UsersMapper;
import com.security.securityImpl.security.repository.UserRepository;
import com.security.securityImpl.security.service.AuthenticationService;
import com.security.securityImpl.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final JWTService jwtService;

//    @Autowired
//    private TokenService tokenService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, UsersMapper usersMapper, JWTService jwtService) {
        this.userRepository = userRepository;
        this.usersMapper = usersMapper;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse generateAuthenticationResponse(String username, AuthResponse authResponse) {
        UsersResponseDto usersResponseDto = findByUsername(username);

        if (usersResponseDto == null) {
            throw new RuntimeException("User not found!");
        }

//        final UserDto userDto = findByUsername(username);
//        final String accessToken = jwtUtil.generateToken(userDto, AppConstants.TokenType.ACCESS_TOKEN);
//        final String refreshToken = jwtUtil.generateToken(userDto, AppConstants.TokenType.REFRESH_TOKEN);

        String accessToken = jwtService.generateToken(String.valueOf(usersResponseDto));
        String refreshToken = jwtService.generateToken(String.valueOf(usersResponseDto));

//        tokenService.saveToken(usersResponseDto, accessToken, refreshToken);
        authResponse.setUsers(usersResponseDto);
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setStatus(HttpStatus.OK.value());
        authResponse.setMessage("USER_WITH_USERNAME_LOGGED_IN_SUCCESSFULLY");

        return authResponse;
    }

    public UsersResponseDto findByUsername(String username) {

        return userRepository.findByEmail(username).map(user -> usersMapper.mapToUserResponseDto(new UsersResponseDto(), user)).orElseThrow(() -> new RuntimeException("User Not FOUND!!!"));
    }

    public UsersResponseDto findByPhoneNumber(String phoneNumber) {

        return userRepository.findByPhoneNumber(phoneNumber).map(user -> usersMapper.mapToUserResponseDto(new UsersResponseDto(), user)).orElseThrow(() -> new RuntimeException("User Not FOUND!!!"));
    }

    @Override
    public AuthResponse generateAuthenticationResponseForPhoneNumber(String phoneNumber, AuthResponse authResponse) {

        UsersResponseDto usersResponseDto = findByPhoneNumber(phoneNumber);

        if (usersResponseDto == null) {
            throw new RuntimeException("User not found!");
        }

        String accessToken = jwtService.generateToken(String.valueOf(usersResponseDto));
        String refreshToken = jwtService.generateToken(String.valueOf(usersResponseDto));

//        tokenService.saveToken(usersResponseDto, accessToken, refreshToken);
        authResponse.setUsers(usersResponseDto);
//        authResponse.setUsers(usersResponseDto1);
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setStatus(HttpStatus.OK.value());
        authResponse.setMessage("USER_WITH_PHONE_NUMBER_LOGGED_IN_SUCCESSFULLY");

        return authResponse;
    }


}
