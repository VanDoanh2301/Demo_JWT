package com.example.demo_jwt.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserRoleController {
    @GetMapping("/all")
    public String allRole() {
        return "all success";
    }
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('USER')")
    public String userRole() {
        return "user success";
    }
    @GetMapping("/admin")
  @PreAuthorize("hasAuthority('ADMIN')")
    public String adminRole() {
        return "admin success";
    }


}
