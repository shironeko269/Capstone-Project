package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.PitchTypeDto;
import com.edu.fud.projectfootbalpitch.dto.SubPitchDto;

import java.util.List;

public interface PitchTypeService {
    List<PitchTypeDto> findAll();
    PitchTypeDto findPitchTypeById(long id);
    List<PitchTypeDto> findPitchTypeByListId(List<SubPitchDto> subPitchDtoList);
}
