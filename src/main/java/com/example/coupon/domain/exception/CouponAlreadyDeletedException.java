package com.example.coupon.domain.exception;

public class CouponAlreadyDeletedException extends DomainException {
    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
