package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.DistrictDto;
import com.edu.fud.projectfootbalpitch.entity.DistrictEntity;
import com.edu.fud.projectfootbalpitch.repository.DistrictsRepository;
import com.edu.fud.projectfootbalpitch.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictsRepository districtsRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Override
    public List<DistrictDto> findAll() {
        List<DistrictDto> districtDtoList=new ArrayList<>();
        List<DistrictEntity> districtEntityList= districtsRepository.findAll();
        for (DistrictEntity districtEntity : districtEntityList
        ){
            districtDtoList.add(beanConfig.modelMapper()
                    .map(districtEntity,DistrictDto.class));
        }
        return districtDtoList;
    }
}
