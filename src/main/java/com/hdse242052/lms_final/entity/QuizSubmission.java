package com.hdse242052.lms_final.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "quiz_submissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"quiz_id", "student_index"})
)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @ToString.Exclude
    private OnlineQuiz quiz;

    @Column(name = "student_index", nullable = false)
    private String studentIndex;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String answersJson;

    private Integer score;
    private Integer total;

    private LocalDateTime submittedAt;
}