package com.example.coupon.domain.model;

import com.example.coupon.domain.exception.CouponAlreadyDeletedException;
import com.example.coupon.domain.exception.InvalidCouponCodeException;
import com.example.coupon.domain.exception.InvalidDiscountValueException;
import com.example.coupon.domain.exception.InvalidExpirationDateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain Object representing a Coupon.
 * Business rules are encapsulated within this class to ensure consistency.
 */
public class Coupon {

    private final UUID id;
    private final String code;
    private final String description;
    private final BigDecimal discountValue;
    private final LocalDateTime expirationDate;
    private final boolean published;
    private final LocalDateTime createdAt;
    private final LocalDateTime deletedAt;

    private Coupon(UUID id, String code, String description, BigDecimal discountValue,
                   LocalDateTime expirationDate, boolean published, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    /**
     * Factory method to create a new Coupon with validation.
     */
    public static Coupon create(String code, String description, BigDecimal discountValue,
                                 LocalDateTime expirationDate, boolean published) {
        
        // 1. Sanitize code: remove special characters, keep only alphanumeric
        String sanitizedCode = sanitizeCode(code);
        
        // 2. Validate mandatory fields (already handled by sanitization and types, but explicit for business rules)
        validateCode(sanitizedCode);
        validateDiscountValue(discountValue);
        validateExpirationDate(expirationDate);
        
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is mandatory");
        }

        return new Coupon(
            UUID.randomUUID(),
            sanitizedCode,
            description,
            discountValue,
            expirationDate,
            published,
            LocalDateTime.now(),
            null
        );
    }

    /**
     * Reconstructs a Coupon from persistence.
     */
    public static Coupon reconstruct(UUID id, String code, String description, BigDecimal discountValue,
                                      LocalDateTime expirationDate, boolean published,
                                      LocalDateTime createdAt, LocalDateTime deletedAt) {
        return new Coupon(id, code, description, discountValue, expirationDate, published, createdAt, deletedAt);
    }

    /**
     * Performs a soft delete by setting the deletedAt timestamp.
     * Throws exception if already deleted.
     */
    public Coupon delete() {
        if (isDeleted()) {
            throw new CouponAlreadyDeletedException("Coupon with code " + this.code + " has already been deleted");
        }
        return new Coupon(
            this.id,
            this.code,
            this.description,
            this.discountValue,
            this.expirationDate,
            this.published,
            this.createdAt,
            LocalDateTime.now()
        );
    }

    private static String sanitizeCode(String code) {
        if (code == null) {
            return "";
        }
        return code.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }

    private static void validateCode(String code) {
        if (code == null || code.length() != 6) {
            throw new InvalidCouponCodeException("Coupon code must have exactly 6 alphanumeric characters");
        }
    }

    private static void validateDiscountValue(BigDecimal discountValue) {
        if (discountValue == null || discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw new InvalidDiscountValueException("Discount value must be at least 0.5");
        }
    }

    private static void validateExpirationDate(LocalDateTime expirationDate) {
        // Business Rule: Cannot be created with expiration date in the past
        if (expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) {
            throw new InvalidExpirationDateException("Expiration date cannot be in the past");
        }
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    // Getters
    public UUID getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public boolean isPublished() { return published; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
