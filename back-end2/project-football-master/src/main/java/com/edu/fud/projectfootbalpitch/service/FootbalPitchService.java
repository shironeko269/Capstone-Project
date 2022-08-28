package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.FootBallPitchDto;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FootbalPitchService {
    FootBallPitchDto save(FootBallPitchDto footBallPitchDto);
    FootBallPitchDto findByIdMax();
    List<FootBallPitchDto> findAll();
    List<FootBallPitchDto> findAllCuaVi();
    Optional<FootBallPitchDto> findPitchById(long id);
    FootBallPitchDto findPitchByIdEdit(long id);
    void deletePitch(long id);
    Optional<FootBallPitchDto> findAllStreetNumber(String streetNumber);
    Optional<FootBallPitchDto> findAllUrlMap(String urlMap);
    String findStreetNumberByPitchId(long pitchId);
    String findUrlMapByPitchId(long pitchId);
    //hieu
    FootBallPitchDto findFootballPitchDtoByUserId(long userId);
    //vi
    List<FootBallPitchDto> findAllByName(String keyword);
    List<FootBallPitchDto> findPitchByDistrictId(long districtId);
    List<FootBallPitchDto> findPitchByWardId(long wardId);
    FootBallPitchDto findPitchByPitchId(long pitchId);
    //duc
    List<FootBallPitchDto> findAllPitch();

    List<FootBallPitchDto> findLimitByDate();
    //hieu,chatbot
    List<FootBallPitchDto> findPitchByWardNameOrDistrictName(String name);
}
