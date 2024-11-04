package com.daniel.kosk.microservices.clientservice.jwt;

import com.daniel.kosk.microservices.clientservice.dto.ResponseDto;
import com.daniel.kosk.microservices.clientservice.entity.Client;
import com.daniel.kosk.microservices.clientservice.repository.ClientRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;
    @Value("${security.jwt.expiration}")
    private long jwtExpiration;


    public ResponseEntity<ResponseDto> generateToken(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration);
        String jwt = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512,jwtSecretKey)
                .compact();

        return new ResponseEntity(new ResponseDto(jwt), HttpStatus.OK);
    }

}
