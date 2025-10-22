package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIndexNumber(String indexNumber);
    boolean existsByIndexNumber(String indexNumber);
}