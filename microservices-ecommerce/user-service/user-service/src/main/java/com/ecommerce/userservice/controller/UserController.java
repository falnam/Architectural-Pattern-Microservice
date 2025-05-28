package com.ecommerce.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.ecommerce.userservice.model.User;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private RestTemplate restTemplate;
    
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
    
    // Method yang diperbaiki - komunikasi dengan Product Service
    @GetMapping("/products")
    public ResponseEntity<?> getUserProducts() {
        try {
            // Panggil Product Service melalui nama service (bukan localhost)
            Object products = restTemplate.getForObject(
                "http://product-service/products",  // 👈 UBAH INI
                Object.class
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Products available for users");
            response.put("products", products);
            response.put("source", "Called from User Service");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Cannot connect to Product Service");
            errorResponse.put("details", e.getMessage());
            errorResponse.put("suggestion", "Make sure Product Service is running and registered to Eureka");
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    // Method dashboard yang diperbaiki
    @GetMapping("/{id}/dashboard")
    public ResponseEntity<?> getUserDashboard(@PathVariable Long id) {
        Map<String, Object> dashboard = new HashMap<>();
        
        try {
            // Ambil data user
            User user = getUserById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            dashboard.put("user", user);
            
            // Ambil info dari Product Service
            String productInfo = restTemplate.getForObject(
                "http://product-service/products/info",  // 👈 UBAH INI JUGA
                String.class
            );
            dashboard.put("productServiceStatus", productInfo);
            dashboard.put("status", "All services connected");
            
            return ResponseEntity.ok(dashboard);
            
        } catch (Exception e) {
            dashboard.put("user", getUserById(id));
            dashboard.put("productServiceStatus", "Product Service unavailable");
            dashboard.put("error", e.getMessage());
            dashboard.put("status", "Partial service failure");
            
            return ResponseEntity.ok(dashboard);
        }
    }
}