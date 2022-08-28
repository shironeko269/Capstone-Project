package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.FootBallPitchDto;
import com.edu.fud.projectfootbalpitch.dto.FootballPitchScheduleDto;
import com.edu.fud.projectfootbalpitch.dto.PitchScheduleServiceDto;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchEntity;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchScheduleEntity;
import com.edu.fud.projectfootbalpitch.entity.PitchScheduleServiceEntity;
import com.edu.fud.projectfootbalpitch.entity.SubPitchEntity;
import com.edu.fud.projectfootbalpitch.repository.FootballPitchScheduleRepository;
import com.edu.fud.projectfootbalpitch.service.FootballPitchScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class FootballPitchScheduleServiceImpl implements FootballPitchScheduleService {

    @Autowired
    FootballPitchScheduleRepository footballPitchScheduleRepository;
    @Autowired
    private BeanConfig beanConfig;

    @Override
    public Long save(FootballPitchScheduleDto dto) {
        FootballPitchScheduleEntity entity = new FootballPitchScheduleEntity();
        entity = beanConfig.modelMapper().map(dto, FootballPitchScheduleEntity.class);
        SubPitchEntity subPitchEntity = new SubPitchEntity();
        subPitchEntity.setId(dto.getSubPitchId());
        entity.setSubPitchEntitySchedule(subPitchEntity);
        footballPitchScheduleRepository.save(entity);
        return entity.getId();
    }

    @Override
    public List<FootballPitchScheduleDto> findAllByDateBooking(Date dateBooking, long subPitchId) {
        List<FootballPitchScheduleDto> dtoList = new ArrayList<>();
        List<FootballPitchScheduleEntity> entityList = footballPitchScheduleRepository.findTimeByDate(dateBooking,subPitchId);
        for (FootballPitchScheduleEntity entity : entityList) {
            FootballPitchScheduleDto dto = beanConfig.modelMapper().map(entity, FootballPitchScheduleDto.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<FootballPitchScheduleDto> findAllNow(long userId) {
        List<FootballPitchScheduleDto> footballPitchScheduleDtos = new ArrayList<>();
        List<FootballPitchScheduleEntity> footballPitchScheduleEntityList = footballPitchScheduleRepository.findAllNow(userId);
        for (FootballPitchScheduleEntity entity : footballPitchScheduleEntityList) {
            FootballPitchScheduleDto dto = beanConfig.modelMapper().map(entity, FootballPitchScheduleDto.class);
            dto.setSubPitchNameNow(entity.getSubPitchEntitySchedule().getName());
            entity.getBookFootballPitchEntitiesUser().forEach(bookFootballPitchEntity ->
                    dto.setUserNameNow(bookFootballPitchEntity.getUserEntityPitchBooking().getUserName())
                    );
            footballPitchScheduleDtos.add(dto);

        }
        return footballPitchScheduleDtos;
    }


}
