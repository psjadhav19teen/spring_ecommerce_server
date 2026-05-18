package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderService orderService;

    /* PLACE ORDER */
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(Authentication auth) {
        return ResponseEntity.ok(orderService.placeOrder(auth));
    }

    /* VIEW ALL MY ORDERS */
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getMyOrders(auth));
    }

    /* VIEW SINGLE ORDER */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId,
                                          Authentication auth) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, auth));
    }

    /* CANCEL ORDER */
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancel(@PathVariable Long orderId,
                                        Authentication auth) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, auth));
    }
}
