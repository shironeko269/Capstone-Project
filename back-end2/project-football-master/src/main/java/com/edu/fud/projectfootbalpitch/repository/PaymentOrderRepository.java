package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentOrderRepository extends JpaRepository<PaymentEntity,Long> {
    //duc
    @Query(value = "SELECT * FROM order_payment where id = :idPayment ",nativeQuery = true)
    PaymentEntity findIspayment(long idPayment);

    PaymentEntity findByOrderPaymentPaypalId(String orderId);
}
