package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.PitchScheduleServiceDto;

import java.util.List;

public interface PitchScheduleServiceService {
    List<PitchScheduleServiceDto> findAllServiceByPitchScheduleByBooking(long bookingId);
    //vi
    Long save(PitchScheduleServiceDto dto);
    //hieu
    List<PitchScheduleServiceDto> findAllByScheduleId(Long scheduleId);
    void UpdatePitchServiceQuantity(int quantity,long scheduleId,long serviceId);
    Long pitchScheduleServiceId(long scheduleId,long serviceId);
    PitchScheduleServiceDto findOnePitchScheduleServiceByScheduleIdAndServiceId(long scheduleId,long serviceId);
    double sumOldScheduleService(long scheduleId);
    //huy
    //huy
    List<PitchScheduleServiceDto> findAllByScheduleServiceEntities(long id1, long id2);
}
