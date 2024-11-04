package com.daniel.kosk.microservices.clientservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String surname;
    @Column(nullable = false)
    String phoneNumber;
    @Email
    @Column(unique = true ,nullable = false)
    String email;
    @Column(nullable = false)
    String address;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,10}$")
    String password;

    String imagePath;

    String roles;

    boolean active;

}
