package com.example.demo_jwt.repostory;


import com.example.demo_jwt.enitity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepostory extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u where u.userName=?1")
    User getUserByName(String username);
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
    User deleteById(int id);
}
