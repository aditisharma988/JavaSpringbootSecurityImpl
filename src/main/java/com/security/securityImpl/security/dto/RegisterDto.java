package com.security.securityImpl.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String username;

    private String email;

    private String password;

    private String phoneNumber;

}
