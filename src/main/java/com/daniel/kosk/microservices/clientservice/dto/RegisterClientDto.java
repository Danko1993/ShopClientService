package com.daniel.kosk.microservices.clientservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterClientDto(
        @NotBlank(message = "Name must be provided")
        String name,
        @NotBlank(message = "Surname must be provided")
        String surname,
        @NotBlank(message = "Phone number must be provided")
        String phoneNumber,
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Name must be provided")
        String address,
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,30}$",
                message = "Password must be between 6 and 10 characters long and contain at least one uppercase letter, one digit, and one special character.")
        String password

) {
}
