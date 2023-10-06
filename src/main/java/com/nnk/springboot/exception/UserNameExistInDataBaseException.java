package com.nnk.springboot.exception;

public class UserNameExistInDataBaseException extends RuntimeException{
    public UserNameExistInDataBaseException(String message) {
        super(message);
    }
}
