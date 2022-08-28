package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.OrderDto;
import org.aspectj.weaver.ast.Or;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDto> findALl();
    OrderDto findOne(long id);
    Optional<OrderDto> findById(long id);
    OrderDto save(OrderDto orderDto);
    void deleteOrder(long orderId);
    //duc
    OrderDto saveOfDuc(OrderDto orderDto, String username, long idpayment);
    //huy
    List<OrderDto> findAllByUserID(long id);
    void deleteOrderID(long id);
}
