package com.edu.fud.projectfootbalpitch.service;
import com.edu.fud.projectfootbalpitch.dto.ServiceDto;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    //hieu
    List<ServiceDto> findAll();
    ServiceDto save(ServiceDto serviceDto);
    Optional<ServiceDto> findById(long id);
    void deleteService(long id);
    //vi
    List<ServiceDto> findAllByCategoryId(long id);
    void updateQuantity(int num, long id);
}
