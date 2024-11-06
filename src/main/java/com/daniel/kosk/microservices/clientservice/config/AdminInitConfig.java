package com.daniel.kosk.microservices.clientservice.config;

import com.daniel.kosk.microservices.clientservice.entity.Client;
import com.daniel.kosk.microservices.clientservice.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class AdminInitConfig {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public void initAdmin() {
        String email = "kosekshop@gmail.com";
        if (!clientRepository.existsByEmail(email)) {
            Client client = new Client();
            client.setEmail(email);
            client.setPassword(passwordEncoder.encode("Password!23"));
            client.setName("Master");
            client.setSurname("Employee");
            client.setRoles("ADMIN");
            client.setPhoneNumber("123456789");
            client.setAddress("ADDRES");
            client.setActive(true);
            clientRepository.save(client);
            log.info("Master employee created");
        }
        log.info("Master employee already exists");
    }
}
