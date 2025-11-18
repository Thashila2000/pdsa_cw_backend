package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.dto.DegreeRequest;
import com.hdse242052.lms_final.entity.Category;
import com.hdse242052.lms_final.entity.Degree;
import com.hdse242052.lms_final.entity.Subject;
import com.hdse242052.lms_final.repository.CategoryRepository;
import com.hdse242052.lms_final.repository.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DegreeService {

    private final DegreeRepository degreeRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Adds a new degree and clones subjects from the selected category.
     */
    @Transactional
    public void addDegree(DegreeRequest request) {
        // 1. Fetch the selected category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 2. Create the new degree
        Degree degree = new Degree();
        degree.setName(request.getName());
        degree.setCategory(category);

        // 3. Clone subjects from the category
        List<Subject> clonedSubjects = new ArrayList<>();
        for (Subject original : category.getSubjects()) {
            Subject clone = new Subject();
            clone.setName(original.getName());
            clone.setCode(original.getCode());
            clone.setCategory(category);
            clone.setDegree(degree); // Link to new degree
            clonedSubjects.add(clone);
        }

        // 4. Attach subjects to degree and save
        degree.setSubjects(clonedSubjects);
        degreeRepository.save(degree); // Cascade saves subjects
    }

    /**
     * Returns all degrees.
     */
    public List<Degree> getAllDegrees() {
        return degreeRepository.findAll();
    }

    /**
     * Deletes a degree by ID.
     */
    public void deleteDegree(Long id) {
        Degree degree = degreeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Degree not found"));
        degreeRepository.delete(degree);
    }

    /**
     * Finds a degree by name.
     */
    public Degree getDegreeByName(String name) {
        return degreeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Degree not found"));
    }
}