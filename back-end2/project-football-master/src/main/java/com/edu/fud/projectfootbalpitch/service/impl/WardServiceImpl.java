package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.WardDto;
import com.edu.fud.projectfootbalpitch.entity.WardEntity;
import com.edu.fud.projectfootbalpitch.repository.WardRepository;
import com.edu.fud.projectfootbalpitch.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WardServiceImpl implements WardService {

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Override
    public List<WardDto> findAll() {
        List<WardDto> wardDtoList =new ArrayList<>();
        List<WardEntity> wardEntityList= wardRepository.findAll();
        for (WardEntity wardEntity: wardEntityList
             ) {
            WardDto wardDto = beanConfig.modelMapper().map(wardEntity,WardDto.class);
            wardDto.setDistrictName(wardEntity.getDistrictEntity().getDistrictName());
            wardDtoList.add(wardDto);
        }
        return wardDtoList;
    }

    @Override
    public List<WardDto> findAllByDistrictId(long id) {
        List<WardDto> wardDtoList =new ArrayList<>();
        List<WardEntity> wardEntityList= wardRepository.findAllByDistrictId(id);
        for (WardEntity wardEntity: wardEntityList
        ) {
            wardDtoList.add(beanConfig.modelMapper().map(wardEntity,WardDto.class));
        }
        return wardDtoList;
    }
}
