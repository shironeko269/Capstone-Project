package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.SupplierDto;
import com.edu.fud.projectfootbalpitch.entity.SupplierEntity;
import com.edu.fud.projectfootbalpitch.repository.SupplierRepository;
import com.edu.fud.projectfootbalpitch.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private BeanConfig beanConfig;
    @Autowired
    private SupplierRepository supplierRepository;
    @Override
    public SupplierDto save(SupplierDto supplierDto) {
        SupplierEntity supplierEntity =new SupplierEntity();
        supplierEntity=beanConfig.modelMapper().map(supplierDto,SupplierEntity.class);
        this.supplierRepository.save(supplierEntity);
        return beanConfig.modelMapper().map(supplierEntity,SupplierDto.class);
    }

    @Override
    public List<SupplierDto> findAll() {
        List<SupplierDto> supplierDtoList =new ArrayList<>();
        List<SupplierEntity> supplierEntityList= supplierRepository.findAll();
        for (SupplierEntity supplierEntity:supplierEntityList
             ) {
            supplierDtoList.add(beanConfig.modelMapper().map(supplierEntity,SupplierDto.class));
        }
        return supplierDtoList;
    }
}
