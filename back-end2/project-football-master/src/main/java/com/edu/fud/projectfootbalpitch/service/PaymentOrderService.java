package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.PaymentOrderDto;

public interface PaymentOrderService {
    //duc
    Long save(PaymentOrderDto paymentBookingDto);

    PaymentOrderDto findByOrderPaymentPaypalId(String orderId);
}
