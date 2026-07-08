package com.Business.Electronics.exception;

public class AddProductFailedException extends RuntimeException {
    public AddProductFailedException(String message) {
        super(message);
    }
}
