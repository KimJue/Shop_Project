package com.example.Shop.controller;

import com.example.Shop.service.CouponService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    // 쿠폰 목록 페이지
    @GetMapping("/coupons")
    public String couponList(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUserEmail") == null) {
            return "redirect:/login";
        }
        model.addAttribute("coupons", couponService.getAllCoupons());
        return "couponList";
    }

    // 쿠폰 발급 요청
    @PostMapping("/coupons/{couponId}/issue")
    public String issueCoupon(@PathVariable Long couponId, HttpSession session) {
        String userEmail = (String) session.getAttribute("loggedInUserEmail");

        if (userEmail == null) {
            return "redirect:/login";
        }

        String result = couponService.issueCoupon(couponId, userEmail);

        return switch (result) {
            case "SUCCESS"        -> "redirect:/coupons?success=true";
            case "ALREADY_ISSUED" -> "redirect:/coupons?error=already";
            case "SOLD_OUT"       -> "redirect:/coupons?error=soldout";
            default               -> "redirect:/coupons?error=retry";  // LOCKED
        };
    }
}