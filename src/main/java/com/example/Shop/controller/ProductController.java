package com.example.Shop.controller;

import com.example.Shop.domain.Product;
import com.example.Shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "productList";
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "productDetail";
    }

    // 관리자 페이지
    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("loggedInUserRole"))) {
            return "redirect:/";
        }
        model.addAttribute("products", productService.getAllProducts());
        return "admin";
    }

    // 상품 수정 폼 페이지
    @GetMapping("/admin/products/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("loggedInUserRole"))) {
            return "redirect:/";
        }
        model.addAttribute("product", productService.getProductById(id));
        return "adminProductEdit";
    }

    // 상품 수정 처리
    @PostMapping("/admin/products/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam int price,
                              @RequestParam String description,
                              @RequestParam String category,
                              @RequestParam int stockQuantity,
                              HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("loggedInUserRole"))) {
            return "redirect:/";
        }
        Product product = productService.getProductById(id);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategory(category);
        product.setStockQuantity(stockQuantity);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    // 상품 삭제 처리
    @PostMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("loggedInUserRole"))) {
            return "redirect:/";
        }
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}