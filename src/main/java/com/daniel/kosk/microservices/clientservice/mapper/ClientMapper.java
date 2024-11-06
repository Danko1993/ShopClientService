package com.daniel.kosk.microservices.clientservice.mapper;

import com.daniel.kosk.microservices.clientservice.dto.ClientDto;
import com.daniel.kosk.microservices.clientservice.dto.RegisterClientDto;
import com.daniel.kosk.microservices.clientservice.dto.ClientActivationDto;
import com.daniel.kosk.microservices.clientservice.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    Client toEntity(RegisterClientDto registerClientDto);
    ClientActivationDto toUserActivationDto(Client client);
    ClientDto toDto(Client client);
}
