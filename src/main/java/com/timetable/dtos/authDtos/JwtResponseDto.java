package com.timetable.dtos.authDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponseDto {
    private String userCode;
    private String token;
    private Long expired;
    private String tokeType = "Bearer";

    public JwtResponseDto(String userCode , String token,Long expired) {
        this.userCode = userCode;
        this.token = token;
        this.expired = expired;
    }
}
