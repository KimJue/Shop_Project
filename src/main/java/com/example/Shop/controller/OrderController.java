package com.example.Shop.controller;

import com.example.Shop.domain.Order;
import com.example.Shop.domain.OrderStatus;
import java.util.List;
import com.example.Shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 결제 요청 처리
    @PostMapping("/order/checkout")
    public String checkout(HttpSession session) {
        String userEmail = (String) session.getAttribute("loggedInUserEmail");

        if (userEmail == null) {
            return "redirect:/login";
        }

        try {
            orderService.checkout(userEmail); // 결제 비즈니스 로직 실행
            return "redirect:/order/success"; // 성공 시 완료 페이지로 이동
        } catch (Exception e) {
            return "redirect:/cart?error=true"; // 실패 시 장바구니로 돌려보냄
        }
    }

    // 결제 성공 완료 화면 보여주기
    @GetMapping("/order/success")
    public String orderSuccess(HttpSession session) {
        if (session.getAttribute("loggedInUserEmail") == null) {
            return "redirect:/login";
        }
        return "orderSuccess";
    }

    // 주문 내역(마이페이지) 화면 보여주기
    @GetMapping("/mypage")
    public String myPage(HttpSession session, org.springframework.ui.Model model) {
        String userEmail = (String) session.getAttribute("loggedInUserEmail");

        if (userEmail == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getMyOrders(userEmail);

        long shippingCount  = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.SHIPPING).count();
        long deliveredCount = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();

        model.addAttribute("orders", orders);
        model.addAttribute("shippingCount", shippingCount);
        model.addAttribute("deliveredCount", deliveredCount);

        return "myPage";
    }
}