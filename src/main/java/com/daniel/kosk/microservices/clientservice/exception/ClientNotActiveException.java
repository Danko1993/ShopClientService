package com.daniel.kosk.microservices.clientservice.exception;

public class ClientNotActiveException extends RuntimeException {
    public ClientNotActiveException(String message) {
        super(message);
    }
}
