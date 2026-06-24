package com.example.Shop.dto;

import lombok.Data; // 또는 @Getter, @Setter
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // @Getter, @Setter, @ToString 등을 한 번에 만들어줌
@NoArgsConstructor // 기본 생성자 (스프링이 객체를 만들 때 필수!)
@AllArgsConstructor
public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
}