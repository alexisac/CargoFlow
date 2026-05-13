package com.example.backendcargoflow.common.exceptions;

import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 400 - Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericApplicationResponseDto> handleBadRequestException(BadRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "400 - BAD_REQUEST", ex.getMessage());
    }

    // 400 - Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericApplicationResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "400 - INVALID_REQUEST", "Invalid request");
    }

    // 401 - Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericApplicationResponseDto> handleUnauthorizedException(UnauthorizedException ex){
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "401 - UNAUTHORIZED", ex.getMessage());
    }

    // 403 - Forbidden
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<GenericApplicationResponseDto> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "403 - FORBIDDEN", "Access denied");
    }

    // 409 - Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<GenericApplicationResponseDto> handleConflictException(ConflictException ex){
        return buildErrorResponse(HttpStatus.CONFLICT, "409 - CONFLICT", ex.getMessage());
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
