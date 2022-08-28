package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.PaymentBookingDto;

public interface PaymentBookingService {
    Long save(PaymentBookingDto paymentBookingDto);
    PaymentBookingDto findByOrderPaymentPaypalId(String orderId);
}
