package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.constant.SystemConstant;
import com.edu.fud.projectfootbalpitch.dto.ServiceDto;
import com.edu.fud.projectfootbalpitch.entity.CategoryServiceEntity;
import com.edu.fud.projectfootbalpitch.entity.ServiceEntity;
import com.edu.fud.projectfootbalpitch.repository.CategoryServiceRepository;
import com.edu.fud.projectfootbalpitch.repository.ServiceRepository;
import com.edu.fud.projectfootbalpitch.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private CategoryServiceRepository categoryServiceRepository;

    @Override
    @Transactional
    public List<ServiceDto> findAll() {
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        List<ServiceEntity> serviceEntityList = serviceRepository.findAll();
        for (ServiceEntity entity:
                serviceEntityList){
            ServiceDto serviceDto = beanConfig.modelMapper().map(entity,ServiceDto.class);
            serviceDto.setCategoryServiceName(entity.getCategoryServiceEntity().getName());
            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }

    @Override
    @Transactional
    public ServiceDto save(ServiceDto serviceDto) {
        CategoryServiceEntity categoryServiceEntity =
                categoryServiceRepository.getById(serviceDto.getCategoryServiceId());
        ServiceEntity serviceEntity = new ServiceEntity();
        if (serviceDto.getId() != null){
            ServiceEntity oldServiceEntity = serviceRepository.findById(serviceDto.getId()).get();
            oldServiceEntity=beanConfig.modelMapper().map(serviceDto,ServiceEntity.class);
            oldServiceEntity.setCategoryServiceEntity(categoryServiceEntity);
            serviceRepository.save(oldServiceEntity);
        }else{
            serviceEntity=beanConfig.modelMapper().map(serviceDto,ServiceEntity.class);
            categoryServiceEntity.setId(serviceDto.getCategoryServiceId());
            serviceEntity.setCategoryServiceEntity(categoryServiceEntity);
            serviceEntity.setStatus(SystemConstant.ACTIVE_STATUS);
            serviceRepository.save(serviceEntity);
        }
        return beanConfig.modelMapper().map(serviceEntity,ServiceDto.class);
    }

    @Override
    public Optional<ServiceDto> findById(long id) {
        Optional<ServiceEntity> opt =serviceRepository.findById(id);
        return opt.map(serviceEntity -> beanConfig.modelMapper().map(serviceEntity,ServiceDto.class));
    }

    @Override
    public void deleteService(long id) { serviceRepository.deleteById(id);}

    @Override
    public List<ServiceDto> findAllByCategoryId(long id) {
        List<ServiceEntity> serviceEntityList = serviceRepository.findAllByCategoryId(id);
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        for (ServiceEntity serviceEntity:serviceEntityList) {
            ServiceDto serviceDto = beanConfig.modelMapper().map(serviceEntity, ServiceDto.class);
            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }

    @Override
    @Transactional
    public void updateQuantity(int num, long id) {
        serviceRepository.updateQuantity(num, id);
    }

}