package com.daniel.kosk.microservices.clientservice.producer;

import com.daniel.kosk.microservices.clientservice.dto.ClientActivationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper jacksonObjectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public void sendClientActivationMessage(String email, String activationLink) {
        ClientActivationDto activationDto = new ClientActivationDto(email,activationLink);
        try{
            String message = jacksonObjectMapper.writeValueAsString(activationDto);
            kafkaTemplate.send("notification-topic", message);
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

    }

}
