package com.example.demo_jwt.service.serviceimpl;

import com.example.demo_jwt.Dto.UserDto;
import com.example.demo_jwt.enitity.Privilege;
import com.example.demo_jwt.enitity.Role;
import com.example.demo_jwt.enitity.User;
import com.example.demo_jwt.repostory.PrivilegeRepostory;
import com.example.demo_jwt.repostory.RoleRepostory;
import com.example.demo_jwt.repostory.UserRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepostory UserRepo;
    @Autowired
    private RoleRepostory RoleRepo;
    @Autowired
    private PrivilegeRepostory privilegeRepo;

    private PasswordEncoder encoder() {
        return  new BCryptPasswordEncoder();
    }
    @Override
    public User saveUser(UserDto userDto) {
        Privilege privilege = privilegeRepo.findByName("USER_READ");
       User user =User.builder()
               .userName(userDto.getUsername())
               .passWord(encoder().encode(userDto.getPassword()))
               .email(userDto.getEmail())
               .roles(Arrays.asList(new Role("USER")))
               .build();
       Collection<Role> roles = user.getRoles();
       Collection<Privilege> privileges = new ArrayList<>();
       privileges.add(privilege);
       roles.forEach(role -> {
           role.setPrivileges(privileges);

       });

        return UserRepo.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = UserRepo.findAll();
        return users;
    }

    @Override
    public void deleteUser(int id) {
        UserRepo.deleteById(id);
    }
}
