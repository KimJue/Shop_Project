package com.example.Shop.controller;

import com.example.Shop.domain.CartItem;
import com.example.Shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. 장바구니에 상품 추가 요청 처리
    @PostMapping("/cart/add")
    public String addCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUserEmail");

        // 로그인이 안 되어 있으면 로그인 페이지로 리다이렉트
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        cartService.addCart(loggedInUser, productId, quantity);
        return "redirect:/cart"; // 담은 후 장바구니 목록 페이지로 이동
    }

    // 2. 장바구니 목록 보기 요청 처리
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        String loggedInUser = (String) session.getAttribute("loggedInUserEmail");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartService.getCartList(loggedInUser);
        int totalAmount = cartItems.stream()
                .mapToInt(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "cart";
    }

    @PostMapping("/cart/delete")
    public String deleteCart(@RequestParam Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return "redirect:/cart"; // 삭제 후 다시 장바구니 페이지로 새로고침
    }
}