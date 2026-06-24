package com.example.Shop.controller;

import com.example.Shop.domain.User;
import com.example.Shop.domain.UserService;
import com.example.Shop.dto.UserSignupRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ==========================================
    // 1. 회원가입 (Signup) GET 매핑과 POST 매핑 분리하면 될까나
    // ==========================================

    // 1.1 회원가입 화면 보여주기 (링크 클릭 시)
    @GetMapping("/signup")
    public String signupForm() {
        return "signup"; // signup.html 화면을 띄워줌
    }

    // 1.2 회원가입 폼 데이터 처리하기 (가입 완료 버튼 클릭 시)
    @PostMapping("/signup")
    public String signup(@ModelAttribute UserSignupRequest request) {
        try {
            userService.signUp(request);
            // 💡 성공 시: 로그인 화면으로 이동하며 'registered=true'라는 신호를 보냅니다.
            return "redirect:/login?registered=true";

        } catch (IllegalArgumentException e) {
            // 💡 실패 시: 회원가입 화면으로 다시 돌아가며 'error=true'라는 신호를 보냅니다.
            return "redirect:/signup?error=true";
        }
    }

    // ==========================================
    // 2. 로그인 (Login) 이것도 GET 매핑과 POST 매핑 분리하면 될까나
    // ==========================================

    // 2.1 로그인 화면 보여주기 (링크 클릭 시)
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.html 화면을 띄워줌
    }

    // 2.2 로그인 폼 데이터 처리하기 (로그인 버튼 클릭 시)
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        boolean isSuccess = userService.login(email, password);

        if (isSuccess) {
            // (이전에 작성한 로그인 성공 처리 로직)
            User dbUser = userService.getUserByEmail(email);
            if (dbUser != null) {
                session.setAttribute("loggedInUserEmail", dbUser.getEmail());
                session.setAttribute("loggedInUserName", dbUser.getName());
            }
            return "redirect:/"; // 성공 시 메인 화면으로 이동
        } else {
            return "redirect:/login?error=true"; // 실패 시 다시 로그인 화면으로
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션을 완전히 비워서 로그아웃 처리를 합니다.
        session.invalidate();

        // 로그아웃이 끝나면 다시 메인 홈 화면으로 돌려보냅니다.
        return "redirect:/";
    }
}