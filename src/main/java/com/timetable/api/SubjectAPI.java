package com.timetable.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timetable.dtos.scheduleDtos.CreateScheduleDTO;
import com.timetable.service.ScheduleService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/schedule")
public class SubjectAPI {
	 @Autowired
	    private ScheduleService subjectService;

	    @PostMapping("/add-schedule")
	    public ResponseEntity<?> addSubject(@RequestBody CreateScheduleDTO createSubjectDTO,HttpServletRequest request){
	        return subjectService.addSubject(createSubjectDTO, request);
	    }
	    @DeleteMapping("/delete-schedule")
	    public ResponseEntity<?> delete(HttpServletRequest request,@RequestParam String type){
	        return subjectService.deleteSubjects(request,type);
	    }
	    @GetMapping("/get-all-by-user")
	    public ResponseEntity<?> findAllByUser(HttpServletRequest request){
	        return subjectService.findAllByUser(request);
	    }

}
