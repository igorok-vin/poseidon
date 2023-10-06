package com.nnk.springboot.exception;

public class BidListNotFoundException extends RuntimeException{
    public BidListNotFoundException(String message) {
        super(message);
    }
}
