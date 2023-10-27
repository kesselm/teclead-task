package com.example.tecleadtask.exception;

import lombok.Getter;

@Getter
public class UserAppException extends RuntimeException {

    private final String message;

    public UserAppException(String message){
        super(message);
        this.message = message;
    }

}
