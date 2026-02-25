package com.example.coupon.domain.exception;

public class InvalidCouponCodeException extends DomainException {
    public InvalidCouponCodeException(String message) {
        super(message);
    }
}
