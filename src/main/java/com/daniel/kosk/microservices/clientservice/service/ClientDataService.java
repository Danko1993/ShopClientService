package com.daniel.kosk.microservices.clientservice.service;

import com.daniel.kosk.microservices.clientservice.dto.RegisterClientDto;
import com.daniel.kosk.microservices.clientservice.entity.ActivationToken;
import com.daniel.kosk.microservices.clientservice.entity.Client;
import com.daniel.kosk.microservices.clientservice.mapper.ClientMapper;
import com.daniel.kosk.microservices.clientservice.producer.NotificationProducer;
import com.daniel.kosk.microservices.clientservice.repository.ActivationTokenRepository;
import com.daniel.kosk.microservices.clientservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    public ResponseEntity<String> registerClient(RegisterClientDto registerClientDto) {
        if (clientRepository.existsByEmail(registerClientDto.email())){
            return new ResponseEntity<>("Email already registered", HttpStatus.CONFLICT);
        }
        Client client = clientMapper.toEntity(registerClientDto);
        client.setRole("USER");
        client.setActive(false);
        clientRepository.save(client);
        String activationLink = this.createActivationToken(client);
        notificationProducer.sendClientActivationMessage(client.getEmail(),activationLink);

        return new ResponseEntity<>("User with email "+ registerClientDto.email()+" registered", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> activateClient(String token) {
        if (activationTokenRepository.existsByToken(token)){
            ActivationToken activationToken = activationTokenRepository.findByToken(token);
            if (activationToken.getExpires().after(new Date()) ){
                return new ResponseEntity<>("Token is expired", HttpStatus.CONFLICT);
            }
            Client client = activationToken.getClient();
            client.setActive(true);
            clientRepository.save(client);
            ResponseEntity<String> response = new ResponseEntity<>("Client activated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Token not found", HttpStatus.CONFLICT);
    }


    public ResponseEntity<String> returnValidationErrors(BindingResult result) {
        String validationErrors = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>("Validating erros: " + validationErrors, HttpStatus.BAD_REQUEST);
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
}
