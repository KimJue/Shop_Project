package com.example.Shop.service;

import com.example.Shop.domain.Coupon;
import com.example.Shop.domain.UserCoupon;
import com.example.Shop.repository.CouponRepository;
import com.example.Shop.repository.UserCouponRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final StringRedisTemplate redisTemplate;

    public CouponService(CouponRepository couponRepository,
                         UserCouponRepository userCouponRepository,
                         StringRedisTemplate redisTemplate) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
        this.redisTemplate = redisTemplate;
    }

    // 쿠폰 전체 목록 조회
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    // 선착순 쿠폰 발급 (Redis 분산 락 적용)
    @Transactional
    public String issueCoupon(Long couponId, String userEmail) {
        String lockKey = "lock:coupon:" + couponId;  // 쿠폰별 락 키

        // 1. Redis 분산 락 획득 시도 (5초 동안 유효)
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "locked", 5, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(acquired)) {
            try {
                // 2. 중복 발급 체크
                if (userCouponRepository.existsByUserEmailAndCouponId(userEmail, couponId)) {
                    return "ALREADY_ISSUED";  // 이미 발급받은 쿠폰
                }

                // 3. 쿠폰 재고 확인
                Coupon coupon = couponRepository.findById(couponId)
                        .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

                if (coupon.getIssuedQuantity() >= coupon.getTotalQuantity()) {
                    return "SOLD_OUT";  // 재고 없음
                }

                // 4. 쿠폰 발급 처리
                coupon.setIssuedQuantity(coupon.getIssuedQuantity() + 1);
                couponRepository.save(coupon);

                UserCoupon userCoupon = UserCoupon.builder()
                        .userEmail(userEmail)
                        .couponId(couponId)
                        .createdAt(LocalDateTime.now())
                        .build();
                userCouponRepository.save(userCoupon);

                return "SUCCESS";

            } finally {
                // 5. 락 해제 (성공/실패 상관없이 반드시 해제)
                redisTemplate.delete(lockKey);
            }
        } else {
            return "LOCKED";  // 다른 요청이 처리 중
        }
    }
}