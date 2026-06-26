# 🛒 Jueun Mall — 대규모 트래픽 처리 데모 쇼핑몰

> Java / Spring Boot 기반의 B2C 쇼핑몰 데모 프로젝트.
> 실제 서비스 환경에서 발생하는 대규모 트래픽 상황을 직접 재현하고, 동시성 제어 및 성능 최적화를 경험하는 것을 목표로 합니다.

---

## 📌 프로젝트 목표

- 상품 목록 조회 시 발생하는 **대규모 Read 트래픽을 Redis 캐싱**으로 최적화
- 선착순 쿠폰 발급 시 발생하는 **대규모 Write 트래픽 및 동시성 문제** 해결
- 부하 테스트 도구(JMeter)를 활용한 **개선 전후 성능 수치 비교**

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
| Test | JMeter |

---

## ✅ 구현 기능

### 👤 회원
- 회원가입 / 로그인 / 로그아웃 (세션 기반)
- 이메일 중복 검사
- 역할 기반 접근 제어 (`USER` / `ADMIN`)

### 🛍️ 상품
- 상품 목록 조회 (카테고리 필터링)
- 상품 상세 페이지 (재고 수량, 사이즈 선택)
- 상품 데이터 자동 초기화 (`DataInitializer`)

### 🛒 장바구니
- 상품 담기 / 삭제
- 장바구니 목록 및 총 결제금액 조회

### 📦 주문 / 결제
- 장바구니 기반 주문 처리
- 주문 상태 관리 (`ORDERED` → `SHIPPING` → `DELIVERED`)
- 마이페이지 주문 내역 조회 (최신순 정렬)

### ⚡ 성능 최적화
- [x] 상품 목록 / 상세 **Redis 캐싱** 적용 (`@Cacheable`, TTL 5분)
- [x] 상품 수정 / 삭제 시 **캐시 무효화** (`@CacheEvict`)
- [x] 선착순 쿠폰 발급 **Redis 분산 락** 적용 (`setIfAbsent`, TTL 5초)
- [x] 중복 발급 방지 및 재고 초과 발급 차단
- [x] JMeter 부하 테스트 및 성능 개선 수치 기록 완료

### 🎟️ 쿠폰
- 선착순 쿠폰 목록 조회 및 발급
- 쿠폰 상태 관리 (`UNUSED` / `USED` / `EXPIRED`)
- 1인 1쿠폰 중복 발급 방지
- 재고 소진 시 품절 처리

### 🔐 관리자
- 역할 기반 관리자 페이지 (`/admin`) 접근 제한
- 상품 수정 / 삭제 (수정/삭제 시 Redis 캐시 자동 무효화)

---

## 🔒 Redis 분산 락 동작 원리

```
1000명이 동시에 쿠폰 발급 요청
→ Redis에 락 키(lock:coupon:{id}) 생성 시도
→ 딱 1명만 락 획득 성공 (setIfAbsent)
→ 나머지 999명은 "LOCKED" 반환 → 재시도 안내
→ 락 획득한 1명이 재고 확인 → 발급 → 락 해제
→ 다음 요청이 락 획득 → 반복
```

---

## 📊 성능 테스트 결과

> 테스트 환경: JMeter / 동시 사용자 500명 / Ramp-up 10초

### 상품 목록 조회 — Redis 캐싱 적용 전후 비교

| 항목 | 캐싱 OFF (DB 직접 조회) | 캐싱 ON (Redis 캐싱) | 개선율 |
|------|----------------------|-------------------|--------|
| 평균 응답시간 | 69ms | 10ms | **85% 개선** ✅ |
| 최대 응답시간 | 613ms | 35ms | **94% 개선** ✅ |
| TPS | 51.0/sec | 49.8/sec | - |
| 에러율 | 0.00% | 0.00% | - |

> TPS가 비슷한 이유: 로컬 환경 특성상 네트워크 병목이 없어 TPS 차이보다 응답시간 개선이 더 두드러짐.
> 실제 운영 환경(DB 서버 분리, 네트워크 지연 존재)에서는 TPS 차이도 크게 벌어질 것으로 예상.

---

## 🗄️ ERD

```
User
├── id (PK)
├── email
├── password
├── name
└── role (USER / ADMIN)

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
├── userEmail
├── couponId
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

### 2. Redis 실행 (Docker)
```bash
docker run --name local-redis \
  -p 6379:6379 \
  -d redis:latest
```

### 3. application.properties 설정
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shop_db
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update

spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### 4. 서버 실행
```bash
./gradlew bootRun
```

### 5. 접속
```
http://localhost:8080

# 테스트 계정
일반 유저: test@test.com / 1234
관리자:    admin@admin.com / admin1234
```

---

## 📁 프로젝트 구조

```
src/main/java/com/example/Shop/
├── config/
│   ├── DataInitializer.java       # 초기 상품/회원/쿠폰 데이터 자동 생성
│   └── RedisConfig.java           # Redis 캐시 설정 (TTL 5분)
├── controller/
│   ├── ProductController.java     # 상품 목록/상세/관리자 CRUD
│   ├── CartController.java
│   ├── OrderController.java
│   ├── CouponController.java      # 쿠폰 목록/발급
│   └── UserController.java
├── domain/
│   ├── Product.java
│   ├── User.java                  # role 필드 (USER / ADMIN)
│   ├── CartItem.java
│   ├── Order.java
│   ├── OrderStatus.java           # enum (ORDERED / SHIPPING / DELIVERED)
│   ├── Coupon.java
│   ├── UserCoupon.java
│   └── CouponStatus.java          # enum (UNUSED / USED / EXPIRED)
├── repository/
│   ├── ProductRepository.java
│   ├── CartItemRepository.java
│   ├── OrderRepository.java
│   ├── UserRepository.java
│   ├── CouponRepository.java
│   └── UserCouponRepository.java
└── service/
    ├── ProductService.java        # @Cacheable / @CacheEvict 적용
    ├── CartService.java
    ├── OrderService.java
    ├── CouponService.java         # Redis 분산 락 적용
    └── UserService.java
```