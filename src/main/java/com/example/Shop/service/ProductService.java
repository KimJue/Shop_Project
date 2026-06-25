package com.example.Shop.service;

import com.example.Shop.domain.Product;
import com.example.Shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        System.out.println("DB에서 상품 목록 조회!");
        return productRepository.findAll();
    }

    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        System.out.println("DB에서 상품 상세 조회! id=" + id);
        return productRepository.findById(id).orElse(null);
    }

    // 상품 수정 시 캐시 무효화
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),  // 목록 캐시 전체 삭제
            @CacheEvict(value = "product", key = "#product.id")  // 해당 상품 캐시 삭제
    })
    public Product updateProduct(Product product) {
        System.out.println("상품 수정 → 캐시 무효화!");
        return productRepository.save(product);
    }

    // 상품 삭제 시 캐시 무효화
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),  // 목록 캐시 전체 삭제
            @CacheEvict(value = "product", key = "#id")          // 해당 상품 캐시 삭제
    })
    public void deleteProduct(Long id) {
        System.out.println("상품 삭제 → 캐시 무효화!");
        productRepository.deleteById(id);
    }
}