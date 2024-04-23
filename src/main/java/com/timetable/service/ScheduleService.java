package com.timetable.service;

import org.springframework.http.ResponseEntity;

import com.timetable.dtos.scheduleDtos.CreateScheduleDTO;
import com.timetable.entities.UserEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface ScheduleService {
	 ResponseEntity<?> addSubject(CreateScheduleDTO createScheduleDTO,HttpServletRequest httpServletRequest );
	 ResponseEntity<?> deleteSubjects(HttpServletRequest request,String type);
	 ResponseEntity<?> findAllByUser(HttpServletRequest request);
}
