package com.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timetable.entities.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleByRoleName(String roleName);
}
