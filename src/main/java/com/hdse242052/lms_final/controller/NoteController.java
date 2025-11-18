package com.hdse242052.lms_final.controller;

import com.hdse242052.lms_final.dto.NoteDto;
import com.hdse242052.lms_final.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Get all notes for a subject
    @GetMapping("/by-subject/{subjectId}")
    public List<NoteDto> getNotes(@PathVariable Long subjectId) {
        return noteService.getNotesBySubject(subjectId);
    }

    // Save a text-only note
    @PostMapping
    public NoteDto saveNote(@RequestBody NoteDto dto) {
        return noteService.saveNote(dto);
    }

    // Update a note
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long noteId, @RequestBody NoteDto dto) {
        NoteDto updated = noteService.updateNote(noteId, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete a note
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }

    // Upload note with image (gallery or camera)
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadNote(
            @RequestParam("text") String text,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        String imageUri = null;

        if (image != null && !image.isEmpty()) {
            imageUri = noteService.saveImage(image); // You implement this
        }

        NoteDto dto = new NoteDto();
        dto.setText(text);
        dto.setSubjectId(subjectId);
        dto.setImageUri(imageUri);
        dto.setTimestamp(System.currentTimeMillis());

        noteService.saveNote(dto);
        return ResponseEntity.ok().build();
    }
}