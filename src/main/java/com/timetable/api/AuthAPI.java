package com.timetable.api;

import com.timetable.dtos.authDtos.LoginDto;
import com.timetable.dtos.authDtos.RegisterDto;
import com.timetable.service.AuthService;
import com.timetable.userDtos.ResetPasswordDto;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
public class AuthAPI {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto){
        return authService.register(registerDto);
    }

    @PostMapping("/log-in")
    public ResponseEntity logIn (@RequestBody LoginDto login) {
        return authService.login(login);
    }

    @GetMapping("/log-out")
    public void logOut(HttpServletRequest request){
        authService.logout(request);
    }

    @GetMapping("/active")
    public RedirectView active(@RequestParam("code") String code, @RequestParam("email") String email){
        return authService.activeAccount(code,email);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        return authService.forgotPassword(email);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email, @RequestBody ResetPasswordDto resetPasswordDto){
        return authService.resetPassword(email, resetPasswordDto);
    }
    @GetMapping("/activation-success")
    public String index() {
        return "activation-success"; // Trả về tên của trang HTML (không kèm phần mở rộng)
    }
}
