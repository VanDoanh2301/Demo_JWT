package com.example.demo_jwt.repostory;

import com.example.demo_jwt.enitity.Privilege;
import com.example.demo_jwt.enitity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepostory extends JpaRepository<Role,Integer> {
    @Query("SELECT u from Role  u where u.name= ?1")
    Role findByName(String name);
    @Query(" From Role u left JOIN  u.privileges p where u.id=?1")
    List<Role> findId(Integer id);

}
