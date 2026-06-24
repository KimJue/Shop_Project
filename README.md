# 🛒 Jueun Mall — 대규모 트래픽 처리 데모 쇼핑몰

> Java / Spring Boot 기반의 B2C 쇼핑몰 데모 프로젝트.
> 실제 서비스 환경에서 발생하는 대규모 트래픽 상황을 직접 재현하고, 동시성 제어 및 성능 최적화를 경험하는 것을 목표로 합니다.

---

## 📌 프로젝트 목표

- 선착순 쿠폰 발급 시 발생하는 **대규모 Write 트래픽 및 동시성 문제** 해결
- 상품 목록 조회 시 발생하는 **대규모 Read 트래픽을 Redis 캐싱**으로 최적화
- 부하 테스트 도구(JMeter / nGrinder)를 활용한 **개선 전후 성능 수치 비교**

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring Data JPA |
| Database | MySQL 9.x, Redis |
| Frontend | Thymeleaf, Bootstrap 5 |
| DevOps | Docker |
| Build | Gradle |

---

## ✅ 구현 기능

### 👤 회원
- 회원가입 / 로그인 / 로그아웃 (세션 기반)
- 이메일 중복 검사

### 🛍️ 상품
- 상품 목록 조회 (카테고리 필터링)
- 상품 상세 페이지 (재고 수량, 사이즈 선택)
- 상품 데이터 자동 초기화 (DataInitializer)

### 🛒 장바구니
- 상품 담기 / 삭제
- 장바구니 목록 및 총 결제금액 조회

### 📦 주문 / 결제
- 장바구니 기반 주문 처리
- 주문 상태 관리 (`ORDERED` → `SHIPPING` → `DELIVERED`)
- 마이페이지 주문 내역 조회 (최신순 정렬)

### ⚡ 트래픽 처리 (진행 중)
- [ ] 상품 목록 Redis 캐싱 적용
- [ ] 선착순 쿠폰 발급 시스템 (동시성 제어)
- [ ] 분산 락 (Redis Distributed Lock)
- [ ] 부하 테스트 및 성능 개선 수치 기록

---

## 🗄️ ERD

```
User
├── id (PK)
├── email
├── password
└── name

Product
├── id (PK)
├── name
├── price
├── description
├── imageUrl
├── stockQuantity
└── category

Order (orders)
├── id (PK)
├── userEmail
├── orderName
├── totalAmount
├── orderDate
└── status (ORDERED / SHIPPING / DELIVERED)

OrderItem
├── id (PK)
├── order_id (FK)
├── product_id (FK)
├── orderPrice
└── count

CartItem
├── id (PK)
├── userEmail
├── product_id (FK)
└── quantity

Coupon
├── id (PK)
├── name
├── discountRate
├── totalQuantity
└── issuedQuantity

UserCoupon
├── id (PK)
├── user_id (FK)
├── coupon_id (FK)
├── status (UNUSED / USED / EXPIRED)
└── createdAt
```

---

## ⚙️ 로컬 실행 방법

### 1. MySQL 실행 (Docker)
```bash
docker run --name local-mysql \
  -e MYSQL_ROOT_PASSWORD=1234 \
  -e MYSQL_DATABASE=shop_db \
  -p 3306:3306 \
  -d mysql:latest
```

### 2. application.properties 설정
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shop_db
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
```

### 3. 서버 실행
```bash
./gradlew bootRun
```

### 4. 접속
```
http://localhost:8080
```

---

## 📁 프로젝트 구조

```
src/main/java/com/example/Shop/
├── config/
│   └── DataInitializer.java       # 초기 상품/회원 데이터 자동 생성
├── controller/
│   ├── ProductController.java
│   ├── CartController.java
│   ├── OrderController.java
│   └── UserController.java
├── domain/
│   ├── Product.java
│   ├── User.java
│   ├── CartItem.java
│   ├── Order.java
│   └── OrderStatus.java           # enum (ORDERED / SHIPPING / DELIVERED)
├── repository/
│   ├── ProductRepository.java
│   ├── CartItemRepository.java
│   ├── OrderRepository.java
│   └── UserRepository.java
└── service/
    ├── ProductService.java
    ├── CartService.java
    ├── OrderService.java
    └── UserService.java
```

---

## 📊 성능 테스트 결과 (예정)

| 시나리오 | 적용 전 TPS | 적용 후 TPS | 개선율 |
|----------|------------|------------|--------|
| 상품 목록 조회 (Redis 캐싱) | - | - | - |
| 선착순 쿠폰 발급 (분산 락) | - | - | - |

> 부하 테스트 도구: JMeter / nGrinder