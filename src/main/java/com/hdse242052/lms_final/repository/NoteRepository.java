package com.hdse242052.lms_final.repository;

import com.hdse242052.lms_final.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findBySubjectId(Long subjectId);
}