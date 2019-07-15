package com.example.SilkWay.repository;

import com.example.SilkWay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getAllByStatus(String status);
    User findByEmail(String email);
    List<User> findAllByFirstName(String firstName);
    User findByToken(String token);
    User findById(int id);
}