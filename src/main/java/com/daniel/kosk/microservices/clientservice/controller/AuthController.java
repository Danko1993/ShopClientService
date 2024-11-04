package com.daniel.kosk.microservices.clientservice.controller;

import com.daniel.kosk.microservices.clientservice.dto.LoginDto;
import com.daniel.kosk.microservices.clientservice.dto.ResponseDto;
import com.daniel.kosk.microservices.clientservice.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto){
        log.info("Login Request");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.email(),
                            loginDto.password()
                    ));
            return jwtProvider.generateToken(authentication.getName());
        }catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ResponseDto("Invalid credentials"),HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            log.error("Login failed", e);
            return new ResponseEntity(new ResponseDto("Login failed"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
