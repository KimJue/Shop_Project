package com.example.Shop.controller;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 이 메서드 하나만
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("session", session);
        return "index";
    }

}