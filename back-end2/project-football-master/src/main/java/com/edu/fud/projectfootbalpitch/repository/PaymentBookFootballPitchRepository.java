package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.PaymentBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentBookFootballPitchRepository extends JpaRepository<PaymentBookingEntity,Long> {
    //vi
    PaymentBookingEntity findByOrderPaymentPaypalId(String orderId);
}
