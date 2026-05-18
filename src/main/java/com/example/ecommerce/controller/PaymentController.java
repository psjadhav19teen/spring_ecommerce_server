package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.enums.PaymentMode;
import com.example.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Payment> pay(
            @PathVariable Long orderId,
            @RequestParam PaymentMode paymentMode,
            Authentication auth
    ) {
        Payment payment = paymentService.pay(orderId, paymentMode, auth);
        return ResponseEntity.ok(payment);
    }

    // USER → my payments
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Payment>> myPayments(Authentication auth) {
        return ResponseEntity.ok(paymentService.getMyPayments(auth));
    }

    // ADMIN → all payments
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> allPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
