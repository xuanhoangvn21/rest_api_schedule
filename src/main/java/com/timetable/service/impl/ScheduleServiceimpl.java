package com.timetable.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.timetable.dtos.scheduleDtos.CreateScheduleDTO;
import com.timetable.dtos.scheduleDtos.SubjectDTO;
import com.timetable.entities.ScheduleEntity;
import com.timetable.entities.UserEntity;
import com.timetable.helper.EntityDtoConverter;
import com.timetable.repository.ScheduleRepository;
import com.timetable.service.ScheduleService;
import com.timetable.service.UserService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
@Service
public class ScheduleServiceimpl implements ScheduleService{
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleRepository  scheduleRepository;
	@Override
	public ResponseEntity<?> addSubject(CreateScheduleDTO createSubjectDTO,HttpServletRequest request) {{
		try {
			  UserEntity userEntity = userService.findUserByToken(request);
		        ScheduleEntity subjectEntity = EntityDtoConverter.convertToEntity(createSubjectDTO, ScheduleEntity.class);	        
		        subjectEntity.setUser(userEntity);
		        scheduleRepository.save(subjectEntity);
		        return ResponseEntity.status(HttpStatus.CREATED).body("Create subject success");}
		 catch (Exception ex){
   System.out.println(ex.getMessage());
   return ResponseEntity.internalServerError().body(ex.getMessage());
	}
	}

}
	
	public ResponseEntity<?> deleteSubjects(HttpServletRequest request,String type) {
	    try {
	        UserEntity userEntity=userService.findUserByToken(request);
	        List<ScheduleEntity> subjects = scheduleRepository.findAllByUserAndType(userEntity,type);
	        System.out.println("user :"+userEntity.getDisplayName());
            System.out.println("mon :"+subjects);
            scheduleRepository.deleteAll(subjects);

	        return ResponseEntity.ok("Deleted subjects for the user successfully");
	    } catch (Exception ex) {
	        System.out.println(ex.getMessage());
	        return ResponseEntity.internalServerError().body("Failed to delete subjects for the user");
	    }
	}

	@Override
	public ResponseEntity<?> findAllByUser(HttpServletRequest request) {
		try { 
			 UserEntity userEntity=userService.findUserByToken(request);
			   List<ScheduleEntity> subjects = scheduleRepository.findAllByUser(userEntity);
		    List<SubjectDTO> subjectdtos= new ArrayList<>();
		    for(ScheduleEntity sj:subjects) {
		    	System.out.println(subjects);    	
		    	SubjectDTO sjdto=EntityDtoConverter.convertToDto(sj,SubjectDTO.class);		    
		    	subjectdtos.add(sjdto);    	
		    }
			return ResponseEntity.ok().body(subjectdtos);
			
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        return ResponseEntity.internalServerError().body(e.getMessage());
	    }
	}
	
}