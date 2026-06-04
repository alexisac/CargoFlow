package com.example.locationservice.common.exceptions;

//500 - Internal Server Error

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
