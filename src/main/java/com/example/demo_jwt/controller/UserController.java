package com.example.demo_jwt.controller;

import com.example.demo_jwt.Dto.UserDto;
import com.example.demo_jwt.config.jwt.JwtProvider;
import com.example.demo_jwt.enitity.User;
import com.example.demo_jwt.payload.AuthorRequest;
import com.example.demo_jwt.payload.AuthorResponse;
import com.example.demo_jwt.repostory.UserRepostory;
import com.example.demo_jwt.service.UserDetailImpl;
import com.example.demo_jwt.service.UserDetailServiceImpl;
import com.example.demo_jwt.service.serviceimpl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepostory userRepo;

    @PostMapping("/signin")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> login(@RequestBody AuthorRequest authorRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authorRequest.getUsername(),
                        authorRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateJwtToken(authentication);
        UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
        List<String> roles=userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        return  ResponseEntity.ok(new AuthorResponse(token,userDetails.getUsername(),roles));
    }

    @PostMapping("/signup")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        if(userRepo.existsByUserName(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is adready taken!");
        }
        if(userRepo.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is adready taken!");
        }
       User user =  userService.saveUser(userDto);
        return ResponseEntity.ok("User register successfully!");

    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        List<User> users = userService.getAllUser();
        return  ResponseEntity.ok(users);
    }
}
