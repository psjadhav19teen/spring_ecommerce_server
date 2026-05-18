package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.enums.SellerStatus;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    // 🔹 CREATE
    public Product addProduct(Product product, Authentication auth) {

        User user = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if ((user.getRole() == Role.SELLER &&
                user.getSellerStatus() == SellerStatus.APPROVED)
                || user.getRole() == Role.ADMIN) {

            product.setSeller(user);
            return productRepo.save(product);
        }

        throw new AccessDeniedException("Not authorized to add product");
    }

    // 🔹 READ (FILTER + PAGINATION)
    public Page<Product> getProducts(
            String category,
            String subcategory,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {

        if (category != null && subcategory != null && minPrice != null && maxPrice != null) {
            return productRepo
                    .findByCategoryIgnoreCaseAndSubcategoryIgnoreCaseAndPriceBetween(
                            category, subcategory, minPrice, maxPrice, pageable);
        }

        if (category != null && minPrice != null && maxPrice != null) {
            return productRepo
                    .findByCategoryIgnoreCaseAndPriceBetween(
                            category, minPrice, maxPrice, pageable);
        }

        if (category != null && subcategory != null) {
            return productRepo
                    .findByCategoryIgnoreCaseAndSubcategoryIgnoreCase(
                            category, subcategory, pageable);
        }

        if (minPrice != null && maxPrice != null) {
            return productRepo.findByPriceBetween(minPrice, maxPrice, pageable);
        }

        if (category != null) {
            return productRepo.findByCategoryIgnoreCase(category, pageable);
        }

        return productRepo.findAll(pageable);
    }

    // 🔹 UPDATE
    public Product updateProduct(Long id, Product updated, Authentication auth) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN ||
                product.getSeller().getId().equals(user.getId())) {

            product.setName(updated.getName());
            product.setPrice(updated.getPrice());
            product.setCategory(updated.getCategory());
            product.setSubcategory(updated.getSubcategory());
            product.setDescription(updated.getDescription());
            product.setImageUrl(updated.getImageUrl());

            return productRepo.save(product);
        }

        throw new AccessDeniedException("Not allowed to update product");
    }

    // 🔹 DELETE
    public void deleteProduct(Long id, Authentication auth) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN ||
                product.getSeller().getId().equals(user.getId())) {

            productRepo.delete(product);
            return;
        }

        throw new AccessDeniedException("Not allowed to delete product");
    }
}
