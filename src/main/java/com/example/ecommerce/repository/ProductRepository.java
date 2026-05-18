package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryIgnoreCase(
            String category,
            Pageable pageable
    );

    Page<Product> findByCategoryIgnoreCaseAndSubcategoryIgnoreCase(
            String category,
            String subcategory,
            Pageable pageable
    );

    Page<Product> findByPriceBetween(
            BigDecimal min,
            BigDecimal max,
            Pageable pageable
    );

    Page<Product> findByCategoryIgnoreCaseAndPriceBetween(
            String category,
            BigDecimal min,
            BigDecimal max,
            Pageable pageable
    );

    Page<Product> findByCategoryIgnoreCaseAndSubcategoryIgnoreCaseAndPriceBetween(
            String category,
            String subcategory,
            BigDecimal min,
            BigDecimal max,
            Pageable pageable
    );
}
