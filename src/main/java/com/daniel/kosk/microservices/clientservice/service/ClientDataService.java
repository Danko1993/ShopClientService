package com.daniel.kosk.microservices.clientservice.service;

import com.daniel.kosk.microservices.clientservice.config.RabbitMQConfig;
import com.daniel.kosk.microservices.clientservice.dto.*;
import com.daniel.kosk.microservices.clientservice.entity.ActivationToken;
import com.daniel.kosk.microservices.clientservice.entity.Client;
import com.daniel.kosk.microservices.clientservice.exception.ClientNotFoundException;
import com.daniel.kosk.microservices.clientservice.mapper.ClientMapper;
import com.daniel.kosk.microservices.clientservice.producer.NotificationProducer;
import com.daniel.kosk.microservices.clientservice.repository.ActivationTokenRepository;
import com.daniel.kosk.microservices.clientservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientDataService {
    @Autowired
    private ClientRepository clientRepository;

    private final ClientMapper clientMapper = ClientMapper.INSTANCE;
    @Autowired
    private NotificationProducer notificationProducer;
    @Autowired
    private ActivationTokenRepository activationTokenRepository;
    @Autowired
    private RabbitMQConfig rabbit;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<ResponseDto> registerClient(RegisterClientDto registerClientDto) {
        if (clientRepository.existsByEmail(registerClientDto.email())){
            return new ResponseEntity<>(new ResponseDto("Email already registered"), HttpStatus.CONFLICT);
        }
        Client client = clientMapper.toEntity(registerClientDto);
        client.setRoles("USER");
        client.setActive(false);
        client.setPassword(passwordEncoder.encode(registerClientDto.password()));
        clientRepository.save(client);
        String activationLink = this.createActivationToken(client);
        ClientActivationDto clientActivationDto = new ClientActivationDto(client.getEmail(), activationLink);
        notificationProducer.sendNotification(rabbit.USER_SAVED_EXCHANGE, rabbit.USER_SAVED_KEY,clientActivationDto );
        return new ResponseEntity<>(new ResponseDto("User with email "+ registerClientDto.email()+" registered"), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<ResponseDto> activateClient(String token) {
        if (activationTokenRepository.existsByToken(token)){
            ActivationToken activationToken = activationTokenRepository.findByToken(token);
            if (activationToken.getExpires().before(new Date()) ){
                return new ResponseEntity<>(new ResponseDto("Token is expired"), HttpStatus.CONFLICT);
            }
            Client client = activationToken.getClient();
            client.setActive(true);
            clientRepository.save(client);
            return new ResponseEntity<>(new ResponseDto("Client activated"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDto("Token not found"), HttpStatus.CONFLICT);
    }


    public ResponseEntity<ResponseDto> returnValidationErrors(BindingResult result) {
        String validationErrors = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>( new ResponseDto("Validating erros: " + validationErrors), HttpStatus.BAD_REQUEST);
    }


    private String createActivationToken(Client client) {
        String token = UUID.randomUUID().toString();
        ActivationToken activationToken = new ActivationToken();
        activationToken.setToken(token);
        activationToken.setClient(client);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 24);
        activationToken.setExpires(calendar.getTime());
        activationTokenRepository.save(activationToken);
        return "http://localhost:8081/register/activate?token="+token;
    }

    public ResponseEntity<ApiResponseDto> getClientByEmail(String email) {
        if (clientRepository.existsByEmail(email)){
            Client client = clientRepository.findByEmail(email);
            return new ResponseEntity<>(clientMapper.toDto(client), HttpStatus.OK);
        }else {
            throw new ClientNotFoundException("Client with email "+ email +" not found");
        }

    }
}
