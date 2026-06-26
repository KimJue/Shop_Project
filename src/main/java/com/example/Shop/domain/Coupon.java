package com.example.Shop.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;           // 쿠폰 이름 (예: "신규가입 10% 할인")
    private int discountRate;      // 할인율 (예: 10 = 10%)
    private int totalQuantity;     // 총 발급 가능 수량
    private int issuedQuantity;    // 현재까지 발급된 수량
}