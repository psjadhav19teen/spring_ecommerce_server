package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMode;
import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;

    public Payment pay(Long orderId, PaymentMode paymentMode, Authentication auth) {

        // ✅ Get logged-in user from JWT
        String email = auth.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Fetch order
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ✅ Validate ownership
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can pay only for your own orders");
        }

        // ❌ Cancelled order check
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot pay for cancelled order");
        }

        // ❌ Already paid check
        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Order already paid");
        }

        // ✅ Update order status
        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);

        // ✅ Create payment record
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMode(paymentMode);

        return paymentRepo.save(payment);
    }

    // USER → own payments
    public List<Payment> getMyPayments(Authentication auth) {

        User user = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentRepo.findByOrderUser(user);
    }

    // ADMIN → all payments
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }
}
