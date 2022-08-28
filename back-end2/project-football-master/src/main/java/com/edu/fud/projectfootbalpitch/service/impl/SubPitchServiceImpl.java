package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.SubPitchDto;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchEntity;
import com.edu.fud.projectfootbalpitch.entity.PitchTypeEntity;
import com.edu.fud.projectfootbalpitch.entity.SubPitchEntity;
import com.edu.fud.projectfootbalpitch.repository.FootballPitchRepository;
import com.edu.fud.projectfootbalpitch.repository.PitchTypeRepository;
import com.edu.fud.projectfootbalpitch.repository.SubPitchRepository;
import com.edu.fud.projectfootbalpitch.service.SubPitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubPitchServiceImpl implements SubPitchService {

    @Autowired
    private SubPitchRepository subPitchRepository;

    @Autowired
    private PitchTypeRepository pitchTypeRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private FootballPitchRepository footballPitchRepository;
    @Override
    @Transactional
    public SubPitchDto save(SubPitchDto subPitchDto) {
        FootballPitchEntity footballPitchEntityMax = footballPitchRepository.findByIdMax();
        PitchTypeEntity pitchTypeEntity= pitchTypeRepository.getById(subPitchDto.getPitchTypeId());
        SubPitchEntity subPitchEntity=new SubPitchEntity();
        if (subPitchDto.getId()!= null){

        }else {
            subPitchEntity=beanConfig.modelMapper().map(subPitchDto,SubPitchEntity.class);
            pitchTypeEntity.setId(subPitchDto.getPitchTypeId());
            footballPitchEntityMax.setId(subPitchDto.getFootballPitchId());
            subPitchEntity.setPitchTypeEntitySub(pitchTypeEntity);
            subPitchEntity.setFootballPitchEntitySub(footballPitchEntityMax);
            subPitchRepository.save(subPitchEntity);
        }
        return beanConfig.modelMapper().map(subPitchEntity,SubPitchDto.class);
    }

    @Override
    public SubPitchDto saveByPitchOld(SubPitchDto subPitchDto) {
        FootballPitchEntity footballPitchEntity = footballPitchRepository.getById(subPitchDto.getFootballPitchId());
        PitchTypeEntity pitchTypeEntity= pitchTypeRepository.getById(subPitchDto.getPitchTypeId());
        SubPitchEntity subPitchEntity=new SubPitchEntity();
        if (subPitchDto.getId()!= null){
            SubPitchEntity oldPitchEntity=subPitchRepository.getById(subPitchDto.getId());
            oldPitchEntity=beanConfig.modelMapper().map(subPitchDto,SubPitchEntity.class);
            oldPitchEntity.setFootballPitchEntitySub(footballPitchEntity);
            oldPitchEntity.setPitchTypeEntitySub(pitchTypeEntity);
            subPitchRepository.save(oldPitchEntity);
        }else {
            subPitchEntity=beanConfig.modelMapper().map(subPitchDto,SubPitchEntity.class);
            pitchTypeEntity.setId(subPitchDto.getPitchTypeId());
            footballPitchEntity.setId(subPitchDto.getFootballPitchId());
            subPitchEntity.setPitchTypeEntitySub(pitchTypeEntity);
            subPitchEntity.setFootballPitchEntitySub(footballPitchEntity);
            subPitchRepository.save(subPitchEntity);
        }
        return beanConfig.modelMapper().map(subPitchEntity,SubPitchDto.class);
    }

    @Override
    public Optional<SubPitchDto> findById(long id) {
        Optional<SubPitchEntity> opt=subPitchRepository.findById(id);
        return opt.map(subPitchEntity -> beanConfig.modelMapper().map(subPitchEntity,SubPitchDto.class));
    }

    @Override
    public List<SubPitchDto> findAllByFootballPitchEntitySub() {
        List<SubPitchDto> subPitchDtoList=new ArrayList<>();
        List<SubPitchEntity> subPitchEntities=subPitchRepository.findAllByFootballPitchEntitySub();
        for (SubPitchEntity subPitchEntity : subPitchEntities
        ){
            subPitchDtoList.add(beanConfig.modelMapper().map(subPitchEntity,SubPitchDto.class));
        }
        return subPitchDtoList;
    }

    @Override
    public List<SubPitchDto> findAndByIdPitchBig(long id) {
        List<SubPitchEntity> subPitchEntities=subPitchRepository.findAndByIdPitchBig(id);
        List<SubPitchDto> subPitchDtoList=new ArrayList<>();
        for (SubPitchEntity subPitchEntity : subPitchEntities
        ){
        subPitchDtoList.add(beanConfig.modelMapper().map(subPitchEntity,SubPitchDto.class));
        }
        return subPitchDtoList;
    }

    @Override
    public void deleteSubPitch(long id) {
        subPitchRepository.deleteById(id);
    }

    @Override
    public List<SubPitchDto> findAllSubPitchByUserId(long userId) {
        List<SubPitchEntity> footballPitchEntityList=subPitchRepository.findAllSubPitchByUserId(userId);
        List<SubPitchDto> subPitchDtoList=new ArrayList<>();
        for (SubPitchEntity subPitchEntity:footballPitchEntityList
        ) {
            subPitchDtoList.add(beanConfig.modelMapper().map(subPitchEntity,SubPitchDto.class));
        }
        return subPitchDtoList;
    }

    @Override
    public List<SubPitchDto> findAllSubPitchByPitchId(long id) {
        List<SubPitchEntity> entityList = subPitchRepository.findAllByPitchId(id);
        List<SubPitchDto> dtoList = new ArrayList<>();
        for (SubPitchEntity entity : entityList) {
            SubPitchDto dto = beanConfig.modelMapper().map(entity, SubPitchDto.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public SubPitchDto findSubPitchBySubPitchId(long id) {
        SubPitchEntity entity = subPitchRepository.getById(id);
        SubPitchDto dto = beanConfig.modelMapper().map(entity, SubPitchDto.class);
        return dto;
    }

    @Override
    public List<SubPitchDto> findAllSubPitchByPitchTypeId(long pitchId, long typeId) {
        List<SubPitchEntity> entityList = subPitchRepository.findAllByPitchTypeId(pitchId, typeId);
        List<SubPitchDto> dtoList = new ArrayList<>();
        for (SubPitchEntity entity : entityList) {
            SubPitchDto dto = beanConfig.modelMapper().map(entity, SubPitchDto.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    @Transactional
    public List<SubPitchDto> findAllBySubPitch(long id1, long id2){
        List<SubPitchDto> subPitchDtoList = new ArrayList<>();
        List<SubPitchEntity> subPitchEntities = subPitchRepository.findAllBySubPitch(id1,id2);
        for(SubPitchEntity entity: subPitchEntities){
            SubPitchDto subPitchDto = beanConfig.modelMapper().map(entity,SubPitchDto.class);
            subPitchDto.setSubTimeStart(subPitchRepository.findAllBySubPitchStartTime(id1,id2));
            subPitchDto.setSubTimeEnd(subPitchRepository.findAllBySubPitchEndTime(id1,id2));
            subPitchDto.setSubtype(entity.getPitchTypeEntitySub().getName());
            subPitchDtoList.add(subPitchDto);
        }

        return subPitchDtoList;
    }

    @Override
    public List<SubPitchDto> findSubPitchByDateAndTime(Date dateBooking, Time timeBooking) {
        List<SubPitchDto> subPitchDtoList = new ArrayList<>();
        List<SubPitchEntity> subPitchEntities = subPitchRepository.findSubPitchByDateAndTime(dateBooking,timeBooking);
        for(SubPitchEntity entity: subPitchEntities){
            SubPitchDto subPitchDto = beanConfig.modelMapper().map(entity,SubPitchDto.class);
            subPitchDto.setSubtype(entity.getPitchTypeEntitySub().getName());
            subPitchDtoList.add(subPitchDto);
        }
        return subPitchDtoList;
    }

    @Override
    public List<SubPitchDto> findSubPitchByAreasAndSpecTime(String keyword, Date dateBooking, Time timeBooking) {
        List<SubPitchDto> subPitchDtoList = new ArrayList<>();
        List<SubPitchEntity> subPitchEntities = subPitchRepository.findSubPitchByAreasAndSpecTime(keyword,dateBooking,timeBooking);
        for(SubPitchEntity entity: subPitchEntities){
            SubPitchDto subPitchDto = beanConfig.modelMapper().map(entity,SubPitchDto.class);
            subPitchDto.setSubtype(entity.getPitchTypeEntitySub().getName());
            subPitchDtoList.add(subPitchDto);
        }
        return subPitchDtoList;
    }
}
