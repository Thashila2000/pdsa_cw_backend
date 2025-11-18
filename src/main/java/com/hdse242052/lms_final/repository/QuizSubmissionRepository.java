package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.QuizSubmission;
import com.hdse242052.lms_final.entity.OnlineQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    Optional<QuizSubmission> findByQuizAndStudentIndex(OnlineQuiz quiz, String studentIndex);

    boolean existsByQuizAndStudentIndex(OnlineQuiz quiz, String studentIndex);
}
