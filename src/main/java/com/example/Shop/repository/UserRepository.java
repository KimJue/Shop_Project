package com.example.Shop.repository;

import com.example.Shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자를 찾는 기능을 추가. (나중에 로그인할 때 사용)
    Optional<User> findByEmail(String email);
}