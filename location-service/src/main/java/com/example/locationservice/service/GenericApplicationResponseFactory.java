package com.example.locationservice.service;

import com.example.locationservice.controller.common.models.GenericApplicationResponseDto;

public final class GenericApplicationResponseFactory {
    private GenericApplicationResponseFactory(){}

    public static GenericApplicationResponseDto success(String code, String message) {
        GenericApplicationResponseDto response = new GenericApplicationResponseDto();
        response.setSuccess(true);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
