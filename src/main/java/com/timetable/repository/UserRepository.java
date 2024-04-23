package com.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable.entities.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
    UserEntity findUserByUserCode(String userCode);
    List<UserEntity> findAllByStatus(String status);
    List<UserEntity> findAllByRoles_RoleName(String roleName);
}
