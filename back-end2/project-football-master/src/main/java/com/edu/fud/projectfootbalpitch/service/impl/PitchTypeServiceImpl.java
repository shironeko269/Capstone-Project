package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.PitchTypeDto;
import com.edu.fud.projectfootbalpitch.dto.SubPitchDto;
import com.edu.fud.projectfootbalpitch.entity.PitchTypeEntity;
import com.edu.fud.projectfootbalpitch.repository.PitchTypeRepository;
import com.edu.fud.projectfootbalpitch.service.PitchTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PitchTypeServiceImpl implements PitchTypeService {

    @Autowired
    private PitchTypeRepository pitchTypeRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Override
    public List<PitchTypeDto> findAll() {
        List<PitchTypeDto> pitchTypeDtos=new ArrayList<>();
        List<PitchTypeEntity> pitchTypeEntities= pitchTypeRepository.findAll();
        for (PitchTypeEntity pitchTypeEntity:
             pitchTypeEntities) {
            pitchTypeDtos.add(beanConfig.modelMapper().map(pitchTypeEntity,PitchTypeDto.class));
        }
        return pitchTypeDtos;
    }

    @Override
    public PitchTypeDto findPitchTypeById(long id) {
        PitchTypeEntity entity = pitchTypeRepository.findPitchTypeById(id);
        PitchTypeDto dto = beanConfig.modelMapper().map(entity, PitchTypeDto.class);
        return dto;
    }
    @Override
    public List<PitchTypeDto> findPitchTypeByListId(List<SubPitchDto> subPitchDtoList) {
        List<Long> pitchTypeIdList = new ArrayList<>();
        for (SubPitchDto pitchTypeId : subPitchDtoList) {
            pitchTypeIdList.add(pitchTypeId.getPitchTypeId());
        }
        List<Long> pitchTypeIdListNotDuplicate = new ArrayList<Long>();
        for (Long element : pitchTypeIdList) {
            if (!pitchTypeIdListNotDuplicate.contains(element)) {
                pitchTypeIdListNotDuplicate.add(element);
            }
        }
        List<PitchTypeDto> dtoList = new ArrayList<>();
        for (Long id : pitchTypeIdListNotDuplicate) {
            PitchTypeEntity entity = pitchTypeRepository.findPitchTypeById(id);
            PitchTypeDto dto = beanConfig.modelMapper().map(entity, PitchTypeDto.class);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
