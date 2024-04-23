package com.timetable.dtos.authDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	    private String email;
	    private String password;
	    private String phoneNumber;
	    private String status;	   
	    private String displayName;
	    private String userCode;

}
