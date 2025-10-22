package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.CourseDto;
import com.hdse242052.lms_final.repository.CourseRepository;
import org.springframework.stereotype.Service;
import com.hdse242052.lms_final.entity.CourseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<CourseDto> getAllCourses() {
        return toDtoList(repository.findAll());
    }

    public CourseDto getCourseById(Long id) {
        CourseEntity entity = repository.findById(id).orElse(null);
        return entity != null ? toDto(entity) : null;
    }

    public CourseDto saveCourse(CourseDto dto) {
        CourseEntity saved = repository.save(toEntity(dto));
        return toDto(saved);
    }

    public CourseDto updateCourse(Long id, CourseDto dto) {
        if (!repository.existsById(id)) return null;
        CourseEntity updated = toEntity(dto);
        updated.setId(id);
        CourseEntity saved = repository.save(updated);
        return toDto(saved);
    }

    public void deleteCourse(Long id) {
        repository.deleteById(id);
    }

    public List<CourseDto> searchCourses(String query) {
        List<CourseEntity> results = repository.findByTitleContainingIgnoreCase(query);
        return toDtoList(results);
    }

    // 🔁 Mapping helpers
    private CourseDto toDto(CourseEntity e) {
        return new CourseDto(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getLevel(),
                e.getImageUrl(),
                new ArrayList<>(e.getTags()),
                new ArrayList<>(e.getPrerequisites())
        );
    }

    private CourseEntity toEntity(CourseDto d) {
        CourseEntity e = new CourseEntity();
        e.setId(d.getId());
        e.setTitle(d.getTitle());
        e.setDescription(d.getDescription());
        e.setLevel(d.getLevel());
        e.setImageUrl(d.getImageUrl());
        e.setTags(d.getTags());
        e.setPrerequisites(d.getPrerequisites());
        return e;
    }

    private List<CourseDto> toDtoList(List<CourseEntity> entities) {
        List<CourseDto> dtos = new ArrayList<>();
        for (CourseEntity e : entities) {
            dtos.add(toDto(e));
        }
        return dtos;
    }
}