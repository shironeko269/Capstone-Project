package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.constant.SystemConstant;
import com.edu.fud.projectfootbalpitch.dto.UserDto;
import com.edu.fud.projectfootbalpitch.entity.IsMemberEntity;
import com.edu.fud.projectfootbalpitch.entity.UserEntity;
import com.edu.fud.projectfootbalpitch.repository.IsMemberRepository;
import com.edu.fud.projectfootbalpitch.repository.UserRepository;
import com.edu.fud.projectfootbalpitch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IsMemberRepository isMemberRepository;

    //https://viblo.asia/p/su-dung-modelmapper-trong-spring-boot-63vKj1Vd52R
    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        if (userDto.getId()==null){
            IsMemberEntity isMemberEntity= isMemberRepository.findIsNotMember();
            userDto.setRole("ROLE_USER");
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userEntity = beanConfig.modelMapper().map(userDto, UserEntity.class);
            userEntity.setIsMember(isMemberEntity);
            this.userRepository.save(userEntity);
        }else {
            IsMemberEntity isMemberEntity= isMemberRepository.getById(userDto.getIsMemberId());
            UserEntity oldEntity=userRepository.getById(userDto.getId());
            oldEntity=beanConfig.modelMapper().map(userDto,UserEntity.class);
            oldEntity.setRole("ROLE_USER");
            oldEntity.setPassword(oldEntity.getPassword());
            oldEntity.setIsMember(isMemberEntity);
            this.userRepository.save(oldEntity);
        }
        return beanConfig.modelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto saveAdmin(UserDto userAdmin) {
        UserEntity userEntity = new UserEntity();
        IsMemberEntity isMemberEntity= isMemberRepository.findIsNotMember();
        userAdmin.setRole("ROLE_ADMIN");
        userAdmin.setPassword(passwordEncoder.encode(userAdmin.getPassword()));
        userEntity = beanConfig.modelMapper().map(userAdmin, UserEntity.class);
        userEntity.setIsMember(isMemberEntity);
        userEntity = this.userRepository.save(userEntity);
        return beanConfig.modelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto saveManager(UserDto userManager) {
        UserEntity userEntity = new UserEntity();
        IsMemberEntity isMemberEntity= isMemberRepository.findIsNotMember();
        userManager.setRole("ROLE_MANAGER");
        userManager.setPassword(passwordEncoder.encode(userManager.getPassword()));
        userEntity = beanConfig.modelMapper().map(userManager, UserEntity.class);
        userEntity.setIsMember(isMemberEntity);
        userEntity = this.userRepository.save(userEntity);
        return beanConfig.modelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public List<UserDto> findAllByRoleAndNoManagerPitch() {
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntityList = userRepository.findAllByRoleAndNoManagerPitch();
        for (UserEntity userEntity : userEntityList
        ) {
            userDtoList.add(beanConfig.modelMapper().map(userEntity, UserDto.class));
        }
        return userDtoList;
    }

    @Override
    public UserDto findByUserMax() {
        UserEntity userEntity = userRepository.findByUserMax();
        if (userEntity==null){
            userEntity=new UserEntity();
        }
        return beanConfig.modelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public List<UserDto> findAllUserRoleManagerAndUserId(long userId) {
        List<UserEntity> userEntityList = userRepository.findAllUserRoleManagerAndUserId(userId);
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList
        ) {
            userDtoList.add(beanConfig.modelMapper().map(userEntity, UserDto.class));
        }
        return userDtoList;
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.getOneByUserName(username);

        return userEntityOptional.map(userEntity -> beanConfig.modelMapper().map(userEntity, UserDto.class));
    }

    @Override
    public Optional<UserDto> getUserByGmail(String gmail) {
        Optional<UserEntity> userEntityOptional = userRepository.getOneByGmail(gmail);

        return userEntityOptional.map(userEntity -> beanConfig.modelMapper().map(userEntity, UserDto.class));
    }

    @Override
    public List<UserDto> findAndUserByRoleManager() {
        List<UserEntity> userEntityList= userRepository.findAndUserByRoleManager();
        List<UserDto> userDtoList=new ArrayList<>();
        for (UserEntity userEntity : userEntityList
        ){
            UserDto userDto=beanConfig.modelMapper().map(userEntity,UserDto.class);
            userEntity.getFootballPitchEntitiesUser().forEach(footballPitchEntity ->
                    userDto.setEmployeeNamePitch( footballPitchEntity.getName())
                    );
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public void deleteUserManager(long managerId) {
        userRepository.deleteById(managerId);
    }

    @Override
    public List<UserDto> findAllUserMostOrder() {
        List<UserDto> userDtoList=new ArrayList<>();
        List<UserEntity> userEntityList= userRepository.findAllUserMostOrder();
        for (UserEntity userEntity : userEntityList
        ){
            UserDto userDto=beanConfig.modelMapper().map(userEntity,UserDto.class);
            userDto.setOrderCount(userEntity.getOrderEntitiesUser().size());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public UserDto findUserById(long id) {
        UserEntity entity = userRepository.findUserEntityById(id);
        UserDto dto = beanConfig.modelMapper().map(entity, UserDto.class);
        return dto;
    }

    @Override
    public UserDto findAllByUsername(String username) {
        UserDto userDtoList;
        UserEntity userEntityList= userRepository.findAllByUsername(username);

        UserDto userDto=beanConfig.modelMapper().map(userEntityList,UserDto.class);

        return userDto;
    }

    @Override
    public UserDto findOneByUserName(String userName) {
        UserEntity userEntity= userRepository.findOneByUserName(userName);
        return beanConfig.modelMapper().map(userEntity,UserDto.class);
    }


    @Override
    public List<UserDto> findAllusername(String username) {
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntityList = userRepository.findAllusername(username);
        for(UserEntity entity: userEntityList){
            UserDto userDto = beanConfig.modelMapper().map(entity,UserDto.class);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public Optional<UserDto> findById(long id){
        Optional<UserEntity> opt = userRepository.findById(id);
        return  opt.map(userEntity -> beanConfig.modelMapper().map(userEntity,UserDto.class));
    }


    @Override
    @Transactional
    public UserDto saveUpdateProfile(UserDto userDto) {
        IsMemberEntity isMemberEntity= isMemberRepository.findIsNotMember();
        UserEntity oldUserEntity=userRepository.findById(userDto.getId()).get();
        oldUserEntity = beanConfig.modelMapper().map(userDto,UserEntity.class);
        oldUserEntity.setRole("ROLE_USER");
        oldUserEntity.setIsMember(isMemberEntity);
        userRepository.save(oldUserEntity);
        return beanConfig.modelMapper().map(oldUserEntity,UserDto.class);
    }

    @Override
    @Transactional
    public UserDto saveUpdatePassword(UserDto userDto) {

        UserEntity oldUserEntity=userRepository.findById(userDto.getId()).get();
        oldUserEntity = beanConfig.modelMapper().map(userDto,UserEntity.class);
        oldUserEntity.setRole("ROLE_USER");
        userRepository.save(oldUserEntity);

        return beanConfig.modelMapper().map(oldUserEntity,UserDto.class);
    }
    @Override
    @Transactional
    public UserDto savePass(UserDto userDto) {
        UserEntity userEntity = beanConfig.modelMapper().map(userDto,UserEntity.class);
        UserEntity userEntity1 = userRepository.save(userEntity);
        UserDto userDto1 = beanConfig.modelMapper().map(userEntity1,UserDto.class);
        return  userDto1;
    }
}
