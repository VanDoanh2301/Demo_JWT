package com.example.demo_jwt;

import com.example.demo_jwt.Dto.UserDto;
import com.example.demo_jwt.enitity.Role;
import com.example.demo_jwt.enitity.RoleName;
import com.example.demo_jwt.enitity.User;
import com.example.demo_jwt.repostory.RoleRepostory;
import com.example.demo_jwt.repostory.UserRepostory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserSaveTest {

    @Autowired
    private UserRepostory userRepo;
    @Autowired
    private RoleRepostory roleRepo;
   public PasswordEncoder encoder(){
       return new BCryptPasswordEncoder();
    }

    @Test
    public void saveUserTest() {
        UserDto userDto = UserDto.builder()
                .username("admin")
                .password("123")
                .email("admin2@gmail.com")
                .role(Arrays.asList("admin"))
                .build();
        Collection<String> strRoles = userDto.getRole();

        Collection<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            if(strRoles == null) {
                Role roleDefaul = roleRepo.findByName(RoleName.USER);
                roles.add(roleDefaul);

            }
            else {


                switch (role) {
                    case "admin": {
                        Role adminRole = roleRepo.findByName(RoleName.ADMIN);
                        if (adminRole == null) {
                            throw new RuntimeException("admin not found");
                        }
                        roles.add(adminRole);
                        break;
                    }
                    case "user": {
                        Role userRole = roleRepo.findByName(RoleName.USER);
                        if (userRole == null) {
                            throw new RuntimeException("user not found");
                        }
                        roles.add(userRole);
                        break;
                    }
                    default: {
                        Role mnRole = roleRepo.findByName(RoleName.MANAGER);
                        if (mnRole == null) {
                            throw new RuntimeException("user not found");
                        }
                        roles.add(mnRole);
                        break;
                    }
                }
            }
        });
        User user =User.builder()
                .userName(userDto.getUsername())
                .passWord(encoder().encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .roles(roles)
                .build();
        Assertions.assertThat(userRepo.save(user)).isNotNull();
    }
    @Test
    public void deleteUser() {
        userRepo.deleteById(5);
    }
    @Test
    public void getAllUser() {
        List<User> users = userRepo.findAll();
        System.out.println(users);
    }
}
