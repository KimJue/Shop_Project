package com.example.Shop.controller;

import com.example.Shop.domain.Product;
import com.example.Shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String list(Model model) {
        // 서비스에서 상품 목록을 가져와서 모델에 담기.
        model.addAttribute("products", productService.getAllProducts());

        // 상품 목록 페이지(productList.html)로 이동.
        return "productList";
    }
    @GetMapping("/products/{id}")
    public String detail(@PathVariable Long id, Model model) {
        // 1. 주소창의 id를 받아와서 해당 상품을 DB에서 찾음
        Product product = productService.getProductById(id);

        // 2. 찾은 상품 데이터를 "product"라는 이름으로 화면에 넘겨줌
        model.addAttribute("product", product);

        // 3. productDetail.html 화면을 보여줌
        return "productDetail";
    }
}