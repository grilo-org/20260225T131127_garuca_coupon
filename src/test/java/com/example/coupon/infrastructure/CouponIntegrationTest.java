package com.example.coupon.infrastructure;

import com.example.coupon.application.dto.CreateCouponRequest;
import com.example.coupon.application.dto.CouponResponse;
import com.example.coupon.domain.exception.CouponAlreadyDeletedException;
import com.example.coupon.domain.exception.DuplicateCouponCodeException;
import com.example.coupon.application.usecase.CreateCouponUseCase;
import com.example.coupon.application.usecase.DeleteCouponUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CouponIntegrationTest {

    @Autowired
    private CreateCouponUseCase createCouponUseCase;

    @Autowired
    private DeleteCouponUseCase deleteCouponUseCase;

    @Test
    @DisplayName("Should create and then soft delete a coupon in the database")
    void shouldCreateAndSoftDeleteCoupon() {
        // 1. Create
        CreateCouponRequest request = new CreateCouponRequest(
            "SAVE10",
            "Save 10 dollars",
            new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(10),
            true
        );

        CouponResponse created = createCouponUseCase.execute(request);
        assertNotNull(created.id());
        assertNull(created.deletedAt());

        // 2. Delete
        var deletedOptional = deleteCouponUseCase.execute(created.id());
        assertTrue(deletedOptional.isPresent());
        assertNotNull(deletedOptional.get().deletedAt());
        
        // 3. Try to delete again - Should throw exception (Business Rule)
        assertThrows(CouponAlreadyDeletedException.class, () -> {
            deleteCouponUseCase.execute(created.id());
        });
    }

    @Test
    @DisplayName("Should not allow duplicate active coupon codes")
    void shouldNotAllowDuplicateActiveCodes() {
        CreateCouponRequest request = new CreateCouponRequest(
            "UNIQUE",
            "Unique coupon",
            new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(10),
            true
        );

        createCouponUseCase.execute(request);

        assertThrows(DuplicateCouponCodeException.class, () -> {
            createCouponUseCase.execute(request);
        });
    }
}
