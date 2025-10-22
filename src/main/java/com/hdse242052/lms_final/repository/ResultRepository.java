package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByIndexNumber(String indexNumber);
}