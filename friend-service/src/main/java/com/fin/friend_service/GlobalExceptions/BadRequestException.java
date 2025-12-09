package com.fin.friend_service.GlobalExceptions;


public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
