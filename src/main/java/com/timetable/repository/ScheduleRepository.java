package com.timetable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timetable.entities.ScheduleEntity;
import com.timetable.entities.UserEntity;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {  
	    List<ScheduleEntity> findAllByUserAndType(UserEntity user,String type); 
	    List<ScheduleEntity> findAllByUser(UserEntity user); 

}
