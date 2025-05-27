# Microservices E-Commerce dengan Spring Boot

Aplikasi e-commerce berbasis microservices yang terdiri dari User Service, Product Service, API Gateway, dan Eureka Server untuk service discovery.

## 📋 Daftar Isi

- [Penjelasan Singkat](#penjelasan-singkat)
- [Arsitektur Sistem](#arsitektur-sistem)
- [Prasyarat](#prasyarat)
- [Struktur Project](#struktur-project)
- [Instalasi dan Setup](#instalasi-dan-setup)
- [Konfigurasi Detail](#konfigurasi-detail)
- [Menjalankan Aplikasi](#menjalankan-aplikasi)
- [Testing API](#testing-api)
- [Troubleshooting](#troubleshooting)
- [FAQ](#faq)

## 🎯 Penjelasan Singkat

**Microservices** adalah arsitektur yang membagi aplikasi menjadi layanan-layanan kecil yang independen. Setiap service memiliki tanggung jawab spesifik dan dapat dikembangkan, di-deploy, dan di-scale secara terpisah.

### Perbedaan dengan Monolithic:
- **Monolithic**: Seperti rumah besar dengan semua ruangan tergabung - jika satu ruangan rusak, seluruh rumah terganggu
- **Microservices**: Seperti kompleks perumahan dengan rumah-rumah kecil terpisah - jika satu rumah rusak, rumah lain tetap berfungsi normal

### Komponen Sistem:
1. **Eureka Server** (Port 8761): Service Registry - direktori yang mengetahui lokasi semua service
2. **User Service** (Port 8081): Mengelola data pelanggan (register, login, profil)
3. **Product Service** (Port 8082): Mengelola data produk (katalog, harga, stok)
4. **API Gateway** (Port 8083): Pintu masuk tunggal untuk semua request dari client

## 🏗️ Arsitektur Sistem

```
┌─────────────────┐    ┌─────────────────┐
│   Client App    │    │   Admin Panel   │
│  (Web/Mobile)   │    │                 │
└─────────┬───────┘    └─────────┬───────┘
          │                      │
          └──────────┬───────────┘
                     │
            ┌────────▼────────┐
            │   API Gateway   │
            │   (Port 8083)   │
            └────────┬────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
   ┌────▼────┐  ┌───▼────┐  ┌───▼────────┐
   │  User   │  │Product │  │   Eureka   │
   │Service  │  │Service │  │   Server   │
   │(8081)   │  │(8082)  │  │   (8761)   │
   └─────────┘  └────────┘  └────────────┘
```

## 📋 Prasyarat

### Software yang Diperlukan:
- **Java Development Kit (JDK) 17** atau lebih tinggi
- **Eclipse IDE** (Spring Tool Suite direkomendasikan)
- **Maven** (biasanya sudah include di Eclipse)
- **Git** (untuk version control)
- **Postman** atau **Thunder Client** untuk testing API

### Verifikasi Instalasi:
```bash
java -version
# Output: java version "17.x.x"

mvn -version
# Output: Apache Maven 3.x.x
```

## 📁 Struktur Project

```
microservices-ecommerce/
├── eureka-server/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/ecommerce/eurekaserver/
│   │       │       └── EurekaServerApplication.java
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
├── user-service/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/ecommerce/userservice/
│   │       │       ├── UserServiceApplication.java
│   │       │       ├── controller/
│   │       │       │   └── UserController.java
│   │       │       └── model/
│   │       │           └── User.java
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
├── product-service/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/ecommerce/productservice/
│   │       │       ├── ProductServiceApplication.java
│   │       │       ├── controller/
│   │       │       │   └── ProductController.java
│   │       │       └── model/
│   │       │           └── Product.java
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
├── api-gateway/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/ecommerce/apigateway/
│   │       │       └── ApiGatewayApplication.java
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
└── README.md
```

## 🚀 Instalasi dan Setup

### STEP 1: Persiapan Workspace

1. **Buat folder utama:**
   ```bash
   mkdir microservices-ecommerce
   cd microservices-ecommerce
   ```

2. **Buat subfolder untuk setiap service:**
   ```bash
   mkdir eureka-server
   mkdir user-service
   mkdir product-service
   mkdir api-gateway
   ```

⚠️ **PENTING**: Setiap service harus dibuka di Eclipse dengan workspace yang terpisah!

### STEP 2: Setup Eureka Server

#### 2.1 Buat Project di Eclipse
1. Buka Eclipse → pilih workspace: `microservices-ecommerce/eureka-server`
2. File → New → Project → Spring Starter Project
3. **Project Details:**
   - Name: `eureka-server`
   - Type: Maven
   - Java Version: 17
   - Group: `com.ecommerce`
   - Artifact: `eureka-server`
   - Package: `com.ecommerce.eurekaserver`

4. **Dependencies:**
   - ✅ Eureka Server
   - ✅ Spring Boot Actuator

#### 2.2 Konfigurasi Eureka Server

**File: `src/main/java/com/ecommerce/eurekaserver/EurekaServerApplication.java`**
```java
package com.ecommerce.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**File: `src/main/resources/application.properties`**
```properties
# Nama aplikasi
spring.application.name=eureka-server

# Port untuk Eureka Server
server.port=8761

# Eureka tidak register ke dirinya sendiri
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

# URL dashboard Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### STEP 3: Setup User Service

#### 3.1 Buat Project di Eclipse
1. Buka Eclipse baru → workspace: `microservices-ecommerce/user-service`
2. **Dependencies:**
   - ✅ Spring Web
   - ✅ Eureka Discovery Client
   - ✅ Spring Boot Actuator

#### 3.2 Konfigurasi User Service

**File: `src/main/resources/application.properties`**
```properties
# Port untuk User Service
server.port=8081

# Nama service (harus unik)
spring.application.name=user-service

# Alamat Eureka Server
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Health check endpoint
management.endpoints.web.exposure.include=health,info
```

**File: `src/main/java/com/ecommerce/userservice/UserServiceApplication.java`**
```java
package com.ecommerce.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

**File: `src/main/java/com/ecommerce/userservice/model/User.java`**
```java
package com.ecommerce.userservice.model;

public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    
    public User() {}
    
    public User(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
```

**File: `src/main/java/com/ecommerce/userservice/controller/UserController.java`**
```java
package com.ecommerce.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.ecommerce.userservice.model.User;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    // Data dummy untuk simulasi
    private List<User> users = Arrays.asList(
        new User(1L, "John Doe", "john@email.com", "081234567890"),
        new User(2L, "Jane Smith", "jane@email.com", "081234567891"),
        new User(3L, "Bob Johnson", "bob@email.com", "081234567892")
    );
    
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return users.stream()
                   .filter(user -> user.getId().equals(id))
                   .findFirst()
                   .orElse(null);
    }
    
    @GetMapping("/info")
    public Map<String, String> getServiceInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("service", "User Service");
        info.put("port", "8081");
        info.put("status", "Running");
        return info;
    }
    
    // Komunikasi dengan Product Service
    @GetMapping("/products")
    public String getUserProducts() {
        try {
            String products = restTemplate.getForObject(
                "http://localhost:8083/products", 
                String.class
            );
            return "Products available: " + products;
        } catch (Exception e) {
            return "Error connecting to Product Service: " + e.getMessage();
        }
    }
    
    @GetMapping("/{id}/dashboard")
    public Map<String, Object> getUserDashboard(@PathVariable Long id) {
        Map<String, Object> dashboard = new HashMap<>();
        
        User user = getUserById(id);
        dashboard.put("user", user);
        
        try {
            String productInfo = restTemplate.getForObject(
                "http://localhost:8083/products/info", 
                String.class
            );
            dashboard.put("productServiceStatus", productInfo);
        } catch (Exception e) {
            dashboard.put("productServiceStatus", "Product Service unavailable");
        }
        
        return dashboard;
    }
}
```

### STEP 4: Setup Product Service

#### 4.1 Buat Project di Eclipse
1. Eclipse baru → workspace: `microservices-ecommerce/product-service`
2. Dependencies sama dengan User Service

#### 4.2 Konfigurasi Product Service

**File: `src/main/resources/application.properties`**
```properties
server.port=8082
spring.application.name=product-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
management.endpoints.web.exposure.include=health,info
```

**File: `src/main/java/com/ecommerce/productservice/ProductServiceApplication.java`**
```java
package com.ecommerce.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
```

**File: `src/main/java/com/ecommerce/productservice/model/Product.java`**
```java
package com.ecommerce.productservice.model;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    
    public Product() {}
    
    public Product(Long id, String name, String description, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
```

**File: `src/main/java/com/ecommerce/productservice/controller/ProductController.java`**
```java
package com.ecommerce.productservice.controller;

import org.springframework.web.bind.annotation.*;
import com.ecommerce.productservice.model.Product;
import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    private List<Product> products = Arrays.asList(
        new Product(1L, "Laptop", "Gaming Laptop 16GB RAM", 15000000.0, 10),
        new Product(2L, "Mouse", "Wireless Gaming Mouse", 500000.0, 25),
        new Product(3L, "Keyboard", "Mechanical RGB Keyboard", 1200000.0, 15)
    );
    
    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }
    
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return products.stream()
                      .filter(product -> product.getId().equals(id))
                      .findFirst()
                      .orElse(null);
    }
    
    @GetMapping("/info")
    public Map<String, String> getServiceInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("service", "Product Service");
        info.put("port", "8082");
        info.put("status", "Running");
        return info;
    }
}
```

### STEP 5: Setup API Gateway

#### 5.1 Buat Project di Eclipse
1. Eclipse baru → workspace: `microservices-ecommerce/api-gateway`
2. **Dependencies:**
   - ✅ Eureka Discovery Client
   - ✅ Spring Boot Actuator

#### 5.2 Update pom.xml Manual

**File: `pom.xml`** - tambahkan dependencies berikut:
```xml
<dependencies>
    <!-- Dependencies yang sudah ada -->
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2022.0.4</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### 5.3 Konfigurasi API Gateway

**File: `src/main/resources/application.properties`**
```properties
server.port=8083
spring.application.name=api-gateway
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Routing untuk User Service
spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://user-service
spring.cloud.gateway.routes[0].predicates[0]=Path=/users/**

# Routing untuk Product Service
spring.cloud.gateway.routes[1].id=product-service
spring.cloud.gateway.routes[1].uri=lb://product-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/products/**

# CORS Configuration
spring.cloud.gateway.globalcors.cors-configurations.[/**].allowed-origins=*
spring.cloud.gateway.globalcors.cors-configurations.[/**].allowed-methods=GET,POST,PUT,DELETE
spring.cloud.gateway.globalcors.cors-configurations.[/**].allowed-headers=*
```

**File: `src/main/java/com/ecommerce/apigateway/ApiGatewayApplication.java`**
```java
package com.ecommerce.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

## 🎮 Menjalankan Aplikasi

### Urutan Menjalankan (PENTING!)
1. **Eureka Server** (port 8761) - HARUS PERTAMA
2. **User Service** (port 8081)
3. **Product Service** (port 8082)
4. **API Gateway** (port 8083) - HARUS TERAKHIR

### Cara Menjalankan
Di setiap Eclipse:
1. Klik kanan pada main class (Application.java)
2. Run As → Java Application

### Verifikasi
1. **Eureka Dashboard**: `http://localhost:8761`
2. **Cek Service Registration**: Pastikan melihat:
   - USER-SERVICE (1 instance)
   - PRODUCT-SERVICE (1 instance)
   - API-GATEWAY (1 instance)

## 🧪 Testing API

### Test via API Gateway (Recommended)

#### User Service Endpoints:
```bash
# Get all users
GET http://localhost:8083/users

# Get user by ID
GET http://localhost:8083/users/1

# Get service info
GET http://localhost:8083/users/info

# Get user dashboard
GET http://localhost:8083/users/1/dashboard

# User see products (inter-service communication)
GET http://localhost:8083/users/products
```

#### Product Service Endpoints:
```bash
# Get all products
GET http://localhost:8083/products

# Get product by ID
GET http://localhost:8083/products/1

# Get service info
GET http://localhost:8083/products/info
```

### Test Direct Service (For Development)

#### User Service Direct:
```bash
GET http://localhost:8081/users
GET http://localhost:8081/users/1
GET http://localhost:8081/users/info
```

#### Product Service Direct:
```bash
GET http://localhost:8082/products
GET http://localhost:8082/products/1
GET http://localhost:8082/products/info
```

## 🔧 Troubleshooting

### 1. Service Tidak Muncul di Eureka Dashboard

**Symptoms:**
- Dashboard Eureka kosong atau service tidak terdaftar
- Gateway tidak bisa route ke service

**Solusi:**
```bash
# 1. Cek application.properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# 2. Cek annotation di main class
@EnableDiscoveryClient

# 3. Cek Eureka Server berjalan di port 8761
curl http://localhost:8761

# 4. Restart service dalam urutan yang benar
```

### 2. Port Already in Use Error

**Error Message:**
```
***************************
APPLICATION FAILED TO START
***************************

Description:
Web server failed to start. Port 8081 was already in use.
```

**Solusi:**
```bash
# 1. Cek port yang digunakan
netstat -tulpn | grep :8081

# 2. Kill process yang menggunakan port
kill -9 <PID>

# 3. Atau ganti port di application.properties
server.port=8084
```

### 3. Gateway Routing Error

**Symptoms:**
- 404 Not Found saat akses via Gateway
- Gateway bisa akses tapi service error

**Solusi:**
```properties
# 1. Cek routing configuration
spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://user-service  # lb = load balancer
spring.cloud.gateway.routes[0].predicates[0]=Path=/users/**

# 2. Pastikan service name sama dengan spring.application.name
spring.application.name=user-service

# 3. Cek logs Gateway untuk error detail
```

### 4. Inter-Service Communication Error

**Error Message:**
```
Error connecting to Product Service: Connection refused
```

**Solusi:**
```java
// 1. Pastikan menggunakan Gateway URL, bukan direct service
String products = restTemplate.getForObject(
    "http://localhost:8083/products",  // Via Gateway
    String.class
);

// 2. Atau gunakan service discovery
@LoadBalanced  // Tambah annotation ini
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}

// Kemudian bisa pakai service name
"http://product-service/products"
```

### 5. Maven Dependencies Error

**Error Message:**
```
Could not resolve dependencies for project
```

**Solusi:**
```bash
# 1. Update Maven project
Right click project → Maven → Reload Projects

# 2. Clean install
mvn clean install

# 3. Cek pom.xml dependency management
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2022.0.4</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 6. Gateway WebFlux vs WebMVC Conflict

**Error Message:**
```
Spring MVC found on classpath, which is incompatible with Spring Cloud Gateway
```

**Solusi:**
```xml
<!-- Jangan include spring-boot-starter-web di Gateway -->
<!-- Hanya gunakan spring-boot-starter-webflux -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

### 7. Eureka Client Config Error

**Error Message:**
```
DiscoveryClient_UNKNOWN/unknown
```

**Solusi:**
```properties
# Tambah config berikut di application.properties
eureka.instance.instance-id=${spring.application.name}:${server.port}
eureka.instance.prefer-ip-address=true
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
```

## ❓ FAQ

### Q: Kenapa harus buka Eclipse terpisah untuk setiap service?
**A:** Setiap service adalah project independen dengan workspace sendiri. Ini memisahkan dependencies dan konfigurasi antar service.

### Q: Bisa ganti port service tidak?
**A:** Bisa, ubah di `application.properties`:
```properties
server.port=9001  # Ganti sesuai kebutuhan
```

### Q: Bagaimana cara tambah service baru?
**A:** 
1. Buat project Spring Boot baru
2. Tambah dependency `Eureka Discovery Client`
3. Tambah `@EnableDiscoveryClient`
4. Konfigurasi di `application.properties`
5. Tambah routing di API Gateway

### Q: Database seperti apa yang cocok untuk microservices?
**A:** Setiap service sebaiknya punya database sendiri:
- User Service → User Database
- Product Service → Product Database
- Order Service → Order Database

### Q: Bagaimana monitoring semua service?
**A:** Gunakan:
- Spring Boot Admin
- Micrometer + Prometheus
- ELK Stack (Elasticsearch, Logstash, Kibana)

### Q: Production deployment seperti apa?
**A:** 
1. Containerize dengan Docker
2. Orchestration dengan Kubernetes
3. Service Mesh dengan Istio
4. CI/CD dengan Jenkins/GitLab

## 🚀 Next Steps

1. **Database Integration**: Tambah JPA dan database untuk setiap service
2. **Security**: Implementasi JWT authentication dan authorization
3. **Message Queue**: Tambah RabbitMQ atau Kafka untuk async communication
4. **Circuit Breaker**: Implementasi Hystrix untuk fault tolerance
5. **Monitoring**: Setup Spring Boot Admin dan logging
6. **Docker**: Containerize semua service
7. **Testing**: Unit test, integration test, dan contract testing

## 📚 Resources

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Microservices Patterns](https://microservices.io/patterns/)

---

**Happy Coding! 🎉**

Jika ada pertanyaan atau error yang tidak tercovered di sini, silakan buat issue atau tanya langsung!
