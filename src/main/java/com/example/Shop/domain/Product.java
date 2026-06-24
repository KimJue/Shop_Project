package com.example.Shop.domain;

import jakarta.persistence.*; // 이 import 문이 있어야 @Id를 인식합니다.
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <--- 번호를 자동으로 1, 2, 3... 매겨줍니다.
    private Long id;

    private String name;
    private int price;
    private String description;
    private String imageUrl; // 이미지 경로

    private int stockQuantity;
    private String category;
}