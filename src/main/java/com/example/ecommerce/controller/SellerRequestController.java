package com.example.ecommerce.controller;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.SellerStatus;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerRequestController {

    private final UserRepository userRepo;

    @PostMapping("/request")
    public String requestSeller(Authentication auth) {

        String email = auth.getName();   // ✅ principal is email
        User user = userRepo.findByEmail(email).orElseThrow();

        user.setSellerStatus(SellerStatus.REQUESTED);
        userRepo.save(user);

        return "Seller request sent to admin";
    }
}


