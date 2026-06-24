package com.example.Shop.service;

import com.example.Shop.domain.CartItem;
import com.example.Shop.domain.Product;
import com.example.Shop.repository.CartItemRepository;
import com.example.Shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // 장바구니에 상품 추가하기
    public void addCart(String userEmail, Long productId, int quantity) {
        // userEmail로 변경된 메서드 사용
        CartItem existingItem = cartItemRepository.findByUserEmailAndProductId(userEmail, productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                CartItem newItem = new CartItem();
                newItem.setUserEmail(userEmail); // userEmail 저장
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                cartItemRepository.save(newItem);
            }
        }
    }

    // 특정 사용자의 장바구니 목록 가져오기
    public List<CartItem> getCartList(String userEmail) {
        return cartItemRepository.findByUserEmail(userEmail); // userEmail로 변경
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}