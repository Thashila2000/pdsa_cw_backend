package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.DegreeRequest;
import com.hdse242052.lms_final.entity.Category;
import com.hdse242052.lms_final.entity.Degree;
import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.repository.CategoryRepository;
import com.hdse242052.lms_final.repository.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DegreeService {

    private final DegreeRepository degreeRepository;
    private final CategoryRepository categoryRepository;

    // Add a new degree and clone subjects from its category
    public void addDegree(DegreeRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Degree degree = new Degree();
        degree.setName(request.getName());
        degree.setCategory(category);

        // Clone subjects from category and link to degree
        List<Subject> clonedSubjects = category.getSubjects().stream().map(original -> {
            Subject clone = new Subject();
            clone.setName(original.getName());
            clone.setCode(original.getCode());
            clone.setCategory(category); // maintain category link
            clone.setDegree(degree);     // link to new degree
            return clone;
        }).toList();

        degree.setSubjects(clonedSubjects);
        degreeRepository.save(degree); // cascade saves subjects
    }

    // Get all degrees
    public List<Degree> getAllDegrees() {
        return degreeRepository.findAll();
    }

    // Delete a degree by ID
    public void deleteDegree(Long id) {
        Degree degree = degreeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Degree not found"));
        degreeRepository.delete(degree);
    }

    // Get a degree by name (used for badge routing)
    public Degree getDegreeByName(String name) {
        return degreeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Degree not found"));
    }
}