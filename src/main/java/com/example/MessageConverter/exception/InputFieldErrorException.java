package com.example.MessageConverter.exception;


public class InputFieldErrorException extends RuntimeException {
    public InputFieldErrorException(String message) {
        super(message);
    }
}
