package com.daniel.kosk.microservices.clientservice.repository;

import com.daniel.kosk.microservices.clientservice.entity.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, UUID> {

    boolean existsByToken(String token);
    ActivationToken findByToken(String token);

}
