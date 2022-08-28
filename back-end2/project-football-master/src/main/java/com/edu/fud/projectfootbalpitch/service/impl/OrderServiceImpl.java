package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.OrderDto;
import com.edu.fud.projectfootbalpitch.entity.*;
import com.edu.fud.projectfootbalpitch.repository.*;
import com.edu.fud.projectfootbalpitch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private BeanConfig beanConfig;
    @Autowired
    private StatusOrderRepository statusOrderRepository;
    @Autowired
    private PaymentOrderRepository paymentOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDto> findALl() {
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<OrderEntity> orderEntityList = ordersRepository.findAll();
        for (OrderEntity orderEntity : orderEntityList
        ) {
            OrderDto orderDto = beanConfig.modelMapper().map(orderEntity, OrderDto.class);
            orderDto.setPaymentOrderName(orderEntity.getPaymentOrderEntity().getStatus());
            orderDto.setUserNameCreate(orderEntity.getUserEntityOrder().getUserName());
            orderDto.setEmailUser(orderEntity.getUserEntityOrder().getGmail());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    @Override
    public OrderDto findOne(long id) {
        OrderEntity optionalOrderEntity = ordersRepository.getById(id);
        OrderDto orderDto=beanConfig.modelMapper().map(optionalOrderEntity,OrderDto.class);
        orderDto.setUserNameCreate(optionalOrderEntity.getUserEntityOrder().getUserName());
        orderDto.setPaymentOrderName(optionalOrderEntity.getPaymentOrderEntity().getStatus());
        orderDto.setStatusName(optionalOrderEntity.getStatusOrderEntity().getName());
        return orderDto;
    }

    @Override
    public Optional<OrderDto> findById(long id) {
        return Optional.empty();
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        PaymentEntity paymentEntity = paymentOrderRepository.getById(orderDto.getPaymentOrderId());
        StatusOrderEntity statusOrderEntity = statusOrderRepository.getById(orderDto.getStatusId());
        UserEntity userEntity=userRepository.getById(orderDto.getUserId());
        OrderEntity oldEntityOrderEntity = ordersRepository.getById(orderDto.getId());
        oldEntityOrderEntity = beanConfig.modelMapper().map(orderDto, OrderEntity.class);
        oldEntityOrderEntity.setPaymentOrderEntity(paymentEntity);
        oldEntityOrderEntity.setStatusOrderEntity(statusOrderEntity);
        oldEntityOrderEntity.setUserEntityOrder(userEntity);
        ordersRepository.save(oldEntityOrderEntity);
        return beanConfig.modelMapper().map(oldEntityOrderEntity,OrderDto.class);
    }

    @Override
    public void deleteOrder(long orderId) {
        ordersRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public OrderDto saveOfDuc(OrderDto orderDto, String username, long idpayment) {
        OrderEntity orderEntity =new OrderEntity();
        if (orderDto.getId() == null){
            UserEntity userEntity1 = userRepository.findAllByUsername(username);
            StatusOrderEntity statusOrderEntity1 = statusOrderRepository.findIsNotConfilm();
            PaymentEntity paymentEntity1 = paymentOrderRepository.findIspayment(idpayment);
            orderEntity = beanConfig.modelMapper().map(orderDto,OrderEntity.class);
            orderEntity.setStatusOrderEntity(statusOrderEntity1);
            orderEntity.setUserEntityOrder(userEntity1);
            orderEntity.setPaymentOrderEntity(paymentEntity1);
            this.ordersRepository.save(orderEntity);
        }
        else {
            OrderEntity orderEntity1=ordersRepository.getById(orderDto.getId());
            double totalPrice=0;
            List<OrderDetailEntity> orderDetailEntityList=orderDetailRepository.findAllByOrderId(orderDto.getId());
            for (OrderDetailEntity orderDetailEntity:orderDetailEntityList){
                totalPrice+=orderDetailEntity.getQuantity()*orderDetailEntity.getProductsEntityOrderDetail().getPrice();
            }
            UserEntity userEntity1 = userRepository.findAllByUsername(username);
            StatusOrderEntity statusOrderEntity1 = statusOrderRepository.findIsNotConfilm();
            PaymentEntity paymentEntity1 = paymentOrderRepository.findIspayment(idpayment);
            orderDto.setTotalPrice(totalPrice);
            orderEntity1 = beanConfig.modelMapper().map(orderDto,OrderEntity.class);
            orderEntity1.setStatusOrderEntity(statusOrderEntity1);
            orderEntity1.setUserEntityOrder(userEntity1);
            orderEntity1.setPaymentOrderEntity(paymentEntity1);
            this.ordersRepository.save(orderEntity1);
        }
        return beanConfig.modelMapper().map(orderEntity,OrderDto.class);
    }

    @Override
    @Transactional
    public List<OrderDto> findAllByUserID(long id){
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<OrderEntity> orderEntityList = ordersRepository.findAllByUserID(id) ;
        for(OrderEntity entity:orderEntityList){
            OrderDto orderDto = beanConfig.modelMapper().map(entity,OrderDto.class);
            orderDto.setStatusName(entity.getStatusOrderEntity().getName());
            orderDto.setPaymentOrderName(entity.getPaymentOrderEntity().getStatus());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;

    }
    @Override
    public void deleteOrderID(long id){
        ordersRepository.deleteById(id);
    }
}
