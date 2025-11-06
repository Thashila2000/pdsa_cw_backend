package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.QuizForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface QuizFormRepository extends JpaRepository<QuizForm, Long> {

    // Case-sensitive lookups
    Optional<QuizForm> findByBadgeSlug(String badgeSlug);
    List<QuizForm> findAllByBadgeSlug(String badgeSlug);

    // Case-insensitive lookups (recommended for robustness)
    Optional<QuizForm> findByBadgeSlugIgnoreCase(String badgeSlug);
    List<QuizForm> findAllByBadgeSlugIgnoreCase(String badgeSlug);
}