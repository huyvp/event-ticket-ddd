
# 🧱 Layered Architecture trong Domain-Driven Design (DDD)

## 1. Giới thiệu

**Layered Architecture** (Kiến trúc phân lớp) là mô hình phổ biến trong **Domain-Driven Design (DDD)**, giúp tách biệt
rõ ràng giữa các phần của hệ thống, tăng khả năng **bảo trì**, **kiểm thử**, và **mở rộng**.

Mỗi lớp có vai trò riêng biệt, chịu trách nhiệm cho một phần của logic ứng dụng, đồng thời **chỉ phụ thuộc xuống lớp
thấp hơn**.

---

## 2. Cấu trúc tổng quát

```

User Interface
↓
Application
↓
Domain
↓
Infrastructure

````

---

## 3. Mô tả chi tiết từng lớp

### 🧩 3.1. User Interface (UI Layer)

- **Nhiệm vụ:** Giao tiếp với người dùng hoặc hệ thống bên ngoài.  
- **Ví dụ:** REST Controller, Web UI, CLI, API Endpoint.  
- **Vai trò:**  
  - Nhận yêu cầu (HTTP, CLI, Message Queue, …)
  - Gọi **Application Layer** để xử lý nghiệp vụ.
  - Trả về kết quả cho người dùng.

**Ví dụ (Spring Boot):**
```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        orderService.createOrder(request);
        return ResponseEntity.ok("Order created");
    }
}
````

---

### ⚙️ 3.2. Application Layer

* **Nhiệm vụ:** Điều phối luồng xử lý giữa các lớp và module.
* **Không chứa logic nghiệp vụ phức tạp** – chỉ dùng để gọi các **Domain Services**, **Repositories**, hoặc **External APIs**.
* **Ví dụ:** Service classes, Use Cases.

**Ví dụ:**

```java
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    public void createOrder(OrderRequest request) {
        Order order = new Order(request.getItems());
        orderRepository.save(order);
        paymentService.charge(order);
    }
}
```

---

### 🧠 3.3. Domain Layer

* **Trái tim của ứng dụng (core business logic)**
* **Chỉ chứa các khái niệm nghiệp vụ thực sự** – độc lập hoàn toàn với framework hoặc database.
* Bao gồm:

    * **Entities** (thực thể)
    * **Value Objects**
    * **Domain Services**
    * **Aggregates**
    * **Repository Interfaces**

**Ví dụ:**

```java
public class Order {
    private List<OrderItem> items;
    private OrderStatus status = OrderStatus.NEW;

    public Order(List<OrderItem> items) {
        this.items = items;
    }

    public Money getTotalPrice() {
        return items.stream()
                .map(OrderItem::getPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public void markPaid() {
        this.status = OrderStatus.PAID;
    }
}
```

---

### 🧰 3.4. Infrastructure Layer

* **Cung cấp khả năng kỹ thuật cho hệ thống**: lưu trữ, giao tiếp, thư viện, v.v.
* Chứa các **implementations** của Repository, EmailService, FileStorage, Database Access, Message Broker, v.v.
* Có thể sử dụng Spring Data JPA, Hibernate, JDBC, Redis, Kafka, v.v.

**Ví dụ:**

```java
@Repository
public class JpaOrderRepository implements OrderRepository {
    private final SpringDataOrderRepository repository;

    public JpaOrderRepository(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Order order) {
        repository.save(order);
    }
}
```

---

## 4. Mối quan hệ giữa các lớp

| Từ lớp             | Có thể gọi đến                 |
| ------------------ | ------------------------------ |
| **UI**             | Application                    |
| **Application**    | Domain, Infrastructure         |
| **Domain**         | Infrastructure (qua interface) |
| **Infrastructure** | Không gọi lên trên             |

👉 **Phụ thuộc luôn đi từ trên xuống dưới**, đảm bảo tính tách biệt và dễ dàng thay thế công nghệ.

---

## 5. Ưu điểm

* Tách biệt rõ ràng giữa **nghiệp vụ** và **kỹ thuật**.
* Dễ **kiểm thử unit test** cho Domain.
* Có thể thay thế công nghệ (ví dụ: đổi DB từ MySQL sang MongoDB) mà không ảnh hưởng đến Domain.

---

## 6. Kết luận

Mô hình **Layered Architecture** giúp đội ngũ phát triển triển khai **Domain-Driven Design** dễ dàng hơn, đảm bảo mã nguồn rõ ràng, dễ bảo trì, và có thể mở rộng về sau.

---

📘 **Tài liệu tham khảo:**

* *Eric Evans - Domain-Driven Design: Tackling Complexity in the Heart of Software*
* *Vaughn Vernon - Implementing Domain-Driven Design*

```

---

Bạn có muốn mình tạo luôn file `.md` để tải về không (ví dụ `layered-architecture-ddd.md`)?  
Mình có thể tạo file và gửi link tải trực tiếp cho bạn.
```
