package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.StatusBookFootballPitchDto;

import java.util.List;

public interface StatusBookFootballPitchService {
    List<StatusBookFootballPitchDto> findAll();
}