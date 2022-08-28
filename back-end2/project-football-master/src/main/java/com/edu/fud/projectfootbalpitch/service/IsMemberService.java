package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.IsMemberDto;

import java.util.List;

public interface IsMemberService {
    List<IsMemberDto> findAll();
    IsMemberDto findIsNotMember();
}
