package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.enums.SellerStatus;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepo;

    @PostMapping("/approve-seller/{id}")
    public ResponseEntity<String> approveSeller(@PathVariable Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (user.getSellerStatus() == SellerStatus.APPROVED) {
            return ResponseEntity.badRequest().body("Seller already approved");
        }

        user.setRole(Role.SELLER);
        user.setSellerStatus(SellerStatus.APPROVED);
        userRepo.save(user);

        return ResponseEntity.ok("Seller approved successfully");
    }
}



