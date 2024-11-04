package com.daniel.kosk.microservices.clientservice.controller;

import com.daniel.kosk.microservices.clientservice.dto.RegisterClientDto;
import com.daniel.kosk.microservices.clientservice.dto.ResponseDto;
import com.daniel.kosk.microservices.clientservice.service.ClientDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private ClientDataService clientDataService;

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<ResponseDto> registerClient(@RequestBody RegisterClientDto registerClientDto, BindingResult result){
        if (result.hasErrors()) {
            return clientDataService.returnValidationErrors(result);
        }
        return clientDataService.registerClient(registerClientDto);
    }

    @GetMapping("/activate")
    public ResponseEntity<ResponseDto>   activateClient(@RequestParam String token){
        return clientDataService.activateClient(token);
    }


}
