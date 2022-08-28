package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.IsMemberDto;
import com.edu.fud.projectfootbalpitch.entity.IsMemberEntity;
import com.edu.fud.projectfootbalpitch.repository.IsMemberRepository;
import com.edu.fud.projectfootbalpitch.service.IsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IsMemberServiceImpl implements IsMemberService {

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private IsMemberRepository isMemberRepository;

    @Override
    public List<IsMemberDto> findAll() {
        List<IsMemberDto> isMemberDtoList=new ArrayList<>();
        List<IsMemberEntity> isMemberEntityList= isMemberRepository.findAll();
        for (IsMemberEntity isMemberEntity : isMemberEntityList
        ){
            isMemberDtoList.add(beanConfig.modelMapper().map(isMemberEntity, IsMemberDto.class));
        }
        return isMemberDtoList;
    }

    @Override
    public IsMemberDto findIsNotMember() {
        IsMemberEntity isMemberEntity= isMemberRepository.findIsNotMember();
        return beanConfig.modelMapper().map(isMemberEntity,IsMemberDto.class);
    }
}
