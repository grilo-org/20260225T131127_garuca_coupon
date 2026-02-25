package com.example.coupon.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    LocalDateTime expirationDate,
    boolean published,
    LocalDateTime createdAt,
    LocalDateTime deletedAt
) {}
