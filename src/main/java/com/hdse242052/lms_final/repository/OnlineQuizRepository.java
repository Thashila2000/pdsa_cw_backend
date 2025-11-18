package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.OnlineQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OnlineQuizRepository extends JpaRepository<OnlineQuiz, Long> {

    // Find quiz by badge slug
    Optional<OnlineQuiz> findByBadgeSlug(String badgeSlug);

    // Find active quizzes at the current time
    @Query("""
        SELECT q FROM OnlineQuiz q
        LEFT JOIN FETCH q.questions
        WHERE :now BETWEEN 
              CASE WHEN q.startTime <= q.endTime THEN q.startTime ELSE q.endTime END
          AND CASE WHEN q.startTime <= q.endTime THEN q.endTime ELSE q.startTime END
    """)
    List<OnlineQuiz> findActiveQuizzes(@Param("now") LocalDateTime now);
}
