package com.example.Shop.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // DB에서 order는 예약어이므로 테이블 이름을 orders로 변경.
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail; // 주문한 사람의 이메일

    private String orderName; // 주문 대표명 (예: "멋진 티셔츠 외 1건")

    private int totalAmount;  // 총 결제 금액

    private LocalDateTime orderDate; // 주문(결제) 시간

    @Enumerated(EnumType.STRING) // DB에 "ORDERED" 같은 문자열로 저장
    @Builder.Default
    private OrderStatus status = OrderStatus.ORDERED; // 기본값: 주문 완료
}