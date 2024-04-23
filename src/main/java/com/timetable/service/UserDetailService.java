package com.timetable.service;

import com.timetable.entities.UserEntity;
import com.timetable.repository.UserRepository;
import com.timetable.service.jwt.UserDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        UserEntity user = repository.findUserByUserCode(userCode);
        return new UserDetail(user);

    }
}
