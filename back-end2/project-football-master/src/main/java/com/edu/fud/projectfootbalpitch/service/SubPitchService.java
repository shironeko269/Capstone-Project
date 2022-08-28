package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.SubPitchDto;
import com.edu.fud.projectfootbalpitch.entity.SubPitchEntity;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface SubPitchService {
    SubPitchDto save(SubPitchDto subPitchDto);
    SubPitchDto saveByPitchOld(SubPitchDto subPitchDto);
    Optional<SubPitchDto> findById(long id);
    List<SubPitchDto> findAllByFootballPitchEntitySub();
    List<SubPitchDto> findAndByIdPitchBig(long id);
    void deleteSubPitch(long id);
    //hieu
    List<SubPitchDto> findAllSubPitchByUserId(long userId);
    //vi
    List<SubPitchDto> findAllSubPitchByPitchId(long id);
    SubPitchDto findSubPitchBySubPitchId(long id);
    List<SubPitchDto> findAllSubPitchByPitchTypeId(long pitchId, long typeId);
    //huy
    List<SubPitchDto> findAllBySubPitch(long id1, long id2);
    //chatbot
    List<SubPitchDto> findSubPitchByDateAndTime(Date dateBooking, Time timeBooking);
    List<SubPitchDto> findSubPitchByAreasAndSpecTime(String keyword,Date dateBooking, Time timeBooking);
}
