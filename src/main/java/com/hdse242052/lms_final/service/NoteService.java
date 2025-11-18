package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.NoteDto;
import com.hdse242052.lms_final.entity.Note;
import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.repository.NoteRepository;
import com.hdse242052.lms_final.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final SubjectRepository subjectRepository;

    public NoteService(NoteRepository noteRepository, SubjectRepository subjectRepository) {
        this.noteRepository = noteRepository;
        this.subjectRepository = subjectRepository;
    }

    // Get all notes for a subject
    public List<NoteDto> getNotesBySubject(Long subjectId) {
        return noteRepository.findBySubjectId(subjectId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Save note from JSON
    public NoteDto saveNote(NoteDto dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID"));

        Note note = new Note();
        note.setText(dto.getText());
        note.setImageUri(dto.getImageUri());
        note.setTimestamp(System.currentTimeMillis());
        note.setSubject(subject);

        Note saved = noteRepository.save(note);
        return toDto(saved);
    }

    // Save image and return URI
    public String saveImage(MultipartFile image) {
        try {
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get("uploads/" + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
            return "/uploads/" + filename; // Adjust if serving via static handler
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    // Update note
    public NoteDto updateNote(Long noteId, NoteDto dto) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        note.setText(dto.getText());
        note.setImageUri(dto.getImageUri());
        note.setTimestamp(System.currentTimeMillis());

        Note updated = noteRepository.save(note);
        return toDto(updated);
    }

    // Delete note
    public void deleteNote(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    // Convert entity to DTO
    public NoteDto toDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getId());
        dto.setText(note.getText());
        dto.setImageUri(note.getImageUri());
        dto.setTimestamp(note.getTimestamp());
        dto.setSubjectId(note.getSubject().getId());
        return dto;
    }
}