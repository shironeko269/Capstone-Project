package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.OrderDetailDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailDto> findAllByOrderId(long orderId);
    //duc
    @Transactional
    OrderDetailDto save(OrderDetailDto orderDetailDto, long orderId);
    //huy
    List<OrderDetailDto> findAllByOrderID(long orderID,long userID);
}
