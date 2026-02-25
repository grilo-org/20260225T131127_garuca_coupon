package com.example.coupon.application.usecase;

import com.example.coupon.application.dto.CouponResponse;
import com.example.coupon.domain.model.Coupon;
import com.example.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListCouponsUseCase {

    private final CouponRepository couponRepository;

    public ListCouponsUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> execute() {
        return couponRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
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
