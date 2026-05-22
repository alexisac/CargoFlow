package com.example.backendcargoflow.common.exceptions;

// 404 - Not found

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
