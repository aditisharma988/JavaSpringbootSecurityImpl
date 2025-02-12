package com.security.securityImpl.security.dto;

import com.security.securityImpl.security.dto.response.UsersResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    private UsersResponseDto users;

    private Integer status;

    private String message;


}
