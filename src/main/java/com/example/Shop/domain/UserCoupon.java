package com.example.Shop.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;      // 발급받은 유저
    private Long couponId;         // 발급받은 쿠폰 ID

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CouponStatus status = CouponStatus.UNUSED;  // 기본값: 미사용

    private LocalDateTime createdAt;  // 발급 시간
}