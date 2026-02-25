package com.example.coupon.domain.exception;

public class DuplicateCouponCodeException extends DomainException {
    public DuplicateCouponCodeException(String message) {
        super(message);
    }
}
