package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.SubjectDto;
import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<SubjectDto> getSubjectsByStudentIndex(String indexNumber) {
        List<Subject> subjects = subjectRepository.findSubjectsByStudentIndex(indexNumber);
        return subjects.stream()
                .map(SubjectDto::new)
                .collect(Collectors.toList());
    }
}