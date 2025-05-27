# Microservices Architecture dengan Spring Boot

## 🎯 Pengenalan

Tutorial ini akan membantu Anda memahami dan mengimplementasikan **Microservices Architecture** menggunakan Spring Boot. Kita akan membangun sistem yang terdiri dari 4 komponen utama:

- **Eureka Server** - Service Registry
- **User Service** - Layanan pengelolaan pengguna
- **Product Service** - Layanan pengelolaan produk
- **API Gateway** - Pintu gerbang untuk mengakses semua layanan

### 🔍 Apa itu Microservices?

Microservices adalah pola arsitektur di mana aplikasi dibagi menjadi beberapa layanan kecil yang:
- Berjalan secara independen
- Berkomunikasi melalui API
- Dapat dikembangkan dan dideploy secara terpisah
- Memiliki database terpisah

### ✅ Kelebihan Microservices
- Tim dapat bekerja paralel
- Deployment independen
- Skalabilitas per layanan
- Fault tolerance
- Teknologi yang beragam

### ❌ Kekurangan Microservices
- Kompleksitas tinggi
- Infrastruktur tambahan
- Latensi komunikasi
- Debugging yang sulit

## 🛠 Prasyarat

Sebelum memulai, pastikan Anda telah menginstall:

- **Java 17** atau lebih tinggi
- **Eclipse IDE** atau IntelliJ IDEA
- **Maven** (biasanya sudah terintegrasi dengan IDE)
- **Postman** atau tools API testing lainnya

## 🏗 Arsitektur Sistem

```
Client Request
     ↓
API Gateway (8083)
     ↓
┌─────────────────┬─────────────────┐
│   User Service  │ Product Service │
│     (8081)      │     (8082)      │
└─────────────────┴─────────────────┘
     ↓                    ↓
Eureka Server (8761)
```

## 📁 Setup Project

### 1. Struktur Folder

Buat struktur folder untuk workspace:

```
microservices-project/
├── eureka-server/
├── user-service/
├── product-service/
└── api-gateway/
```

### 2. Workspace Eclipse

**Penting**: Setiap service harus dibuka di Eclipse workspace yang terpisah!

## 🚀 Implementasi

### 1. Eureka Server

#### a. Buat Project Baru
1. Buka Eclipse dengan workspace `eureka-server`
2. File → New → Project → Spring Starter Project
3. Konfigurasi:
   - **Name**: `eureka-server`
   - **Type**: `Maven`
   - **Java Version**: `17`

#### b. Dependencies
Pilih dependencies berikut:
- **Eureka Server**
- **Spring Boot Actuator**

#### c. Konfigurasi

**EurekaServerApplication.java**
```java
package com.example.demo;

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

**application.properties**
```properties
spring.application.name=eureka-server
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

### 2. User Service

#### a. Setup Project
1. Buka Eclipse baru dengan workspace `user-service`
2. Buat Spring Starter Project dengan nama `user-service`

#### b. Dependencies
- **Spring Web**
- **Eureka Discovery Client**
- **Spring Boot Actuator**

#### c. Konfigurasi

**application.properties**
```properties
server.port=8081
spring.application.name=user-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

**UserServiceApplication.java**
```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

#### d. Controller

Buat package `com.example.demo.controller` dan file `UserController.java`:

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @GetMapping("/")
    public String getUser() {
        return "Test User";
    }
}
```

### 3. Product Service

#### a. Setup Project
1. Buka Eclipse baru dengan workspace `product-service`
2. Buat Spring Starter Project dengan nama `product-service`

#### b. Dependencies
Same sebagai User Service:
- **Spring Web**
- **Eureka Discovery Client**
- **Spring Boot Actuator**

#### c. Konfigurasi

**application.properties**
```properties
server.port=8082
spring.application.name=product-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

**ProductServiceApplication.java**
```java
package com.example.demo;

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

#### d. Controller

Buat `ProductController.java`:

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    @GetMapping("/")
    public String getProduct() {
        return "Test Product";
    }
}
```

