package com.daniel.kosk.microservices.clientservice.dto;

import java.util.UUID;

public record ClientDto(
        UUID id,
        String name,
        String surname,
        String phoneNumber,
        String email,
        String address,
        String imagePath,
        String roles
) implements ApiResponseDto {
}
