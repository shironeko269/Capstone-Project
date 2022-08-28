package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.PaymentOrderDto;
import com.edu.fud.projectfootbalpitch.entity.PaymentEntity;
import com.edu.fud.projectfootbalpitch.repository.PaymentOrderRepository;
import com.edu.fud.projectfootbalpitch.service.PaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Override
    public Long save(PaymentOrderDto paymentOrderDto) {
        PaymentEntity myOrder=beanConfig.modelMapper().map(paymentOrderDto,PaymentEntity.class);
        this.paymentOrderRepository.save(myOrder);
        return myOrder.getId();
    }

    @Override
    public PaymentOrderDto findByOrderPaymentPaypalId(String orderId) {
        PaymentEntity paymentEntity=paymentOrderRepository.findByOrderPaymentPaypalId(orderId);
        return beanConfig.modelMapper().map(paymentEntity,PaymentOrderDto.class);
    }

}
