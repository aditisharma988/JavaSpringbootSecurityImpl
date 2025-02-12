package com.security.securityImpl.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {

    private Integer id;

    private String accessToken;

    private String refreshToken;

    private Date refreshTokenExpiresAt;

    private UsersResponseDto usersResponseDto;
}
