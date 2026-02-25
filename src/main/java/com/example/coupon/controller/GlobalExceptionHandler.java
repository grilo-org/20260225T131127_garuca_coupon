package com.example.coupon.controller;

import com.example.coupon.domain.exception.CouponAlreadyDeletedException;
import com.example.coupon.domain.exception.DomainException;
import com.example.coupon.domain.exception.DuplicateCouponCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        HttpStatus status = determineStatus(ex);
        ErrorResponse error = new ErrorResponse(
            status.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }

    private HttpStatus determineStatus(DomainException ex) {
        if (ex instanceof CouponAlreadyDeletedException) {
            return HttpStatus.CONFLICT;
        }
        if (ex instanceof DuplicateCouponCodeException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.BAD_REQUEST;
    }

    public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
}
