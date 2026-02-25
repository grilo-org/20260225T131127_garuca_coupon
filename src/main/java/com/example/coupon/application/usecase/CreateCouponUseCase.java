package com.example.coupon.application.usecase;

import com.example.coupon.application.dto.CreateCouponRequest;
import com.example.coupon.application.dto.CouponResponse;
import com.example.coupon.domain.exception.DuplicateCouponCodeException;
import com.example.coupon.domain.model.Coupon;
import com.example.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Use case focused solely on creating a coupon.
 * Follows the "Single Responsibility" principle and avoids generic services.
 */
@Service
public class CreateCouponUseCase {

    private final CouponRepository couponRepository;

    public CreateCouponUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public CouponResponse execute(CreateCouponRequest request) {
        // Business Rule: Characters are removed before saving and returning
        String sanitizedCode = sanitize(request.code());
        
        // Business Rule: Check for duplicates (even if it's a soft delete, 
        // usually we don't want two active coupons with the same code, 
        // or depending on requirements, even deleted ones)
        Optional<Coupon> existingCoupon = couponRepository.findByCode(sanitizedCode);
        if (existingCoupon.isPresent() && !existingCoupon.get().isDeleted()) {
            throw new DuplicateCouponCodeException("Active coupon with code '" + sanitizedCode + "' already exists");
        }

        // Domain handles validation and internal sanitization
        Coupon coupon = Coupon.create(
            request.code(),
            request.description(),
            request.discountValue(),
            request.expirationDate(),
            request.published()
        );

        Coupon savedCoupon = couponRepository.save(coupon);

        return toResponse(savedCoupon);
    }

    private String sanitize(String code) {
        if (code == null) return "";
        return code.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
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
