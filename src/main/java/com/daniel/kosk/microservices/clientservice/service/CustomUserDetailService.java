package com.daniel.kosk.microservices.clientservice.service;

import com.daniel.kosk.microservices.clientservice.entity.Client;
import com.daniel.kosk.microservices.clientservice.exception.ClientNotActiveException;
import com.daniel.kosk.microservices.clientservice.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final ClientRepository clientRepository;
    public CustomUserDetailService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by email: {}", username);
        Client client = clientRepository.findByEmail(username);
        if (client == null) {
            log.warn("User not found: {}", username);
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        log.info("User found: {}", client);
        if (!client.isActive()) {
            throw new ClientNotActiveException("Client: " + username + " is not active");
        }
        List<SimpleGrantedAuthority> authorities = Arrays.stream(client.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(client.getEmail(), client.getPassword(), authorities);
    }
}
