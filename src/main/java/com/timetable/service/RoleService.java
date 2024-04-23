package com.timetable.service;

import com.timetable.dtos.roleDtos.UpdateRoleDto;
import com.timetable.entities.RoleEntity;

import org.springframework.http.ResponseEntity;

public interface RoleService {
    RoleEntity findRoleByRoleName(String roleName);
    ResponseEntity<?> createRole(String roleName);

    ResponseEntity<?> getRoles();

    ResponseEntity<?> updateRole(UpdateRoleDto updateRoleDto);

    ResponseEntity<?> deleteRole(String roleName);
}
