package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.StatusBookFootballPitchDto;
import com.edu.fud.projectfootbalpitch.entity.StatusBookFootballPitchEntity;
import com.edu.fud.projectfootbalpitch.repository.StatusBookFootballPitchRepository;
import com.edu.fud.projectfootbalpitch.service.StatusBookFootballPitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusBookFootballPitchServiceImpl implements StatusBookFootballPitchService {

    @Autowired
    private StatusBookFootballPitchRepository statusBookFootballPitchRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Override
    public List<StatusBookFootballPitchDto> findAll() {
        List<StatusBookFootballPitchDto> statusBookFootballPitchDtos = new ArrayList<>();
        List<StatusBookFootballPitchEntity> statusBookFootballPitchEntities = statusBookFootballPitchRepository.findAll();
        for (StatusBookFootballPitchEntity statusBookFootballPitchEntity: statusBookFootballPitchEntities){
            statusBookFootballPitchDtos.add(beanConfig.modelMapper().map(statusBookFootballPitchEntity,StatusBookFootballPitchDto.class));
        }
        return statusBookFootballPitchDtos;
    }
}