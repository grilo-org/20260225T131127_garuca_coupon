package com.example.coupon.domain.model;

import com.example.coupon.domain.exception.CouponAlreadyDeletedException;
import com.example.coupon.domain.exception.InvalidCouponCodeException;
import com.example.coupon.domain.exception.InvalidDiscountValueException;
import com.example.coupon.domain.exception.InvalidExpirationDateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("Should create coupon with valid data")
    void shouldCreateCouponWithValidData() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon = Coupon.create(
            "ABC123",
            "Test Description",
            new BigDecimal("10.00"),
            futureDate,
            true
        );

        assertNotNull(coupon.getId());
        assertEquals("ABC123", coupon.getCode());
        assertEquals("Test Description", coupon.getDescription());
        assertEquals(new BigDecimal("10.00"), coupon.getDiscountValue());
        assertEquals(futureDate, coupon.getExpirationDate());
        assertTrue(coupon.isPublished());
        assertNotNull(coupon.getCreatedAt());
        assertNull(coupon.getDeletedAt());
        assertFalse(coupon.isDeleted());
    }

    @Test
    @DisplayName("Should sanitize code by removing special characters")
    void shouldSanitizeCodeByRemovingSpecialCharacters() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon = Coupon.create(
            "A-B!C@1#2$3%",
            "Test Description",
            new BigDecimal("10.00"),
            futureDate,
            true
        );

        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    @DisplayName("Should convert code to uppercase")
    void shouldConvertCodeToUppercase() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon = Coupon.create(
            "abc123",
            "Test Description",
            new BigDecimal("10.00"),
            futureDate,
            true
        );

        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    @DisplayName("Should throw exception when code has less than 6 characters after sanitization")
    void shouldThrowExceptionWhenCodeHasLessThan6Characters() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        assertThrows(InvalidCouponCodeException.class, () -> {
            Coupon.create(
                "ABC12",
                "Test Description",
                new BigDecimal("10.00"),
                futureDate,
                true
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when code has more than 6 characters after sanitization")
    void shouldThrowExceptionWhenCodeHasMoreThan6Characters() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        assertThrows(InvalidCouponCodeException.class, () -> {
            Coupon.create(
                "ABC1234",
                "Test Description",
                new BigDecimal("10.00"),
                futureDate,
                true
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when code becomes empty after sanitization")
    void shouldThrowExceptionWhenCodeBecomesEmptyAfterSanitization() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        assertThrows(InvalidCouponCodeException.class, () -> {
            Coupon.create(
                "!@#$%",
                "Test Description",
                new BigDecimal("10.00"),
                futureDate,
                true
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when discount value is less than 0.5")
    void shouldThrowExceptionWhenDiscountValueIsLessThanMinimum() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        assertThrows(InvalidDiscountValueException.class, () -> {
            Coupon.create(
                "ABC123",
                "Test Description",
                new BigDecimal("0.49"),
                futureDate,
                true
            );
        });
    }

    @Test
    @DisplayName("Should accept discount value equal to 0.5")
    void shouldAcceptDiscountValueEqualToMinimum() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon = Coupon.create(
            "ABC123",
            "Test Description",
            new BigDecimal("0.5"),
            futureDate,
            true
        );

        assertEquals(new BigDecimal("0.5"), coupon.getDiscountValue());
    }

    @Test
    @DisplayName("Should throw exception when expiration date is in the past")
    void shouldThrowExceptionWhenExpirationDateIsInPast() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        
        assertThrows(InvalidExpirationDateException.class, () -> {
            Coupon.create(
                "ABC123",
                "Test Description",
                new BigDecimal("10.00"),
                pastDate,
                true
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when expiration date is null")
    void shouldThrowExceptionWhenExpirationDateIsNull() {
        assertThrows(InvalidExpirationDateException.class, () -> {
            Coupon.create(
                "ABC123",
                "Test Description",
                new BigDecimal("10.00"),
                null,
                true
            );
        });
    }

    @Test
    @DisplayName("Should create unpublished coupon")
    void shouldCreateUnpublishedCoupon() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon = Coupon.create(
            "ABC123",
            "Test Description",
            new BigDecimal("10.00"),
            futureDate,
            false
        );

        assertFalse(coupon.isPublished());
    }

    @Test
    @DisplayName("Should perform soft delete")
    void shouldPerformSoftDelete() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon = Coupon.create(
            "ABC123",
            "Test Description",
            new BigDecimal("10.00"),
            futureDate,
            true
        );

        assertFalse(coupon.isDeleted());
        
        Coupon deletedCoupon = coupon.delete();
        
        assertTrue(deletedCoupon.isDeleted());
        assertNotNull(deletedCoupon.getDeletedAt());
        assertEquals(coupon.getId(), deletedCoupon.getId());
        assertEquals(coupon.getCode(), deletedCoupon.getCode());
    }

    @Test
    @DisplayName("Should throw exception when deleting already deleted coupon")
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon = Coupon.create(
            "ABC123",
            "Test Description",
            new BigDecimal("10.00"),
            futureDate,
            true
        );

        Coupon deletedCoupon = coupon.delete();
        
        assertThrows(CouponAlreadyDeletedException.class, deletedCoupon::delete);
    }

    @Test
    @DisplayName("Should reconstruct coupon from persistence")
    void shouldReconstructCouponFromPersistence() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime deletedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(5);
        
        Coupon coupon = Coupon.reconstruct(
            java.util.UUID.randomUUID(),
            "XYZ789",
            "Reconstructed Description",
            new BigDecimal("25.00"),
            expirationDate,
            true,
            createdAt,
            deletedAt
        );

        assertNotNull(coupon.getId());
        assertEquals("XYZ789", coupon.getCode());
        assertTrue(coupon.isDeleted());
        assertEquals(deletedAt, coupon.getDeletedAt());
    }
}
