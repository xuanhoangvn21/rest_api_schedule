package com.timetable.dtos.scheduleDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private String subjectname;	  
    private int numberofcredits;	 	
    private int dayofweek;	 
    private String lesson ;	   
    private String location ;	
    private String dayofsubject;
    private String type;

}
