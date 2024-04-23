package com.timetable.api;

import com.timetable.dtos.authDtos.RegisterDto;
import com.timetable.service.impl.UserServiceImpl;
import com.timetable.userDtos.ChangePasswordDto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserAPI {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/get-me")
    public ResponseEntity<?> getDetailUser(HttpServletRequest request){
        return userService.getMe(request);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request,@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(request, changePasswordDto);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return userService.refreshToken(request);
    }

    @PostMapping("/create-account-admin")
    public void createAdminAccount(RegisterDto registerDto){
        userService.createAccountAdmin(registerDto);
    }

    @GetMapping("/toggle-lock-user")
    public ResponseEntity<?> toggleLockUser(String email,String value){
        return userService.toggleLockUser(email,value);
    }
}
