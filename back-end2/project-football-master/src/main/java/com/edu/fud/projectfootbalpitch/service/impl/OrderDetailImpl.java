package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.OrderDetailDto;
import com.edu.fud.projectfootbalpitch.entity.OrderDetailEntity;
import com.edu.fud.projectfootbalpitch.entity.OrderEntity;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import com.edu.fud.projectfootbalpitch.repository.OrderDetailRepository;
import com.edu.fud.projectfootbalpitch.repository.OrdersRepository;
import com.edu.fud.projectfootbalpitch.repository.ProductRepository;
import com.edu.fud.projectfootbalpitch.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailImpl implements OrderDetailService {

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetailDto> findAllByOrderId(long orderId) {
        List<OrderDetailDto> orderDetailDtoList=new ArrayList<>();
        List<OrderDetailEntity> orderDetailEntityList= orderDetailRepository.findAllByOrderId(orderId);
        for (OrderDetailEntity orderDetailEntity : orderDetailEntityList
        ){
            OrderDetailDto orderDetailDto=beanConfig.modelMapper().map(orderDetailEntity,OrderDetailDto.class);
            orderDetailDto.setPrice(orderDetailEntity.getProductsEntityOrderDetail().getPrice());
            orderDetailDto.setProductName(orderDetailEntity.getProductsEntityOrderDetail().getName());
            orderDetailDtoList.add(orderDetailDto);
        }
        return orderDetailDtoList;
    }

    @Override
    @Transactional
    public OrderDetailDto save(OrderDetailDto orderDetailDto, long orderId) {
        OrderDetailEntity orderDetailEntity ;


        OrderEntity orderEntity1 = orderRepository.findAllByOrder(orderId);
        ProductsEntity productsEntity1 = productRepository.findAllByID(orderDetailDto.getProductId());

        orderDetailEntity = beanConfig.modelMapper().map(orderDetailDto, OrderDetailEntity.class);

        orderDetailEntity.setOrderEntityOrderDetail(orderEntity1);
        orderDetailEntity.setProductsEntityOrderDetail(productsEntity1);


        this.orderDetailRepository.save(orderDetailEntity);

        return beanConfig.modelMapper().map(orderDetailEntity,OrderDetailDto.class);
    }

    @Override
    @Transactional
    public List<OrderDetailDto> findAllByOrderID(long orderID,long userID){
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findAllByOrderID(orderID,userID);
        for(OrderDetailEntity entity: orderDetailEntities){
            OrderDetailDto orderDetailDto = beanConfig.modelMapper().map(entity,OrderDetailDto.class);
            orderDetailDto.setProductName(entity.getProductsEntityOrderDetail().getName());
            orderDetailDto.setPrice(entity.getProductsEntityOrderDetail().getPrice());
            orderDetailDto.setAddress(entity.getOrderEntityOrderDetail().getAddress());
            orderDetailDto.setUnitProduct(entity.getProductsEntityOrderDetail().getUnit());
            orderDetailDto.setUserID(entity.getOrderEntityOrderDetail().getUserEntityOrder().getId());
            orderDetailDto.setCategoryId(entity.getProductsEntityOrderDetail().getCategoryProductEntity().getId());
            orderDetailDto.setProductId(entity.getProductsEntityOrderDetail().getId());
            orderDetailDtoList.add(orderDetailDto);
        }
        return  orderDetailDtoList;
    }
}
