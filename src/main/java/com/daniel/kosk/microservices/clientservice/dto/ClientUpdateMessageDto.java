package com.daniel.kosk.microservices.clientservice.dto;

import lombok.Data;


@Data
public class ClientUpdateMessageDto {
    private String email;
    private String dataName;
    private String oldValue;
    private String newValue;
}
