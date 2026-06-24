package com.example.Shop.service;

import com.example.Shop.domain.Product;
import com.example.Shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 이 메서드가 있어야 ProductController에서 빨간 줄이 사라짐
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product getProductById(Long id) {
        // ID로 상품을 찾고, 만약 없으면 null을 반환
        return productRepository.findById(id).orElse(null);
    }
}