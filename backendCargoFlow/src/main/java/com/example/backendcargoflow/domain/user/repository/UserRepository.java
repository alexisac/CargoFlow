package com.example.backendcargoflow.domain.user.repository;

import com.example.backendcargoflow.domain.user.entity.User;
import com.example.backendcargoflow.domain.user.entity.UserFullNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<UserFullNameProjection> findFullNameProjectionById(Long id);
}
