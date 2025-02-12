package com.security.securityImpl.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "users")
public class Users extends Base {


    private String username;

    private String email;

    private String password;

    private String phoneNumber;

    private boolean active;

    private String otp;

    private LocalDateTime otpGeneratedTime;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "users", fetch = FetchType.LAZY)
    private List<Token> tokenList;


}
