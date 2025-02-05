package com.security.securityImpl.security.repository;

import com.security.securityImpl.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {


    Users findByUsername(String username);

    Optional<Users> findByEmail(String email);
}
