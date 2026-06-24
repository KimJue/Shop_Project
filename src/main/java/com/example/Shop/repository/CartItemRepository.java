package com.example.Shop.repository;

import com.example.Shop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 메서드 이름도 UserEmail로 맞춰서 변경!
    List<CartItem> findByUserEmail(String userEmail);

    CartItem findByUserEmailAndProductId(String userEmail, Long productId);
}