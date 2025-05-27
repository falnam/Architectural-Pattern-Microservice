# Microservices dengan Spring Boot

## 🎯 Penjelasan Singkat

**Microservices** adalah cara membangun aplikasi dengan membaginya menjadi bagian-bagian kecil yang independen. Setiap bagian bertanggung jawab atas satu fungsi spesifik dan dapat berjalan sendiri.

**Perbedaan dengan Monolithic:**
- **Monolithic**: Seperti rumah besar dengan semua ruangan tergabung - jika satu ruangan rusak, seluruh rumah terganggu
- **Microservices**: Seperti kompleks perumahan dengan rumah-rumah kecil terpisah - jika satu rumah rusak, rumah lain tetap berfungsi normal

## 🏪 Real Case: Sistem E-Commerce

Kita akan membangun sistem e-commerce sederhana dengan komponen:

1. **User Service**: Mengelola data pelanggan (register, login, profil)
2. **Product Service**: Mengelola data produk (katalog, harga, stok)
3. **API Gateway**: Pintu masuk untuk semua request dari aplikasi web/mobile
4. **Eureka Server**: Direktori yang tahu dimana semua service berada

**Skenario Penggunaan:**
- Customer buka aplikasi e-commerce
- Aplikasi kirim request ke API Gateway
- Gateway forward ke User Service untuk data profil
- Gateway forward ke Product Service untuk data katalog
- Semua data dikembalikan ke aplikasi customer

## 📁 Struktur Project

```
microservices-ecommerce/
├── eureka-server/          # Service Registry
├── api-gateway/           # Pintu masuk utama
├── user-service/          # Service untuk data user
└── product-service/       # Service untuk data produk
```

## 🚀 Langkah-langkah Implementasi

### STEP 1: Persiapan Workspace

1. Buat folder utama: `microservices-ecommerce`
2. Di dalamnya buat 4 subfolder:
   ```
   microservices-ecommerce/
   ├── eureka-server/
   ├── api-gateway/
   ├── user-service/
   └── product-service/
   ```

**⚠️ PENTING**: Setiap service harus dibuka di Eclipse yang terpisah dengan workspace yang berbeda!

---

### STEP 2: Membuat Eureka Server (Service Registry)

**Analogi**: Eureka Server seperti buku telepon/direktori mall yang tahu alamat semua toko.

#### 2.1 Setup Project

1. Buka Eclipse → pilih workspace: `microservices-ecommerce/eureka-server`
2. File → New → Project → Spring Starter Project
3. Isi detail:
   - **Name**: `eureka-server`
   - **Type**: Maven
   - **Java Version**: 17
   - **Group**: `com.ecommerce`
   - **Artifact**: `eureka-server`

4. Pilih Dependencies:
   - ✅ **Eureka Server**
   - ✅ **Spring Boot Actuator**

#### 2.2 Struktur Folder Eureka Server
```
eureka-server/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/ecommerce/eurekaserver/
│       │       └── EurekaServerApplication.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── target/
```

#### 2.3 Konfigurasi Code

**File: `EurekaServerApplication.java`**
```java
package com.ecommerce.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  // 👈 Ini yang bikin jadi Eureka Server
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**File: `application.properties`**
```properties
# Nama aplikasi
spring.application.name=eureka-server

# Port untuk Eureka Server (standar 8761)
server.port=8761

# Eureka tidak perlu register ke dirinya sendiri
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

**Penjelasan Konfigurasi:**
- `@EnableEurekaServer`: Mengaktifkan fungsi Eureka Server
- `register-with-eureka=false`: Server tidak register ke dirinya sendiri
- `fetch-registry=false`: Server tidak ambil data registry dari tempat lain

---

### STEP 3: Membuat User Service

**Analogi**: User Service seperti toko yang khusus jual informasi customer (KTP, alamat, dll).

#### 3.1 Setup Project

1. Buka Eclipse baru → workspace: `microservices-ecommerce/user-service`
2. Buat Spring Starter Project:
   - **Name**: `user-service`
   - **Group**: `com.ecommerce`
   - **Artifact**: `user-service`

3. Dependencies:
   - ✅ **Spring Web** (untuk REST API)
   - ✅ **Eureka Discovery Client** (untuk register ke Eureka)
   - ✅ **Spring Boot Actuator** (untuk monitoring)

#### 3.2 Struktur Folder User Service
```
user-service/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/ecommerce/userservice/
│       │       ├── UserServiceApplication.java
│       │       ├── controller/
│       │       │   └── UserController.java
│       │       └── model/
│       │           └── User.java
│       └── resources/
│           └── application.properties
└── pom.xml
```

