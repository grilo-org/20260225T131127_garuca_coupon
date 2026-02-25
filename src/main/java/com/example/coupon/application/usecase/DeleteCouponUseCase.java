package com.example.coupon.application.usecase;

import com.example.coupon.application.dto.CouponResponse;
import com.example.coupon.domain.model.Coupon;
import com.example.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case focused solely on deleting a coupon.
 * Implements soft delete business logic.
 */
@Service
public class DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    public DeleteCouponUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Optional<CouponResponse> execute(UUID id) {
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);

        if (optionalCoupon.isEmpty()) {
            return Optional.empty();
        }

        Coupon coupon = optionalCoupon.get();
        
        // Business Rule: Soft delete and "Não deve ser possível deletar um cupom já deletado"
        // The domain object throws an exception if already deleted
        Coupon deletedCoupon = coupon.delete();
        
        // Save the updated state (soft delete)
        Coupon savedCoupon = couponRepository.save(deletedCoupon);

        return Optional.of(toResponse(savedCoupon));
    }

    private CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(
            coupon.getId(),
            coupon.getCode(),
            coupon.getDescription(),
            coupon.getDiscountValue(),
            coupon.getExpirationDate(),
            coupon.isPublished(),
            coupon.getCreatedAt(),
            coupon.getDeletedAt()
        );
    }
}
