package com.nnk.springboot.exception;

public class RuleNameNotFoundException extends RuntimeException{
    public RuleNameNotFoundException(String message) {
        super(message);
    }
}
