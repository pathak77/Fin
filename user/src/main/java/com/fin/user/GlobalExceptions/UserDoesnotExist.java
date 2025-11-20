package com.fin.user.GlobalExceptions;


public class UserDoesnotExist extends RuntimeException {
    public UserDoesnotExist(String message) {
        super(message);
    }
}
