package com.example.demo_jwt.controller;

import com.example.demo_jwt.Dto.UserDto;
import com.example.demo_jwt.enitity.Privilege;
import com.example.demo_jwt.enitity.Role;
import com.example.demo_jwt.enitity.User;
import com.example.demo_jwt.payload.UserDemo;
import com.example.demo_jwt.repostory.PrivilegeRepostory;
import com.example.demo_jwt.repostory.RoleRepostory;
import com.example.demo_jwt.repostory.UserRepostory;
import com.example.demo_jwt.service.UserDetailImpl;
import com.example.demo_jwt.service.serviceimpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/auth")
public class UserRoleController {
    @Autowired
    private UserRepostory userRepo;
    @Autowired
    private PrivilegeRepostory privilegeRepo;
    @Autowired
    private RoleRepostory roleRepo;
    @Autowired
    private UserService userService;
    @GetMapping("/all")
    public String allRole() {
        return "all success";
    }
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('USER_READ')")
    public String userRole() {
        return "USER success";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN_ALL')")
    public String adminRole() {
        return "ADMIN success";
    }

    @GetMapping("/signin")
    @PreAuthorize("hasAuthority('USER_SIGNIN')")
    public String singin() {
        return "Singin success";
    }

    @GetMapping("/signup")
    @PreAuthorize("hasAuthority('USER_SIGNUP')")
    public String signup() {
        return "Signup success";
    }
    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<?> demo(@RequestBody UserDemo userDemo) {
        User user = userRepo.getUserByName(userDemo.getName());
        Collection<Role> roles = user.getRoles();
        Privilege privilege = privilegeRepo.findByName(userDemo.getPrivilege());
        roles.forEach(role -> {
            Collection<Privilege> privileges =  role.getPrivileges();
            privileges.add(privilege);
            roleRepo.save(role);

        });
        return  ResponseEntity.ok(user);
    }
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<?> add(@RequestBody UserDto userDto) {
        User user  = userService.saveUser(userDto);
        Collection<Role> roles = user.getRoles();
        return  ResponseEntity.ok(user);
    }
}
