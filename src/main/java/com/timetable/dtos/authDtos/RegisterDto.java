package com.timetable.dtos.authDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String displayName;
    private String userCode;
   
}