#### 3.3 Konfigurasi Code

**File: `application.properties`**
```properties
# Port untuk User Service
server.port=8081

# Nama service (harus unik)
spring.application.name=user-service

# Alamat Eureka Server untuk register
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

**File: `UserServiceApplication.java`**
```java
package com.ecommerce.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 👈 Ini yang bikin service register ke Eureka
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

**File: `model/User.java`**
```java
package com.ecommerce.userservice.model;

public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    
    // Constructor
    public User() {}
    
    public User(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters dan Setters
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

**File: `controller/UserController.java`**
```java
package com.ecommerce.userservice.controller;

import org.springframework.web.bind.annotation.*;
import com.ecommerce.userservice.model.User;
import java.util.*;

@RestController
@RequestMapping("/users")  // Base URL: /users
public class UserController {
    
    // Data dummy untuk simulasi database
    private List<User> users = Arrays.asList(
        new User(1L, "John Doe", "john@email.com", "081234567890"),
        new User(2L, "Jane Smith", "jane@email.com", "081234567891"),
        new User(3L, "Bob Johnson", "bob@email.com", "081234567892")
    );
    
    // GET /users - Ambil semua user
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
    
    // GET /users/{id} - Ambil user berdasarkan ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return users.stream()
                   .filter(user -> user.getId().equals(id))
                   .findFirst()
                   .orElse(null);
    }
    
    // GET /users/info - Info service
    @GetMapping("/info")
    public String getServiceInfo() {
        return "User Service is running on port 8081";
    }
}
```

---

### STEP 4: Membuat Product Service

**Analogi**: Product Service seperti toko yang khusus jual katalog produk (nama, harga, stok).

#### 4.1 Setup Project

1. Eclipse baru → workspace: `microservices-ecommerce/product-service`
2. Dependencies sama dengan User Service:
   - ✅ **Spring Web**
   - ✅ **Eureka Discovery Client**
   - ✅ **Spring Boot Actuator**

#### 4.2 Struktur Folder Product Service
```
product-service/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/ecommerce/productservice/
│       │       ├── ProductServiceApplication.java
│       │       ├── controller/
│       │       │   └── ProductController.java
│       │       └── model/
│       │           └── Product.java
│       └── resources/
│           └── application.properties
└── pom.xml
```

#### 4.3 Konfigurasi Code

**File: `application.properties`**
```properties
server.port=8082
spring.application.name=product-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

**File: `ProductServiceApplication.java`**
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

**File: `model/Product.java`**
```java
package com.ecommerce.productservice.model;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    
    // Constructor
    public Product() {}
    
    public Product(Long id, String name, String description, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }
    
    // Getters dan Setters
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

**File: `controller/ProductController.java`**
```java
package com.ecommerce.productservice.controller;

import org.springframework.web.bind.annotation.*;
import com.ecommerce.productservice.model.Product;
import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    // Data dummy produk
    private List<Product> products = Arrays.asList(
        new Product(1L, "Laptop", "Gaming Laptop 16GB RAM", 15000000.0, 10),
        new Product(2L, "Mouse", "Wireless Gaming Mouse", 500000.0, 25),
        new Product(3L, "Keyboard", "Mechanical RGB Keyboard", 1200000.0, 15)
    );
    
    // GET /products - Ambil semua produk
    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }
    
    // GET /products/{id} - Ambil produk berdasarkan ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return products.stream()
                      .filter(product -> product.getId().equals(id))
                      .findFirst()
                      .orElse(null);
    }
    
    // GET /products/info - Info service
    @GetMapping("/info")
    public String getServiceInfo() {
        return "Product Service is running on port 8082";
    }
}
```

---

### STEP 5: Membuat API Gateway

**Analogi**: API Gateway seperti security/receptionist mall yang mengatur semua tamu yang masuk dan mengarahkan ke toko yang tepat.

#### 5.1 Setup Project

1. Eclipse baru → workspace: `microservices-ecommerce/api-gateway`
2. Dependencies:
   - ✅ **Eureka Discovery Client**
   - ✅ **Spring Boot Actuator**

#### 5.2 Tambah Dependencies Manual

Karena Spring Cloud Gateway tidak ada di starter, tambah manual di `pom.xml`:

**File: `pom.xml`** (tambahkan di dalam `<dependencies>`)
```xml
<dependencies>
    <!-- Dependencies yang sudah ada -->
    
    <!-- Tambahan untuk Gateway -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
</dependencies>

<!-- Tambahkan juga dependency management -->
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

