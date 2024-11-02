package com.daniel.kosk.microservices.clientservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ActivationToken {

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    @OneToOne
    @JoinColumn(nullable = false)
    private Client client;

    private Date expires;

}
