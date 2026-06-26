package com.example.Shop.repository;

import com.example.Shop.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    // 특정 유저가 특정 쿠폰을 이미 발급받았는지 확인
    boolean existsByUserEmailAndCouponId(String userEmail, Long couponId);
}