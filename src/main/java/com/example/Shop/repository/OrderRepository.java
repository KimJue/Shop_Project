package com.example.Shop.repository;

import com.example.Shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 나중에 마이페이지에서 내 주문 내역을 최신순으로 볼 때 사용할 메소드
    List<Order> findByUserEmailOrderByOrderDateDesc(String userEmail);
}