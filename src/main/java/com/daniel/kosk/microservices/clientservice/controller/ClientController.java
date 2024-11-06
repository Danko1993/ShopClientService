package com.daniel.kosk.microservices.clientservice.controller;

import com.daniel.kosk.microservices.clientservice.dto.ApiResponseDto;
import com.daniel.kosk.microservices.clientservice.dto.ResponseDto;
import com.daniel.kosk.microservices.clientservice.dto.UpdateClientDto;
import com.daniel.kosk.microservices.clientservice.service.ClientDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Authorization,Content-Type",
//        methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST,
//                RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.DELETE})
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
    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDto> updateClient(@RequestParam("email")String email,@RequestBody UpdateClientDto updateClientDto){
        try {
            return clientDataService.updateClient(email,updateClientDto);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
