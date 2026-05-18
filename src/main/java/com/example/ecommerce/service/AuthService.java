package com.example.ecommerce.service;

import com.example.ecommerce.config.JwtUtil;
import com.example.ecommerce.dto.JwtResponse;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.enums.SellerStatus;
import com.example.ecommerce.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setSellerStatus(SellerStatus.NONE);

        userRepo.save(user);
        return "User registered successfully";
    }

//    public JwtResponse login(LoginRequest request) {
//
//        User user = userRepo.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//
//        if (!encoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        // ✅ Generate JWT (STATELESS)
//        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
//
//        return new JwtResponse(token, user.getRole());
//    }

    public JwtResponse login(LoginRequest request, HttpSession session) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // 🔥 STORE JWT IN SESSION
        session.setAttribute("JWT_TOKEN", token);

        return new JwtResponse(user.getRole());
    }

}
