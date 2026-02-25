package com.example.coupon.domain.repository;

import com.example.coupon.domain.model.Coupon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {
    Coupon save(Coupon coupon);
    Optional<Coupon> findById(UUID id);
    List<Coupon> findAll();
    Optional<Coupon> findByCode(String code);
}
