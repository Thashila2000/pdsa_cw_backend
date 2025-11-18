package com.hdse242052.lms_final.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "online_quiz_questions")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OnlineQuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @Column(length = 2000)
    private String optionsJson; // store multiple options as JSON

    private String correctAnswer; // the correct answer

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    @ToString.Exclude
    private OnlineQuiz quiz;
}
