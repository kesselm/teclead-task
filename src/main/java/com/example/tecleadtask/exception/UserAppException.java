package com.example.tecleadtask.exception;

import lombok.Getter;

public class UserAppException extends RuntimeException {

    @Getter
    private final String message;

    public UserAppException(String message){
        super(message);
        this.message = message;
    }

}
