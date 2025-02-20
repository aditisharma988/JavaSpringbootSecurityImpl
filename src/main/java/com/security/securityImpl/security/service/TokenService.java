package com.security.securityImpl.security.service;

import com.security.securityImpl.security.dto.response.UsersResponseDto;

public interface TokenService {

    void saveToken(UsersResponseDto usersResponseDto, String accessToken, String refreshToken);
}
