package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    // USER: see own payments
    List<Payment> findByOrderUser(User user);
}
