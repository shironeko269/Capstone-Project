package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.StatusOrderDto;
import com.edu.fud.projectfootbalpitch.entity.OrderEntity;
import com.edu.fud.projectfootbalpitch.entity.StatusOrderEntity;
import com.edu.fud.projectfootbalpitch.repository.OrdersRepository;
import com.edu.fud.projectfootbalpitch.repository.StatusOrderRepository;
import com.edu.fud.projectfootbalpitch.service.StatusOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusOrderServiceImpl implements StatusOrderService {

    @Autowired
    private StatusOrderRepository statusOrderRepository;

    @Autowired
    private OrdersRepository  ordersRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Override
    public List<StatusOrderDto> findAll() {
        List<StatusOrderDto> statusOrderDtos = new ArrayList<>();
        List<StatusOrderEntity> statusOrderEntities = statusOrderRepository.findAll();
        for (StatusOrderEntity statusOrderEntity : statusOrderEntities
        ) {
            statusOrderDtos.add(beanConfig.modelMapper().map(statusOrderEntity, StatusOrderDto.class));
        }
        return statusOrderDtos;
    }

    @Override
    @Transactional
    public void deleteAllByStatusAndIdCancel() {
        //statusOrderRepository.deleteById(statusOrderRepository.findAllByIdStatus3());
        long[] orderEntityList = statusOrderRepository.deleteAllByStatusAndIdCancel();
        for (long id : orderEntityList) {
            ordersRepository.deleteById(id);
        }
    }
}
