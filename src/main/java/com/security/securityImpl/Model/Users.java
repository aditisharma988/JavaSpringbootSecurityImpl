package com.security.securityImpl.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@RequiredArgsConstructor
//@NoArgsConstructor
@Data
@Table(name="users")
public class Users {

    @Id
    private int id;
    private String username;
    private String password;

}
