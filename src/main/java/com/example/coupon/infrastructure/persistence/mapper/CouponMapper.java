package com.example.coupon.infrastructure.persistence.mapper;

import com.example.coupon.domain.model.Coupon;
import com.example.coupon.infrastructure.persistence.entity.CouponJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponJpaEntity toJpaEntity(Coupon coupon) {
        CouponJpaEntity entity = new CouponJpaEntity();
        entity.setId(coupon.getId());
        entity.setCode(coupon.getCode());
        entity.setDescription(coupon.getDescription());
        entity.setDiscountValue(coupon.getDiscountValue());
        entity.setExpirationDate(coupon.getExpirationDate());
        entity.setPublished(coupon.isPublished());
        entity.setCreatedAt(coupon.getCreatedAt());
        entity.setDeletedAt(coupon.getDeletedAt());
        return entity;
    }

    public Coupon toDomain(CouponJpaEntity entity) {
        return Coupon.reconstruct(
            entity.getId(),
            entity.getCode(),
            entity.getDescription(),
            entity.getDiscountValue(),
            entity.getExpirationDate(),
            entity.isPublished(),
            entity.getCreatedAt(),
            entity.getDeletedAt()
        );
    }
}
