package com.example.Shop.service;

import com.example.Shop.domain.CartItem;
import com.example.Shop.domain.Order;
import com.example.Shop.repository.CartItemRepository;
import com.example.Shop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    // 결제(주문) 처리 메소드
    @Transactional // 작업 도중 에러가 나면 모든 걸 원래대로 되돌리는 안전장치?
    public void checkout(String userEmail) {
        // 1. 사용자의 장바구니 목록을 모두 가져오기
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(userEmail);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        // 2. 총 결제 금액 계산 및 주문 대표명 만들기
        int totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += (item.getProduct().getPrice() * item.getQuantity());
        }

        String orderName = cartItems.get(0).getProduct().getName();
        if (cartItems.size() > 1) {
            orderName += " 외 " + (cartItems.size() - 1) + "건";
        }

        // 3. 주문 내역(Order) 생성 및 DB에 저장
        Order newOrder = Order.builder()
                .userEmail(userEmail)
                .orderName(orderName)
                .totalAmount(totalAmount)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(newOrder);

        // 4. 결제가 끝났으니 장바구니를 싹 비워주기
        cartItemRepository.deleteAll(cartItems);
    }
    // 내 주문 내역 조회 메소드
    public List<Order> getMyOrders(String userEmail) {
        // 이미 OrderRepository에 만들어둔 메소드를 사용
        return orderRepository.findByUserEmailOrderByOrderDateDesc(userEmail);
    }
}