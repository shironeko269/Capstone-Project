package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.PitchScheduleServiceDto;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchScheduleEntity;
import com.edu.fud.projectfootbalpitch.entity.PitchScheduleServiceEntity;
import com.edu.fud.projectfootbalpitch.entity.ServiceEntity;
import com.edu.fud.projectfootbalpitch.repository.FootballPitchScheduleRepository;
import com.edu.fud.projectfootbalpitch.repository.PitchScheduleServiceRepository;
import com.edu.fud.projectfootbalpitch.service.PitchScheduleServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PitchScheduleServiceServiceImpl implements PitchScheduleServiceService {


    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private PitchScheduleServiceRepository pitchScheduleServiceRepository;

    @Autowired
    private FootballPitchScheduleRepository footballPitchScheduleRepository;

    @Override
    public List<PitchScheduleServiceDto> findAllServiceByPitchScheduleByBooking(long bookingId) {
        List<PitchScheduleServiceDto> pitchScheduleServiceDtoList=new ArrayList<>();
        List<PitchScheduleServiceEntity> pitchScheduleServiceEntityList= pitchScheduleServiceRepository.findAllServiceByPitchScheduleByBooking(bookingId);
        for (PitchScheduleServiceEntity pitchScheduleServiceEntity : pitchScheduleServiceEntityList
        ){
            PitchScheduleServiceDto pitchScheduleServiceDto=
                    beanConfig.modelMapper().map(pitchScheduleServiceEntity,PitchScheduleServiceDto.class);
            pitchScheduleServiceDto.setNameService(pitchScheduleServiceEntity.getServiceEntityPitch().getName());
            pitchScheduleServiceDto.setUnit(pitchScheduleServiceEntity.getServiceEntityPitch().getUnit());
            pitchScheduleServiceDtoList.add(pitchScheduleServiceDto);
        }
        return pitchScheduleServiceDtoList;
    }

    @Override
    public Long save(PitchScheduleServiceDto dto) {
        PitchScheduleServiceEntity entity = new PitchScheduleServiceEntity();
        entity = beanConfig.modelMapper().map(dto, PitchScheduleServiceEntity.class);

        FootballPitchScheduleEntity footballPitchScheduleEntity = new FootballPitchScheduleEntity();
        footballPitchScheduleEntity.setId(dto.getFootballPitchScheduleId());
        entity.setFootballPitchScheduleEntityService(footballPitchScheduleEntity);

        ServiceEntity service = new ServiceEntity();
        service.setId(dto.getServicePitchId());
        entity.setServiceEntityPitch(service);

        pitchScheduleServiceRepository.save(entity);
        return footballPitchScheduleRepository.findLastInsertId();
    }

    ////////////hieu
    @Override
    public List<PitchScheduleServiceDto> findAllByScheduleId(Long scheduleId) {
        List<PitchScheduleServiceDto> pitchScheduleServiceDtos = new ArrayList<>();
        List<PitchScheduleServiceEntity> pitchScheduleServiceEntityList =
                pitchScheduleServiceRepository.findAllByScheduleId(scheduleId);
        for (PitchScheduleServiceEntity entity: pitchScheduleServiceEntityList){
            PitchScheduleServiceDto pitchScheduleServiceDto =
                    beanConfig.modelMapper().map(entity,PitchScheduleServiceDto.class);
            pitchScheduleServiceDtos.add(pitchScheduleServiceDto);
        }
        return pitchScheduleServiceDtos;
    }

    @Override
    public void UpdatePitchServiceQuantity(int quantity, long scheduleId, long serviceId) {
        pitchScheduleServiceRepository.UpdatePitchServiceQuantity(quantity,scheduleId,serviceId);
    }

    @Override
    public Long pitchScheduleServiceId(long scheduleId, long serviceId) {
        if (pitchScheduleServiceRepository.pitchScheduleServiceId(scheduleId,serviceId)!=null){
            return pitchScheduleServiceRepository.pitchScheduleServiceId(scheduleId,serviceId);
        }else{
            return null;
        }

    }

    @Override
    public PitchScheduleServiceDto findOnePitchScheduleServiceByScheduleIdAndServiceId(long scheduleId, long serviceId) {
        if (pitchScheduleServiceRepository.findOnePitchScheduleServiceByScheduleIdAndServiceId(scheduleId,serviceId)!=null){
            PitchScheduleServiceEntity pitchScheduleServiceEntity =
                    pitchScheduleServiceRepository.findOnePitchScheduleServiceByScheduleIdAndServiceId(scheduleId,serviceId);
            PitchScheduleServiceDto pitchScheduleServiceDto = beanConfig.modelMapper().map(pitchScheduleServiceEntity,PitchScheduleServiceDto.class);
            return pitchScheduleServiceDto;
        }else{
            return null;
        }
    }

    @Override
    public double sumOldScheduleService(long scheduleId) {
        double sum = 0;

        if(pitchScheduleServiceRepository.sumOldScheduleServiceCheck(scheduleId)==null){

        }else{
            sum =pitchScheduleServiceRepository.sumOldScheduleService(scheduleId);
        }
        return sum;
    }
    @Override
    @Transactional
    public List<PitchScheduleServiceDto> findAllByScheduleServiceEntities(long id1, long id2){
        List<PitchScheduleServiceDto> pitchScheduleServiceDtoList = new ArrayList<>();
        List<PitchScheduleServiceEntity> pitchScheduleServiceEntities = pitchScheduleServiceRepository.findAllByScheduleService(id1,id2);
        for(PitchScheduleServiceEntity entity:pitchScheduleServiceEntities){
            PitchScheduleServiceDto pitchScheduleServiceDto = beanConfig.modelMapper().map(entity,PitchScheduleServiceDto.class);
            pitchScheduleServiceDto.setSerName(entity.getServiceEntityPitch().getName());
            pitchScheduleServiceDto.setSerUnit(entity.getServiceEntityPitch().getUnit());
            pitchScheduleServiceDto.setSerPrice(entity.getServiceEntityPitch().getPrice());
            pitchScheduleServiceDtoList.add(pitchScheduleServiceDto);
        }
        return pitchScheduleServiceDtoList ;
    }
}