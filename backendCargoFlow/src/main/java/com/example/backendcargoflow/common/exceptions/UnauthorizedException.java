package com.example.backendcargoflow.common.exceptions;

//401 - Unauthorized

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
