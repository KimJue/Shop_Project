package com.example.Shop.domain;

import jakarta.persistence.*; // 이 import 문이 있어야 @Id를 인식합니다.
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <--- 번호를 자동으로 1, 2, 3... 매겨줌.
    private Long id;

    private String name;
    private int price;
    private String description;
    private String imageUrl; // 이미지 경로

    private int stockQuantity;
    private String category;
}