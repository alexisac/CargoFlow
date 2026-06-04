package com.example.locationservice.common.exceptions;

import com.example.locationservice.controller.common.models.GenericApplicationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 401 - Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericApplicationResponseDto> handleUnauthorizedException(UnauthorizedException ex){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "401 - UNAUTHORIZED", ex.getMessage());
    }

    // 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericApplicationResponseDto> handleGenericException(Exception ex) {
        log.error("Unexpected error occured", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500 - INTERNAL_SERVER_ERROR", "Internal server error");
    }

    private ResponseEntity<GenericApplicationResponseDto> buildErrorResponse(HttpStatus status, String code, String message) {
        GenericApplicationResponseDto response = new GenericApplicationResponseDto();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return ResponseEntity.status(status).body(response);
    }
}
