package com.example.coupon.application.usecase;

import com.example.coupon.application.dto.CouponResponse;
import com.example.coupon.domain.model.Coupon;
import com.example.coupon.domain.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListCouponsUseCaseTest {

    private CouponRepository couponRepository;
    private ListCouponsUseCase listCouponsUseCase;

    @BeforeEach
    void setUp() {
        couponRepository = mock(CouponRepository.class);
        listCouponsUseCase = new ListCouponsUseCase(couponRepository);
    }

    @Test
    @DisplayName("Should return empty list when no coupons exist")
    void shouldReturnEmptyListWhenNoCouponsExist() {
        when(couponRepository.findAll()).thenReturn(Collections.emptyList());

        List<CouponResponse> response = listCouponsUseCase.execute();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(couponRepository).findAll();
    }

    @Test
    @DisplayName("Should return list of coupons")
    void shouldReturnListOfCoupons() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        
        Coupon coupon1 = Coupon.create(
            "ABC123",
            "First Coupon",
            new BigDecimal("10.00"),
            futureDate,
            true
        );
        
        Coupon coupon2 = Coupon.create(
            "DEF456",
            "Second Coupon",
            new BigDecimal("20.00"),
            futureDate,
            false
        );

        when(couponRepository.findAll()).thenReturn(Arrays.asList(coupon1, coupon2));

        List<CouponResponse> response = listCouponsUseCase.execute();

        assertNotNull(response);
        assertEquals(2, response.size());
        
        assertEquals("ABC123", response.get(0).code());
        assertEquals("First Coupon", response.get(0).description());
        
        assertEquals("DEF456", response.get(1).code());
        assertEquals("Second Coupon", response.get(1).description());
        
        verify(couponRepository).findAll();
    }
}
