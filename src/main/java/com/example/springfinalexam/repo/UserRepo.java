package com.example.springfinalexam.repo;

import com.example.springfinalexam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findTopByOrderByIdDesc();

    User findByEmail(String email);

    Optional<User> findByUsername(String username);
}
