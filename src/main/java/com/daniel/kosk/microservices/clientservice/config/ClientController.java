package com.daniel.kosk.microservices.clientservice.config;

import com.daniel.kosk.microservices.clientservice.dto.ApiResponseDto;
import com.daniel.kosk.microservices.clientservice.dto.ClientDto;
import com.daniel.kosk.microservices.clientservice.dto.ResponseDto;
import com.daniel.kosk.microservices.clientservice.service.ClientDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientDataService clientDataService;

    @GetMapping
    public ResponseEntity<ApiResponseDto> getClient(@RequestParam("email")String email){
        try {
            return clientDataService.getClientByEmail(email);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
