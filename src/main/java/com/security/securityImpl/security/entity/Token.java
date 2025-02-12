package com.security.securityImpl.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token_config")
public class Token extends Base{

    @Lob
    @Column(name = "access_token", columnDefinition = "MEDIUMTEXT")
    private String accessToken;

    @Lob
    @Column(name = "refresh_token", columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @Column(name = "expire_at")
    private Date accessTokenExpiredAt;

    @Column(name = "refresh_token_expire_at")
    private Date refreshTokenExpireAt;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private Users users;


}


