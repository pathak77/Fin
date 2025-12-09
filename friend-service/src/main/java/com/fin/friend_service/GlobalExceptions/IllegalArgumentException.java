package com.fin.friend_service.GlobalExceptions;

public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
    }
}
