package com.example.demo_jwt.repostory;

import com.example.demo_jwt.enitity.Role;
import com.example.demo_jwt.enitity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepostory extends JpaRepository<Role,Integer> {
    Role findByName(RoleName name);
}
