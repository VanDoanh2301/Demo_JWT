package com.example.demo_jwt.repostory;

import com.example.demo_jwt.enitity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepostory extends JpaRepository<Role,Integer> {
    @Query("SELECT u from Role  u where u.name= ?1")
    Role findByName(String name);
}
