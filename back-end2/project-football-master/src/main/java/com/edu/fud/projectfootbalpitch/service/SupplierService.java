package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.SupplierDto;

import java.util.List;

public interface SupplierService {
    SupplierDto save(SupplierDto supplierDto);
    List<SupplierDto> findAll();
}
