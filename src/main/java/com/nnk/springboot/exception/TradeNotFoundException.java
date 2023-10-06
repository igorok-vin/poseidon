package com.nnk.springboot.exception;

public class TradeNotFoundException extends RuntimeException{
    public TradeNotFoundException(String message) {
        super(message);
    }
}
