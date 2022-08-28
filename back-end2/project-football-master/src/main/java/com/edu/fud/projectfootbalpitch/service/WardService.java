package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.WardDto;

import java.util.List;

public interface WardService {
    List<WardDto> findAll();
    List<WardDto> findAllByDistrictId(long id);
}
