package com.security.securityImpl.security.service.impl;

import com.security.securityImpl.security.dto.response.UsersResponseDto;
import com.security.securityImpl.security.entity.Token;
import com.security.securityImpl.security.entity.Users;
import com.security.securityImpl.security.repository.TokenRepository;
import com.security.securityImpl.security.service.JWTService;
import com.security.securityImpl.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;


@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    JWTService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    @Override
    public void saveToken(UsersResponseDto usersResponseDto, String accessToken, String refreshToken) {

        try {
            if (null != usersResponseDto && StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) {

                final Users users = Users.builder().build();
                users.setId(usersResponseDto.getId());

                final Date accessTokenExpireTime = jwtService.extractExpiration(accessToken);

                final Date refreshTokenExpireTime = jwtService.extractExpiration(refreshToken);

                final Token token = Token.builder().users(users).accessToken(accessToken).refreshToken(refreshToken).accessTokenExpiredAt(accessTokenExpireTime).refreshTokenExpireAt(refreshTokenExpireTime).build();

                tokenRepository.save(token);
            }
        } catch (Exception ex) {
            throw ex;


        }

    }
}
