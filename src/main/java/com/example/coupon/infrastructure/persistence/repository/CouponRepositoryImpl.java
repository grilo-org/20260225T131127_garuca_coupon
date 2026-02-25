package com.example.coupon.infrastructure.persistence.repository;

import com.example.coupon.domain.model.Coupon;
import com.example.coupon.domain.repository.CouponRepository;
import com.example.coupon.infrastructure.persistence.entity.CouponJpaEntity;
import com.example.coupon.infrastructure.persistence.mapper.CouponMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository jpaRepository;
    private final CouponMapper mapper;

    public CouponRepositoryImpl(CouponJpaRepository jpaRepository, CouponMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponJpaEntity entity = mapper.toJpaEntity(coupon);
        CouponJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Coupon> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return jpaRepository.findByCode(code)
            .map(mapper::toDomain);
    }
}
