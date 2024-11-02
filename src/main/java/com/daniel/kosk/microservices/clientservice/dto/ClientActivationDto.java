package com.daniel.kosk.microservices.clientservice.dto;

public record ClientActivationDto(
        String email,
        String activationLink
) {
}
