package com.example.coupon.application.usecase;

import com.example.coupon.application.dto.CouponResponse;
import com.example.coupon.domain.model.Coupon;
import com.example.coupon.domain.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteCouponUseCaseTest {

    private CouponRepository couponRepository;
    private DeleteCouponUseCase deleteCouponUseCase;

    @BeforeEach
    void setUp() {
        couponRepository = mock(CouponRepository.class);
        deleteCouponUseCase = new DeleteCouponUseCase(couponRepository);
    }

    @Test
    @DisplayName("Should delete coupon successfully")
    void shouldDeleteCouponSuccessfully() {
        UUID couponId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon existingCoupon = Coupon.create(
            "ABC123",
            "Test Coupon",
            new BigDecimal("15.00"),
            futureDate,
            true
        );

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(existingCoupon));
        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<CouponResponse> response = deleteCouponUseCase.execute(couponId);

        assertTrue(response.isPresent());
        assertNotNull(response.get().deletedAt());
        assertNotNull(response.get().id());

        verify(couponRepository).findById(couponId);
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("Should return empty when coupon not found")
    void shouldReturnEmptyWhenCouponNotFound() {
        UUID couponId = UUID.randomUUID();
        
        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        Optional<CouponResponse> response = deleteCouponUseCase.execute(couponId);

        assertTrue(response.isEmpty());
        verify(couponRepository).findById(couponId);
        verify(couponRepository, never()).save(any(Coupon.class));
    }
}
