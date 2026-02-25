package com.example.coupon.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateCouponCodeExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        String message = "Coupon code already exists";
        
        DuplicateCouponCodeException exception = new DuplicateCouponCodeException(message);
        
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }
}
