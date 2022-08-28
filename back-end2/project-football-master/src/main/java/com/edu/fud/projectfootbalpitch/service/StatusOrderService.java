package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.StatusOrderDto;

import java.util.List;

public interface StatusOrderService {
    List<StatusOrderDto> findAll();
    void deleteAllByStatusAndIdCancel();
}
