package com.security.securityImpl.security.repository;

import com.security.securityImpl.security.entity.Users;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {



    Optional<Users> findByEmail(String email);


   Optional<Users> findByPhoneNumber(String phoneNumber);
}
