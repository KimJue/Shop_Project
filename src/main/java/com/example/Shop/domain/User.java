package com.example.Shop.domain;

import jakarta.persistence.*;
import lombok.*; // 1. 롬복을 임포트.

@Entity
@Table(name = "users")
@Getter // 2. 모든 필드의 Getter를 자동으로 만듬.
@Setter // 3. 모든 필드의 Setter를 자동으로 만듬.
@Builder // 4. 이제 User.builder()를 쓸 수 있음.
@NoArgsConstructor // 5. 빈 생성자를 만듬.
@AllArgsConstructor // 6. 모든 필드를 가진 생성자를 만듬. (빌더 사용 시 필수!)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

}