package com.hdse242052.lms_final.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "online_quizzes")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OnlineQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String badgeSlug; // corresponds to student degree/group

    private String title;

    @Column(length = 2000)
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<OnlineQuizQuestion> questions = new ArrayList<>();

    public void setQuestions(List<OnlineQuizQuestion> questions) {
        this.questions.clear();
        if (questions != null) {
            questions.forEach(this::addQuestion);
        }
    }

    public void addQuestion(OnlineQuizQuestion question) {
        question.setQuiz(this);
        this.questions.add(question);
    }
}
