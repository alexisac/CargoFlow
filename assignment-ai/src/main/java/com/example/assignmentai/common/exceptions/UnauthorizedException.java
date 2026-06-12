package com.example.assignmentai.common.exceptions;

//401 - Unauthorized

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
