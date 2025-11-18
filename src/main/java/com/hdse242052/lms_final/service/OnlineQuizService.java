package com.hdse242052.lms_final.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdse242052.lms_final.dto.*;
import com.hdse242052.lms_final.entity.OnlineQuiz;
import com.hdse242052.lms_final.entity.OnlineQuizQuestion;
import com.hdse242052.lms_final.entity.QuizSubmission;
import com.hdse242052.lms_final.repository.OnlineQuizRepository;
import com.hdse242052.lms_final.repository.QuizSubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OnlineQuizService {

    private final OnlineQuizRepository quizRepository;
    private final QuizSubmissionRepository submissionRepository;
    private final ObjectMapper objectMapper;

    public OnlineQuizService(OnlineQuizRepository quizRepository,
                             QuizSubmissionRepository submissionRepository,
                             ObjectMapper objectMapper) {
        this.quizRepository = quizRepository;
        this.submissionRepository = submissionRepository;
        this.objectMapper = objectMapper;
    }

    // ------------------- CREATE QUIZ -------------------
    @Transactional
    public OnlineQuiz createQuiz(CreateOnlineQuizDTO dto) {
        OnlineQuiz quiz = new OnlineQuiz();
        quiz.setBadgeSlug(dto.getBadgeSlug());
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setStartTime(dto.getStartTime());
        quiz.setEndTime(dto.getEndTime());

        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            List<OnlineQuizQuestion> questionEntities = dto.getQuestions().stream().map(qdto -> {
                OnlineQuizQuestion q = new OnlineQuizQuestion();
                q.setQuiz(quiz);
                q.setQuestionText(qdto.getQuestion());
                q.setType(qdto.getType());
                q.setCorrectAnswer(qdto.getCorrectAnswer());
                try {
                    q.setOptionsJson(objectMapper.writeValueAsString(
                            qdto.getOptions() == null ? new ArrayList<>() : qdto.getOptions()
                    ));
                } catch (Exception e) {
                    q.setOptionsJson("[]");
                }
                return q;
            }).collect(Collectors.toList());
            quiz.setQuestions(questionEntities);
        }

        return quizRepository.save(quiz);
    }

    // ------------------- GET ACTIVE QUIZ FOR STUDENT -------------------
    @Transactional(readOnly = true)
    public Optional<QuizWithAnswersDTO> findActiveQuizForStudent(String studentIndex) {
        LocalDateTime now = LocalDateTime.now();
        // get all active quizzes
        List<OnlineQuiz> activeQuizzes = quizRepository.findActiveQuizzes(now);

        if (activeQuizzes.isEmpty()) return Optional.empty();

        // first check for quizzes the student has NOT submitted yet
        Optional<OnlineQuiz> unsubmittedQuizOpt = activeQuizzes.stream()
                .filter(q -> !submissionRepository.existsByQuizAndStudentIndex(q, studentIndex))
                .findFirst();

        OnlineQuiz quiz;
        boolean submitted = false;
        Map<Integer, String> studentAnswers = new HashMap<>();
        SubmissionResultDTO previousResult = null;

        if (unsubmittedQuizOpt.isPresent()) {
            quiz = unsubmittedQuizOpt.get();
            submitted = false;
        } else {
            // all active quizzes already submitted, return the most recent submitted quiz
            quiz = activeQuizzes.get(0);
            submitted = true;
            previousResult = null; // DO NOT display answers again
        }

        QuizWithAnswersDTO dto = new QuizWithAnswersDTO();
        dto.setId(quiz.getId());
        dto.setBadgeSlug(quiz.getBadgeSlug());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setStartTime(quiz.getStartTime());
        dto.setEndTime(quiz.getEndTime());
        dto.setSubmitted(submitted);
        dto.setPreviousResult(previousResult);

        // Map questions to DTO
        List<QuestionWithAnswerDTO> qlist = quiz.getQuestions().stream().map(q -> {
            QuestionWithAnswerDTO qdto = new QuestionWithAnswerDTO();
            qdto.setQuestion(q.getQuestionText());
            qdto.setType(q.getType());
            qdto.setCorrectAnswer(q.getCorrectAnswer());
            try {
                qdto.setOptions(objectMapper.readValue(
                        q.getOptionsJson() == null ? "[]" : q.getOptionsJson(),
                        new TypeReference<List<String>>() {}
                ));
            } catch (Exception e) {
                qdto.setOptions(new ArrayList<>());
            }
            return qdto;
        }).collect(Collectors.toList());
        dto.setQuestions(qlist);

        return Optional.of(dto);
    }

    // ------------------- SUBMIT QUIZ -------------------
    @Transactional
    public SubmissionResultDTO submitAnswers(SubmissionRequestDTO req) {
        LocalDateTime now = LocalDateTime.now();
        List<OnlineQuiz> activeQuizzes = quizRepository.findActiveQuizzes(now);
        if (activeQuizzes.isEmpty())
            throw new IllegalStateException("No active quiz at this time.");

        // find first unsubmitted quiz
        OnlineQuiz quiz = activeQuizzes.stream()
                .filter(q -> !submissionRepository.existsByQuizAndStudentIndex(q, req.getStudentIndex()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("All active quizzes already submitted."));

        Map<String, String> answersMap = req.getAnswers() == null ? new HashMap<>() : req.getAnswers();
        int score = 0;
        List<OnlineQuizQuestion> questions = quiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            OnlineQuizQuestion q = questions.get(i);
            String given = answersMap.get(String.valueOf(i));
            if (given != null && q.getCorrectAnswer() != null &&
                    q.getCorrectAnswer().trim().equalsIgnoreCase(given.trim())) {
                score++;
            }
        }

        try {
            String answersJson = objectMapper.writeValueAsString(answersMap);
            QuizSubmission submission = new QuizSubmission();
            submission.setQuiz(quiz);
            submission.setStudentIndex(req.getStudentIndex());
            submission.setAnswersJson(answersJson);
            submission.setScore(score);
            submission.setTotal(questions.size());
            submission.setSubmittedAt(LocalDateTime.now());
            submissionRepository.save(submission);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save submission", e);
        }

        Map<Integer, String> convertedAnswers = answersMap.entrySet().stream()
                .collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), Map.Entry::getValue));

        SubmissionResultDTO result = new SubmissionResultDTO();
        result.setScore(score);
        result.setTotal(questions.size());
        result.setStudentAnswers(convertedAnswers);

        return result;
    }
}
