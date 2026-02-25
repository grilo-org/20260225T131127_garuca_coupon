package com.example.coupon.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCouponRequest(
    String code,
    String description,
    BigDecimal discountValue,
    LocalDateTime expirationDate,
    boolean published
) {}