package com.timetable.service;

import com.timetable.dtos.authDtos.RegisterDto;
import com.timetable.entities.UserEntity;
import com.timetable.userDtos.ChangePasswordDto;
import com.timetable.userDtos.ResetPasswordDto;
import com.timetable.userDtos.UpdateUserDto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createAccountUser(RegisterDto registerDto);
    ResponseEntity<?> createAccountAdmin(RegisterDto registerDto);
    ResponseEntity<?> changePassword(HttpServletRequest request, ChangePasswordDto changePasswordDto);
    void saveRefreshToken(String email,String refreshToken);
    ResponseEntity<?> refreshToken(HttpServletRequest request);
    ResponseEntity<?> getMe(HttpServletRequest request);
    String updateUser(HttpServletRequest request, UpdateUserDto updateUserDto);
    ResponseEntity<?> toggleLockUser(String email,String value);
    boolean activeUser(String email);
    UserEntity checkUser(String userCode);
    UserEntity checkUserbyEmail(String email);
    
    UserEntity findUserById(Long id);
    UserEntity findUserByToken(HttpServletRequest request);
    boolean resetPassword(String email, ResetPasswordDto resetPasswordDto);
}
