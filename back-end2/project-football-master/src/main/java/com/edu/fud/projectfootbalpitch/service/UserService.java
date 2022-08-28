package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.FootBallPitchDto;
import com.edu.fud.projectfootbalpitch.dto.UserDto;
import com.edu.fud.projectfootbalpitch.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto save(UserDto user);
    UserDto saveAdmin(UserDto user);
    UserDto saveManager(UserDto user);
    List<UserDto> findAllByRoleAndNoManagerPitch();
    UserDto findByUserMax();
    //list ra tat ca san nho theo san lon lon nhat
    List<UserDto> findAllUserRoleManagerAndUserId(long userId);
    Optional<UserDto> getUserByUsername(String username);
    Optional<UserDto> getUserByGmail(String gmail);
    List<UserDto> findAndUserByRoleManager();
    void deleteUserManager(long managerId);
    List<UserDto> findAllUserMostOrder();
    //vi
    UserDto findUserById(long id);
    //duc
    UserDto findAllByUsername(String username);
    //huy
    UserDto findOneByUserName(String userName);
    List<UserDto> findAllusername(String username);
    Optional<UserDto> findById(long id);
    UserDto saveUpdateProfile(UserDto userDto);
    UserDto saveUpdatePassword(UserDto userDto);
    UserDto savePass(UserDto userDto);
}
