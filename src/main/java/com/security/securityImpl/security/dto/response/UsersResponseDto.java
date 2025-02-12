package com.security.securityImpl.security.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponseDto {

    private Integer id;

    private String username;

    private String email;

    private String password;

    private String address;

    private String phoneNumber;

    private List<TokenResponseDto> tokenList;

}
