package com.timetable.service.impl;

import com.timetable.dtos.roleDtos.UpdateRoleDto;
import com.timetable.entities.RoleEntity;
import com.timetable.entities.UserEntity;
import com.timetable.repository.RoleRepository;
import com.timetable.repository.UserRepository;
import com.timetable.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public RoleEntity findRoleByRoleName(String roleName) {
        return repository.findRoleByRoleName(roleName);
    }

    public boolean checkRole(String roleName){
        try{
            RoleEntity role = findRoleByRoleName(roleName);
            if(role != null){
                return true;
            }else{
                return false;
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    @Override
    public ResponseEntity<?> createRole(String roleName) {
        try {
            boolean roleExists = checkRole(roleName);
            if (roleExists) {
                return ResponseEntity.badRequest().body("Role is exists");
            } else {
                RoleEntity newRole = new RoleEntity(roleName);
                repository.save(newRole);
                return ResponseEntity.ok().body("Role created successfully");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }

    @Override
    public ResponseEntity<?> getRoles() {
        List<RoleEntity> roles = repository.findAll();
        return ResponseEntity.ok().body(roles);
    }

    @Override
    public ResponseEntity<?> updateRole(UpdateRoleDto updateRoleDto) {
        try {
            RoleEntity role = findRoleByRoleName(updateRoleDto.getOldRoleName());
            if(role == null){
                return ResponseEntity.badRequest().body("Role is not exists");
            }else{
                role.setRoleName(updateRoleDto.getNewRoleName());
                repository.save(role);
                return ResponseEntity.ok().body("Role updated successfully");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }

    @Override
    public ResponseEntity<?> deleteRole(String roleName) {
        try{
            RoleEntity role = findRoleByRoleName(roleName);
            if(role == null){
                return ResponseEntity.badRequest().body("Role is not exists");
            }else {
                RoleEntity userRole = repository.findRoleByRoleName("OTHER");
                if (userRole == null) {
                    userRole = new RoleEntity();
                    userRole.setRoleName("ADMIN");
                    repository.save(userRole);
                }
                List<UserEntity> users = userRepository.findAllByRoles_RoleName(roleName);
                for (UserEntity user : users) {
                    user.getRoles().clear();
                    user.getRoles().add(userRole);
                    userRepository.save(user);
                }
                repository.deleteById(role.getId());
                return ResponseEntity.ok().body("Role deleted successfully and users updated");
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body("An error occurred: " + ex.getMessage());
        }
    }
}
