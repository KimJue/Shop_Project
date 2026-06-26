package com.example.Shop.config;

import com.example.Shop.domain.Coupon;
import com.example.Shop.domain.Product;
import com.example.Shop.domain.User;
import com.example.Shop.repository.CouponRepository;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository,
                                      UserRepository userRepository,
                                      CouponRepository couponRepository) {
        return args -> {
            // 1. 상품 데이터 초기화
            if (productRepository.count() == 0) {
                String[] categories = {"상의", "하의", "아우터", "신발", "악세서리"};
                String[] names = {
                        "오버핏 반팔 티셔츠", "베이직 롱슬리브", "스트라이프 카라 티셔츠", "그래픽 반팔 티셔츠",
                        "와이드 데님 팬츠", "슬랙스 트라우저", "카고 쇼츠", "린넨 와이드 팬츠",
                        "오버핏 코치 재킷", "크롭 바람막이", "울 체크 셔츠", "덱케 패딩 베스트",
                        "캔버스 스니커즈", "청키 로퍼", "레더 샌들", "슬립온 뮬",
                        "버킷햇", "캔버스 토트백", "골지 비니", "레더 벨트"
                };

                for (int i = 0; i < 20; i++) {
                    Product product = new Product();
                    product.setName(names[i]);
                    product.setPrice(19000 + ((i + 1) * 1000));
                    product.setImageUrl("/images/p" + (i + 1) + ".jpg");
                    product.setCategory(categories[i / 4]);
                    product.setStockQuantity(50 + (i * 5));
                    productRepository.save(product);
                }
                System.out.println("상품 데이터 20개 초기화 완료!");
            }

            // 2. 회원 데이터 초기화
            if (userRepository.count() == 0) {
                User testUser = new User();
                testUser.setEmail("test@test.com");
                testUser.setPassword("1234");
                testUser.setName("주은");
                testUser.setRole("USER");
                userRepository.save(testUser);

                User adminUser = new User();
                adminUser.setEmail("admin@admin.com");
                adminUser.setPassword("admin1234");
                adminUser.setName("관리자");
                adminUser.setRole("ADMIN");
                userRepository.save(adminUser);

                System.out.println("회원 데이터 초기화 완료!");
            }

            // 3. 쿠폰 데이터 초기화
            if (couponRepository.count() == 0) {
                Coupon coupon1 = Coupon.builder()
                        .name("신규가입 10% 할인 쿠폰")
                        .discountRate(10)
                        .totalQuantity(100)
                        .issuedQuantity(0)
                        .build();
                couponRepository.save(coupon1);

                Coupon coupon2 = Coupon.builder()
                        .name("여름 시즌 20% 할인 쿠폰")
                        .discountRate(20)
                        .totalQuantity(50)
                        .issuedQuantity(0)
                        .build();
                couponRepository.save(coupon2);

                System.out.println("쿠폰 데이터 초기화 완료!");
            }
        };
    }
}