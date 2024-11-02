package com.daniel.kosk.microservices.clientservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;
    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512,jwtSecretKey)
                .compact();
    }

}
