package com.timetable.service.impl;

import com.timetable.dtos.authDtos.*;
import com.timetable.entities.UserEntity;
import com.timetable.service.AuthService;
import com.timetable.service.SendEmailService;
import com.timetable.service.UserService;
import com.timetable.service.jwt.JwtAuthenticationFilter;
import com.timetable.service.jwt.JwtService;
import com.timetable.userDtos.ResetPasswordDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SendEmailService sendEmailService;

    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        try {
        	 String prefix = "GVDTC24";
        	    Random random = new Random();
        	 
        	    StringBuilder suffix = new StringBuilder();
        	    for (int i = 0; i < 6; i++) {
        	        char randomChar = (char) (random.nextInt(26) + 'A'); 
        	        suffix.append(randomChar);
        	    }
        	    String userCode= prefix + suffix.toString();
        	    registerDto.setUserCode(userCode);
            ResponseEntity<?> createAccountResponse = userService.createAccountUser(registerDto);
            if (!createAccountResponse.getStatusCode().is2xxSuccessful()) {
                return createAccountResponse;
            }
            String hashedEmail = bCryptPasswordEncoder.encode(registerDto.getEmail());
            Resource resource = new ClassPathResource("templates/ActiveAccount.html");
            String htmlContent = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
            String processedHtmlContent = htmlContent
                    .replace("[[Recipient_Name]]", registerDto.getDisplayName())
                    .replace("[[User_Code]]", registerDto.getUserCode());
                   
            sendEmailService.sendEmail(registerDto.getEmail(),
                    "Cung cấp mã giảng viên",
                    processedHtmlContent);
            userService.activeUser(userCode);
            return ResponseEntity.ok().body("Check Email to codeUser");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> login(LoginDto login) {
        try {
            UserEntity user = userService.checkUser(login.getUserCode());
            if(user == null ){
                return ResponseEntity.badRequest().body("Account not is exists");
            }
            if (user.getStatus().equals("Activate")) {
                Authentication authentication = authenticationManager.authenticate(new
                        UsernamePasswordAuthenticationToken(login.getUserCode(), login.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                TokenResponseDto tokenResponseDto = jwtService.generateToken(authentication);
                String refreshToken = jwtService.generateRefreshToken(authentication);
                userService.saveRefreshToken(login.getUserCode(), refreshToken);
                return ResponseEntity.ok(new JwtResponseDto(login.getUserCode(), tokenResponseDto.getToken(), tokenResponseDto.getExpired()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not Activate or Block");

            }
        } catch (Exception ex) {
            System.out.println((ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password not correct");
    }


    @Override
    public void logout(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getToken(request);
        String email = jwtService.getUsernameFromToken(token);
        userService.saveRefreshToken(email, "");
        SecurityContextHolder.clearContext();
    }

    public RedirectView activeAccount(String code, String email) {
        try {
            boolean isMatch = bCryptPasswordEncoder.matches(email, code);
            if (isMatch && userService.activeUser(email)) {
                return new RedirectView("http://localhost:8083/api/auth/activation-success");
            } else {
                return new RedirectView("http://localhost:8081/activefalse");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new RedirectView("http://localhost:3000/login?isActivate=false");
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        try {
            UserEntity user = userService.checkUser(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("Email not exists");
            } else {
                sendEmailService.sendEmail(email, "Reset password", "http://www.localhost:8081/auth/reset-password?email="+email);
                return ResponseEntity.ok().body("Check email to reset password");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("An error");
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(String email, ResetPasswordDto resetPasswordDto) {
        try {
           if(userService.resetPassword(email, resetPasswordDto)){
               return ResponseEntity.ok("Change password success");
           }else {
               return ResponseEntity.badRequest().body("Check password and confirm password");
           }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("Error");
        }
    }

}