#### 5.3 Struktur Folder API Gateway
```
api-gateway/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/ecommerce/apigateway/
│       │       └── ApiGatewayApplication.java
│       └── resources/
│           └── application.properties
└── pom.xml
```

#### 5.4 Konfigurasi Code

**File: `application.properties`**
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
```

**Penjelasan Routing:**
- `lb://user-service`: Load balancer akan cari service bernama "user-service"
- `Path=/users/**`: Semua request yang mulai dengan `/users/` akan diarahkan ke user-service

**File: `ApiGatewayApplication.java`**
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

---

### STEP 6: Menjalankan Semua Service

#### 6.1 Urutan Menjalankan
1. **Eureka Server** (port 8761) - harus pertama
2. **User Service** (port 8081)
3. **Product Service** (port 8082)
4. **API Gateway** (port 8083) - harus terakhir

#### 6.2 Cara Menjalankan
Di setiap Eclipse, klik kanan pada main class → Run As → Java Application

#### 6.3 Verifikasi
1. Buka browser: `http://localhost:8761`
2. Cek apakah semua service terdaftar:
   - USER-SERVICE
   - PRODUCT-SERVICE
   - API-GATEWAY

---

### STEP 7: Testing API

Gunakan Postman, browser, atau curl untuk test:

#### 7.1 Test User Service via Gateway
```
GET http://localhost:8083/users
GET http://localhost:8083/users/1
GET http://localhost:8083/users/info
```

#### 7.2 Test Product Service via Gateway
```
GET http://localhost:8083/products
GET http://localhost:8083/products/1
GET http://localhost:8083/products/info
```

---

### STEP 8: Komunikasi Antar Service

Mari buat User Service bisa mengambil data dari Product Service.

#### 8.1 Update User Service

**File: `UserServiceApplication.java`** - tambah RestTemplate Bean
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

**File: `UserController.java`** - tambah method baru
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
    
    // ... method yang sudah ada ...
    
    // Method baru: User bisa lihat semua produk
    @GetMapping("/products")
    public String getUserProducts() {
        try {
            // Panggil Product Service via Gateway
            String products = restTemplate.getForObject(
                "http://localhost:8083/products", 
                String.class
            );
            return "Products available for users: " + products;
        } catch (Exception e) {
            return "Error connecting to Product Service: " + e.getMessage();
        }
    }
    
    // Method baru: User dashboard dengan info produk
    @GetMapping("/{id}/dashboard")
    public Map<String, Object> getUserDashboard(@PathVariable Long id) {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Ambil data user
        User user = getUserById(id);
        dashboard.put("user", user);
        
        // Ambil info produk service
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

#### 8.2 Test Komunikasi Antar Service
```
GET http://localhost:8083/users/products
GET http://localhost:8083/users/1/dashboard
```

---

## 🎉 Hasil Akhir

Setelah mengikuti tutorial ini, Anda akan memiliki:

1. **Eureka Server** - Registry untuk semua service
2. **User Service** - Kelola data pengguna
3. **Product Service** - Kelola data produk
4. **API Gateway** - Pintu masuk tunggal
5. **Komunikasi antar service** - User bisa akses Product

## 🔍 Debugging Tips

### Jika Service Tidak Muncul di Eureka:
1. Cek `application.properties` - pastikan nama service benar
2. Cek apakah Eureka Server jalan di port 8761
3. Cek annotation `@EnableDiscoveryClient` ada

### Jika Gateway Tidak Bisa Route:
1. Cek konfigurasi routing di `application.properties`
2. Pastikan service sudah register ke Eureka
3. Cek port tidak bentrok

### Jika Komunikasi Antar Service Error:
1. Cek URL yang dipanggil (harus via Gateway)
2. Pastikan RestTemplate Bean sudah dibuat
3. Cek network/firewall

## 📚 Kesimpulan

Microservices memberikan fleksibilitas dalam:
- **Development**: Tim bisa kerja parallel
- **Deployment**: Deploy service satu-satu
- **Scaling**: Scale sesuai kebutuhan
- **Maintenance**: Update tanpa ganggu yang lain

Dengan tutorial ini, Anda sudah memahami dasar-dasar microservices dan siap untuk mengembangkan aplikasi yang lebih kompleks!

## 🚀 Next Steps

1. Tambah database (MySQL/PostgreSQL) ke setiap service
2. Implementasi authentication dengan JWT
3. Tambah monitoring dengan Spring Boot Admin
4. Implementasi Circuit Breaker untuk fault tolerance
5. Containerization dengan Docker