### 4. API Gateway

#### a. Setup Project
1. Buka Eclipse baru dengan workspace `api-gateway`
2. Buat Spring Starter Project dengan nama `api-gateway`

#### b. Dependencies
- **Eureka Discovery Client**
- **Spring Boot Actuator**

#### c. Tambah Dependencies Manual

Edit `pom.xml` dan tambahkan:

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

#### d. Konfigurasi

**application.properties**
```properties
server.port=8083
spring.application.name=api-gateway
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Routing Configuration
spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://user-service
spring.cloud.gateway.routes[0].predicates[0]=Path=/users/**

spring.cloud.gateway.routes[1].id=product-service
spring.cloud.gateway.routes[1].uri=lb://product-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/products/**
```

**ApiGatewayApplication.java**
```java
package com.example.demo;

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

## 🧪 Testing

### 1. Menjalankan Semua Service

Jalankan service dalam urutan berikut:
1. **Eureka Server** (8761)
2. **User Service** (8081)  
3. **Product Service** (8082)
4. **API Gateway** (8083)

### 2. Verifikasi Eureka Dashboard

Buka browser dan akses: `http://localhost:8761`

Anda akan melihat semua service yang terdaftar.

### 3. Test API Endpoints

#### Via API Gateway:

**Test User Service:**
```bash
GET http://localhost:8083/users/
Response: "Test User"
```

**Test Product Service:**
```bash
GET http://localhost:8083/products/
Response: "Test Product"
```

#### Via Direct Service:

**Direct User Service:**
```bash
GET http://localhost:8081/users/
Response: "Test User"
```

**Direct Product Service:**
```bash
GET http://localhost:8082/products/
Response: "Test Product"
```

## 🔄 Komunikasi Antar Service

### 1. Konfigurasi RestTemplate

Tambahkan di `UserServiceApplication.java`:

```java
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

### 2. Update User Controller

```java
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/")
    public String getUser() {
        return "Test User";
    }
    
    @GetMapping("/product")
    public String getProductMessage() {
        String productMessage = restTemplate.getForObject(
            "http://localhost:8083/products/", 
            String.class
        );
        return "User Service calling Product Service: " + productMessage;
    }
}
```

### 3. Test Komunikasi Antar Service

```bash
GET http://localhost:8083/users/product
Response: "User Service calling Product Service: Test Product"
```

## 🔧 Troubleshooting

### Common Issues:

1. **Service tidak terdaftar di Eureka**
   - Pastikan `@EnableDiscoveryClient` sudah ditambahkan
   - Check konfigurasi `eureka.client.service-url.defaultZone`

2. **Port sudah digunakan**
   - Ganti port di `application.properties`
   - Kill proses yang menggunakan port tersebut

3. **Gateway routing tidak bekerja**
   - Periksa konfigurasi routing di `application.properties`
   - Pastikan service sudah terdaftar di Eureka

4. **Dependencies error**
   - Pastikan Spring Cloud version compatible
   - Check `pom.xml` untuk missing dependencies

### Health Check Endpoints:

- Eureka: `http://localhost:8761/actuator/health`
- User Service: `http://localhost:8081/actuator/health`
- Product Service: `http://localhost:8082/actuator/health`
- API Gateway: `http://localhost:8083/actuator/health`

## 🎉 Kesimpulan

Selamat! Anda telah berhasil mengimplementasikan Microservices Architecture dengan Spring Boot. Sistem yang telah dibuat mencakup:

- ✅ Service Registry dengan Eureka Server
- ✅ Multiple independent services
- ✅ API Gateway untuk routing
- ✅ Service-to-service communication
- ✅ Load balancing dengan Ribbon

### Next Steps:
- Implementasi database untuk setiap service
- Menambahkan authentication & authorization
- Implementasi circuit breaker dengan Hystrix
- Containerization dengan Docker
- Monitoring dan logging terpusat

---

**Happy Coding! 🚀**
