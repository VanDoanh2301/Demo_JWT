package com.example.demo_jwt.service.serviceimpl;

import com.example.demo_jwt.Dto.UserDto;
import com.example.demo_jwt.enitity.Role;
import com.example.demo_jwt.enitity.RoleName;
import com.example.demo_jwt.enitity.User;
import com.example.demo_jwt.repostory.RoleRepostory;
import com.example.demo_jwt.repostory.UserRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepostory UserRepo;
    @Autowired
    private RoleRepostory RoleRepo;

    private PasswordEncoder encoder() {
        return  new BCryptPasswordEncoder();
    }
    @Override
    public User saveUser(UserDto userDto) {
        Collection<String> strRoles = userDto.getRole();
        Collection<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            //Mac dinh la user
            if(strRoles == null) {
                Role userRole = RoleRepo.findByName(RoleName.USER);
                if(userRole == null) {
                    throw  new RuntimeException("User not found");
                }
                roles.add(userRole);
            }
            switch (role) {
                case "admin":
                    Role adminRole = RoleRepo.findByName(RoleName.ADMIN);
                    if(adminRole == null) {
                        throw  new RuntimeException("admin not found");
                    }
                    roles.add(adminRole);
                    break;
                case "user":
                    Role userRole = RoleRepo.findByName(RoleName.USER);
                    if(userRole == null) {
                        throw  new RuntimeException("user not found");
                    }
                    roles.add(userRole);
                    break;
                default:
                    Role mnRole = RoleRepo.findByName(RoleName.MANAGER);
                    if(mnRole == null) {
                        throw  new RuntimeException("user not found");
                    }
                    roles.add(mnRole);
                    break;
            }
        });
       User user =User.builder()
               .userName(userDto.getUsername())
               .passWord(encoder().encode(userDto.getPassword()))
               .email(userDto.getEmail())
               .roles(roles)
               .build();
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
