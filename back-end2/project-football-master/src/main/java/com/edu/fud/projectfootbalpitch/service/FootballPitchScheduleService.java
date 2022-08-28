package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.FootballPitchScheduleDto;
import com.edu.fud.projectfootbalpitch.dto.PitchScheduleServiceDto;

import java.sql.Date;
import java.util.List;

public interface FootballPitchScheduleService {
    Long save(FootballPitchScheduleDto footballPitchScheduleDto);
    List<FootballPitchScheduleDto> findAllByDateBooking(Date dateBooking, long subPitchId);
    List<FootballPitchScheduleDto> findAllNow(long userId);
}
