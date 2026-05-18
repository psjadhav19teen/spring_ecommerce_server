package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    // ✅ Add to cart
    @PostMapping("/add/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addToCart(
            @PathVariable Long productId,
            Authentication authentication
    ) {
        cartService.addToCart(productId, authentication);
        return ResponseEntity.ok("Product added to cart");
    }
    @PutMapping("/increase/{productId}")
    public ResponseEntity<Cart> increase(@PathVariable Long productId,
                                         Authentication auth) {
        return ResponseEntity.ok(cartService.increaseQty(productId, auth));
    }

    @PutMapping("/decrease/{productId}")
    public ResponseEntity<Cart> decrease(@PathVariable Long productId,
                                         Authentication auth) {
        return ResponseEntity.ok(cartService.decreaseQty(productId, auth));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> remove(@PathVariable Long productId,
                                       Authentication auth) {
        return ResponseEntity.ok(cartService.removeItem(productId, auth));
    }

    @GetMapping
    public ResponseEntity<Cart> view(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(auth));
    }
}
