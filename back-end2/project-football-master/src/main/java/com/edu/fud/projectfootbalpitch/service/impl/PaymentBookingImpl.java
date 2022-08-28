package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.PaymentBookingDto;
import com.edu.fud.projectfootbalpitch.dto.PitchTypeDto;
import com.edu.fud.projectfootbalpitch.dto.SubPitchDto;
import com.edu.fud.projectfootbalpitch.entity.PaymentBookingEntity;
import com.edu.fud.projectfootbalpitch.entity.PitchTypeEntity;
import com.edu.fud.projectfootbalpitch.entity.SubPitchEntity;
import com.edu.fud.projectfootbalpitch.repository.PaymentBookFootballPitchRepository;
import com.edu.fud.projectfootbalpitch.service.PaymentBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class PaymentBookingImpl implements PaymentBookingService {
    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private PaymentBookFootballPitchRepository paymentBookingRepository;

    @Override
    public Long save(PaymentBookingDto paymentBookingDto) {
        PaymentBookingEntity myOrder=beanConfig.modelMapper().map(paymentBookingDto,PaymentBookingEntity.class);
        this.paymentBookingRepository.save(myOrder);
        return myOrder.getId();
    }

    @Override
    public PaymentBookingDto findByOrderPaymentPaypalId(String orderId) {
        PaymentBookingEntity paymentBookingEntity=paymentBookingRepository.findByOrderPaymentPaypalId(orderId);
        return beanConfig.modelMapper().map(paymentBookingEntity,PaymentBookingDto.class);
    }

}
