package com.fin.ledger_service.GlobalExceptions;

public class DoesNotExistException extends RuntimeException {
    public DoesNotExistException(String message) {
        super(message);
    }
}
