package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 🔹 GET + FILTER + PAGINATION
    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subCategory,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productService.getProducts(
                category, subCategory, minPrice, maxPrice, pageable
        );
    }

    // 🔹 CREATE
    @PostMapping
    public Product addProduct(
            @RequestBody Product product,
            Authentication auth
    ) {
        return productService.addProduct(product, auth);
    }

    // 🔹 UPDATE
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product product,
            Authentication auth
    ) {
        return productService.updateProduct(id, product, auth);
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable Long id,
            Authentication auth
    ) {
        productService.deleteProduct(id, auth);
    }
}
