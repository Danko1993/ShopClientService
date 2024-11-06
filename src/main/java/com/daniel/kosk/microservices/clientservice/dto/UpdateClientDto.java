package com.daniel.kosk.microservices.clientservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Data
public class UpdateClientDto {
    private Optional<String> phoneNumber = Optional.empty();
    private Optional<String> address = Optional.empty();
}