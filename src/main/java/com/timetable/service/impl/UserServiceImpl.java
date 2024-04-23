package com.timetable.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.timetable.dtos.authDtos.JwtResponseDto;
import com.timetable.dtos.authDtos.RegisterDto;
import com.timetable.dtos.authDtos.TokenResponseDto;
import com.timetable.dtos.authDtos.UserDTO;
import com.timetable.entities.RoleEntity;
import com.timetable.entities.UserEntity;
import com.timetable.helper.EntityDtoConverter;
import com.timetable.repository.UserRepository;
import com.timetable.service.RoleService;
import com.timetable.service.UserService;
import com.timetable.service.jwt.JwtAuthenticationFilter;
import com.timetable.service.jwt.JwtService;
import com.timetable.userDtos.ChangePasswordDto;
import com.timetable.userDtos.ResetPasswordDto;
import com.timetable.userDtos.UpdateUserDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
  

    @Override
    public ResponseEntity<?> createAccountUser(RegisterDto registerDto) {
        try {
            UserEntity u = checkUserbyEmail(registerDto.getEmail());
            if (u != null) {
                return ResponseEntity.badRequest().body("email da ton tai");
            }
            if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
                return ResponseEntity.badRequest().body("xac nhan mat khau khong khop");
            }
            UserEntity us = EntityDtoConverter.convertToEntity(registerDto, UserEntity.class);
            us.setPassword(bCryptPasswordEncoder.encode(us.getPassword()));
            us.setStatus("Activate");
            UserEntity user = repository.save(us);
           
            return ResponseEntity.ok().body("Create success");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createAccountAdmin(RegisterDto registerDto) {
        try {
            UserEntity u = checkUserbyEmail(registerDto.getEmail());
            if (u != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is exists, please choose an other email");
            }
            UserEntity user = EntityDtoConverter.convertToEntity(registerDto, UserEntity.class);
            user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
            RoleEntity userRole = roleService.findRoleByRoleName("ADMIN");
            if (roleService.findRoleByRoleName("ADMIN") == null) {
                roleService.createRole("ADMIN");
                userRole = roleService.findRoleByRoleName("ADMIN");
            }
            user.getRoles().add(userRole);
            user.setStatus("Activate");
            repository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Create success");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred");
        }
    }
   
    @Override
    public ResponseEntity<?> changePassword(HttpServletRequest request, ChangePasswordDto changePasswordDto) {
        try {
            String token = jwtAuthenticationFilter.getToken(request);
            UserEntity user = jwtService.getMeFromToken(token);
            boolean checkPassword = bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword());
            if (!checkPassword) {
                return ResponseEntity.badRequest().body("Old password not correct");
            }
            if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Confirm password not correct");

            }
            user.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getNewPassword()));
            user.setUpdatedAt(new Date());
            repository.save(user);
            return ResponseEntity.ok().body("Change password success");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("Change password not success " + ex.getMessage());
        }

    }

    @Override
    public void saveRefreshToken(String userCode, String refreshToken) {
        try{
            UserEntity user = repository.findUserByUserCode(userCode);
            user.setRefreshToken(refreshToken);
            repository.save(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            UserEntity user = findUserByToken(request);
            if (jwtService.validateRefreshToken(user.getRefreshToken())) {
                Authentication authentication = authenticationManager.authenticate(new
                        UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                TokenResponseDto tokenResponseDto = jwtService.generateToken(authentication);
                return ResponseEntity.ok(new JwtResponseDto(user.getEmail(), tokenResponseDto.getToken(), tokenResponseDto.getExpired()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public UserEntity checkUser(String userCode) {

        try {
            return repository.findUserByUserCode(userCode);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    @Override
    public UserEntity checkUserbyEmail(String email) {

        try {
            return repository.findUserByEmail(email);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
 
    @Override
    public String updateUser(HttpServletRequest request, UpdateUserDto updateUserDto) {
        try{
            UserEntity user = findUserByToken(request);
            if(user == null){
                return "User not found";
            }else{
                user.setPhoneNumber(updateUserDto.getPhoneNumber());
                repository.save(user);
                return "Update user successfully";
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<?> toggleLockUser(String email,String value) {
        try{
            UserEntity user = repository.findUserByEmail(email);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not exists");
            }else{
                user.setStatus(value);
                repository.save(user);
                return ResponseEntity.ok().body(value+" user success");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }

    public boolean activeUser(String userCode) {
        try {
            UserEntity user = repository.findUserByUserCode(userCode);
            if (user == null) {
                return false;
            } else {
                RoleEntity userRole = roleService.findRoleByRoleName("USER");
                if (userRole == null) {
                    roleService.createRole("USER");
                    userRole = roleService.findRoleByRoleName("USER");
                }
                user.getRoles().add(userRole);
                repository.save(user);
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public UserEntity findUserByToken(HttpServletRequest request){
        try{
            String token = jwtAuthenticationFilter.getToken(request);
            return jwtService.getMeFromToken(token);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteAccount() {
        try {
            List<UserEntity> users = repository.findAllByStatus("DeActivate");
            Date currentDate = new Date();
            for (UserEntity user : users) {
                long diffInMillies = Math.abs(currentDate.getTime() - user.getCreatedAt().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if (diff > 7) {
                  
                    repository.delete(user);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public boolean resetPassword(String email, ResetPasswordDto resetPasswordDto) {
        try{
            UserEntity user = checkUser(email);
            if(user == null){
                return false;
            }else{
                if(resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())){
                    String hashPassword = bCryptPasswordEncoder.encode(resetPasswordDto.getNewPassword());
                    user.setPassword(hashPassword);
                    repository.save(user);
                    return true;
                }else{
                    return false;
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public UserEntity findUserById(Long id) {
        try{
            Optional<UserEntity> user = repository.findById(id);
            return user.orElse(null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

	@Override
	public ResponseEntity<?> getMe(HttpServletRequest request) {
		  try{ UserEntity entity=findUserByToken(request);
	           UserDTO dto=EntityDtoConverter.convertToDto(entity,UserDTO.class);
	       	return ResponseEntity.ok().body(dto);
	        }catch (Exception e){
	            System.out.println(e.getMessage());
	            return null;
	        }
	}
}
