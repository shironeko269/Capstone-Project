package com.edu.fud.projectfootbalpitch.service.impl;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import com.edu.fud.projectfootbalpitch.dto.MyUser;
import com.edu.fud.projectfootbalpitch.entity.UserEntity;
import com.edu.fud.projectfootbalpitch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity=userRepository.findOneByUserName(s);
        if (userEntity == null) {
            UserEntity userGG=new UserEntity();
            throw new UsernameNotFoundException("User not found");
        }
        MyUser myUser = new MyUser(userEntity);
        return myUser;
    }
}
