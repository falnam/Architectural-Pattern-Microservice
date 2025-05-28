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