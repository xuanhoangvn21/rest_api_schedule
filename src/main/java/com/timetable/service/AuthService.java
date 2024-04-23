package com.timetable.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import com.timetable.dtos.authDtos.LoginDto;
import com.timetable.dtos.authDtos.RegisterDto;
import com.timetable.userDtos.ResetPasswordDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    ResponseEntity<?> register(RegisterDto registerDto);
    ResponseEntity<?> login(LoginDto login);
    public void logout(HttpServletRequest request);
    RedirectView activeAccount(String code, String email);
    ResponseEntity<?> forgotPassword(String email);
    ResponseEntity<?> resetPassword(String email, ResetPasswordDto resetPasswordDto);
}
