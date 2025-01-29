package com.security.securityImpl.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private String secretKey;

    public JWTService() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey1 =keyGenerator.generateKey();
        secretKey= Base64.getEncoder().encodeToString(secretKey1.getEncoded());
    }

    public String generateToken(String username) {

        Map<String,Object> claims= new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .addClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 60*60*30))
                .signWith(getkey())
                .compact();

    }

    private Key getkey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
